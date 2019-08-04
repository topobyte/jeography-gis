#!/bin/bash

set -e

DIR=$(dirname $0)

pushd "$DIR" > /dev/null
./gradlew clean installDist setupScripts
popd

"$DIR"/exe/build/setup/install.sh
"$DIR"/exe/build/setup/post-install.sh
