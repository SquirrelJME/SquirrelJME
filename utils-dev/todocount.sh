#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Counts the number of TODOs in the current project

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Allow a custom directory to be specified
if [ "$#" -ge "1" ]
then
	__dir="$1"
else
	__dir="$(pwd)"
fi

# Get the directory of the project
__base="$("$__exedir/projectbase.sh" "$__dir")"

# Keep counts of everything per project
grep -cR 'todo\.TODO' "$__base" | while read __line
do
	echo "$__line" | cut -d ':' -f 2
done | datamash -t ' ' sum 1

