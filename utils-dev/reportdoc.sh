#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Builds the port index.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

(for __file in *__dev.mkd *__user.mkd
do
	# Must exist
	if [ ! -f "$__file" ]
	then
		continue
	fi
	
	# Get the base before the user or developer marker
	__base="$(echo "$__file" | sed 's/^\(.*\)__[a-zA-Z]*\.mkd$/\1/g')"
	__type="$(echo "$__file" | sed 's/^.*__\([a-zA-Z]*\)\.mkd$/\1/g')"
	
	# Dot form of it
	__dots="$(echo "$__base" | sed 'y/_/./')"
	
	# Extract bits
	__arch="$(echo "$__dots" | cut -d '.' -f 1)"
	__name="$(echo "$__dots" | cut -d '.' -f 2)"
	__vari="$(echo "$__dots" | cut -d '.' -f 3)"
	
	# Rename generic arch so it is first
	if [ "$__arch" = "generic" ]
	then
		__arch="000generic"
	fi
	
	# Rename user so it is first
	if [ "$__type" = "user" ]
	then
		__type="000user"
	fi
	
	# output
	echo "$__arch.$__name.$__vari.$__type"
done) | sort | while read __line
do
	# Extract bits
	__arch="$(echo "$__line" | cut -d '.' -f 1)"
	__name="$(echo "$__line" | cut -d '.' -f 2)"
	__vari="$(echo "$__line" | cut -d '.' -f 3)"
	__type="$(echo "$__line" | cut -d '.' -f 4)"
	
	# Revert generic
	if [ "$__arch" = "000generic" ]
	then
		__arch="generic"
	fi
	
	# Revert user
	if [ "$__type" = "000user" ]
	then
		__type="user"
	fi
	
	# Repass
	echo "$__arch.$__name.$__vari.$__type"
done

