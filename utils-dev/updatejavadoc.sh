#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Generates the JavaDoc and synchronizes the changes into the
# fossil unversioned space

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Generate JavaDoc
__javadocfailed=0
if true
then
	rm -rf "javadoc"
	if ! "$__exedir/javadoc.sh"
	then
		echo "Failed to generate JavaDoc, will only update errors"
		__javadocfailed=1
	fi
fi

# Remember this point
__docroot="$(pwd)/javadoc"

# Go to the project home directory so fossil works
cd "$__exedir/.."
__srcdir="$__exedir/.."

# Generate and update errors list
echo "Generating error list..."
"$__exedir/updateerror.sh"

# Do not bother updating the docs
if [ "$__javadocfailed" -ne "0" ]
then
	echo "Failed to build the doc generator, cannot update."
	exit 1
fi

# Count the number of documents that should exist
__ad="$("$__exedir/lsall.sh" | grep '\.java$' | wc -l)"
__hd="$(expr "$(expr "$__ad" "/" "2")" "+" "$(expr "$__ad" "/" "4")")"

# If half of the documents fail to generate, then do not generate at all
__nd="$(cd "$__docroot"; find -type f | grep '\.mkd$' | wc -l)"
if [ "$__nd" -lt "$__hd" ]
then
	echo "Not enough documents, not going to update."
	echo "$__nd did not meet threshold of $__hd for $__ad sources."
	exit 1
fi

# Files in javadoc and unversioned space
# Add a line that is different from A and B
(cd "$__docroot"; find -type f | grep '\.mkd$' | sed 's/\.\///g'; \
	echo "@IgnoreA") | sort > /tmp/$$.a
(fossil unversion ls | grep '^javadoc\/' | grep '\.mkd$' | \
	sed 's/^javadoc\///g'; echo "@IgnoreB") | sort > /tmp/$$.b

# Go through all files
# Set the unified diff surrounding change count to a really high value so that
# all changes can possibly be detected. Otherwise, there would need to be a
# second pass for files which were not changed, just to see if they were
# changed (sort, sort, uniq).
diff -U 99999 /tmp/$$.b /tmp/$$.a | grep '^[\-\+ ][^\-\+]' | \
	while read -r __line
do
	# Get the change mode and the file
	__mode="$(echo "$__line" | cut -c 1)"
	__file="$(echo "$__line" | cut -c 2-)"
	
	# Space gone away?
	if [ "$__mode" != "-" ] && [ "$__mode" != "+" ]
	then
		__file="$__mode$__file"
		__mode="="
	fi
	
	# Ignore?
	__fcha="$(echo "$__file" | cut -c 1)"
	if [ "$__fcha" = "@" ]
	then
		continue
	fi
	
	# The unversioned target
	__targ="javadoc/$__file"
	
	# Debug
	echo "$__mode $__file -> $__targ"
	
	# Adding file?
	if [ "$__mode" = "+" ]
	then
		fossil unversion add "$__docroot/$__file" --as "$__targ"
	
	# Removing file?
	elif [ "$__mode" = "-" ]
	then
		fossil unversion rm "$__targ"
	
	# Updating, if changed
	else
		# Get sha1 for both files
		__was="$(fossil unversion cat "$__targ" | fossil sha1sum - | \
			cut -d ' ' -f 1)"
		__now="$(fossil sha1sum - < "$__docroot/$__file" | \
			cut -d ' ' -f 1)"
		
		# Did the file change?
		if [ "$__was" != "$__now" ]
		then
			fossil unversion add "$__docroot/$__file" --as "$__targ"
		fi
	fi
done

# Cleanup
rm -f /tmp/$$.a
rm -f /tmp/$$.b

