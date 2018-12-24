#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Automated SquirrelJME builds!
# Note the return value is the number of successful builds, so zero means
# failure!

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"
__realexedir="$("$__exedir/absolute.sh" "$__exedir")"

# Current build date, for record keeping
__date="$(date --utc +%s) $(date --utc)"

# Go to temporary directory to build things
__tmp="/tmp/$$"
mkdir -p "$__tmp"
cd "$__tmp"

# Place the date somewhere
echo "$__date" > "$__tmp/date"

# Go through all distributions and build them all
__okay="0"
"$__realexedir/../build.sh" dist-list | while read __dist
do
	# Output ZIP name
	__zip="squirreljme-$__dist.zip"
	
	# Build from temporary directory
	cd "$__tmp"
	
	# Build the distribytion
	if ! "$__realexedir/../build.sh" dist "$__dist"
	then
		echo "Failed to build $__dist." 1>&2
		continue
	fi
	
	# Make sure it exists
	if [ ! -f "$__zip" ]
	then
		echo "Output ZIP $__zip does not exist?" 1>&2
		continue
	fi
	
	# Repack because SquirrelJME lacks compression for now so we do not
	# want to waste space in the JAR for upload
	if which pack200
	then
		echo "Original size: $(stat -c %s "$__zip")"
		if pack200 --repack --no-gzip -G -O -E9 /tmp/$$.jar "$__zip"
		then
			mv -f /tmp/$$.jar "$__zip"
			echo "Compressed size: $(stat -c %s "$__zip")"
		fi
	fi
	
	# Need to access the fossil unversion
	cd "$__realexedir"
	
	# Add it
	if fossil unversion add "$__tmp/$__zip" \
		--as "auto/$(basename -- "$__zip")"
	then
		# Add date file as well!
		fossil unversion add "$__tmp/date" \
			--as "auto/$(basename -- "$__zip").date.mkd"
		
		# Mark as uploaded
		__okay="$(($__okay + 1))"
	fi
done

# Cleanup
echo rm -rvf "$__tmp"

# Return with the number of builds done
exit $__okay

