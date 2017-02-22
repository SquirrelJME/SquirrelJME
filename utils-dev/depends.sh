#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Prints the dependencies of a project.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Project to get dependencies for
if [ "$#" -le "0" ]
then
	__name="$("$__exedir/projectname.sh")"
else
	__name="$1"
fi

# Recursive
__recursive_deps()
{
	# Convert manifest data
	__md="$(tr '\n' '\v' < \
		"$("$__exedir/projectwhere.sh" "$1")/META-INF/MANIFEST.MF" | \
		sed 's/\v //g' | tr '\v' '\n')"
	
	# Get dependencies
	__pd="$(echo "$__md" | grep -i 'x-squirreljme-depends' |
		sed 's/^[^:]*:[ \t]*//' | sed 's/,/ /g')"
	
	# If this is not cldc-compact, depend on it
	if [ "$1" != "cldc-compact" ]
	then
		echo "cldc-compact"
		
		# Depend on the normal CLDC
		if [ "$1" != "cldc" ]
		then
			echo "cldc"
		fi
	fi
		
	# Add them all
	(for __dep in $__pd
	do
		# That dependency
		echo "$__dep"
		
		# Recursive run
		__recursive_deps "$__dep"
	done) | sort | uniq | while read __line
	do
		echo "$__line"
	done
}

# Run
__recursive_deps "$__name" | sort | uniq

