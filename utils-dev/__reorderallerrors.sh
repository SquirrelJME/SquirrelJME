#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This goes through all projects and reorders all of their errors

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through every single project
"$__exedir/lsprojects.sh" | while read __project
do
	echo ">> $__project"
	"$__exedir/reordererrors.sh" "$__exedir/../$__project"
	fossil commit -m "Reorder errors in $__project"
done

