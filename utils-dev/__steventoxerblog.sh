#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Convert Steven to Xer in the blog.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# LS locks the database sadly
fossil uv ls > /tmp/$$.ls

# Go through all UV entries
cat /tmp/$$.ls | while read __line
do
	# Only accept notes from old name
	if ! echo "$__line" | grep '^developer-notes\/steven-gawroriski\/' > /dev/null
	then
		continue
	fi
	
	# New path
	__new="$(echo "$__line" | sed 's/steven-gawroriski/xer/g')"
	
	echo "## $__line -> $__new"
	
	# Extract it, copy, then remove it
	if fossil uv cat "$__line" > /tmp/$$
	then
		fossil uv add /tmp/$$ --as "$__new"
		fossil uv rm "$__line"
	fi
done

rm -f /tmp/$$ /tmp/$$.ls

