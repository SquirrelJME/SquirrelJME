#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Generates font files.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# These two are important!
if ! which bdftopcf || ! which fontforge
then
	echo "Need both bdftopcf and fontforge!"
	exit 7
fi

# Output directory
__out="$__exedir/../runt/apis/midp-lcdui/cc/squirreljme/runtime/lcdui/font/"

# Where temporary files go
mkdir -p "/tmp/$$"

# Generate a bunch of fonts first
for __dir in "$__exedir/../assets/font/"*.sfdir
do
	# Get base name
	__base="$(basename -- "$__dir" .sfdir)"
	
	echo "Generating $__base!"
	if ! fontforge -c 'fontforge.open(sys.argv[1]).generate(sys.argv[2])' \
		"$__dir" "/tmp/$$/$__base-*.bdf"
	then
		echo "Failed to generate $__base!"
		exit 1
	fi
done

# Convert to PCFs which would be easier to handle, since integers are best way
# to handle things (since Java is naturally in ints), all fonts are treated
# as a bunch of ints! This allows the font data to just be read as quickly as
# needed be. Since Java is in big endian, the compiled font is in big endian
# format.
for __file in "/tmp/$$/"*.bdf
do
	# Get base name
	__base="$(basename -- "$__file")"
	
	# Convert first
	echo "Converting $__base!"
	if ! bdftopcf -p4 -u4 -M "$__file" | uuencode -m "$__base.pcf" > \
		"/tmp/$$/ok"
	then
		echo "Failed to convert $__base!"
		exit 2
	fi
	
	# Only use valid files
	mv -vf "/tmp/$$/ok" "$__out/$__base.pcf.__base64"
done

# Cleanup
rm -rvf -- "/tmp/$$/"


