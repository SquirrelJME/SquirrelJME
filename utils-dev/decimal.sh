#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Convert to decimal a bunch of values for byte array usage.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through all inputs
for __x in $*
do
	__v="$(printf '%d' $__x)"
	
	if [ "$__v" -gt "127" ]
	then
		echo -n "$(expr $__v - 256) "
	else
		echo -n "$__v "
	fi
done

echo

