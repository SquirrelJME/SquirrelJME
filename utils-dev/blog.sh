#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Creates a new blog entry! joy!

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Requires command
if [ "$#" -lt "1" ]
then
	echo "Usage: $0 (command) [...]"
	exit 1
fi

# Get command and shift down
__cmd=$1
shift 1

# Current user
__myname="$(fossil user default 2> /dev/null)"
if [ -z "$__myname" ]
then
	__myname="$USER"
fi

# Dots to dashes
__myname="$(echo "$__myname" | sed 's/\./-/g')"

# Get the current date in string form.
__nowyear="$(date +%Y)"
__nowmont="$(date +%m)"
__nowdayy="$(date +%d)"

# Note list file
__notelist="$__exedir/../src/developer-notes/notes.list"

__nowtime="$__nowyear$__nowmont$__nowdayy"
__htmtime="$__nowyear\/$__nowmont\/$__nowdayy"
#__nowfile="$__exedir/../chm/blog/$__nowtime.htm"
__sublet="$__nowyear/$__nowmont/$__nowdayy.mkd"
__basedir="$__myname/$__sublet"
__nowfile="$__exedir/../src/developer-notes/$__basedir"

# Create if missing
if [ ! -f "$__nowfile" ]
then
	echo "Does not exist, creating."
	"$__exedir/create.sh" "$__nowfile"
	sed "s/YYYYMMDD/$__htmtime/g" < "$__exedir/crtmpl/blog.mkd" > "$__nowfile"
	
	# Add to the note list
	touch "$__notelist"
	cat "$__notelist" > /tmp/$$
	echo "$__basedir" >> /tmp/$$
	sort < /tmp/$$ > "$__notelist"
	rm -f /tmp/$$
fi

# Open it
"$__cmd" $* "$__nowfile"

