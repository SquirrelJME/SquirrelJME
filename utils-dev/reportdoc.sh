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
	
	# Change generic back
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
done | while read __line
do
	# Extract bits
	__arch="$(echo "$__line" | cut -d '.' -f 1)"
	__name="$(echo "$__line" | cut -d '.' -f 2)"
	__vari="$(echo "$__line" | cut -d '.' -f 3)"
	__type="$(echo "$__line" | cut -d '.' -f 4)"
	
	# Split triplet
	__trip="$__arch.$__name.$__vari"
	
	# The base file form
	__xfil="${__arch}_${__name}_${__vari}"
	
	# Change generic to an asterisk to represent any
	if [ "$__arch" = "generic" ]
	then
		__disarch="*"
	else
		__disarch="$__arch"
	fi
	
	# New header
	if [ "$__last" != "$__trip" ]
	then
		echo " * _$__disarch.$__name.${__vari}_"
		__last="$__trip"
	fi
	
	# Depends on the type
	case "$__type" in
		"dev")
			echo "   * [Developer](${__xfil}__dev.mkd)"
			;;
		
		"user")
			echo "   * [User](${__xfil}__user.mkd)"
			;;
	esac
done

