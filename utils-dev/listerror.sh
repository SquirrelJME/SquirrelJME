#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Describe this.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# List errors
echo "******** LIST OF ERRORS *********" 1>&2
(grep -rl '{@squirreljme\.error[ \t]\{1,\}....' . | while read __file
do
	tr '\n' ' ' < "$__file" | sed 's/{@code[ \t]\{1,\}\([^}]*\)}/\1/g' |
		sed 's/{@squirreljme\.error[ \t]\{1,\}\([^}]*\)}/\v##ER \1 ##FI\v/g' |
		sed 's/\/\///g' | sed 's/\/\*//g' | tr '\v' '\n' |
		grep '##ER' | sed 's/^##ER[ \t]*//g' |
		sed 's/##FI/<'"$(basename $__file)"'>/g' |
		sed 's/[ \t]\{2,\}/ /g'
done) | sort
echo "*********************************" 1>&2

