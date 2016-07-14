#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Moves a file with fossil mv and then mv

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Expect only two arguments
if [ "$#" -ne "2" ]
then
	echo "Usage: $0 (from) (to)" 1>&2
	exit 1
fi

# Get source and destination file
__src="$1"
__dst="$2"

# If the destination is just ., use bae of source
if [ "$__dst" = "." ]
then
	__dst="$(basename -- "$__src")"
fi

# Source must exist (and be an actual file) and the destination must not exist
if [ ! -f "$__src" ]
then
	echo "The source file '$__src' does not exist or is not a file." 1>&2
	exit 2
fi
if [ -e "$__dst" ]
then
	echo "The destination file '$__dst' already exists." 1>&2
	exit 3
fi

# Perform fossil move
if ! fossil mv "$__src" "$__dst"
then
	echo "Fossil move from '$__src' to '$__dst' failed." 1>&2
	exit 4
fi

# Normal move
if ! mv "$__src" "$__dst"
then
	echo "Real move from '$__src' to '$__dst' failed." 1>&2
	exit 5
fi

# All ok
exit 0

