#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Builds winter coat; Builds a ROM for winter coat; Then
# executes

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Build the wintercoat ROM first
if ! "$__exedir/../build.sh" wintercoat-rom wintercoat.rom $*
then
	echo "Failed to build the ROM for Wintercoat"
	exit 1
fi

# Build wintercoat
if ! make -f "$__exedir/../assets/wintercoat/makefile"
then
	echo "Failed to build Wintercoat"
	exit 1
fi

# Execute wintercoat with the ROM
./wintercoat -Rwintercoat.rom
exit $?

