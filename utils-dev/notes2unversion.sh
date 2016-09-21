#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
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

