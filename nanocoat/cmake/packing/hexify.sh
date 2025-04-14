#!/bin/sh

# Which ones are available?
__xd="$(which xxd)"
__hd="$(which hexdump)"

# There needs to be at least one of these
if [ -z "$__xd" ] && [ -z "$__hd" ]
then
	echo "No hexdump executables found." 1>&2
	exit 1
fi

# Go through all binary files that would exist
find . -type f | grep -e '\.ico$' | while read -r __file
do
	if [ -n "$__xd" ]
	then
		xxd -p < "$__file" | tr -d '\n' | fold -w 78 > "$__file.__hex"
	else
		hexdump -ve '1/1 "%02x"' < "$__file" | fold -w 78 > "$__file.__hex"
	fi
done
