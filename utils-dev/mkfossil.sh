#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Exports fossil image

# Target location
if [ -n "$1" ]
then
	TARGET="$1"
else
	TARGET="/tmp/squirreljme.fossil"
fi

# Kill previous fossil repo, if it exists
if [ -f /tmp/squirreljme-fossil-pid ]
then
	kill $(cat /tmp/squirreljme-fossil-pid)
	rm -f /tmp/squirreljme-fossil-pid
fi

# Force C locale
export LC_ALL=C

# Current checkout
INFOLINE="$(fossil info | grep 'checkout:' | head -n 1)"
NOW="$(echo "$INFOLINE" | sed 's/.*\([0-9a-fA-F]\{40\}\).*/\1/g')"

# Generate random port to use
PORT="$(expr $(expr $$ % 32765) + 32765)"

# Sadfully, a local repo cannot be cloned to a new one (it is treated as
# a direct copy).
echo "Serving local copy to clone it..." 1>&2
fossil serve -P $PORT &
FOSSILPID=$!
echo $FOSSILPID > /tmp/squirreljme-fossil-pid
fossil clone -A squirreljme "http://127.0.0.1:$PORT" \
	"/tmp/$$.fsl"
kill -- $FOSSILPID
wait
rm -f -- /tmp/squirreljme-fossil-pid

# If target exists, Do a compressed rebuild
if [ -f "/tmp/$$.fsl" ]
then
	# Set difficult to use password to discourage people
	echo "Setting a random password..." 1>&2
	RPASS="$( (sha1sum "/tmp/$$.fsl" || md5sum "/tmp/$$.fsl") | tr '\t' ' ' | \
		cut -d ' ' -f 1)"
	echo "which is: '$RPASS'" 1>&2
	fossil user password squirreljme "$RPASS" -R "/tmp/$$.fsl"
	fossil user default squirreljme -R "/tmp/$$.fsl"
	
	# Modify default permissions to make them a bit strong a bit
	# Anonymous logins can spam the ticket or wiki system, which is an exploit
	# provided they know how to cut and paste a password.
	echo "Changing permissions..." 1>&2
	CAPANON="$(fossil user capabilities anonymous -R "/tmp/$$.fsl")"
	CAPDEVE="$(fossil user capabilities developer -R "/tmp/$$.fsl")"
	CAPNOBO="$(fossil user capabilities nobody -R "/tmp/$$.fsl")"
	CAPREAD="$(fossil user capabilities reader -R "/tmp/$$.fsl")"
	
	# Change Powers
		# nobody
	echo "nobody was: $CAPNOBO" 1>&2
	CAPNOBO="ghjorz"
	fossil user capabilities nobody $CAPNOBO -R "/tmp/$$.fsl"
	echo "Now: $CAPNOBO" 1>&2
	
		# Anon (same as nobody)
	echo "anonymous was: $CAPANON" 1>&2
	CAPANON="$CAPNOBO"
	fossil user capabilities anonymous $CAPANON -R "/tmp/$$.fsl"
	echo "Now: $CAPANON" 1>&2
	
		# reader
	echo "reader was: $CAPREAD" 1>&2
	CAPREAD="kptwhmnczgjor"
	fossil user capabilities reader $CAPREAD -R "/tmp/$$.fsl"
	echo "Now: $CAPREAD" 1>&2
	
		# Dev
	echo "developer was: $CAPDEVE" 1>&2
	CAPDEVE="deihmnczgjor"
	fossil user capabilities developer $CAPDEVE -R "/tmp/$$.fsl"
	echo "Now: $CAPDEVE" 1>&2
	
	# Change some settings
	echo "Changing a few settings..." 1>&2
		# URL being set to the local clone is bad by default
	fossil remote-url -R "/tmp/$$.fsl" off
	fossil remote-url -R "/tmp/$$.fsl" \
		http://multiphasicapps.net/
		# Autosync to a 127.0.0.1 == Very annoying
	fossil settings -R "/tmp/$$.fsl" autosync 0
		# Case sensitivity (for Windows/DOS)
	fossil settings -R "/tmp/$$.fsl" case-sensitive 1
		# Enable the manifest so it appears in the ZIPs and such
	fossil settings -R "/tmp/$$.fsl" manifest 1
	
	# Add all of these settings
	for __conf in project skin shun ticket
	do
		fossil config push "$__conf" "/tmp/$$.fsl"
	done

	# Rebuild
	#echo "Rebuilding..." 1>&2
	#fossil rebuild --force --vacuum --pagesize 4096 --compress \
	#	--stats "/tmp/$$.fsl"
	
	# Create a ZIP file to append to the repository (note that any changes made
	# to the repository will damage it). Although this wastes a bit of space,
	# it is rather cool so that way you can still extract and compile the OS
	# from the appended ZIP without needing a fossil installation.
	echo "Exporting a ZIP of current code..." 1>&2
	fossil zip "$NOW" "/tmp/$$.zip" --name "squirreljme" -R "/tmp/$$.fsl"
	
	# Compress using some algorithms
	# Just used as a test to see how well things compress, not really that
	# important.
	if false
	then
		# Base tar to recompress
		fossil tar tip - --name "squirreljme" -R "/tmp/$$.fsl" \
			| gunzip -c - > "/tmp/$$.tar"
		
		# GZIP
		if which gzip 2> /dev/null > /dev/null
		then
			if ! gzip -9 -v -c - < "/tmp/$$.tar" > "/tmp/$$.tgz"
			then
				rm -f "/tmp/$$.tgz"
			fi
		fi
	
		# BZIP2
		if which bzip2 2> /dev/null > /dev/null
		then
			if ! bzip2 -9 -v -c - < "/tmp/$$.tar" > "/tmp/$$.tb2"
			then
				rm -f "/tmp/$$.tb2"
			fi
		fi
		
		# XZ
		if which xz 2> /dev/null > /dev/null
		then
			if ! xz -9 -v -c - < "/tmp/$$.tar" > "/tmp/$$.txz"
			then
				rm -f "/tmp/$$.txz"
			fi
		fi
	fi
	
	# Join the repository and the zip as one
	echo "Merging the files..." 1>&2
	cat "/tmp/$$.fsl" "/tmp/$$.zip" > "$TARGET"
	ls -sh "/tmp/$$."??? "$TARGET" | sort -h
	
	# Clear old files
	echo "Cleaning up..." 1>&2
	rm -f "/tmp/$$.fsl" "/tmp/$$.zip" "/tmp/$$.tar" "/tmp/$$.tgz" \
		"/tmp/$$.txz" "/tmp/$$.tb2"
else
	exit 1
fi

echo "Done." 1>&2
exit 0

