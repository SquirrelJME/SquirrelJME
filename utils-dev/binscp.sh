#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Writes the bins classpath

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Look in the local bins directory
__peg="0"
for __jar in bins/*.jar
do
	if [ "$__peg" -eq "0" ]
	then
		__peg="1"
	else
		printf '%s' ":"
	fi
	printf '%s' "$__jar"
done

# New line
echo ""
