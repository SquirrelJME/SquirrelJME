#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Returns the path where the project is located

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Project to get path for
if [ "$#" -le "0" ]
then
	__name="$("$__exedir/projectname.sh")"
else
	__name="$1"
fi

# Go through all namespaces
for __ns in $("$__exedir/lsnamespaces.sh")
do
	# Ignore build
	if [ "$__ns" = "build" ]
	then
		continue
	fi
	
	__try="$__exedir/../$__ns/$__name"
	if [ -f "$__try/META-INF/MANIFEST.MF" ]
	then
		echo "$__try"
		exit 0
	fi
done

# Not found
exit 1

