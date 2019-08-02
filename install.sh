#!/bin/bash

DIR=$(dirname $0)

pushd "$DIR" > /dev/null
./gradlew clean installDist postInstallScript
popd

TARGET="$HOME/share/topobyte/jeography-gis/jeography-gis-snapshot"

mkdir -p "$TARGET"
rsync -av --delete "$DIR/exe/build/install/jeography-gis/" "$TARGET"

"$DIR"/exe/build/setup/post-install.sh
