#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Compiles all the jasmin files.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Need uuencode
if ! which uuencode > /dev/null
then
	echo "Required: jasmin uuencode" 2> /dev/null
	exit 1
fi

# Find all jasmin files
find "$__exedir/.." -type f | grep '\.j$' | while read __file
do
	__dir="$(dirname "$__file")"
	__base="$(basename "$__file" .j)"
	__class="$__dir/$__base.class"
	__outdir="$("$__exedir/projectbase.sh")"
	
	if "$__exedir/jasmin.sh" -d "$__outdir" "$__file"
	then
		if [ ! -f "$__class" ]
		then
			echo "No class output for '$__file'?" 1>&2
			echo "Expected: '$__class'" 1>&2
			echo "Output directory: '$__outdir'" 1>&2
			exit 1
		fi
		
		# Encode it
		uuencode -m "$__base.class" < "$__class" > "$__class.__mime"
		rm -v "$__class"
	fi
done


