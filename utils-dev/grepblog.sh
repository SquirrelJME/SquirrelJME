#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Greps through the notes that may have been written.

# Force C locale
export LC_ALL=C

# Usage
if [ "$#" -le "0" ]
then
	echo "Usage: $0 (grep expression...)"
	exit 1
fi

# Supports multiple greps of stdin
__grep()
{
	# Single expression, used directly
	if [ "$#" -eq "1" ]
	then
		grep "$1"
		return $?
	
	# Group all of the expressions together then grep
	else
		grep $(while [ "$#" -ge "1" ]
			do
				echo "-e"
				echo "$1"
				shift
			done)
		return $?
	fi
}

# Directory of this script
__exedir="$(dirname -- "$0")"

# Go through the list of files
fossil unversion ls | grep '^developer-notes/' | sort | \
	while read __line
do
	# Grep contents for this note
	fossil unversion cat "$__line" | __grep $@ > /tmp/$$
	
	# Print search, but only if it has size
	if [ -s "/tmp/$$" ]
	then
		echo "###### $(echo "$__line" | cut -c 17-) ######"
		cat /tmp/$$
		echo ""
	fi
done

# Delete temporary
rm -f /tmp/$$
