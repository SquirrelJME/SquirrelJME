#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Generates operation switches and tables.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

(while read __line
do
	# Ignore blanks
	if [ -z "$__line" ]
	then
		continue
	fi
	
	# Get operation details
	__dec="$(echo "$__line" | cut -d ' ' -f 1)"
	__hex="$(echo "$__line" | cut -d ' ' -f 2 | sed 'y/abcdef/ABCDEF/')"
	__nam="$(echo "$__line" | cut -d ' ' -f 3 | \
		sed 'y/qwertyuiopasdfghjklzxcvbnm/QWERTYUIOPASDFGHJKLZXCVBNM/')"
	
	# Switch
	if false
	then
		printf 'case %s: __handleOp%s(); break;\n' "$__hex" "$__nam" 1>&2
	
	# Methods
	else
		echo "$__nam"
	fi
done) | sort -n | cut -d ' ' -f 1 | while read __sub
do
	printf '
/**
 * Handles %s.
 *
 * @throws IOException On read errors.
 * @since 2016/03/23
 */
private void __handleOp%s()
	throws IOException
{
	// Get source
	DataInputStream source = _source;
	
	throw new Error("TODO");
}\n' "$__sub" "$__sub"

done

