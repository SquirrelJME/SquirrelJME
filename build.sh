#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
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
: ${BOOTSTRAP_CLASS:=NewBootstrap}

# If the Java compiler was not detected, try ECJ instead
if ! which "$JAVAC" > /dev/null
then
	__ecj="$(which ecj 2> /dev/null)"
	if [ "$__ecj" != "" ]
	then
		JAVAC="$__ecj"
	fi
fi

# The build class is missing or out of date?
if [ ! -f "$BOOTSTRAP_CLASS.class" ] || \
	[ "$__exedir/$BOOTSTRAP_CLASS.java" -nt "$BOOTSTRAP_CLASS.class" ]
then
	# Clear potential old stuff
	rm -f "$BOOTSTRAP_CLASS.class" "$BOOTSTRAP_CLASS\$$*.class"
	
	# Build it
	echo "Building the build system..." 1>&2
	if ! "$JAVAC" -source 1.7 -target 1.7 -d . \
		"$__exedir/$BOOTSTRAP_CLASS.java"
	then
		echo "Failed to build the build system." 1>&2
		exit 1
	fi
fi

# JamVM on older Debian versions has trouble when tools.jar is dynamically
# loaded, it cannot find the resources JAR.
__flaw="$("$JAVA" $JAVA_OPTIONS "$BOOTSTRAP_CLASS" "--toolsflaw" |
	grep '\.jar$' | head -n 1)"

# No flaw was detected, operate normally
if [ -z "$__flaw" ]
then
	__cpflaw="."
	__bsflaw="sjmeboot.jar"

# Otherwise use the library which was found
else
	__cpflaw=".:$__flaw"
	__bsflaw="$__flaw:sjmeboot.jar"
	
	# Add a note that the flaw is being worked around
	echo "Working around tools.jar flaw ($__flaw)." 1>&2
fi

# Run it once to build the bootstrap
if ! "$JAVA" $JAVA_OPTIONS \
	"-Dnet.multiphasicapps.squirreljme.bootstrap.binary=$(pwd)" \
	"-Dnet.multiphasicapps.squirreljme.builder.root=$__exedir" \
	-classpath "$__cpflaw" "$BOOTSTRAP_CLASS" "$@"
then
	exit 1
fi

# Run it again to run the bootstrap
if ! "$JAVA" $JAVA_OPTIONS \
	"-Dnet.multiphasicapps.squirreljme.bootstrap.binary=$(pwd)" \
	"-Dnet.multiphasicapps.squirreljme.builder.root=$__exedir" \
	"-Dnet.multiphasicapps.squirreljme.runtime.javase.java=$JAVA" \
	"-Dnet.multiphasicapps.squirreljme.runtime.javase.bootpath=sjmeboot.jar" \
	-cp "$__bsflaw" "net.multiphasicapps.squirreljme.runtime.javase.Main" "$@"
then
	exit 1
fi

