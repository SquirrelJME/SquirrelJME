#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Prints the name of the namespace.

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

# The namespace name is the last component of the base directory
"$__exedir/namespacebase.sh" "$__dir" | sed 's/.*\/\(.*\)$/\1/'

