#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Script used to upload releases to the repository, for a release.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"
__realexedir="$("$__exedir/absolute.sh" "$__exedir")"

# Version specifier for sources?
__vspec="$(cat "$__realexedir/../squirreljme-version")"
__vcomm="$(cat "$__realexedir/../manifest.uuid")"

# Create directory for the version, where all the binaries go
__specdir="$(pwd)/$__vspec"
mkdir -p "$__specdir"

# Auto build into that directory
if ! "$__exedir/autobuild.sh" "$__specdir"
then
	echo "Could not auto-build." 1>&2
	exit 1
fi

# Upload the release to the repository
for __file in "$__specdir/"*.zip
do
	# Need to access the fossil unversion
	cd "$__realexedir"
	
	# Note it
	echo "Uploading $(basename -- "$__file")..."
	
	# Add it
	if fossil unversion add "$__file" \
		--as "release/$__vspec/$(basename -- "$__file")"
	then
		# Mark as uploaded
		__okay="$(($__okay + 1))"
	fi
done

