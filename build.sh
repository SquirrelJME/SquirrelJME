#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
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

# The class to use for bootstrapping
: ${BOOTSTRAP_CLASS:=Bootstrap}

# The build class is missing or out of date?
if [ ! -f "$BOOTSTRAP_CLASS.class" ] || \
	[ "$__exedir/$BOOTSTRAP_CLASS.java" -nt "$BOOTSTRAP_CLASS.class" ]
then
	# Clear potential old stuff
	rm -f "$BOOTSTRAP_CLASS.class" "$BOOTSTRAP_CLASS\$*.class"
	
	# Build it
	echo "Building the build system..." 1>&2
	if ! "$JAVAC" -source 1.7 -target 1.7 -d . \
		"$__exedir/$BOOTSTRAP_CLASS.java"
	then
		echo "Failed to build the build system." 1>&2
		exit 1
	fi
fi

# Run it once to build the bootstrap
if ! "$JAVA" $JAVA_OPTIONS \
	"-Dproject.root=$__exedir" \
	"-Dnet.multiphasicapps.squirreljme.bootstrap.onlybuild=true" \
	"-Dnet.multiphasicapps.squirreljme.bootstrap.source=$__exedir/src" \
	"-Dnet.multiphasicapps.squirreljme.bootstrap.binary=$(pwd)" \
	"$BOOTSTRAP_CLASS" $*
then
	exit $?
fi

# Run it twice to actually perform the work
if [ "$BOOTSTRAP_CLASS" != "Build" ]
then
	if ! "$JAVA" $JAVA_OPTIONS \
		"-Dproject.root=$__exedir" \
		"-Dnet.multiphasicapps.squirreljme.bootstrap.onlybuild=false" \
		"-Dnet.multiphasicapps.squirreljme.bootstrap.source=$__exedir/src" \
		"-Dnet.multiphasicapps.squirreljme.bootstrap.binary=$(pwd)" \
		-classpath ".:sjmeboot.jar" "$BOOTSTRAP_CLASS" $*
	then
		exit $?
	fi
fi

