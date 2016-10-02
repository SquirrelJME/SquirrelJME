#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Generates the JavaDoc and synchronizes the changes into the
# fossil unversioned space

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Generate JavaDoc
if false
then
	rm -rf "javadoc"
	if ! "$__exedir/javadoc.sh"
	then
		echo "Failed to generate JavaDoc"
		exit 1
	fi
fi

# Remember this point
__docroot="$(pwd)/javadoc"

# Go to the project home directory so fossil works
cd "$__exedir/.."

# Files in javadoc and unversioned space
(cd "$__docroot"; find -type f | grep '\.mkd$' | sed 's/\.\///g') | \
	sort > /tmp/$$.a
fossil unversion ls | grep '^javadoc\/' | sed 's/^javadoc\///g' | \
	sort > /tmp/$$.b

# Go through all files
diff -u /tmp/$$.b /tmp/$$.a | grep '^[\-\+ ][^\-\+]' | while read -r __line
do
	# Get the change mode and the file
	__mode="$(echo "$__line" | cut -c 1)"
	__file="$(echo "$__line" | cut -c 2-)"
	
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
		echo "Removing $__file"
	
	# Updating, if changed
	else
		echo "Update $__file"
	fi
done

# Cleanup
rm -f /tmp/$$.a
rm -f /tmp/$$.b

