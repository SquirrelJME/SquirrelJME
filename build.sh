#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Builds whatever is needed to support hairball and then invokes
# it using the host virtual machine.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Default compilers and run-times
: ${JAVA:=java}
: ${JAVAC:=javac}

# The build class is missing or out of date?
if [ ! -f "Build.class" ] || [ "$__exedir/Build.java" -nt "Build.class" ]
then
	# Clear potential old stuff
	rm -f "Build.class" "Build\$*.class"
	
	# Build it
	echo "Building the build system..." 1>&2
	if ! "$JAVAC" -source 1.7 -target 1.7 -d . "$__exedir/Build.java"
	then
		echo "Failed to build the build system." 1>&2
		exit 1
	fi
fi

# Run it
"$JAVA" $JAVA_OPTIONS "-Dproject.root=$__exedir" "Build" $*
exit $?

