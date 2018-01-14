#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Describe this.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Compile sorting script
if ! javac -d . "$__exedir/SortImports.java"
then
	echo "Failed to compile sorting program!" 1>&2
	exit 1
fi

# Go through all projects
"$__exedir/lsprojects.sh" | while read __project
do
	# Get project base path
	__path="$__exedir/../$__project"
	
	# Go through all Java files to sort imports
	find "$__path" -type f | grep '\.java$' | while read __file
	do
		java SortImports "$__file"
	done
	
	# Commit these changes
	fossil commit -m "Sort imports for $__project."
done

