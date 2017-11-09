#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Prints the base directory of a namespace.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Get the base to use
if [ "$#" -eq "0" ]
then
	__base="$(pwd)"
else
	__base="$1"
fi

# Keep adding .. until the version identifier is found
# Include two sets of .. in the event that we are in a given namespace
# Also try others since we may be in the namespace root.
while true
do
	__base="$("$__exedir/absolute.sh" "$__base")"
	
	# Find the manifest for a project
	__above="$("$__exedir/absolute.sh" "$__base/../")"
	if [ -f "$__base/NAMESPACE.MF" ] ||
		[ -f "$__above/TIMESPACE.MF" ] ||
		[ -f "$__above/squirreljme-version" ] ||
		[ -f "$__base/squirreljme-version" ] ||
		[ "$__base" = "/" ]
	then
		echo "$__base"
		break
	fi
	
	# Otherwise go up
	__base="$__base/.."
done

