#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: JavaP command for the VM classes

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Build classpath
__classpath=""
for file in bins/bbld/*.jar
do
	if [ "$__classpath" != "" ]
	then
		__classpath="$__classpath:"
	fi
	
	__classpath="$__classpath$file"
done

javap -bootclasspath "$__classpath" -verbose -private $*


