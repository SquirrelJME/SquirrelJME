#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Likely a Linux only script, run the build to a given ZIP then
# extract it and try running it.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Perform the build and run it
rm -f "$$.zip"
if JAVA_OPTIONS="-Dnet.multiphasicapps.squirreljme.builder.dumptarget=true" \
	"$__exedir/../build.sh" $* "$$.zip"
then
	# Unzip then delete the ZIP
	unzip -o "$$.zip" squirreljme
	rm -f "$$.zip"
	
	# Run it
	chmod +x squirreljme
	./squirreljme
	exit $?
else
	# Failed, so fail
	rm -f "$$.zip"
	echo "Failed build..."
	exit 1
fi

