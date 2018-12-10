#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Goes through all sources and generates Java documentation.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Recursive dependency calculation
__root="$__exedir/../src"
__recursive_deps()
{
	# Convert manifest data
	__md="$(tr '\n' '\v' < "$__root/$1/META-INF/MANIFEST.MF" | sed 's/\v //g' |
		tr '\v' '\n')"
	
	# Get dependencies
	__pd="$(echo "$__md" | grep -i 'x-squirreljme-depends' |
		sed 's/^[^:]*:[ \t]*//' | sed 's/,/ /g')"
	
	# Add them all
	(for __dep in $__pd
	do
		echo "$__root/$__dep"
		
		# Recursive run
		__recursive_deps "$__dep"
	done) | sort | uniq | while read __line
	do
		echo "$__line"
	done
}

# Prints the dependencies nicely
__dep_print()
{
	__recursive_deps "$1" | while read __line
	do
		printf '%s' ":$__line"
	done
}

# Go through everything
for __dir in "$__root/"*
do
	# Ignore directories
	if [ ! -d "$__dir" ]
	then
		continue
	fi
	
	# Only allow projects
	if [ ! -f "$__dir/META-INF/MANIFEST.MF" ]
	then
		continue
	fi
	
	# Convert manifest data
	__md="$(tr '\n' '\v' < "$__dir/META-INF/MANIFEST.MF" | sed 's/\v //g' |
		tr '\v' '\n')"
	
	# Get name of this package and its dependencies
	__pn="$(echo "$__md" | grep -i 'x-squirreljme-name' |
		sed 's/^[^:]*:[ \t]*//')"
	
	# Header
	echo "*** $__pn" 1>&2
	
	# Build the classpath
	__cp="$__dir$(__dep_print "$__pn")"
	
	# Find all Java source files to document
	__sf="$(find "$__dir" -type f | grep '\.java$')"
	
	# Run java-doc on it
	mkdir -p "javadoc/$__pn"
	javadoc \
		-locale "en_US" \
		-d "javadoc/$__pn" \
		-private \
		-source 1.7 \
		-bootclasspath "$__cp" \
		-classpath "$__cp" \
		-sourcepath "$__cp" \
		-encoding "utf-8" \
		-author \
		-version \
		-windowtitle "SquirrelJME JavaDoc: $__pn" \
		-doctitle "SquirrelJME JavaDoc: $__pn" \
		-header "SquirrelJME JavaDoc: $__pn" \
		-footer "\
Copyright (C) Stephanie Gawroriski (xer@multiphasicapps.net)<br>
Copyright (C) Multi-Phasic Applications (multiphasicapps.net)<br>
SquirrelJME is under the GNU General Public License v3, or later.
See license.mkd for licensing and copyright information.
" \
		-linksource \
		-sourcetab 4 \
		-docencoding "utf-8" \
		$__sf
done

