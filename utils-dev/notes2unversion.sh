#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This moves all of the developer notes from the repository to
# the unversioned space in fossil.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Copy all notes to unversioned areas
(cd "$__exedir/../src/developer-notes"; find -type f | sed 's/^\.\///g' | \
	grep '\.mkd' | grep '\/') | while read __line
do
	fossil unversion add "$__exedir/../src/developer-notes/$__line" \
		--as "developer-notes/$__line"
done

