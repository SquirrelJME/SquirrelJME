#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Outputs some random bytes as hexadecimal

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Need count?
if [ "$#" -le "0" ]
then
	echo "Usage: $0 (count)"
	exit 1
fi

# If outputting no bytes, ignore
__count="$1"
if [ "$__count" -le "0" ]
then
	exit 0
fi

# Real random source, if it exists
if [ -c /dev/urandom ] || [ -c /dev/random ]
then
	# Unblocked
	(if [ -c /dev/urandom ]
	then
		dd if=/dev/urandom "bs=$__count" count=1
		
	# Normal
	else
		dd if=/dev/random "bs=$__count" count=1
	fi) 2> /dev/null | hexdump -x |
		sed 's/^[0-9a-fA-F]\{1,\}//g' | tr -d ' ' | tr -d '\n' | tr -d '\t'
	
	# End with newline
	echo ""

# Not really random, but something
else
	# Seed the random number generator and get a value
	__seed="$( (echo -n $$ & echo -n $!; date +%s & echo -n $!) |
		tr -d ' ' | tr -d '\n' | sed 's/[^0-9]*//g' | cut -c 1-8)"
	
	# Loop
	__i=0
	while [ "$__i" -lt "$__count" ]
	do
		# Calculate a value
		__a="$(expr $__seed '*' 1337 | cut -c 1-8)"
		
		# Set new seed
		echo -n "" &
		__pid="$!"
		__seed="$__a$__pid"
		
		# Add all of the seed values together
		__rest="$__seed"
		__xa="0"
		__xb="1"
		__j="0"
		while [ -n "$__rest" ]
		do
			__char="$(echo "$__rest" | cut -c 1)"
			
			# Add to A or B?
			if [ "$__j" -eq "0" ]
			then
				__xa="$(expr "$__xa" + "$__char")"
				__j="1"
			else
				__xb="$(expr "$__xb" + "$__char")"
				__j="0"
			fi
			
			# Cut character
			__rest="$(echo "$__rest" | cut -c 2-)"
		done
		
		# Modulo 16 for hex
		__ya="$(expr $__xa % 16)"
		__yb="$(expr $__xb % 16)"
		
		# Echo individual hex characters
		for __q in $__ya $__yb
		do
			case $__q in
				10) printf '%s' "a";;
				11) printf '%s' "b";;
				12) printf '%s' "c";;
				13) printf '%s' "d";;
				14) printf '%s' "e";;
				15) printf '%s' "f";;
				*) printf '%s' "$__q";;
			esac
		done
		
		# Increment
		__i="$(expr $__i + 1)"
	done
	
	# End with newline
	echo ""
fi

