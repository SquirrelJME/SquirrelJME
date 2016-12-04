#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Migrates error codes from MANIFEST.MF to SQUIRRELJME-GENERIC.MF.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

find "$__exedir/.." -type d | while read __dir
do
	__man="$__dir/META-INF/MANIFEST.MF"
	__new="$__dir/META-INF/SQUIRRELJME-GENERIC.MF"
	if [ -f "$__man" ]
	then
		# Original error code
		__err="$(sed \
			'y/abcdefghijklmnopqrstuvwxyz/ABCDEFGHIJKLMNOPQRSTUVWXYZ/' \
			< "$__man" | grep '^X-SQUIRRELJME-ERROR[ \t]*:' |
			sed 's/^X-SQUIRRELJME-ERROR[ \t]*:[ \t]*\([^ \t]*\)[ \t]*/\1/')"
		
		# Do not update if not migrated
		if [ -f "$__new" ]
		then
			__too="$(sed \
				'y/abcdefghijklmnopqrstuvwxyz/ABCDEFGHIJKLMNOPQRSTUVWXYZ/' \
				< "$__new" | grep '^ERROR-PREFIX[ \t]*:' |
				sed 's/^ERROR-PREFIX[ \t]*:[ \t]*\([^ \t]*\)[ \t]*/\1/')"
		else
			__too=""
		fi
		
		# Only if there is an error code and it is missing from the other
		if [ -n "$__err" ] && [ -z "$__too" ]
		then
			# If the target manifest does not exist then initialize a blank
			# one
			if [ ! -f "$__new" ]
			then
				echo "Manifest-Version: 1.0" > /tmp/$$
			else
				cp "$__new" /tmp/$$
			fi
			
			# Append the error prefix
			echo "Error-Prefix: $__err" >> /tmp/$$
			
			# Debug
			cat /tmp/$$
			
			# Remove the prefix from the old one
			grep -vi "^x-squirreljme-error[ \t]*:" < "$__man"
			
			echo "$__dir $__err $__too"
		fi
	fi
done

