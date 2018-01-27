#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Moves a file within the repository to another destination.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Prints usage
__help()
{
	echo "Usage: $0 [-x] (from...) (to)" 1>&2
	echo "    If there are multiple source files, to must be a dir." 1>&2
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

# Not enough arguments
if [ "$#" -lt "2" ]
then
	__help
	exit 1
fi

# Destination file or directory
__dest="$("$__exedir/lastarg.sh" "$@")"

# The number of files to consider
__count="$(($# - 1))"

# Copy of multiple files, target cannot be a non-directory
#if [ "$__count" -gt 1 ] && [ ! -d "$__dest" ]
#then
#	echo "Destination '$__dest' is not a directory!" 1>&2
#	exit 1
#fi
	
# Copy each file
for __i in $(seq 1 $__count)
do
	# Read source file
	__src="$1"
	shift
	
	# Determine the target file name
	if [ "$__count" -gt 1 ]
	then
		__target="$__dest/$(basename -- "$__src")"
	else
		if [ -d "$__dest" ]
		then
			__target="$__dest/$(basename -- "$__src")"
		else
			__target="$__dest"
		fi
	fi
	
	# Make relative for simpler handling
	__rsrc="$("$__exedir/relative.sh" "$__src")"
	__rtrg="$("$__exedir/relative.sh" "$__target")"
	
	# Mark how the file is moving
	echo "$__rsrc" 1>&2
	echo "    -> $__rtrg" 1>&2
	
	# If the source is a directory, use directory move script
	if [ -d "$__src" ]
	then
		if [ "$__noop" != 0 ]
		then
			"$__exedir/dirmv.sh" "-x" "$__src" "$__target"
			
		else
			"$__exedir/dirmv.sh" "$__src" "$__target"
		fi
	
	# Otherwise treat as normal file move/rename
	else
		__targetdir="$(dirname -- "$__target")"
		__maketargetdir="0"
		if [ ! -d "$__targetdir" ]
		then
			__maketargetdir="1"
			__rtd="$("$__exedir/relative.sh" "$__targetdir")"
			echo "    md $__rtd" 1>&2
		fi
		
		# Not actually going to move anything
		if [ "$__noop" != 0 ]
		then
			continue
		fi
		
		# Make target directory?
		if [ "$__maketargetdir" != "0" ]
		then
			mkdir -p "$__targetdir"
		fi
		
		# Move fossil file first
		if fossil mv "$__src" "$__target" > /dev/null
		then
			if ! mv "$__src" "$__target" > /dev/null
			then
				echo "    !! FAILED TO MOVE REGULAR FILE." 1>&2
			fi
		else
			echo "    !! FAILED TO MOVE FOSSIL FILE." 1>&2
		fi
	fi
done

# Okay!
exit 0

