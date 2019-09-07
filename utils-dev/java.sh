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

# Common directories
__exedir="$(dirname -- "$0")"
__tmpdir="$("$__exedir/tmpdir.sh")"

# Allow for the forcing of the simulated VM
if [ -z "$USE_SIMJVM" ] || [ "$USE_SIMJVM" -eq "0" ]
then
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
fi

# If there is a C compiler then use the Simulated JVM instead!
# This is a very primitive and basic JVM just for the building of
# SquirrelJME so it definitely is as very minimal as possible.
if which cc > /dev/null
then
	# JVM does not exist or is out of date?
	if [ ! -x "$__tmpdir/simjvm" ] || \
		[ "$__exedir/simjvm.c" -nt "$__tmpdir/simjvm" ]
	then
		if ! cc -o "$__tmpdir/simjvm" "$__exedir/simjvm.c" 1>&2
		then
			echo "Failed to build the Simulated JVM!" 1>&2
			exit 19
		fi
	fi
	
	# Execute it
	"$__tmpdir/simjvm" "$@"
	exit $?
fi

# Fail
echo "Could not find a Java executable!" 1>&2
exit 15
