#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Calculates the absolute path of a file

# File to check
__check="$1"

# path is absolute, just needs . and .. removed
if echo "$__check" | grep '^\/' > /dev/null
then
	__yuck="$__check"
	
# Get relative path from PWD
else
	__yuck="$(pwd)/$__check"
fi

# Split path by slashes and handle each line
# Need file due to argument stuff.
rm -f /tmp/$$.abs
touch /tmp/$$.abs
echo "$__yuck" | sed 's/\//\n/g' | while read __seg
do
	# If this segment is ".", ignore it
	# Also ignore blank segments too
	if [ "$__seg" = "." ] || [ -z "$__seg" ]
	then
		continue
	
	# If this is .., remove the topmost path element
	elif [ "$__seg" = ".." ]
	then
		sed 's/\/[^/]*$//g' < /tmp/$$.abs > /tmp/$$.abs2
		mv /tmp/$$.abs2 /tmp/$$.abs
	
	# Otherwise append to path
	else
		printf '%s' "/$__seg" >> /tmp/$$.abs
	fi
done

# Blank result ends in /
__result="$(cat /tmp/$$.abs)"
rm -f /tmp/$$.abs
if [ -z "$__result" ]
then
	echo "/"
else
	echo "$__result"
fi

