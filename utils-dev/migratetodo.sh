#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Migrates TODO exceptions

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through every file
__i=1
__j=1
grep -rl 'Error("TODO")' "$__exedir/.." | grep '\.java$' | while read __file
do
	# Replace
	if sed 's/Error("TODO")/todo.TODO()/g' < "$__file" > /tmp/$$
	then
		mv /tmp/$$ "$__file"
	fi
	
	# Commit?
	if [ "$__i" -ge "100" ]
	then
		# Go back
		__i="1"
		
		# Commit
		if ! fossil commit -m "Migrate TODO (files so far $__j)"
		then
			exit 1
		fi
	fi
	__i="$(expr $__i + 1)"
	__j="$(expr $__j + 1)"
done

# Commit
if ! fossil commit -m "Finish TODO migrate (files migrated $__j)"
then
	exit 1
fi

