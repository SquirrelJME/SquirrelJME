#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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

# Non-name test
__ntname="$(basename -- "$__name" ".test")"

# Go through all namespaces
for __ns in $("$__exedir/lsnamespaces.sh")
do
	# Ignore build
	if [ "$__ns" = "build" ]
	then
		continue
	fi
	
	# Try to find exact project name
	__try="$__exedir/../$__ns/$__name"
	__tst="$__exedir/../$__ns/$__ntname"
	if [ -f "$__try/META-INF/MANIFEST.MF" ] ||
		[ -f "$__try/META-INF/TEST.MF" ]
	then
		echo "$__try"
		exit 0
	fi
done

# Not found
exit 1

