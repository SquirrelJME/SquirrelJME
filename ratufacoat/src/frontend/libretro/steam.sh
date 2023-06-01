#!/bin/sh

# Are we running on FlatPak?
if which flatpak-spawn > /dev/null
then
	# Check if Steam exists
	if ! flatpak-spawn --host which steam > /dev/null
	then
		echo "Running in FlatPak but host has no Steam..." 1>&2
		exit 1
	fi
else
	# Check if Steam exists
	if ! which steam > /dev/null
	then
		echo "No steam available..." 1>&2
		exit 1
	fi
fi

# Running with a command?
if [ "$#" -gt "0" ]
then
	# We need to percent encode our path
	# steam://run/1118310//-L%20hi/
	__encPath="$(readlink -f -- "$1" | sed 's/\//%2f/g;s/ /%20/g')"
	__steamUrl="steam://run/1118310//-L%20$__encPath/"

	# Launch Steam
	if which flatpak-spawn > /dev/null
	then
		flatpak-spawn --host steam "$__steamUrl"
		exit $?
	else
		steam "$__steamUrl"
		exit $?
	fi

# Just wanted to see if Steam exists
else
	exit 0
fi
