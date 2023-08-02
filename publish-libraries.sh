#!/bin/bash

./gradlew -Ptopobyte clean \
	jeography-config:publish \
	jeography-core:publish \
	jeography-places:publish \
	jeography-tiles:publish \
	jeography-tools:publish \
	jeography-viewers:publish
