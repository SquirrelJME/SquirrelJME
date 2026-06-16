#!/bin/sh
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Outputs the temporary directory

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Use this environment variable
if [ -n "$TMPDIR" ]
then
	echo "$TMPDIR"

# Otherwise a fixed directory
else
	echo "/tmp"
fi
