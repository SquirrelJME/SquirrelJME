#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Updates the error script.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Needs to be in a repository
cd "$__exedir"

# Build
"$__exedir/builderrors.sh" > /tmp/$$

# Did the errors actually change?
if [ "$(fossil unversion cat "errors.mkd" | fossil sha1sum - | \
	cut -d ' ' -f 1)" != "$(fossil sha1sum - < /tmp/$$ | cut -d ' ' -f 1)" ]
then
	echo "Errors updated"
	fossil unversion add /tmp/$$ --as "errors.mkd"
else
	echo "Errors untouched"
fi
rm -f /tmp/$$

