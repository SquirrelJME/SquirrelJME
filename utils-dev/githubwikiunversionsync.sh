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
#
# Note that the GitHub Wiki has severe limitations:
#  * Directories just do not exist in any way when it comes to content.
#  * Links to other Wiki pages are in this format: `
#    `developer-notes_@_stephanie-gawroriski_@_2013_@_12_@_22` which refers to
#    that page, so I believe wiki links need to handle that accordingly.
#    I tested it and that works, so any kind of link to a markdown file must
#    for the most part remove the extension and use the slash form.
#
# So to correct for this, the directory separator is mapped to `_@_` so that
# the given information exists and it can easily be remapped back to
# a directory.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Where the working Wiki is stored
__tmp="/tmp/$$"
__cat="/tmp/$$.cat"
__boop="/tmp/$$.boop"

# Need this to better process files
if ! javac -d . "$__exedir/GitHubWikiReformat.java"
then
	echo "Failed to compile Java helper!"
	exit 1
fi

# Clone the Wiki
if ! git clone git@github.com:XerTheSquirrel/SquirrelJME.wiki.git "$__tmp"
then
	echo "Failed to clone the Wiki."
	exit 1
fi

# Map Fossil filename to Git
__fossil_to_git()
{
	if [ "$(basename "$1" .mkd)" = "$1" ]
	then
		echo "$1"
	else
		echo "$1" | sed 's/\.mkd$/.md/' | sed 's/\//@d@/g' |
			sed 's/-/@h@/g'
	fi
}

# Map Git filename to Fossil
__git_to_fossil()
{
	if [ "$(basename "$1" .md)" = "$1" ]
	then
		echo "$1"
	else
		echo "$1" | sed 's/\.md$/.mkd/' | sed 's/@d@/\//g' |
			sed 's/@h@/-/g'
	fi
}

# Determine if any files in the git repository need to be deleted
(cd "$__tmp" && git ls-files) | while read __file
do
	# Names are mapped
	__fossilfile="$(__git_to_fossil "$__file")"	
	
	# Need to actually cat the file	since there is no way to check if it is
	# an actual file, an invalid file is just blank
	fossil unversion cat "$__fossilfile" > "$__cat"
	if [ ! -s "$__cat" ]
	then
		echo "Deleting $__file (as $__fossilfile)..."
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
	
	# Need to map the fossil filename to git
	__gitfile="$(__fossil_to_git "$__file")"	
	
	# The directory needs to exist first!
	mkdir -p $(dirname "$__tmp/$__gitfile")
	fossil unversion cat "$__file" > "$__boop"
	
	# If the file is a markdown file then the extensions need to be remapped
	# so everything works, due to GitHub Wiki limitations
	if echo "$__file" | grep '\.mkd$' > /dev/null
	then
		echo "Remapping $__file for GitHub Wiki..."
		java GitHubWikiReformat "$__file" < "$__boop" > "$__tmp/$__gitfile" 
	
	# Otherwise a 1:1 copy
	else
		mv -f -- "$__boop" "$__tmp/$__gitfile" 
	fi
	
	# Add to the changes
	if ! (cd "$__tmp" && git add "$__gitfile")
	then
		echo "Failed to add file $__file (as $__gitfile)!"
	fi 
done

# Commit changes to the repository
if ! (cd "$__tmp" && git commit -v -m "Synchronize Unversion Space" --author "SquirrelJME <nobody@squirreljme.cc>")
then
	echo "Failed to commit!"
fi

# Push changes
if true
then
	if ! (cd "$__tmp" && git push)
	then
		echo "Failed to push!"
	fi
fi

# Cleanup
rm -rf -- "$__tmp"
rm -f -- "$__cat" "$__boop" "$__boop.1" "$__boop.2"



