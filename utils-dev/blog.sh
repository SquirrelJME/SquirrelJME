#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Creates a new blog entry! joy!

# Force C locale
export LC_ALL=C

# Common directories
__exedir="$(dirname -- "$0")"
__tmpdir="$("$__exedir/tmpdir.sh")"

# Current user
__myname="$($__exedir/myname.sh)"

# Get the current date in string form.
__nowyear="$(date +%Y)"
__nowmont="$(date +%m)"
__nowdayy="$(date +%d)"
__htmtime="$__nowyear\/$__nowmont\/$__nowdayy"

# Determine the name of the file
__fname="developer-notes/$__myname/$__nowyear/$__nowmont/$__nowdayy.mkd"

# Does it need to be added into unversioned space?
__job=0
if [ "$( (fossil unversion ls; echo "$__fname") | sort | uniq -d | wc -l)" \
	-eq "0" ]
then
	# Create temporary
	sed "s/YYYYMMDD/$__htmtime/g" < "$__exedir/crtmpl/blog.mkd" \
		> "$__tmpdir/$$"
	
	# Add to unversioned space
	fossil unversion add "$__tmpdir/$$" --as "$__fname"
	
	# Rebuild the blog index
	"$__exedir/indexblog.sh"
	
	# Delete temporary
	rm -f "$__tmpdir/$$"
fi

# Edit it
fossil unversion edit "$__fname"


