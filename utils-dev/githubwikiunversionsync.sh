#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Pushes everything in the fossil unversioned space to the GitHub
# Wiki.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Where the working Wiki is stored
__tmp="/tmp/$$"
__cat="/tmp/$$.cat"

# Clone the Wiki
if ! git clone git@github.com:XerTheSquirrel/SquirrelJME.wiki.git "$__tmp"
then
	echo "Failed to clone the Wiki."
	exit 1
fi

# Determine if any files in the git repository need to be deleted
(cd "$__tmp" && git ls-files) | while read __file
do
	# Need to actually cat the file	since there is no way to check if it is
	# an actual file, an invalid file is just blank
	fossil unversion cat "$__file" > "$__cat"
	if [ ! -s "$__cat" ]
	then
		echo "Deleting $__file..."
		if ! (cd "$__tmp" && git rm "$__file")
		then
			echo "Failed to delete file $__file!"
		fi
	fi
done

# Go through the unversioned set of files and add all of the files that exist
fossil unversion ls | while read __file
do
	echo "Storing $__file..."
	
	# The directory needs to exist first!
	mkdir -p $(dirname "$__tmp/$__file")
	fossil unversion cat "$__file" > "$__tmp/$__file"
	
	# Add to the changes
	if ! (cd "$__tmp" && git add "$__file")
	then
		echo "Failed to add file $__file!"
	fi 
done

# Commit changes to the repository
if ! (cd "$__tmp" && git commit -v -m "Synchronize Unversion Space on $(date)" --author "SquirrelJME <nobody@squirreljme.cc>")
then
	echo "Failed to commit!"
fi

# Push changes
if ! (cd "$__tmp" && git push)
then
	echo "Failed to push!"
fi

# Cleanup
rm -rf "$__tmp" "$__cat"



