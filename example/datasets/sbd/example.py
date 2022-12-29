import io

import numpy
from PIL import Image as PILImage
from PIL import ImageDraw

from starwhale import dataset

ds_name = "sbd/version/latest"
ds = dataset(ds_name)
row = ds["2008_000202"]
data = row.data
annotations = row.annotations
with PILImage.open(io.BytesIO(data.fp)) as img:
    draw = ImageDraw.Draw(img)

    msk = PILImage.fromarray(
        numpy.frombuffer(annotations["boundaries"], dtype=numpy.uint8).reshape(
            annotations["shape"]
        )
        * 50
    ).convert("RGBA")
    msk.putalpha(127)
    img.paste(msk, (0, 0), mask=msk)
    img.show()