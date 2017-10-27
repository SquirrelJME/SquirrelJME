#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Builds the error file

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Header
echo "# List of Errors"
echo ""
__old="00"

# Process all errors
"$__exedir/listerror.sh" "$__exedir/.." 2> /dev/null | while read __line
do
	# Code prefix
	__pref="$(echo "$__line" | cut -c 1-2)"
	
	# Did the prefix change?
	if [ "$__old" != "$__pref" ]
	then
		echo ""
		echo "# "'`'"$__pref"'`'
		echo ""
		__old="$__pref"
	fi
	
	# Extract code and description
	__code="$(echo "$__line" | cut -c 1-4)"
	__desc="$(echo "$__line" | cut -c 5- | sed 's/\([_\*<(\`]\)/\\\1/g')"
	
	# Are there parameters?
	if echo "$__desc" | grep '([^)]*)[ \t]\{1,\}\\<' > /dev/null
	then
		# Extract and remove them
		__parm="$(echo "$__desc" |
			sed 's/^.*(\([^)]*\))[ \t]\{1,\}\\<.*$/\1/g' |
			sed 's/[ \t]\{2,\}/ /g')"
		__desc="$(echo "$__desc" |
			sed 's/([^)]*)[ \t]\{1,\}\\</\\</g' |
			sed 's/[ \t]\{2,\}/ /g')"
		
		# Base output
		echo " * ***"'`'"$__code"'`'"***: $__desc"
		
		# No delimeter?
		__nodelim="$(echo "$__parm" | grep '\;' > /dev/null; echo $?)"
		
		# Output all parameters
		__i=1
		while true
		do
			# Extract item
			__item="$(echo "$__parm" | cut -d ';' -f "$__i" |
				sed 's/^[ \t]*//g;s/[ \t]*$//g')"
			
			# Nothing
			if [ -z "$__item" ]
			then
				break
			fi
			
			# Print it
			echo "   * $__item"
			
			# Increase
			__i="$(expr "$__i" + 1)"
			
			# Stop if no delimeters are used
			if [ "$__nodelim" -ne "0" ]
			then
				break;
			fi
		done
		
	# There are none
	else
		echo " * ***"'`'"$__code"'`'"***: $__desc"
	fi
done

