#!/bin/bash

./gradlew clean installDist postInstallScript

mkdir -p ~/share/topobyte
rsync -av --delete exe/build/install/jeography-gis ~/share/topobyte/

./exe/build/setup/post-install.sh
