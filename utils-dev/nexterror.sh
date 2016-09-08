#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Find the next error code that is available.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Letter to number
__letter_to_number()
{
	case "$1" in
		A) __num="0";;
		B) __num="1";;
		C) __num="2";;
		D) __num="3";;
		E) __num="4";;
		F) __num="5";;
		G) __num="6";;
		H) __num="7";;
		I) __num="8";;
		J) __num="9";;
		K) __num="10";;
		L) __num="11";;
		M) __num="12";;
		N) __num="13";;
		O) __num="14";;
		P) __num="15";;
		Q) __num="16";;
		R) __num="17";;
		S) __num="18";;
		T) __num="19";;
		U) __num="20";;
		V) __num="21";;
		W) __num="22";;
		X) __num="23";;
		Y) __num="24";;
		Z) __num="25";;
		*) __num="0";;
	esac
	
	# Print it
	echo "$__num"
}

# Converts a number to a letter
__number_to_letter()
{
	case "$1" in
		0) __let="A";;
		1) __let="B";;
		2) __let="C";;
		3) __let="D";;
		4) __let="E";;
		5) __let="F";;
		6) __let="G";;
		7) __let="H";;
		8) __let="I";;
		9) __let="J";;
		10) __let="K";;
		11) __let="L";;
		12) __let="M";;
		13) __let="N";;
		14) __let="O";;
		15) __let="P";;
		16) __let="Q";;
		17) __let="R";;
		18) __let="S";;
		19) __let="T";;
		20) __let="U";;
		21) __let="V";;
		22) __let="W";;
		23) __let="X";;
		24) __let="Y";;
		25) __let="Z";;
		*) __num="A";;
	esac
	
	# Print it
	echo "$__let"
}

# Letter pair to number
__pair_to_number()
{
	__a="$(echo "$1" | cut -c 1)"
	__b="$(echo "$1" | cut -c 2)"
	
	# Convert
	expr $(expr "$(__letter_to_number $__a)" '*' "26") '+' \
		"$(__letter_to_number $__b)"
}

# Pair to number
__number_to_pair()
{
	__hi="$(expr "$1" / 26)"
	__lo="$(expr "$1" % 26)"
	
	# Convert
	echo "$(__number_to_letter $__hi)$(__number_to_letter $__lo)"
}

# Prefixes with banned prefixes
__prefixes()
{
	# Print banned ones
	echo "AS banned"
	echo "BJ banned"
	echo "BS banned"
	echo "FC banned"
	echo "FK banned"
	echo "FU banned"
	
	# Print normals
	"$__exedir/errorprefixes.sh"
}

# The last number used
__last="0"

# Go through error code list
__prefixes | sort | while read __line
do
	# Get code and extract number from it
	__code="$(echo "$__line" | cut -d ' ' -f 1)"
	__numb="$(__pair_to_number $__code)"
	
	echo "$__numb"
done | while read __number
do
	# Difference between this number and the last
	__diff="$(expr "$__number" - "$__last")"	
	__was="$__last"
	__last="$__number"
	
	# If the difference is at least 2 then there is a free slot after the last
	if [ "$__diff" -ge "2" ]
	then
		__number_to_pair "$(expr "$__was" + 1)"
		break
	fi
done

