#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This is used to migrate from using the combined source and
# resource Java classes and resources, due to Gradle differences from the
# SquirrelJME build system.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Build gradles are where projects exist
find "$__exedir/.." -type f | grep 'build.gradle$' | while read __gradle
do
	__projectdir="$(dirname -- "$__gradle")"

	# For both main and test groups
	for __group in main test
	do
		__srcdir="$__projectdir/src/$__group/java"
		__resdir="$__projectdir/src/$__group/resource"

		# Might not be a directory
		if [ ! -d "$__srcdir" ]
		then
			continue
		fi

		# Move files over to resources
		find "$__srcdir" -type f | grep -v '\.java$' | while read __file
		do
			# Get relative to the source base
			__relpath="$("$__exedir/relative.sh" "$__srcdir" "$__file")"

			# It will be placed in the resource root now
			__target="$__resdir/$__relpath"

			# Perform the move
			"$__exedir/fmv.sh" "$__file" "$__target"
		done
	done
done

