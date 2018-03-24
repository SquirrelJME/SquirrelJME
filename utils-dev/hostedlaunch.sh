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

# The file to run
if [ -f "$__file" ]
then
	__run="$__file"
elif [ -f "bins/bbld/$__proj.jar" ]
then
	__run="bins/bbld/$__proj.jar"
else
	__run="$__file"
fi

# Attempt building it if it has not been detected to be a normal JAR but a
# standard project
if [ ! -f "$__file" ]
then
	if ! "$__exedir/../build.sh" build -b "$__proj"
	then
		echo "Could not build $__proj." 1>&2
		exit 1
	fi

# Build these projects because they are standard and may be relied upon for
# running a given file
else
	for __maybe in midp-lcdui meep-rms media-api
	do
		if ! "$__exedir/../build.sh" build -b "$__maybe"
		then
			echo "Failed to build $__maybe" 1>&2
			exit 1
		fi
	done
fi

# Generates classpath
__gen_classpath()
{
	__rv=""
	
	# Prepend target if it exists
	if [ -f "$1" ]
	then
		__rv="$1"
	fi
	
	# Build classpath
	for __jar in sjmeboot.jar bins/bbld/*.jar
	do
		if [ "$__rv" != "" ]
		then
			__rv="$__rv$__sepchar"
		fi
		
		# Append
		__rv="$__rv$__jar"
	done
	
	# Use it
	echo "$__rv"
}

# Run the JVM with the bootstrap followed
__main="$("$__exedir/mainclass.sh" "$__run")"
"$__javacmd" -classpath "$(__gen_classpath "$__run")" \
	$HOSTED_JAVA_OPTIONS \
	$JAVA_OPTIONS \
	"-Dcc.squirreljme.runtime.javase.servermain=$__main" \
	"-Dcc.squirreljme.runtime.javase.program=$__numb" \
	cc.squirreljme.runtime.javase.Main "$@"
exit $?

