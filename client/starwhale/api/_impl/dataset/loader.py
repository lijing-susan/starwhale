from __future__ import annotations

import os
import sys
import typing as t
from abc import ABCMeta, abstractmethod
from urllib.parse import urlparse

import loguru
from loguru import logger as _logger

from starwhale.utils import load_dotenv
from starwhale.consts import AUTH_ENV_FNAME
from starwhale.base.uri import URI
from starwhale.base.type import InstanceType, DataFormatType, ObjectStoreType
from starwhale.core.dataset.type import DataField
from starwhale.core.dataset.store import FileLikeObj, ObjectStore, DatasetStorage
from starwhale.core.dataset.tabular import TabularDataset, TabularDatasetRow


class DataLoader(metaclass=ABCMeta):
    def __init__(
        self,
        dataset_uri: URI,
        start: int = 0,
        end: int = sys.maxsize,
        logger: t.Union[loguru.Logger, None] = None,
    ):
        self.dataset_uri = dataset_uri
        self.start = start
        self.end = end
        self.logger = logger or _logger
        # TODO: refactor TabularDataset with dataset_uri
        # TODO: refactor dataset, tabular_dataset and standalone dataset module
        self.tabular_dataset = TabularDataset.from_uri(
            dataset_uri, start=start, end=end
        )
        self._stores: t.Dict[str, ObjectStore] = {}

        self._load_dataset_auth_env()

    def _load_dataset_auth_env(self) -> None:
        # TODO: support multi datasets
        if self.dataset_uri.instance_type == InstanceType.STANDALONE:
            auth_env_fpath = (
                DatasetStorage(self.dataset_uri).snapshot_workdir / AUTH_ENV_FNAME
            )
            load_dotenv(auth_env_fpath)

    def _get_store(self, row: TabularDatasetRow) -> ObjectStore:
        _k = f"{row.object_store_type.value}.{row.auth_name}"
        _store = self._stores.get(_k)
        if _store:
            return _store

        if row.object_store_type == ObjectStoreType.REMOTE:
            _store = ObjectStore.from_data_link_uri(row.data_uri, row.auth_name)
        else:
            _store = ObjectStore.from_dataset_uri(self.dataset_uri)

        self._stores[_k] = _store
        return _store

    def _get_key_compose(self, row: TabularDatasetRow, store: ObjectStore) -> str:
        if row.object_store_type == ObjectStoreType.REMOTE:
            data_uri = urlparse(row.data_uri).path
        else:
            data_uri = row.data_uri
            if store.key_prefix:
                data_uri = os.path.join(store.key_prefix, data_uri.lstrip("/"))

        if self.kind == DataFormatType.SWDS_BIN:
            offset, size = (
                int(row.extra_kw["_swds_bin_offset"]),
                int(row.extra_kw["_swds_bin_size"]),
            )
        else:
            offset, size = row.data_offset, row.data_size

        _key_compose = f"{data_uri}:{offset}:{offset + size - 1}"
        return _key_compose

    def __iter__(self) -> t.Generator[t.Tuple[DataField, DataField], None, None]:
        _attr = {
            "ds_name": self.tabular_dataset.name,
            "ds_version": self.tabular_dataset.version,
        }
        for row in self.tabular_dataset.scan():
            # TODO: tune performance by fetch in batch
            # TODO: remove ext_attr field
            _store = self._get_store(row)
            _key_compose = self._get_key_compose(row, _store)

            self.logger.info(f"@{_store.bucket}/{_key_compose}")
            _file = _store.backend._make_file(_store.bucket, _key_compose)
            for data_content, data_size in self._do_iter(_file, row):
                label = DataField(
                    idx=row.id,
                    data_size=sys.getsizeof(row.label),
                    data=row.label,
                    ext_attr=_attr,
                )
                data = DataField(
                    idx=row.id, data_size=data_size, data=data_content, ext_attr=_attr
                )

                yield data, label

    @abstractmethod
    def _do_iter(
        self, file: FileLikeObj, row: TabularDatasetRow
    ) -> t.Generator[t.Tuple[bytes, int], None, None]:
        raise NotImplementedError

    def __str__(self) -> str:
        return f"[{self.kind.name}]DataLoader for {self.dataset_uri}"

    def __repr__(self) -> str:
        return f"[{self.kind.name}]DataLoader for {self.dataset_uri}, start:{self.start}, end:{self.end}"

    @property
    def kind(self) -> DataFormatType:
        raise NotImplementedError


class UserRawDataLoader(DataLoader):
    @property
    def kind(self) -> DataFormatType:
        return DataFormatType.USER_RAW

    def _do_iter(
        self,
        file: FileLikeObj,
        row: TabularDatasetRow,
    ) -> t.Generator[t.Tuple[bytes, int], None, None]:
        yield file.read(row.data_size), row.data_size


class SWDSBinDataLoader(DataLoader):
    @property
    def kind(self) -> DataFormatType:
        return DataFormatType.SWDS_BIN

    def _do_iter(
        self, file: FileLikeObj, row: TabularDatasetRow
    ) -> t.Generator[t.Tuple[bytes, int], None, None]:
        from .builder import _header_size, _header_struct

        size: int
        padding_size: int
        header: bytes = file.read(_header_size)
        _, _, _, size, padding_size, _, _ = _header_struct.unpack(header)
        data: bytes = file.read(size + padding_size)
        yield data[:size], size


def get_data_loader(
    dataset_uri: URI,
    start: int = 0,
    end: int = sys.maxsize,
    logger: t.Union[loguru.Logger, None] = None,
) -> DataLoader:
    from starwhale.core.dataset import model

    summary = model.Dataset.get_dataset(dataset_uri).summary()
    include_user_raw = summary.include_user_raw
    _cls = UserRawDataLoader if include_user_raw else SWDSBinDataLoader
    return _cls(dataset_uri, start, end, logger or _logger)