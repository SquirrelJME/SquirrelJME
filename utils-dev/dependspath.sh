#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Depends as classpath

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Project to get dependencies for
if [ "$#" -le "0" ]
then
	__name="$("$__exedir/projectname.sh")"
else
	__name="$1"
fi

# Go through them all
__i="0"
"$__exedir/depends.sh" "$__name" | while read __line
do
	# Separator?
	if [ "$__i" -eq "1" ] 
	then
		printf '%s' ":"
	fi
	__i="1"
	
	# Add line
	printf '%s' "bins/$__line.jar"
done
echo ""

