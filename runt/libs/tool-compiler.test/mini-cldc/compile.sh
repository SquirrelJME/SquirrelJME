#!/bin/sh -e

# Compile sources
javac -source 1.7 -target 1.7 -d . -bootclasspath source-files -classpath source-files \
	source-files/*/*/*.java \
	source-files/*/*/*.java

# UUEncode classes
find | grep '\.class$' | while read __file
do
	uuencode -m "$__file" "$__file" > "$__file.__mime"
	rm "$__file"
done

# Build list
find | sed 's/^\.\///g' | sed 's/\.__mime//g' > list
