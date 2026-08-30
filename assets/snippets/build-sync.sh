#!/bin/sh -e
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Fossil synchronization script
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

# Set the default user for the repository since it does not always get set by
# default
"$__fossilCommand" user default "$__fossilUser" -R "$__fossilRepo"

# Pull all settings from the remote repository
"$__fossilCommand" config pull all -R "$__fossilRepo"

# Never auto-sync, as this is a build system and also annoying but additionally
# slows things down immensely
"$__fossilCommand" settings autosync off -R "$__fossilRepo"

# Synchronize all artifacts and unversioned files
"$__fossilCommand" sync -u -v -R "$__fossilRepo"
"$__fossilCommand" uv sync -v -R "$__fossilRepo"
