#!/bin/sh -e
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Fossil cleanup script
# This script is non-Bash POSIX compliant and should work on any POSIX system!

# Force C locale
export LC_ALL=C

# Load in the configuration script
__execDir="$(dirname -- "$0")"
if [ "$SQUIRRELJME_CICD_CONFIG_SCRIPT" != "" ] && \
	[ -x "$SQUIRRELJME_CICD_CONFIG_SCRIPT" ]
then
	. "$SQUIRRELJME_CICD_CONFIG_SCRIPT"
else
	. "$__execDir/build-config.sh"
fi

# Rebuild the Fossil repository, compress and vacuum it to reduce and optimize
# the space
"$__fossilCommand" rebuild --vacuum --compress "$__fossilRepo"
