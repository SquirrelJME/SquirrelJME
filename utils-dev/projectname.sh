#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Prints the name of a project.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Directory to get dependencies for
if [ "$#" -ge "0" ]
then
	__dir="$(pwd)"
else
	__dir="$1"
fi

# Get the project name
grep -i '^x-squirreljme-name' < \
	"$("$__exedir/projectbase.sh" "$__dir")/META-INF/MANIFEST.MF" | \
	cut -d ':' -f 2 | sed 's/^[ \t]\([^ \t]*\)[ \t]*$/\1/g'
