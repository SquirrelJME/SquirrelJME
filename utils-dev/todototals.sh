#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Prints the totals for all namespaces.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Allow a custom root to be specified
if [ "$#" -ge "1" ]
then
	__root="$1"
else
	__root="$__exedir/.."
fi

# Count through all projects
"$__exedir/lsprojects.sh" | while read __project
do
	__count="$("$__exedir/todocount.sh" "$__root/$__project")"
	
	echo "$__project $__count"
done

