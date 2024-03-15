#!/bin/sh
/usr/lib/sdk/openjdk11/jvm/openjdk-11/bin \
	-jar /app/bin/squirreljme-standalone.jar "$@"
exit $?
