#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This creates a PNG image from a text output, so it can be drawn
# into and such.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Calculate hosted launch script location
__hosted="$("$__exedir/absolute.sh" "$__exedir/hostedlaunch.sh")"

# Go to temp dir
__tempdir="/tmp/sx"
if [ ! -d "$__tempdir" ]
then
	mkdir -p "$__tempdir"
fi
cd "$__tempdir"

# Arguments
__pkg="$1"
__cls="$2"

# All methods or a single one?
if [ "$#" -eq "3" ]
then
	__mth=""
	__out="$3"
else
	__mth="$3"
	__out="$4"
fi

# Execute handler, export file first
if "$__hosted" sxs "$__pkg" "$__cls" "$__mth" > "/tmp/$$"
then
	# Then convert it to a PBM
	if "$__hosted" txt-to-pbm "/tmp/$$" > "/tmp/$$.pbm"
	then
		if which pnmtopng > /dev/null
		then
			# Convert
			pnmtopng "/tmp/$$.pbm" > "$__out"
			
			# Cleanup
			rm -f "/tmp/$$.pbm"
		else
			mv -f "/tmp/$$.pbm" "$__out"
		fi
	else
		# Cleanup
		rm -f "/tmp/$$.pbm"
	fi
fi

# Cleanup
rm -f "/tmp/$$"
