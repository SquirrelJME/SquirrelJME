#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Gets the project name from the error prefix.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

if [ "$#" -ne "1" ]
then
	echo "Usage: $0 (prefix)" 1>&2
	exit 1
fi

# Go through prefixes and try to find it
"$__exedir/errorprefixes.sh" | while read __line
do
	__co="$(echo "$__line" | cut -d ' ' -f 1)"
	__pr="$(echo "$__line" | cut -d ' ' -f 2)"
	
	if [ "$__co" = "$1" ]
	then
		echo "$__pr"
		break
	fi
done

