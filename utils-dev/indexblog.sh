#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Re-indexes the blogs

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Counts arguments used
__count_args()
{
	echo "$#"
}

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
	__oldauth="nobody"
	__oldyear=0
	__oldmont=0

	# Go through all files
	fossil unversion ls | grep '^developer-notes\/' | \
		sed 's/^developer-notes\///g' | \
		grep '[0-9]\{4\}\/[0-9][0-9]\/[0-9][0-9]\.mkd$' | \
		sed 's/^\.\///g' | while read __preline
	do
		# Slash separated
		__x_auth="$(echo "$__preline" | cut -d '/' -f 1)"
		__x_year="$(echo "$__preline" | cut -d '/' -f 2)"
		__x_mont="$(echo "$__preline" | cut -d '/' -f 3)"
		__x_dayy="$(echo "$__preline" | cut -d '/' -f 4 | sed 's/\.mkd//g')"
	
		# Sort by author first so that the documents can be grouped together
		echo "$__x_auth $__x_year $__x_mont"

	# Combine lines which share the same data and append authors on top of that
	# line
	done | sort | uniq | while read __postline
	do
		# TODO
		echo "$__postline"

	# Generate output file
	done | while read __line
	do
		# Space separated
		__y_auth="$(echo "$__line" | cut -d ' ' -f 1)"
		__y_year="$(echo "$__line" | cut -d ' ' -f 2)"
		__y_mont="$(echo "$__line" | cut -d ' ' -f 3)"
		
		# Author changed? Then print the author title
		if [ "$__oldauth" != "$__y_auth" ]
		then
			# Starting header?
			if [ "$__oldauth" = "nobody" ]
			then
				echo "# Developer Notes"
			fi
			
			# Print title
			echo "# $("$__exedir/mapauthor.sh" "$__y_auth")"
			
			# Set author
			__oldauth="$__y_auth"
			
			# The year is invalidated, so clear it
			__oldyear="0"
		fi
		
		# New year?
		if [ "$__y_year" -ne "$__oldyear" ]
		then
			# Print header
			echo ""
			echo "## $__y_year"
		
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
			echo "### $__y_mont"
			echo ""
		
			# For later generation
			__oldmont="$__y_mont"
		fi
		
		# Go through the calendar for this month/year
		# Convert anything that is not a number to a space, this removes the
		# month and day names and removes any hidden escape sequences that may
		# be used to highlight the current day
		__firstweek="1"
		cal "$__y_mont" "$__y_year" | sed 's/_.//g' | sed 's/[^0-9\ ]/ /g' |
			while read __week
		do
			# Remove the line that contains the year along with lines that
			# contain days
			if echo "$__week" | grep "$__y_year" > /dev/null
			then
				continue
			fi
			
			# If there are no days in the week, do nothing
			__days="$(__count_args $__week)"
			if [ "$__days" -le "0" ]
			then
				continue
			fi
			
			# Mark start
			echo -n ' * '
			
			# If this is the first week then add a bunch of spaces at the
			# start of the week so that it is aligned
			if [ "$__firstweek" -eq "1" ]
			then
				# Add missing dates
				__i="$__days"
				__origi="$__i"
				while [ "$__i" -lt "7" ]
				do
					echo -n '`--` '
					__i="$(expr "$__i" + 1)"
				done
				
				# Go to next line to make a bit cleaner
				# But only do this if dashes were output
				if [ "$__origi" -ne "$__i" ]
				then
					echo
				fi
			fi
			
			# Go through all days on the week
			for __d in $__week
			do
				# Correct day to always use double digits
				if [ "$(echo -n "$__d" | wc -c)" -eq "1" ]
				then
					__d="0$__d"
				fi
				
				# Guess file name to use
				__want="$__y_auth/$__y_year/$__y_mont/$__d.mkd"
				
				# If the file exists then link to it
				if [ "$(fossil unversion cat "developer-notes/$__want" | \
					wc -c)" -gt "0" ]
				then
					echo "["**'`'"$__d"'`'**"]($__want)"
				
				# Otherwise write a very blank date there
				else
					echo '`'"$__d"'`'
				fi
			done
			
			# For the last week, if any week has less then seven days add
			# dashes following so it looks the same
			if [ "$__firstweek" -eq "0" ] && [ "$__days" -lt "7" ]
			then
				# Add missing dates
				__i="$__days"
				while [ "$__i" -lt "7" ]
				do
					echo -n ' `--`'
					__i="$(expr "$__i" + 1)"
				done
				
				# Newline for safety
				echo
			fi
			
			# Stop first week special handling
			__firstweek="0"
		done
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
	fossil unversion ls | grep '^developer-notes\/' | \
		sed 's/^developer-notes\///g' | \
		grep '[a-zA-Z0-9_\-]\{1,\}\/other\/[a-zA-Z0-9_\-]\{1,\}\.mkd' | \
		sort | while read __line
	do
		# Extract author and the document name
		__auth="$(echo "$__line" | \
			sed 's/^[^[a-zA-Z0-9_\-]*\([a-zA-Z0-9_\-]\{1,\}\)\/.*/\1/g')"
		__name="$(echo "$__line" | \
			sed 's/.*\/\([a-zA-Z0-9_\-]\{1,\}\)\.mkd/\1/g')"
		
		# Find the first header field
		__head="$(fossil unversion cat "developer-notes/$__line" | \
			grep '^#[^#]' | head -n 1 | \
			sed 's/^#*[ \t]*\(.*\)[ \t]*/\1/g')"
		
		# Did the author change?
		if [ "$__la" != "$__auth" ]
		then
			echo ""
			echo "## $("$__exedir/mapauthor.sh" "$__auth")"
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
	fossil unversion add /tmp/$$ --as developer-notes/index.mkd
fi

# Delete temporary file
rm -f /tmp/$$

