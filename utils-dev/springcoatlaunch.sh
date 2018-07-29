#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Launches the specified program using the SpringCoat VM.

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
__wineswitch=""
__numb=0
while getopts wp: __opt
do
	case "$__opt" in
		w)
			__wineswitch="-w"
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

# Forward to hosted launch with the known parameters
"$__exedir/hostedlaunch.sh" $__wineswitch springcoat-vm "$__file:$__numb" "$@"
exit $?

