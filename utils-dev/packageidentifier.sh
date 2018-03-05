#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This returns the identifier of the package a path is in.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Get the base to use
if [ "$#" -eq "0" ]
then
	__dir="$(pwd)"
else
	__dir="$1"
fi

# Java files could be passed and the script is not really that smart enough
# to know if a passed input was really a directory or just a file, so just
# for the most part try to guess
__fn="$(basename -- "$__dir")"
if echo "$__fn" | grep -e '\.java$' -e '\.class$' -e '\.jas$' -e '\.j$' > /dev/null
then
	__dir="$__dir/.."
fi

# Build the package by continually going up until the target directory was
# reached
__base="$("$__exedir/projectbase.sh" "$__dir")"
__at="$("$__exedir/absolute.sh" "$__dir")"
__result=""
while true
do
	# Make sure it is always absolute
	__at="$("$__exedir/absolute.sh" "$__at")"
	
	# Stop if the base was reached
	if [ "$__base" -ef "$__at" ]
	then	
		break
	fi
	
	# Need to prefix with dot?
	if [ ! -z "$__result" ]
	then
		__result=".$__result"
	fi
	
	# Get the base and append it to the root
	__result="$(echo "$__at" | sed 's/.*\/\(.*\)$/\1/')$__result"
	
	# Go back up
	__at="$__at/.."
done

# Always make sure the package is valid somewhat
if [ -z "$__result" ]
then
	echo "unknown.package"
else
	echo "$__result"
fi

