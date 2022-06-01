import os
import typing as t
from pathlib import Path

from starwhale.utils import console, in_production
from starwhale.consts import DefaultYAMLName, DEFAULT_PAGE_IDX, DEFAULT_PAGE_SIZE
from starwhale.base.uri import URI
from starwhale.base.type import URIType, EvalTaskType
from starwhale.base.view import BaseTermView
from starwhale.core.model.store import ModelStorage

from .model import Model, StandaloneModel


class ModelTermView(BaseTermView):
    def __init__(self, model_uri: str) -> None:
        super().__init__()

        self.raw_uri = model_uri
        self.uri = URI(model_uri, expected_type=URIType.MODEL)
        self.model = Model.get_model(self.uri)

    @BaseTermView._simple_action_print
    def remove(self, force: bool = False) -> t.Tuple[bool, str]:
        return self.model.remove(force)

    @BaseTermView._simple_action_print
    def recover(self, force: bool = False) -> t.Tuple[bool, str]:
        return self.model.recover(force)

    @BaseTermView._header
    def info(self, fullname: bool = False) -> None:
        self._print_info(self.model.info(), fullname=fullname)

    @BaseTermView._header
    def history(self, fullname: bool = False) -> None:
        self._print_history(
            title="Model History List", history=self.model.history(), fullname=fullname
        )

    def extract(self, force: bool = False, target_dir: str = "") -> None:
        console.print(":oncoming_police_car: try to extract ...")
        path = self.model.extract(force, target_dir)
        console.print(f":clap: extracted @ {path.resolve()} :tada:")

    @classmethod
    def eval(
        cls,
        target: str,
        yaml_name: str = DefaultYAMLName.MODEL,
        typ: str = "",
        kw: t.Dict[str, t.Any] = {},
    ) -> None:
        if in_production() or (os.path.exists(target) and os.path.isdir(target)):
            workdir = Path(target)
        else:
            uri = URI(target, URIType.MODEL)
            store = ModelStorage(uri)
            workdir = store.loc

        if typ in (EvalTaskType.CMP, EvalTaskType.PPL):
            console.print(f":golfer: try to eval {typ} @ {workdir}...")
            StandaloneModel.eval_user_handler(
                typ,
                workdir,
                yaml_name=yaml_name,
                kw=kw,
            )
        else:
            pass

    @classmethod
    @BaseTermView._pager
    @BaseTermView._header
    def list(
        cls,
        project_uri: str = "",
        fullname: bool = False,
        show_removed: bool = False,
        page: int = DEFAULT_PAGE_IDX,
        size: int = DEFAULT_PAGE_SIZE,
    ) -> t.Tuple[t.Dict[str, t.Any], t.Dict[str, t.Any]]:
        _uri = URI(project_uri, expected_type=URIType.PROJECT)
        _models, _pager = Model.list(_uri, page, size)
        BaseTermView._print_list(_models, show_removed, fullname)
        return _models, _pager

    @classmethod
    def build(
        cls, workdir: str, project: str, yaml_name: str = DefaultYAMLName.MODEL
    ) -> None:
        _model_uri = cls.prepare_build_bundle(
            workdir, project, yaml_name, URIType.MODEL
        )
        _m = Model.get_model(_model_uri)
        _m.build(Path(workdir), yaml_name)

    @classmethod
    def copy(cls, src_uri: str, dest_uri: str, force: bool = False) -> None:
        Model.copy(src_uri, dest_uri, force)
        console.print(":clap: copy done.")