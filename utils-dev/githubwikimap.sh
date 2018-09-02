#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Maps a file so that it works and links properly in the wiki
# Operates on standard input

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Newlines to vertical tabs so formatting becomes easier
# Split any markdown kind of looking block to its own line
tr '\n' '\v' | sed 's/(\([^)]*\.mkd\))/(\n@@@@\1\n)/g' |
	sed '/^@@@@.*\.mkd$/s/\//@d@/g' |
	sed '/^@@@@.*\.mkd$/s/-/@h@/g' |
	sed '/^@@@@/s/\.mkd//g' |
	sed 's/^@@@@//g' |
	tr -d '\n' |
	tr '\v' '\n'

