#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Runs a target project or JAR in a hosted environment.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# May be replaced
: ${JAVA:=java}

__print_usage()
{
	echo "Usage: $0 [-w] [-p #] (project|file.jar)" 1>&2
	echo "" 1>&2
	echo "  [-w]   Run with Wine instead" 1>&2
	echo "  [-p #] Can be 0 or greater to specify that an alternative" 1>&2
	echo "         application be used for a given program." 1>&2
}

# Usage
if [ "$#" -lt 1 ]
then
	__print_usage
	exit 1
fi

# Parse arguments
__javacmd="$JAVA"
__sepchar=":"
__numb=0
while getopts wp: __opt
do
	case "$__opt" in
		w)
			__javacmd="$__exedir/winejava.sh"
			__sepchar=";"
			;;
		
		p)
			__numb="$OPTARG"
			;;
		
		*)
			__print_usage
			exit 1
			;;
	esac
done

# Done
shift $(($OPTIND - 1))

# File is the first one
__file="$1"
shift

__proj="$(basename "$__file" .jar)"

# Build these projects because they are standard and may be relied upon
for __maybe in midp-lcdui meep-rms media-api
do
	if ! "$__exedir/../build.sh" build "$__maybe"
	then
		echo "Failed to build $__maybe" 1>&2
		exit 1
	fi
done

# Attempt building it, but ignore if it does not work because we might be
# running a non-SquirrelJME JAR
if ! "$__exedir/../build.sh" build "$__proj"
then
	echo "Did not build $__proj, ignoring." 1>&2
fi

# Generates classpath
__gen_classpath()
{
	# Build classpath
	__rv=""
	for __jar in sjmeboot.jar bins/*.jar
	do
		if [ "$__rv" != "" ]
		then
			__rv="$__rv$__sepchar"
		fi
		
		# Append
		__rv="$__rv$__jar"
	done
	
	# Add target file if it exists
	if [ -f "$1" ]
	then
		__rv="$__rv$__sepchar$1"
	fi
	
	# Use it
	echo "$__rv"
}

# The file to run
if [ -f "$__file" ]
then
	__run="$__file"
elif [ -f "bins/$__proj.jar" ]
then
	__run="bins/$__proj.jar"
else
	__run="$__file"
fi

# Run the JVM with the bootstrap followed
"$__javacmd" -classpath "$(__gen_classpath "$__run")" \
	$HOSTED_JAVA_OPTIONS \
	-Dcc.squirreljme.runtime.javase.clientmain="$("$__exedir/")" \
	cc.squirreljme.runtime.javase.Main \
	"-$__numb" "$__run" "$@"
exit $?

