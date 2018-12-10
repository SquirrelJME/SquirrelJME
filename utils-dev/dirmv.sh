#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Moves a directory from one location to another within fossil,
# potentially merging them

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Prints usage
__help()
{
	echo "Usage: $0 [-x] (src) (dest)" 1>&2
	echo "    -x Do nothing, just print what would be done."
}

# Handle arguments
__noop="0"
while getopts "x" __arg
do
	case $__arg in
		x)
			__noop="1"
			;;
		
		*)
			__help
			exit 1
			;;
	esac 
done

# Down they go
shift $(($OPTIND - 1))

# Only two args
if [ "$#" -ne "2" ]
then
	__help
	exit 1
fi

# Read source and destination as absolute paths so it can handle recursive
# moving
__src="$("$__exedir/absolute.sh" "$1")"
__dest="$("$__exedir/absolute.sh" "$2")"

# Need to cut characters from the source path so it is known where they are
__cut="$(expr $(printf '%s' "$__src" | wc -m) + 2)"

# Go through each file in the source, only consider files
find "$__src" -type f | while read __file
do
	# Strip from the source to get the basepath
	__abs="$("$__exedir/absolute.sh" "$__file")"
	__basepath="$(echo "$__abs" | cut -c "$__cut-")"
	
	# Target file
	__target="$__dest/$__basepath"
	
	# Call other move script to handle this file
	if [ "$__noop" != 0 ]
	then
		"$__exedir/fmv.sh" "-x" "$__abs" "$__target"
	else
		"$__exedir/fmv.sh" "$__abs" "$__target"
	fi
done

