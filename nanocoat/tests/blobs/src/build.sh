#!/bin/bash

# Compile all files
for file in *.java
do
	if ! javac "$file"
	then
		exit 1
	fi
done

# Compress them all into a single Jar
if ! 7z a -tzip -mx=9 -- mock.jar *.class hello.txt
then
	exit 1
fi

# Encode all of these
for file in mock.jar *.class hello.txt
do
	uuencode -m "$file" "$file" > "../$file.__mime"
done