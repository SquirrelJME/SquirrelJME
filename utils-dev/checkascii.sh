#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Checks for files which do no contain the valid ASCII range.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Base directory of code
__rootdir="."

# Go through all files
echo "The following files contain non-ascii characters" 1>&2
find "$__rootdir" -type f | while read -r __file
do
	# Base file name
	__name="$(basename -- "$__file")"	
	
	# Ignore the fossil file
	if [ "$__name" = "_FOSSIL_" ] || [ "$__name" = ".fslckout" ]
	then
		continue
	fi
	
	# Grep it
	if grep -E '[^\x0A\x20-\x7E\t\r]' < "$__file" > /dev/null
	then
		echo "$__file"
	fi
done

