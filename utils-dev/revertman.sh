#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Reverts badly converted manifests.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Revert only blank files
for __file in "$__exedir/../"*/*/META-INF/MANIFEST.MF
do
	if [ ! -s "$__file" ]
	then
		fossil revert -r 6e34fad4a026e591d8f1505276146470e29b592b "$__file"
	fi
done

