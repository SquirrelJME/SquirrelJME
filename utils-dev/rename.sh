#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Renames the given files and moves them under fossil by using
# the given regular expression.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Need at least two arguments
if [ "$#" -lt "2" ]
then
	echo "Usage: $0 (sed expression) (files...)"
	exit 1
fi

# Read the sed expression
__sed="$1"
shift

# Go through all the given files
while [ "$#" -ge "1" ]
do
	# Ignore directories
	if [ -d "$1" ]
	then
		continue
	fi
	
	# Get the directory the file is in and its base name
	__dir="$(dirname -- "$1")"
	__fna="$(basename -- "$1")"
	
	# No longer need the argument
	shift
	
	# Sed transform the file name
	__tra="$(echo "$__fna" | sed "$__sed")"
	
	# Only rename if the name has changed
	if [ -n "$__tra" ] && [ "$__tra" != "$__fna" ]
	then
		# Source and destination
		__src="$__dir/$__fna"
		__dst="$__dir/$__tra"
		
		# Perform the move
		if fossil mv "$__src" "$__dst"
		then
			mv -v "$__src" "$__dst"
		fi
	fi
done

