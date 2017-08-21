#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This compiles the specified program and turns it into a JAD.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Check
if [ "$#" -ne "1" ]
then
	echo "Usage: $0 (file.jas/file.java)" 1>&2
	exit 1
fi

# Determine names
__jav="$(basename "$1" .java)"
__jas="$(basename "$1" .jas)"
__bas="$(basename "$(basename "$1" .java)" .jas)"
__cls="$__bas.class"
__jar="$__bas.jar"
__jad="$__bas.jad"

# Compiling Java source?
if [ "$__bas" = "$__jav" ]
then
	# Compile some base SquirrelJME APIs needed to run some of these
	for __api in midp-lcdui
	do
		if ! "$__exedir/../../build.sh" build "$__api"
		then
			echo "Failed to compile API $__api" 1>&2
			exit 5
		fi
	done
	
	# Compile
	if ! javac -source 1.3 -target 1.3 -d "." \
		-cp "$("$__exedir/../binscp.sh")" "$1"
	then
		echo "Failed to compile $1" 1>&2
		exit 4
	fi

# Assembling byte code
elif [ "$__bas" = "$__jas" ]
then
	if ! jasmin -d . "$1" 
	then
		echo "Failed to assemble $1" 1>&2
		exit 3
	fi

# Unknown?
else
	echo "Do not know how to compile $1" 1>&2
	exit 2
fi

# Create manifest
echo "Manifest-Version: 1.0
MIDlet-1: $__bas,,$__bas
MIDlet-Name: $__bas
MIDlet-Vendor: $__bas
MIDlet-Version: 1.0
MicroEdition-Configuration: CLDC-1.1
MicroEdition-Profile: MIDP-2.0
" > "$__jad"

# Create JAR file
if ! jar cfm "$__jar" "$__jad" "$__cls"
then
	echo "Failed to create JAR $__jar" 1>&2
	exit 6
fi

# Recreate a valid JAD
echo "Manifest-Version: 1.0
MIDlet-1: $__bas,,$__bas
MIDlet-Name: $__bas
MIDlet-Vendor: $__bas
MIDlet-Version: 1.0
MIDlet-Jar-URL: $__jar
MIDlet-Jar-Size: $(stat -c %s "$__jar")
MicroEdition-Configuration: CLDC-1.1
MicroEdition-Profile: MIDP-2.0
" > "$__jad"

# Note
echo "Built $__jar and $__jad" 1>&2
exit 0

