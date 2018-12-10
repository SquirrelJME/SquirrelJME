#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This scans the directory tree for classes which are really
# long and have really long names, it will list them all.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

find "$__exedir/.." -type f | grep '\.java$' | while read __file
do
	__classname="$("$__exedir/longclass.sh" "$__file")"
	__charlen="$(printf '%s' "$__classname" | wc -c)"
	
	# SquirrelJME forces 79 columns, so this means
	# "import ;" causes a loss of 8 from 79, so characters cannot exceed
	# 71, otherwise it is too long. But for consistency this will be 70
	if [ "$__charlen" -ge "70" ]
	then
		echo "$__classname ($__charlen) <$("$__exedir/projectname.sh" "$__file")>"
	fi
done

