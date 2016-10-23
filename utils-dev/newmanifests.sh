#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: This script converts manifests from the old SquirrelJME format
# to the new one.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Need to compile the program
__cls="NewManifest"
__src="$__exedir/$__cls.java"
__odr="/tmp"
__out="$__odr/$__cls.class"

# Need to compile?
if [ ! -f "$__out" ] || [ "$__src" -nt "$__out" ]
then
	if ! javac -d "$__odr" "$__src"
	then
		echo "Failed to compile"
		exit 1
	fi
fi

# Go through all manifests
__i=0
for __file in */*/META-INF/MANIFEST.MF
do
	echo ">>> $__file"
	if java -classpath "$__odr" "$__cls" < "$__file" > /tmp/$$
	then
		mv /tmp/$$ "$__file"
		
		__i=$(expr "$__i" + 1)
		
		if [ "$__i" -ge "25" ]
		then
			__i=0
			fossil commit -m "Translate manifests."
		fi
	fi
done

