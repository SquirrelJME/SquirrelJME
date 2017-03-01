#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
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

# Usage
if [ "$#" -lt 1 ]
then
	echo "Usage: $0 (project|file.jar)"
	exit 1
fi

# Determine project name, if possible
__file="$1"
__proj="$(basename "$__file" .jar)"
shift 1

# Build these projects because they are standard and may be relied upon
for __maybe in midp-lcdui meep-rms squirreljme-rms-file media-api
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
			__rv="$__rv:"
		fi
		
		# Append
		__rv="$__rv$__jar"
	done
	
	# Add target file if it exists
	if [ -f "$1" ]
	then
		__rv="$__rv:$1"
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
java -classpath "$(__gen_classpath "$__run")" \
	net.multiphasicapps.squirreljme.build.host.javase.HostedLaunch "$__run" $*
exit $?

