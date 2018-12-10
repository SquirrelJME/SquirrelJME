#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Calculates the relative path from one target to another

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# No base specified, just a target
if [ "$#" -eq "1" ]
then
	__base="$(pwd)"
	__targ="$1"

# Base and target specified
elif [ "$#" -gt "1" ]
then
	__base="$1"
	__targ="$2"

# Bad command call
else
	echo "Usage: $0 [target]" 1>&2
	echo "       $0 [base] [target]" 1>&2
	exit 1
fi

# Make absolute
__base="$("$__exedir/absolute.sh" "$__base")"
__targ="$("$__exedir/absolute.sh" "$__targ")"

# Keep going up to skip the parts of the path which are exactly the same
__i=2
while true
do
	# Extract both components	
	__a="$(echo "$__base" | cut -d '/' -f "$__i")"
	__b="$(echo "$__targ" | cut -d '/' -f "$__i")"
	
	if [ "$__a" != "$__b" ]
	then
		break
	fi
	
	__i="$(($__i + 1))"
done

# Go up the base directory and make dots for every path element
__j="$__i"
while true
do
	__a="$(echo "$__base" | cut -d '/' -f "$__j")"
	__j="$(($__j + 1))"
	
	# Nothing left to add
	if [ -z "$__a" ]
	then
		break;
	fi
	
	# Add slash
	printf '%s' "../"
done

# Append elements on the second set from the base to the result
__j="$__i"
while true
do
	__b="$(echo "$__targ" | cut -d '/' -f "$__j")"
	__j="$(($__j + 1))"
	
	# Nothing left to add
	if [ -z "$__b" ]
	then
		break
	fi
	
	# Add that element
	printf '%s' "$__b"
	
	# If this is not the last element, add the slash
	__zb="$(echo "$__targ" | cut -d '/' -f "$__j")"
	if ! [ -z "$__zb" ]
	then
		printf '%s' "/"
	fi
done

# Ending newline
echo

