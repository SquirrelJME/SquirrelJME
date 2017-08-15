#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Builds the MMIX operation table

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

while read __line
do
	__hex="$(echo "$__line" | cut -f 1)"
	__nam="$(echo "$__line" | cut -f 2 |
		sed 'y/qwertyuiopasdfghjklzxcvbnm/QWERTYUIOPASDFGHJKLZXCVBNM/')"
	__dsc="$(echo "$__line" | cut -f 3)"
	
	printf '/** %s. */\npublic static final int %s =\n\t%d;\n\n' \
		"$__dsc" "$__nam" "0x$__hex"
done


