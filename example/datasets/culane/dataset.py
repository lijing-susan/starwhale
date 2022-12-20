import json

import requests

from starwhale import Link, Image, dataset, MIMEType  # noqa: F401
from starwhale.utils.retry import http_retry

PATH_ROOT = "https://starwhale-examples.oss-cn-beijing.aliyuncs.com/dataset/culane"
DATA_PATH = "driver_182_30frame"
SEG_LABEL_PATH = f"laneseg_label_w16/{DATA_PATH}"


@http_retry
def request_link_text(anno_link):
    return requests.get(anno_link, timeout=10).text


def do_iter_item():
    ds = dataset("culane", create=True)
    tree = json.loads(request_link_text(f"{PATH_ROOT}/{DATA_PATH}/tree.json"))
    for dir in tree["children"]:
        dir_name_: str = dir["name"]
        if not dir_name_.endswith(".MP4") or dir["type"] != "directory":
            continue
        files = dir["children"]
        for f in files:
            f_name_: str = f["name"]
            if not f_name_.endswith(".jpg"):
                continue
            name_no_suff = f_name_.split(".")[0]
            splitlines = request_link_text(
                f"{PATH_ROOT}/{DATA_PATH}/{dir_name_}/{name_no_suff}.lines.txt"
            ).splitlines()
            annotation = {
                "mask": Link(
                    with_local_fs_data=False,
                    data_type=Image(
                        display_name=f"{name_no_suff}.png", mime_type=MIMEType.PNG
                    ),
                    uri=f"{PATH_ROOT}/{SEG_LABEL_PATH}/{dir_name_}/{name_no_suff}.png",
                ),
                "lines": splitlines,
            }
            ds.append(
                (
                    f"{dir_name_}/{name_no_suff}",
                    Link(
                        uri=f"{PATH_ROOT}/{DATA_PATH}/{dir_name_}/{f_name_}",
                        data_type=Image(display_name=f_name_, mime_type=MIMEType.JPEG),
                        with_local_fs_data=False,
                    ),
                    annotation,
                )
            )
    ds.commit()
    ds.close()


if __name__ == "__main__":
    do_iter_item()