#!/bin/sh
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Creates and then applies the all patches

# Force C locale
export LC_ALL=C

# Directory of this script
__execDir="$(dirname -- "$0")"

# Try to make less files if things happen to fail
if [ "$PPID" != "" ] && [ "$PPID" -ne "0" ]
then
	__pid="$PPID"
else
	__pid="$$"
fi

# Delete temporary patch
__patch="/tmp/$$.patch"
rm -f -- "$__patch"

# Go through all C files
find . -type f | grep '\.c$' | while read __source
do
	# Process the file
	"$__execDir/nanocoat-throws.sh" "$__source" > "$__patch"
	
	# Is this patch even valid?
	__lines="$(wc -l < "$__patch")"
	if [ -s "$__patch" ] && [ "$__lines" -gt "2" ]
	then
		echo "APPLYING $__patch"
		patch -p1 < "$__patch"
	fi
done

# Clear temporary patch
rm -f -- "$__patch"
