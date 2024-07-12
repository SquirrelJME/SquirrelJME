#!/bin/sh -e
javac RGBAlpha.java
java RGBAlpha
for __file in *.rgb
do
	uuencode -m "$__file" "$__file" > "$__file.__mime"
done
