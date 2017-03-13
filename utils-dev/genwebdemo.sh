#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Generates the web demo.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Demo file location
__demo="webdemo.html"

# Build the web demo
if "$__exedir/build.sh" "webdemo" "$__demo"
then
	# Go to fossil directory
	cd "$__exedir"
	
	# Update
	fossil uv add "$__demo" --as "webdemo.html"
# Failed
else
	# Fail
	echo "Failed to build the web demo."
	exit 1
fi

