#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Creates an "other" note.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Usage
if [ "$#" -lt "3" ]
then
	echo "Usage: $0 (command) (short) (Title Name)"
	exit 1
fi

# Extract
__cmd="$1"
__short="$(echo "$2" | sed 's/[^a-zA-Z0-9_\-]//g' | sed \
	'y/QWERTYUIOPASDFGHJKLZXCVBNM/qwertyuiopasdfghjklzxcvbnm/')"
__title="$3"

# Current user
__myname="$($__exedir/myname.sh)"

# Where notes go
__notebase="$__exedir/../src/developer-notes/$__myname/other"
__outfile="$__notebase/$__short.mkd"

# Print simple document
if "$__exedir/create.sh" -c "$__cmd" "$__outfile"
then
	echo "# $__title
	" > "$__outfile"
fi

# Rebuild the blog index (in the background)
"$__exedir/indexblog.sh" &

