#!/bin/bash

./gradlew clean installDist postInstallScript

TARGET="$HOME/share/topobyte/jeography-gis/jeography-gis-snapshot"

mkdir -p "$TARGET"
rsync -av --delete exe/build/install/jeography-gis/ "$TARGET"

./exe/build/setup/post-install.sh
