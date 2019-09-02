#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Runs the Java compiler command that exists

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Use user specified binary
if [ -n "$JAVAC" ] && which "$JAVAC" > /dev/null
then
	"$JAVAC" "$@"
	exit $?
fi

# Use main Java compiler
if which javac > /dev/null
then
	javac "$@"
	exit $?
fi

# Use secondary ECJ compiler
if which ecj > /dev/null
then
	ecj "$@"
	exit $?
fi

# Fail
echo "Could not find a Java compiler!" 1>&2
exit 17
