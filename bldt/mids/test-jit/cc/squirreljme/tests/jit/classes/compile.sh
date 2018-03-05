#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Compiles class files and encodes them.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Remove all old class files
rm -vf -- *.class

# Compile all jasmin files
for __file in *.j
do
	jasmin -d "$("../../../../../../../../utils-dev/projectbase.sh" ".")" "$__file"
done

# Go through all class files and encode them
for __file in *.class
do
	uuencode -m "$__file" "$__file" < "$__file" > "$__file.b64"
done

