#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Searches through every revision for the specified regular
# expression across all files. This operation is slow and is really intended
# as a last resort.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Usage
if [ "$#" -lt "1" ]
then
	echo "Usage: (regexp)"
	exit 1
fi

# Go through every revision
fossil json timeline checkin | grep uuid |
	sed 's/.*"\([a-fA-F0-9]\{1,\}\)".*/\1/' | while read __uuid
do
	fossil ls -r "$__uuid" | while read __file
	do
		fossil cat "$__exedir/../$__file" -r "$__uuid" |
			grep -n $1 | while read __match
		do
			echo "$__uuid:$__file:$__match"
		done
	done
done

