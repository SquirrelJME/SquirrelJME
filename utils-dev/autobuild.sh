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

# Directory to place final things instead of in fossil?
__indir=""
if [ ! -z "$1" ]
then
	__indir="$("$__exedir/absolute.sh" "$1")"
fi

# Version specifier for sources?
__vspec="$(cat "$__realexedir/../squirreljme-version")"
__vcomm="$(cat "$__realexedir/../manifest.uuid")"

# Build source code set there as well
if [ ! -z "$__indir" ]
then
	# Need to access the fossil repo
	cd "$__realexedir"
	
	# Create ZIP
	if ! fossil zip "$__vcomm" "$__indir/squirreljme-src-$__vspec.zip" \
		--name "squirreljme-$__vspec"
	then
		echo "Could not ZIP revision." 1>&2
		exit 4
	fi
	
	# Create TGZ
	if ! fossil tar "$__vcomm" "$__indir/squirreljme-src-$__vspec.tgz" \
		--name "squirreljme-$__vspec"
	then
		echo "Could not TAR revision." 1>&2
		exit 5
	fi
fi

# Go to temporary directory to build things
__tmp="/tmp/$$"
mkdir -p "$__tmp"
cd "$__tmp"

# Build from these sources instead of using the commit tree, so that way it
# is super clean!
if [ ! -z "$__indir" ]
then
	# Extract
	tar -xzvf "$__indir/squirreljme-src-$__vspec.tgz"
	
	# Set our directories to the source location here
	__exedir="$__tmp/squirreljme-$__vspec/utils-dev"
	__realexedir="$("$__exedir/absolute.sh" "$__exedir")"
fi

# Place the date somewhere
echo "$__date" > "$__tmp/date"

# Go through all distributions and build them all
__okay="0"
__fail="0"
"$__realexedir/../build.sh" dist-list | while read __dist
do
	# Output ZIP name
	__zip="squirreljme-$__dist.zip"
	
	# Build from temporary directory
	cd "$__tmp"
	
	# Build the distribytion
	if ! "$__realexedir/../build.sh" dist "$__dist"
	then
		__fail="1"
		echo "Failed to build $__dist." 1>&2
		continue
	fi
	
	# Make sure it exists
	if [ ! -f "$__zip" ]
	then
		__fail="1"
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
	
	# Store into fossil
	if [ -z "$__indir" ]
	then
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
		else
			__fail="1"
		fi
	
	# Place into the given directory
	else
		# Make sure it exists
		mkdir -p "$__indir"
		
		# Move it there
		if ! mv -v "$__tmp/$__zip" \
			"$__indir/$(basename -- "$__zip" .zip)-$__vspec.zip"
		then
			__fail="1"
		else
			__okay="$(($__okay + 1))"
		fi
	fi
done

# Cleanup
echo rm -rvf "$__tmp"

# Return failure if any build failed
if [ "$__fail" -ne "0" ]
then
	exit "$__fail"
fi 
exit 0

