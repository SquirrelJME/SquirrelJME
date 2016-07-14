#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: This shows the list of error prefixes.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through all projects
(for __dir in "$__exedir/../src/"*
do
	__man="$__dir/META-INF/MANIFEST.MF"
	if [ -f "$__man" ]
	then
		__err="$(sed \
			'y/abcdefghijklmnopqrstuvwxyz/ABCDEFGHIJKLMNOPQRSTUVWXYZ/' \
			< "$__man" | grep 'X-SQUIRRELJME-ERROR' |
			sed 's/X-SQUIRRELJME-ERROR[ \t]*:[ \t]*\([^ \t]*\)[ \t]*/\1/')"
		if [ -n "$__err" ]
		then
			echo "$__err $(basename $__dir)"
		fi
	fi
done) | sort

