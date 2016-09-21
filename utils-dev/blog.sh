#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Creates a new blog entry! joy!

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

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
	sed "s/YYYYMMDD/$__htmtime/g" < "$__exedir/crtmpl/blog.mkd" > /tmp/$$
	
	# Add to unversioned space
	fossil unversion add /tmp/$$ --as "$__fname"
	
	# Rebuild the blog index
	"$__exedir/indexblog.sh"
	__job="$!"
	
	# Delete temporary
	rm -f /tmp/$$
fi

# Edit it
fossil unversion edit "$__fname"

# Foreground the background job
if [ "$__job" -ne "0" ]
then
	fg "$__job"
fi

