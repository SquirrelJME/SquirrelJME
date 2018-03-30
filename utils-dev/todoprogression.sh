#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This counts the progression of TODO statements across the
# entire project over the course of the project.
#
# This detects:
#  * 'new Error("TODO")'
#  * 'new todo.TODO()'

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Requires the FUSE root
if [ "$#" -le "0" ]
then
	echo "Usage: $0 [Fossil Fuse Root]"
	exit 1
fi

# Need to compile the program
__cls="TodoProgression"
__src="$__exedir/$__cls.java"
__odr="/tmp"
__out="$__odr/$__cls.class"

# Need to compile?
if [ ! -f "$__out" ] || [ "$__src" -nt "$__out" ]
then
	if ! javac -d "$__odr" "$__src"
	then
		echo "Failed to compile"
		exit 1
	fi
fi

# The root where fuse projects are
__fuse="$1"

# Go through the tag list to get the tags to check for progression
cat "$__exedir/tagslist" | while read __tag
do
	# Get the base directory for that tag
	__base="$__fuse/checkins/$__tag/"
	
	# Go through all projects in the base
	"$__exedir/lsprojects.sh" "$__base" | while read __project
	do
		__uuid="$("$__exedir/projectuuid.sh" "$__base/$__project")"
		__count="$("$__exedir/todocount.sh" "$__base/$__project")"
		
		echo "$__tag" "$__project" "$__uuid" "$__count"
	done
done | java -classpath "$__odr" "$__cls" "$@"
exit $?

