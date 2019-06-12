#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This quickly and easily launches the side by side debug.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Calculate hosted launch script location
__hosted="$("$__exedir/absolute.sh" "$__exedir/hostedlaunch.sh")"

# Go to temp dir
__tempdir="/tmp/sx"
if [ ! -d "$__tempdir" ]
then
	mkdir -p "$__tempdir"
fi
cd "$__tempdir"

# Execute handler
"$__hosted" sxs "$@"
