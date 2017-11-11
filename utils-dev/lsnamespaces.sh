#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This lists the namespaces which are available

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Scan every directory for namespaces
__root="$__exedir/.."
(find "$__root" -type d | while read __dir
do
	if [ -f "$__dir/NAMESPACE.MF" ]
	then
		"$__exedir/relative.sh" "$__root" "$__dir"
	fi
done) | sort

