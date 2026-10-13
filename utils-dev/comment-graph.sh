#!/bin/sh
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Makes a line graph of SquirrelJME's comment logs

# Force C locale
export LC_ALL=C

# Directory of this script
__exeDir="$(dirname -- "$0")"

# Temporary files
__logs="/tmp/$$.timeline"
__xpl="/tmp/$$.xpl"
__gpl="/tmp/$$.gpl"
	
# Remove any old files
rm -f "$__logs" "$__xpl" "$__gpl"

# Setup base XPL
cat <<EOF > "$__xpl"
unsigned unsigned
title
SquirrelJME Commit Comment Lengths
xlabel
Comment Length
ylabel
Commit Count
EOF

# Put the full timeline somewhere, so it is not dumped all the time
echo "Dumping logs..." 1>&2
fossil timeline --full -n 0 | grep 'Comment:' |
	sed 's/Comment: *//g' > "$__logs"
	
# This will take awhile
echo "Processing commits..." 1>&2

# These intervals should make sense
__old="0"
__add="5"
__i="$__add"
while [ "$__i" -le "2200" ]
do
	# Progress...
	echo -n "." 1>&2

	# How many commits have comments that are this long?
	__count="$(grep '^.\{'"$__old"','"$__i"'\}$' < "$__logs" | wc -l)"
	
	# Plot it, if not zero
	if [ "$__count" -ne "0" ]
	then
		echo "plus $__i $__count" >> "$__xpl"
	fi
	
	# Bump up so commits in the old group are not counted and the count itself
	__old="$__i"
	__i="$(expr "$__i" + "$__add")"
done

# Done!
echo "Done!" 1>&2

# Finalize
echo "go" >> "$__xpl"

# Convert the plot, switch to /tmp because it just outputs it in the current
# working directory which is not what we want
(cd "/tmp"; xpl2gpl "$__xpl")

# Show the plot, this uses local file references which gnuplot I suppose
# does not support looking them up if the graph is elsewhere... *sigh*
(cd "/tmp"; gnuplot "$__gpl")

# Cleanup
rm -f "$__logs" "$__xpl" "$__gpl"
