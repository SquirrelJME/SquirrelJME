#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Cuts keys.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

tr '\t' ' ' | while read __line
do
	__name="$(echo "$__line" | cut -d ' ' -f 1)"
	__code="$(echo "$__line" | cut -d ' ' -f 2)"
	
	printf '
/** %s. */
public static final char %s =
	0x%04X;
' "$__name" "$__name" "$__code"
done

