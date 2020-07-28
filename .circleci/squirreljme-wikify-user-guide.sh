#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Wikifies SquirrelJME

# Must be valid!
if [ "$#" -lt "2" ]
then
	echo "Usage: $0 [repo] [wiki-git-repo]"
	exit 1
fi

# These are directories
__repo="$1/assets/user-guide"
__wiki="$2"

# These both must be directories
if [ ! -d "$__repo" ] || [ ! -d "$__wiki" ]
then
	echo "Both arguments must be directories."
	exit 1
fi

# Target must be a Git repo
if [ ! -d "$__wiki/.git" ]
then
	echo "Target not a Git repository."
	exit 1
fi

# Remove any files which are missing
find "$__wiki" -type f | while read __wikiFile
do
	__baseName="$(basename "$__wikiFile")"
	
	# File is missing, remove it
	if [ ! -f "$__repo/$__baseName" ]
	then
		echo "Removing file $___baseName..."
		
		git rm -f "$__wikiFile"
	fi
done

# Copy and add any files over
find "$__repo" -type f | while read __repoFile
do
	__baseName="$(basename "__repoFile")"
	
	cp -vf "$__repoFile" "$__wiki/$__baseName"
	(cd "$__wiki" && git add "$__wiki/$__baseName")
done
