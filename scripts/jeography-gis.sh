#!/bin/bash

DIR=$(dirname $0)
LIBS="$DIR/../build/lib-run"
LOG_CONFIG="$DIR/../log4j.properties"

if [ ! -d "$LIBS" ]; then
	echo "Please run 'gradle createRuntime'"
	exit 1
fi

CLASSPATH="$LIBS/*"

exec java -cp "$CLASSPATH" -Dlog4j.configuration="file:$LOG_CONFIG" "$@"
