#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Re-indexes the blogs

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go to the blog base
cd "$__exedir/../src/developer-notes"

# Uppercase author name
__upperauth()
{
	# Remove non-letter
	__non="$(echo "$1" | sed 's/[^a-zA-Z]/ /g')"
	
	# Capitalize the first letter
	__i="1"
	while true
	do
		# Get the used word
		__word="$(echo "$__non" | cut -d ' ' -f $__i)"
		__i="$(($__i + 1))"
		
		# Nothing?
		if [ -z "$__word" ]
		then
			break
		fi
		
		# Uppercase only the first letter in this word
		__into="$(echo "$__word" | cut -c 1 | \
			sed 'y/qwertyuiopasdfghjklzxcvbnm/QWERTYUIOPASDFGHJKLZXCVBNM/' \
			)$(echo "$__word" | cut -c 2-)"
		
		# Prepend space if there is stuff in it
		if [ -n "$__out" ]
		then
			__out="$__out "
		fi
		__out="$__out$__into"
	done
	
	# Output
	echo "$__out"
}

# Primary note index
__primary()
{
	# For usage
	__oldyear=0
	__oldmont=0

	# Go through all files
	find -type f | grep '[0-9]\{4\}\/[0-9][0-9]\/[0-9][0-9]\.mkd$' | \
		sed 's/^\.\///g' | while read __preline
	do
		# Slash separated
		__x_auth="$(echo "$__preline" | cut -d '/' -f 1)"
		__x_year="$(echo "$__preline" | cut -d '/' -f 2)"
		__x_mont="$(echo "$__preline" | cut -d '/' -f 3)"
		__x_dayy="$(echo "$__preline" | cut -d '/' -f 4 | sed 's/\.mkd//g')"
	
		# Echo it in changed order so that the dates sort first, thus regardless
		# of the author it is in chronological order
		echo "$__x_year $__x_mont $__x_dayy $__x_auth"

	# Combine lines which share the same data and append authors on top of that
	# line
	done | sort | while read __postline
	do
		# TODO
		echo "$__postline"

	# Generate output file
	done | while read __line
	do
		# Space separated
		__y_year="$(echo "$__line" | cut -d ' ' -f 1)"
		__y_mont="$(echo "$__line" | cut -d ' ' -f 2)"
		__y_dayy="$(echo "$__line" | cut -d ' ' -f 3)"
	
		# New year?
		if [ "$__y_year" -ne "$__oldyear" ]
		then
			# Starting header?
			if [ "$__oldyear" -eq "0" ]
			then
				echo "# Developer Notes"
			fi
		
			# Print header
			echo ""
			echo "# $__y_year"
		
			# For next header generation
			__oldyear="$__y_year"
		
			# Force the old month to always be invalid, in case there are no
			# notes for a year (which would be rather sad in and of itself)
			__oldmont=0
		fi
	
		# New month?
		if [ "$__y_mont" -ne "$__oldmont" ]
		then
			# Print header
			echo ""
			echo "## $__y_mont"
			echo ""
		
			# For later generation
			__oldmont="$__y_mont"
		fi
	
		# Print the date
		echo " * $__y_dayy: "
	
		# Print all authors
		__i=0
		while true
		do
			# Get author here
			__y_auth="$(echo "$__line" | cut -d ' ' -f $(expr 4 + $__i))"
		
			# End loop?
			if [ -z "$__y_auth" ]
			then
				break
			fi
		
			__i="$(expr $__i + 1)"
		
			# The file path
			__fpath="$__y_auth/$__y_year/$__y_mont/$__y_dayy.mkd"
		
			# Print author bracket
			echo "   [$(__upperauth "$__y_auth")]($__fpath)"
		done
	
		__y_auth="$(echo "$__line" | cut -d ' ' -f 4)"
	done
}

# Secondary note sources
__secondary()
{
	# Print title
	echo ""
	echo "# Other"
	
	# Go through all
	__la=""
	find -type f | \
		grep '[a-zA-Z0-9_\-]\{1,\}\/other\/[a-zA-Z0-9_\-]\{1,\}\.mkd' | \
		sort | while read __line
	do
		# Extract author and the document name
		__auth="$(echo "$__line" | \
			sed 's/^[^[a-zA-Z0-9_\-]*\([a-zA-Z0-9_\-]\{1,\}\)\/.*/\1/g')"
		__name="$(echo "$__line" | \
			sed 's/.*\/\([a-zA-Z0-9_\-]\{1,\}\)\.mkd/\1/g')"
		
		# Find the first header field
		__head="$(grep '^#[^#]' "$__line" | head -n 1 | \
			sed 's/^#*[ \t]*\(.*\)[ \t]*/\1/g')"
		
		# Did the author change?
		if [ "$__la" != "$__auth" ]
		then
			echo ""
			echo "## $(__upperauth "$__auth")"
			echo ""
			
			__la="$__auth"
		fi
		
		# Print out page
		echo " * [$__head]($(echo "$__line" | sed 's/^\.\///g'))"
	done
}

# Print both
if (__primary && __secondary) > /tmp/$$
then
	mv /tmp/$$ index.mkd
fi

