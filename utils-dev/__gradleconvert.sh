#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Converts "all" projects to Gradle projects

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Module directory
__modulesdir="$__exedir/../modules"

# Process all projects
"$__exedir/lsprojects.sh" | grep '^runt\/' | grep -v '\.test' |
	while read __project
do
	__basename="$(basename -- "$__project")"
	__moduledir="$__modulesdir/$__basename"
	
	echo "Processing $__project ($__basename)..."
	
	mkdir -p "$__moduledir"
	touch "$__moduledir/build.gradle"
	
	fossil add "$__moduledir/build.gradle"
	
	"$__exedir/fmv.sh" "$__exedir/../$__project" "$__moduledir/src/main/java"
done
