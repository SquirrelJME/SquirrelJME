#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Updates my name for every file.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

grep -rl -e 'Steven Gawroriski' -e 'steven@multiphasicapps\.net' | \
	while read __file
do
	# Ignore this script and symlinks
	if [ "$__file" -ef "$0" ] || [ -L "$__file" ]
	then
		continue
	fi
	
	# Get the old file mode, this is so the file modes are not lost
	__mode="$(stat -c %a "$__file")"
	
	# Replace strings in file
	if sed 's/Steven Gawroriski/Stephanie Gawroriski/g' < "$__file" | \
		sed 's/steven@multiphasicapps\.net/xer@multiphasicapps.net/g' > /tmp/$$
	then
		mv /tmp/$$ "$__file"
		chmod "0$__mode" "$__file"
	fi
done

