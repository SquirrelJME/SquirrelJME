#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: This class reads the pixel array graphics and processes them
# through the C preprocessor to generate the files and have it where they
# are for the most part inlined. The purpose is to create ugly code that has
# very similar operations that never crosses method bounds as much as it can.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through all format types
for __type in type/*.h
do
	gcc -nostdinc -nostdlib -include "$__type" -E -CC - < \
		TemplateArrayGraphics.java | \
		sed '/^\#/d' | sed '/^$/d' | tr '$' '/' | \
		astyle --style=allman \
		--close-templates --max-code-length=79 --pad-oper -T4 \
		--indent-after-parens > /tmp/$$.java
	
	# Determine name of the file
	__name="$(cat /tmp/$$.java | grep 'public.*class' | head -n 1 |
		sed 's/.*class[ \t]*\([a-zA-Z0-9_]\{1,\}\).*/\1/')"
	mv -v /tmp/$$.java "$__exedir/../../runt/apis/midp-lcdui/cc/squirreljme/runtime/lcdui/gfx/$__name.java"
done

# Cleanup
rm -f /tmp/$$.java

