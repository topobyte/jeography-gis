#!/bin/bash

./gradlew clean installDist linkerScript

rsync -av --delete exe/build/install/jeography-gis ~/share/topobyte/
