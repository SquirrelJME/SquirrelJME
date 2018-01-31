#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Stores a bunch of classes into a list.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Need to compile the program
__cls="AppendClass"
__src="$__exedir/$__cls.java"
__odr="/tmp"
__out="$__odr/$__cls.class"

# Need to compile?
if [ ! -f "$__out" ] || [ "$__src" -nt "$__out" ]
then
	if ! javac -d "$__odr" "$__src" < /dev/null
	then
		echo "Failed to compile"
		exit 1
	fi
fi

# Execute it
java -classpath "$__odr" "$__cls" "$@"
exit $?

