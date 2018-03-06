#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: List all relative projects from the base of the source code.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through all namespaces
__root="$__exedir/.."
("$__exedir/lsnamespaces.sh" | while read __dir
do
	# And directories within the namespaces
	for __file in "$__root/$__dir/"*
	do
		# Ignore non-directories
		if [ ! -d "$__file" ]
		then
			continue
		fi
		
		# If there is a project here, print that directory
		if [ -f "$__file/META-INF/MANIFEST.MF" ] ||
			[ -f "$__file/META-INF/TEST.MF" ]
		then
			"$__exedir/relative.sh" "$__root" "$__file"
		fi
	done
done) | sort

