#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Makes sure that no file exceeds 80 columns

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Base directory of code
__rootdir="."

# Go through all files
echo "The following files exceed 79 characters" 1>&2
find "$__rootdir" -type f | while read -r __file
do
	# Base file name
	__name="$(basename -- "$__file")"	
	
	# Ignore the fossil file
	if [ "$__name" = "_FOSSIL_" ]
	then
		continue
	fi
	
	# Obtain extension
	__ext=""
		
	# Count number of lines past 80
	__nl="$(expand -t 4 < "$__file" | sed 's/^\(.\{0,79\}\)$//;/^$/d' | \
		wc -l)"
	
	# File has lines above 80
	if [ "$__nl" -ge "1" ]
	then
		# For line count cheat on stderr
		printf '%s' "$__file"
		
		# Cheat space on stderr
		printf '%s' " ($__nl)" 1>&2
		
		# Ending line
		echo
	fi
done

