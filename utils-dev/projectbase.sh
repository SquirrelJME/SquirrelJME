#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Gets the base of the current project

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
# Include two sets of .. in the event that we are in a given project
# Also try others since we may be in the project root.
while [ ! -f "$__base/squirreljme-version" ] &&
	[ ! -f "$__base/../squirreljme-version" ] &&
	[ ! -f "$__base/../../squirreljme-version" ]
do
	__base="$__base/.."
done

# Turn it into an absolute path
"$__exedir/absolute.sh" "$__base"

