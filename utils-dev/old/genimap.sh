#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Generates instruction map table since there are a ton of
# Math operation.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

for __type in "I:INT:INTEGER" "L:LONG:LONG" "F:FLOAT:FLOAT" "D:DOUBLE:DOUBLE"
do
	for __op in "ADD" "SUB" "MUL" "DIV" "REM" "SHL" "SHR" "USHR" "AND" "OR" \
		"XOR"
	do
		__jt="$(echo "$__type" | cut -d ':' -f 1)"
		__rt="$(echo "$__type" | cut -d ':' -f 2)"
		__xt="$(echo "$__type" | cut -d ':' -f 3)"
		
		printf 'case InstructionIndex.%s:\n\tthis.__runMathABC(RegisterOperationType.%s,\n\t\tJavaType.%s);\n\tbreak;\n\n' "${__jt}${__op}" "${__rt}_${__op}" "$__xt"
	done
done
