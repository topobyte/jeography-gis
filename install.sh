#!/bin/bash

./gradlew clean installDist

rsync -av --delete exe/build/install/jeography-gis ~/share/topobyte/
