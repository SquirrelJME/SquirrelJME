#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Uploads blogs from k8

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through all notes
find "$1/vmjar/developer-blog/steven-gawroriski" | grep '\.mkd$' | 
	while read __line
do
	# Add disclaimer so people know!
	sed '1 a\
\
***DISCLAIMER***: _These notes are from the defunct k8 project which_\
_precedes SquirrelJME. The notes for SquirrelJME start on 2016/02/26!_' \
	< "$__line" > /tmp/$$
	
	# Determine the base of the output
	__target="developer-notes/xer/$(echo "$__line" |
		sed 's/.*steven-gawroriski\///')"
	echo "$__line -> $__target"
	
	# Add to the note archive
	fossil uv add "/tmp/$$" -as "$__target"
done

# Cleanup
rm -f /tmp/$$

