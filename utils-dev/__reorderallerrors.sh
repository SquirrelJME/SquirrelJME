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
for __dir in "$__exedir/../apis/"* "$__exedir/../libs/"* "$__exedir/../mids/"*
do
	if [ ! -d "$__dir" ]
	then
		continue
	fi
	
	echo "$__dir"
done

