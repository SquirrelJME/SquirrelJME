#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Reads a blog entry.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Not enough arguments?
if [ "$#" -lt "3" ]
then
	echo "Usage: $0 (year) (month) (day)" 1>&2
	echo "Usage: $0 (user) (year) (month) (day)" 1>&2
	exit 1
fi

# 3 has implied user
if [ "$#" -eq "3" ]
then
	__user="$("$__exedir/myname.sh")"

# 4 has explicit user
else
	__user="$1"
	shift
fi

# Build details
fossil unversion cat "developer-notes/$__user/$1/$2/$3.mkd" | less
exit $?


