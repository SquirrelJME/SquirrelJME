#!/bin/sh
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Emits a warning.

# Force C locale
export LC_ALL=C

# Common directories
__exedir="$(dirname -- "$0")"
__tmpdir="$("$__exedir/tmpdir.sh")"

echo "The underscore scripts are potentially dangerous!" 1>&2
exit 1

