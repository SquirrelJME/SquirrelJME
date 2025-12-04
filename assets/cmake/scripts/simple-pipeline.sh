#!/bin/sh

# This command is required
__rsstail="$(which "rsstail" 2> /dev/null)"
if [ "$__rsstail" = "" ] || [ ! -x "$__rsstail" ]
then
	echo "Required utilities are missing:" 1>&2
	echo " - rsstail: $__rsstail" 1>&2
	exit 1
fi

# Default settings
__fossilUrl="https://squirreljme.cc/"
__fossilTag="trunk"

# Help
__squirreljme_help() {
	echo "$0 [options]" 1>&2
	echo "   -h" 1>&2
	echo "      Print this help text." 1>&2
	echo "   -u $__fossilUrl" 1>&2
	echo "      Fossil repository to check for updates in." 1>&2
	echo "   -t $__fossilTag" 1>&2
	echo "      Fossil tag to check for updates in." 1>&2
}

# Parse command-line settings
while getopts hu:t: __option
do
	case $__option in
		u)
			__fossilUrl="$OPTARG"
			;;
		
		t)
			__fossilTag="$OPTARG"
			;;
		
		h)
			__squirreljme_help
			exit 0
			;;

		?)
			__squirreljme_help
			exit 1
			;;
	esac
done

# Determine timeline RSS feed
__timelineRss="$__fossilUrl/timeline.rss?type=ci&tag=$__fossilTag"

# Notice
echo "Reading events from $__timelineRss..."

# Start the infinite run loop to check for updates
# Note we cannot grep for link here because rsstail seems to have some
# issues when buffering into read
rsstail -H -n 1 -l -i 30 -u "$__timelineRss" | while read -r __baseLine
do
	# Extract what is being used
	__form="$(echo "$__baseLine" | cut -d ':' -f 1)"
	__formLen="$(echo "$__form" | wc -c)"
	__data="$(echo "$__baseLine" | 
		cut -c "$(expr "1" "+" "$__formLen")-" | xargs)"
	
	# Only consider links that contain info
	if [ "$__form" != "Link" ] || \
		! (echo "$__data" | grep '\/info\/' > /dev/null)
	then
		# Skip
		continue
	fi
	
	echo "$__form ... $__data"
done
