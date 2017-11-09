#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This returns the directory based form for a package.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Target a given directory or not
if [ "$#" -eq "0" ]
then
	__dir="$(pwd)"
else
	__dir="$1"
fi

# Same as the package identifier but with dots instead
"$__exedir/packageidentifier.sh" "$__dir" | tr '.' '/'

