#!/bin/bash

./gradlew clean installDist postInstallScript

rsync -av --delete exe/build/install/jeography-gis ~/share/topobyte/

./exe/build/setup/post-install.sh
