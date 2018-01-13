#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This will go through all projects and re-domain every
# reference from the old domain to the new one.
# `net.multiphasicapps.squirreljme` -> `cc.squirreljme`.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through all projects
"$__exedir/lsprojects.sh" | while read __project
do
	# Get project base path
	__path="$__exedir/../$__project"
	
	# If the old directory set exists, it will be moved
	if [ -d "$__path/net/multiphasicapps/squirreljme" ]
	then
		"$__exedir/realmv.sh" "$__path/net/multiphasicapps/squirreljme" \
			"$__path/cc/squirreljme"
	fi
	
	# Replace all instances of the old domain with the new one
	grep -rl 'net\.multiphasicapps\.squirreljme' "$__path" | while read __file
	do
		sed 's/net\.multiphasicapps\.squirreljme/cc.squirreljme' < "$__file" \
			> /tmp/$$
		mv -v /tmp/$$ "$__file"
	done
	
	# Commit these changes
	fossil commit -m "Re-domain for $__project."
done

