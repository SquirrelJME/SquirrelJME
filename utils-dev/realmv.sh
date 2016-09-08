#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Performs a complex fossil move.

# Only two args
if [ "$#" -ne "2" ]
then
	echo "Usage: $0 (src) (dest)" 1>&2
	exit 1
fi

# Source and dest
mkdir -p "$2"
SRC="$(readlink -f -- "$1")"
DST="$(readlink -f -- "$2")"

# Character in the base
CC="$(expr $(echo -n "$SRC" | wc -m) + 2)"

# Go through source files
find "$SRC" -type f | while read -r file
do
	# Strip base directory from the input
	SB="$(echo "$file" | cut -c $CC-)"	
	
	# Destination filename
	DF="$DST/$SB"
	
	# Destination directory
	DB="$(dirname -- "$DF")"
	
	# Make target directory
	mkdir -p "$DB"
	
	# Move file there
	if fossil mv "$file" "$DF"
	then
		mv "$file" "$DF"
	fi
done

# Done
exit 0

