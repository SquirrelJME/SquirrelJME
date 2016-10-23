#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
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

