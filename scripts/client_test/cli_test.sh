#!/usr/bin/env bash

set -x

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
REPO_PATH=$( cd -- "$SCRIPT_DIR/../.." &> /dev/null && pwd )
export WORK_DIR=`mktemp -d`
export SW_CLI_CONFIG="$WORK_DIR/config.yaml"
export SW_LOCAL_STORAGE="$WORK_DIR/data"
# deletes the temp directory
function cleanup {
  rm -rf "$WORK_DIR"
  echo "Deleted temp working directory $WORK_DIR"
}
trap cleanup EXIT
rsync -q -av $REPO_PATH/client $WORK_DIR --exclude venv --exclude .venv
rsync -q -av $REPO_PATH/example/ucf101 $WORK_DIR/example --exclude venv --exclude .venv --exclude .starwhale --exclude data --exclude models
rsync -q -av $REPO_PATH/example/runtime/pytorch-e2e $WORK_DIR/example/runtime --exclude venv --exclude .venv --exclude .starwhale
rsync -q -av $REPO_PATH/scripts/example $WORK_DIR/scripts --exclude venv --exclude .venv --exclude .starwhale
cp $REPO_PATH/README.md $WORK_DIR
pushd $WORK_DIR
python3 -m venv venv && . venv/bin/activate && python3 -m pip install --upgrade pip
if [[ -n $PYPI_RELEASE_VERSION ]] ; then
  python3 -m pip install starwhale==$PYPI_RELEASE_VERSION
else
  python3 -m pip install -e client
fi
swcli --version

ls -lah ${REPO_PATH}/client/dist
ls -lah ${WORK_DIR}/client/dist

if [ -f "${REPO_PATH}/client/dist/starwhale-100.0.0-py3-none-any.whl" ]; then
    SRC_PATH=${REPO_PATH}/client/dist/starwhale-100.0.0-py3-none-any.whl
else
    python3 -m pip install -r client/requirements-install.txt
    make -C client build-wheel
    SRC_PATH=${WORK_DIR}/client/dist/starwhale-${PYPI_RELEASE_VERSION:=0.0.0.dev0}-py3-none-any.whl
fi
cp ${SRC_PATH} ${WORK_DIR}/scripts/example/starwhale-0.0.0.dev0-py3-none-any.whl

popd

bash "$SCRIPT_DIR"/update_controller_setting.sh
for i in $@; do
    python3 "$SCRIPT_DIR"/cli_test.py $i || exit 1
done
