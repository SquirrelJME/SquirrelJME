#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Runs the specified JAD/JAR in the emulator
#
# !!! IF YOU GET THE FOLLOWING: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
# Error: Cannot find any soundbank file
# Execution completed.
# ---------------------------------------------------------------------------
# You need to place gm.dls into c:\windows\system32\drivers
# !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Path of the emulator
: ${EMULATOR_PATH:="$HOME/.wine/drive_c/WTK2.5.2_01/bin/emulator.exe"}
#: ${EMULATOR_PATH:="$HOME/.wine/drive_c/Java_ME_platform_SDK_3.4/bin/emulator.exe"}

# Usage
if [ "$#" -ne "1" ]
then
	echo "Usage: $0 (file.jad)"
	exit 1
fi

# Clear all old programs
if ! wine "$EMULATOR_PATH" -Xjam:remove=all
then
	echo "Could not remove all old programs, ignoring" 1>&2
fi

# Run the JAD
if ! wine "$EMULATOR_PATH" "-Xdescriptor:$(winepath -w "$1")" # -Xverbose:calls
then
	echo "Run failed" 1>&2
	exit 1
fi

