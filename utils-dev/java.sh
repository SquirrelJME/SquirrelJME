#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Runs the Java command that exists

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Use user specified binary
if [ -n "$JAVA" ] && which "$JAVA" > /dev/null
then
	"$JAVA" "$@"
	exit $?
fi

# Use main Java binary
if which java > /dev/null
then
	java "$@"
	exit $?
fi

# Fail
echo "Could not find a Java executable!" 1>&2
exit 15
