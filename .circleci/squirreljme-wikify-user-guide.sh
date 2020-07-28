#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Wikifies SquirrelJME for GitHub, since GitHub uses a different
# format for their Wikis...

# Must be valid!
if [ "$#" -lt "2" ]
then
	echo "Usage: $0 [repo] [wiki-git-repo]"
	exit 1
fi

# These are directories
__foss="$1/assets/user-guide"
__gith="$2"

# These both must be directories
if [ ! -d "$__foss" ] || [ ! -d "$__gith" ]
then
	echo "Both arguments must be directories."
	exit 1
fi

# Target must be a Git repo
if [ ! -d "$__gith/.git" ]
then
	echo "Target not a Git repository."
	exit 1
fi

# Convert to GitHub name
__toGitHubName()
{
	__srcName="$1"
	
	if [ "$__srcName" = "readme.mkd" ]
	then
		echo "Home.markdown"
	else
		echo "$__srcName" | sed 's/\.mkd$/\.markdown/'
	fi
}

# Convert to Fossil name
__toFossilName()
{
	__srcName="$1"
	
	if [ "$__srcName" = "Home.markdown" ]
	then
		echo "readme.mkd"
	else
		echo "$__srcName" | sed 's/\.markdown$/\.mkd/'
	fi
}

# Remove any other old files
find "$__gith" -type f | grep -e '\.mkd$' -e '\.md$' | while read __githFile
do
	echo "Removing old file $__githFile..."
	(cd "$__gith" && git rm -f "$__githFile")
done

# Remove any files which are missing
find "$__gith" -type f | grep '\.markdown$' | while read __githFile
do
	__baseGith="$(basename "$__githFile")"
	__baseFoss="$(__toFossilName "$__baseGith")"
	
	# The file may be in the root of the project or elsewhere...
	if ! find "$__foss/" -type f | grep "$__baseFoss" > /dev/null
	then
		echo "Removing file $__githFile (aka $__baseFoss)..."
		
		(cd "$__gith" && git rm -f "$__githFile")
	fi
done

# Copy and convert the files for GitHub's Wiki
find "$__foss" -type f | grep '\.mkd$' | while read __fossFile
do
	__baseFoss="$(basename "$__fossFile")"
	__baseGith="$(__toGitHubName "$__baseFoss")"
	
	echo "Converting $__baseFoss to $__baseGith..."
	
	# Links need to be properly converted or they will be lost
	sed 's/(readme\.mkd)/(Home)/g' < "$__fossFile" | \
		sed 's/(\([^.]*\)\.mkd)/(\1)/g' > "$__gith/$__baseGith"
	(cd "$__gith" && git add "$__gith/$__baseGith")
done
