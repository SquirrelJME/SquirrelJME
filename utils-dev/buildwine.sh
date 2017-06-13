#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Runs the build system using the Wine version of the compilers
# and run-time instead of one on the native system (used to test cross
# platformness).

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

wine cmd.exe /C "$(winepath -w "$__exedir/../build.cmd")" $*

