#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Prints the main class for a project.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Print program usage
if [ "$#" -lt "1" ]
then
	echo "Usage: $0 [program]" 1>&2
	exit 1
fi

(if [ -f "$1" ]
then
	unzip -p "$1" META-INF/MANIFEST.MF
else
	__base="$("$__exedir/projectwhere.sh" "$1")"
	if [ -f "$__base/META-INF/TEST.MF" ]
	then
		echo "Main-Class: cc.squirreljme.tests._$(basename -- "$__base" .test |
			sed 's/-/_/g').MainTest"
	else
		cat "$__base/META-INF/MANIFEST.MF"
	fi
fi ) | tr -d '\r' | tr '\n' '|' | sed 's/| //g' | tr '|' '\n' | \
	while read __line
do
	echo "$__line" | grep -i '^ *main-class' | cut -d ':' -f 2 | \
		sed 's/^ *//g;s/ *$//g'
	echo "$__line" | grep -i '^ *midlet-1' | cut -d ':' -f 2 | \
		cut -d ',' -f 3 | sed 's/^ *//g;s/ *$//g'
done | sort | head -n 1


