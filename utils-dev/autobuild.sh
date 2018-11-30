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

# Build the Java SE binary
if ! "$__realexedir/hostedlaunch.sh" vm-shader -- \
	-javase bootsjme/javase-runtime.jar
then
	echo "Failed to build the Java SE binary."
fi

# Build the Java ME binary
if ! "$__realexedir/hostedlaunch.sh" vm-shader -- -javame
then
	echo "Failed to build the Java ME binary."
fi

# Need to access the fossil unversion
cd "$__realexedir"

# Upload each one
__okay="0"
for __upload in "/tmp/$$/squirreljme-javase.jar" \
	"/tmp/$$/squirreljme-javame.jar"
do
	if [ -f "$__upload" ]
	then
		# Repack because SquirrelJME lacks compression for now so we do not
		# want to waste space in the JAR for upload
		if which pack200
		then
			if echo "$__upload" | grep '\.jar$'
			then
				echo "Original size: $(stat -c %s "$__upload")"
				if pack200 --repack --no-gzip -G -O -E9 /tmp/$$.jar "$__upload"
				then
					mv -f /tmp/$$.jar "$__upload"
					echo "Compressed size: $(stat -c %s "$__upload")"
				fi

			fi
		fi
		
		# Add it
		if fossil unversion add "$__upload" \
			--as "auto/$(basename -- "$__upload")"
		then
			# Add date file as well!
			fossil unversion add "$__tmp/date" \
				--as "auto/$(basename -- "$__upload").date.mkd"
			
			# Mark as uploaded
			__okay="$(($__okay + 1))"
		fi
	fi
done

# Cleanup
echo rm -rvf "$__tmp"

# Return with the number of builds done
exit $__okay

