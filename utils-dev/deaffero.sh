#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Removes the afferro moniker from a given number of files.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Only change the first 25 files (to prevent massive commits)
__from="GNU Affero General Public License v3"
grep -rl -e 'GNU Affero General Public License v3\+\{1\}' | \
	sort | head -n 33 | while read __file
do
	if sed "s/$__from/GNU General Public License v3/g" < "$__file" > /tmp/$$
	then
		mv /tmp/$$ "$__file"
	fi
done

