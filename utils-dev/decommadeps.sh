#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Removes commas from all dependencies, optional and required.
# This makes it simpler to just use spaces to separate everything

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through all manifests
for __file in */META-INF/MANIFEST.MF
do
	# Remove any continuations and force them in single line sets, then
	# convert them back
	# Also remove trailing newlines to fix them at one
	tr '\n' '\v' < "$__file" | sed 's/\v$//g' | \
		sed 's/\( *\)\v \( *\)/\1\2/g' | \
		sed 's/  */ /g' | tr '\v' '\n' | \
		while read -r __line
	do
		__first="1"
		# Remove commas
		(if echo "$__line" | grep -i -e 'x-squirreljme-depends' \
			-e 'x-squirreljme-optional' > /dev/null
		then
			# Convert commas to spaces and remove extra spaces
			echo "$__line" | sed 's/,/ /g' | sed 's/  */ /g'
		
		# Do not treat
		else
			echo "$__line"
			
		# For any lines which wrap because they are very long, add
		# a space on the following line
		fi) | fold -w 70 -s | while read __fold
		do
			# First line?
			if [ "$__first" -eq "1" ]
			then
				# No longer first
				__first="0"
				
				# As-is
				echo "$__fold"
			
			# Later lines
			else
				echo " $__fold"
			fi
		done
	done > /tmp/$$
	
	# Add a few extra newline
	echo "" >> /tmp/$$
	echo "" >> /tmp/$$
	
	# Copy over the project
	mv /tmp/$$ "$__file"
done

# Spacer
echo

