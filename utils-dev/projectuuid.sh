#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This returns the UUID of the specified project, this is used
# to keep track of which projects were just renames and such and which ones
# are new.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Project to get dependencies for
if [ "$#" -le "0" ]
then
	__now="$(pwd)"
else
	__now="$1"
fi

# Get the base of the project
__base="$("$__exedir/projectbase.sh" "$__now")"


# Go through the manifest which can contain multiple fields for the UUID
# since the field has changed throughout the project
if [ -f "$__base/META-INF/MANIFEST.MF" ]
then
	# Determine Default fallback UUID
	__defl="00000000-0000-0000-0000-$(fossil sha1sum - < \
		"$__base/META-INF/MANIFEST.MF" | cut -c 1-12)"
	
	# Get sum
	echo "X-SquirrelJME-UUID:$__defl" | cat "$__base/META-INF/MANIFEST.MF" - |
	grep -i -e 'X-Hairball-UUID' -e 'X-SquirrelJME-UUID' | \
		cut -d ':' -f 2 | head -n 1 | tr -d ' '

# Not known othewise
else
	echo "00000000-0000-0000-0000-000000000000"
fi



