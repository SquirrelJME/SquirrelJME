#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: A more efficient but a more search chaotic grep of every single
# file in the repository
# The results are output based on their artifact ID and not the revision they
# are in, so all files are treated special

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Trigger to indicate that the request is being made, since it can take awhile
printf '%s' "...generating request..." 1>&2
__requested=0

# The HTTP interface allows one to get the list of all the blobs, however
# it is honypotted but despite that it still has the needed information.
# It is however in HTTP format. But html2text allows very plain text to be
# output, which means it is easier to parse
# The output is in the format of:
#  1044 613351de24 file src/extra-io/net/multiphasicapps/io/BitInputStream.java
# So these lines must be detected
__count="1"
__lastf=""
__ldate="1970-01-01"
echo "GET /bloblist?s=0&n=90000" | fossil http | 
	html2text -width 1000 -style compact -ascii |
	grep '^ *[0-9]\{1,\}  *[0-9a-fA-F]\{1,\}  *.*$' | sed 's/^ *//g' |
	while read __line
do
	# Request was made?
	if [ "$__requested" -eq "0" ]
	then
		echo "...getting lines!" 1>&2
		__requested=1
	fi
	
	# Extract bits of each
	__id="$(echo "$__line" | cut -d ' ' -f 1)"
	__uu="$(echo "$__line" | cut -d ' ' -f 2)"
	__at="$(echo "$__line" | cut -d ' ' -f 3)"
	__dd="$(echo "$__line" | cut -d ' ' -f 4)"
	
	# Only consider files
	if [ "$__at" != "file" ]
	then
		#
		if [ "$__at" = "check-in" ]
		then
			__ldate="$(echo "$__line" | cut -d ' ' -f 5)"
		fi
		
		continue
	fi
	
	# Indicate the number of files read
	if [ "$(($__count % 500))" -eq "0" ]
	then
		echo "...After $__ldate (counted $__count)..." 1>&2
	fi
	__count="$(($__count + 1))"
	
	# Cat the file and grep its contents, prefix each line with the artifact
	# ID, note that the truncated form can be used
	fossil artifact "$__uu" | grep -i -n -- "$1" | while read __match
	do
		echo "$__uu:$__dd:$__match"
	done
done

