#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2017 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2017 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: This script mass bumps copyrights

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Just update anything with an older copyright
__count=0
grep -rl 'Copyright (C) 20[0-9]\{2\}-2016' "$__exedir/.." | \
	grep -e '\.java$' -e '\.sh$' -e '\.cmd$' |
	while read __file
do
	# Just change how things are done
	if sed 's/Copyright (C) 20[0-9]\{2\}-20[0-9]\{2\}/Copyright (C)/g' \
		< "$__file" | sed 's/For more information see license\.mkd\./See license.mkd for licensing and copyright information./g' > /tmp/$$
	then
		mv -v /tmp/$$ "$__file"
	fi
	
	__count="$(expr "$__count" + 1)"
	
	if [ "$__count" -ge "100" ]
	then
		fossil commit -m "Update copyright statement."
		__count=0
	fi
done

