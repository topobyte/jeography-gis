#!/bin/bash

sizes="16"

for s in $sizes; do
	mkdir -p "$s"
done

for f in *.png; do
	for s in $sizes; do
		echo $f $s;
		convert -resize "$s" "$f" "$s/$f"
	done
done
