#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: This scans all source files for special JavaDoc tags which
# describe what the error codes mean.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Get the directory of the project
__base="$("$__exedir/projectbase.sh" "$(pwd)")"

# Print error code, potentially
echo "******** LIST OF ERRORS *********" 1>&2
if [ -f "$__base/META-INF/MANIFEST.MF" ]
then
	__code="$(grep -i 'x-squirreljme-error' < "$__base/META-INF/MANIFEST.MF" |
		cut -d ':' -f 2 | tr -d ' ')"
	
	echo "Project Error Code: $__code" 1>&2
fi

# List errors
(grep -rl '{@squirreljme\.error[ \t]\{1,\}....' "$__base" | while read __file
do
	tr '\n' ' ' < "$__file" | sed 's/{@code[ \t]\{1,\}\([^}]*\)}/\1/g' |
		sed 's/{@squirreljme\.error[ \t]\{1,\}\([^}]*\)}/\v##ER \1 ##FI\v/g' |
		sed 's/\/\///g' | sed 's/\/\*//g' | tr '\v' '\n' |
		grep '##ER' | sed 's/^##ER[ \t]*//g' |
		sed 's/##FI/<'"$(basename $__file)"'>/g' |
		sed 's/[ \t]\{2,\}/ /g'
done) | sort
echo "*********************************" 1>&2

