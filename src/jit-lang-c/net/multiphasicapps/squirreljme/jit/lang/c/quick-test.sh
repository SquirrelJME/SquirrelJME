#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Performs a quick test of the C based build.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Root directory of SquirrelJME
__rootdir="$__exedir/../../../../../../../../"

# Build
__target="lang-32+generic,undefined.c.posix"
if "$__rootdir/build.sh" native "$__target"
then
	# Find the highest number archive
	__highnum="0"
	for __file in squirreljme-*.zip
	do
		if [ ! -f "$__file" ]
		then
			continue
		fi
		
		# Make sure it is our target (ignore ZIPs of other targets)
		if ! unzip -p "$__file" "target" | grep "$__target" > /dev/null
		then
			continue
		fi
		
		# Better?
		__num="$(echo "$__file" |
			sed 's/^squirreljme-\([0-9]\{1,\}\)\.zip$/\1/g')"
		if [ "$__num" -gt "$__highnum" ]
		then
			__highnum="$__num"
		fi
	done
	
	# Determine ZIP to extract
	if [ "$__highnum" -eq "0" ]
	then
		__zip="$(pwd)/squirreljme.zip"
	else
		__zip="$(pwd)/squirreljme-$__highnum.zip"
	fi
	
	# Make temporary directory
	__temp="/tmp/csquirrel$$"
	mkdir "$__temp"
	
	# Go there and extract the ZIP
	__oldpwd="$(pwd)"
	cd "$__temp"
	unzip -o "$__zip"
	
	# Extract the C source code
	unzip -o "squirreljme_c.zip"
	
	# Compile everything
	if gcc -g3 -O0 -o squirreljme *.c
	then
		# Run it
		gdb --args ./squirreljme $*
	fi
	
	# Delete
	cd "$__oldpwd"
	rm -rf "$__temp"

# Failed
else
	echo "Build failed."
fi

