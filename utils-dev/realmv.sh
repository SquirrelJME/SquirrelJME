#!/bin/sh
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Performs a complex fossil move.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Only two args
if [ "$#" -ne "2" ]
then
	echo "Usage: $0 (src) (dest)" 1>&2
	exit 1
fi

# Forward to other move script
"$__exedir/fmv.sh" "$1" "$2"
exit $?

