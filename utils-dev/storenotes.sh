#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Extracts notes from the unversioned space and stores them in
# the repository for backup purposes.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Get the name of today's blog
__myname="$($__exedir/myname.sh)"
__nowyear="$(date +%Y)"
__nowmont="$(date +%m)"
__nowdayy="$(date +%d)"
__htmtime="$__nowyear\/$__nowmont\/$__nowdayy"
__fname="developer-notes/$__myname/$__nowyear/$__nowmont/$__nowdayy.mkd"

# Go through all unversioned files for the blog
__changed=0
fossil uv ls | grep '^developer-notes\/' | while read __line
do
	# Ignore today
	if [ "$__line" = "$__fname" ]
	then
		continue
	fi	
	
	# Target directory
	__target="$__exedir/../assets/$__line"
	__tardir="$(dirname -- "$__target")"
	
	# Calculate the sums of both files
	__isum="$(fossil cat "$__line" | fossil sha1sum - |
		tr '\t' ' ' | cut -d ' ' -f 1)"
	__xsum="$(if [ -f "$__target" ];
		then cat "$__target" | fossil sha1sum - | tr '\t' ' ' | cut -d ' ' -f 1;
		else echo "0"; fi)"
	
	# Debug
	echo "$__line -> $__target ($__tardir) [$__isum $__xsum]"
	
	# If the sums differ then overwrite the destination files
	if [ "$__isum" != "$__xsum" ]
	then
		# Make the target directory
		if [ ! -d "$__tardir" ]
		then
			mkdir -p "$__tardir"
		fi
		
		# Extract and add it
		fossil uv cat "$__line" > "$__target"
		fossil add "$__target"
		
		# Increase change count
		__changed="$(($__changed + 1))"
	fi
done

# Files changed?
if [ "$__changed" -gt "0" ]
then
	fossil commit -m "Backup developer notes ($__changed)."
fi

# Clear temporary
rm -f "/tmp/$$"

