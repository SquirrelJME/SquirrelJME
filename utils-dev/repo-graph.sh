#!/bin/sh
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Makes a line graph of SquirrelJME's repository stats
# Originally for comment lengths, but considering GitHub is going down the
# drain...

# Force C locale
export LC_ALL=C

# Directory of this script
__exeDir="$(dirname -- "$0")"

# Prints usage
__help()
{
	echo "Usage: $0 [-h] [-x] [-g] [-p] [-d dataset] [-|outfile]" 1>&2
	echo "    Makes a graph of a repository dataset." 1>&2
	echo "    -h -- Print this help text." 1>&2
	echo "    -x -- Output an 'xplot.org' graph." 1>&2
	echo "    -g -- Output an 'gnuplot' graph." 1>&2
	echo "    -p -- Preview the output graph." 1>&2
	echo "    -d -- The dataset to choose" 1>&2
	echo "       comments -- Comment lengths." 1>&2
	echo "       year     -- Commits made on a year." 1>&2
	echo "       month    -- Commits made on a specific month." 1>&2
	echo "       day      -- Commits made on a specific day." 1>&2
	echo "       hour     -- Commits made on a specific hour." 1>&2
	echo "       minute   -- Commits made on a specific minute." 1>&2
}

# Plots data
__plot()
{
	# Extract arguments
	___xpl="$1"
	___gpl="$2"
	___tempXpl="$3"
	___tempGpl="$4"
	___x="$5" 
	___y="$6"
	___lines="$7"
	
	# Outputting an XPL?
	if [ "$___xpl" -ne "0" ]
	then
		if [ "$___lines" -ne "0" ]
		then
			# xplot needs the old line coordinates for lines to work
			___lastX="$(cat "$___tempXpl.lx" 2> /dev/null)"
			___lastY="$(cat "$___tempXpl.ly" 2> /dev/null)"
			
			# If the coordinates are invalid, then plot a plus at the start
			if [ "$___lastX" = "" ] || [ "$___lastY" = "" ]
			then
				printf 'plus %d %d\n' "$___x" "$___y" >> "$___tempXpl"
				
			# Otherwise plot the line
			else
				printf 'line %d %d %d %d\n' \
					"$___lastX" "$___lastY" "$___x" "$___y" >> "$___tempXpl"
			fi
			
			# Record the last X/Y for next time
			printf '%d' "$___x" > "$___tempXpl.lx"
			printf '%d' "$___y" > "$___tempXpl.ly"
		else
			printf 'plus %d %d\n' "$___x" "$___y" >> "$___tempXpl"
		fi
	fi
	
	# Outputting a GPL?
	if [ "$___gpl" -ne "0" ]
	then
		printf '%d %d\n' "$___x" "$___y" >> "$___tempGpl"
	fi
	
	# Success!
	return
}

# Time based plotting with a simple grep
__timeGrep()
{
	# Extract arguments
	___xpl="$1"
	___gpl="$2"
	___tempXpl="$3"
	___tempGpl="$4" 
	___x="$5"
	___lines="$6"
	___tempStamps="$7"
	___grep="$8"
	
	# Progress...
	echo -n "." 1>&2
	
	# Regex is whatever is needed based on the input
	# 2024-05-13 18:00:54
	__count="$(grep "$___grep" < \
		"$___tempStamps" | wc -l)"
	
	# Plot
	__plot "$___xpl" "$___gpl" "$___tempXpl" "$___tempGpl" \
		"$___x" "$__count" "$___lines"
		
	# Done!
	return
}

# Handle arguments
__dataset=""
__xpl="0"
__gpl="0"
__preview="0"
while getopts "hd:xgp" __arg
do
	case "$__arg" in
		'd')
			__dataset="$OPTARG"
			;;

		'g')
			__gpl="1"
			;;

		'x')
			__xpl="1"
			;;

		'p')
			__preview="1"
			;;

		'h')
			__help
			exit 0
			;;
		
		*)
			__help
			exit 1
			;;
	esac
done

# Down they go
shift $(($OPTIND - 1))

# Wrong number of arguments?
if [ "$#" -gt "1" ]
then
	__help
	exit 1
fi

# The output file is purely optional
__outFile=""
if [ "$#" -eq "1" ]
then
	__outFile="$1"
fi

# Previewing the plot?
if [ "$__preview" -ne "0" ]
then
	# If a preview was asked for, but no plot type was asked for then default
	# to gnuplot before xplot.org
	if [ "$__xpl" -eq "0" ] && [ "$__gpl" -eq "0" ]
	then
		# gnuplot?
		if which gnuplot 2>&1 > /dev/null
		then
			__gpl="1"
			
		# xplot.org?
		elif which xplot.org 2>&1 > /dev/null
		then
			__xpl="1"
			
		# Nothing, fail
		else
			echo "Preview was requested, however neither xplot.org or" 1>&2
			echo "gnuplot were found on this system...." 1>&2
			exit 2
		fi
	fi
else
	# Rather pointless if no specific plot was requested and no preview is
	# being done
	if [ "$__xpl" -eq "0" ] && [ "$__gpl" -eq "0" ]
	then
		__help
		exit 3
	fi
fi

# Get the dataset information
__title="SquirrelJME"
__xLabel="X Label"
__yLabel="Y Label"
__xUnits="X Units"
__origX="0"
__origY="0"
__lines="0"
case "$__dataset" in
	'comments')
		__title="$__title Commit Comment Lengths"
		__xLabel="Comment Length"
		__yLabel="Commit Count"
		__xUnits="B"
		;;

	'year')
		__title="$__title Commits Per Year"
		__xLabel="Year"
		__yLabel="Commit Count"
		__xUnits="CE"
		__origX="2014"
		__lines="1"
		;;

	'month')
		__title="$__title Commits Per Given Month"
		__xLabel="Month"
		__yLabel="Commit Count"
		__xUnits="th"
		__lines="1"
		;;

	'day')
		__title="$__title Commits Per Given Day"
		__xLabel="Day"
		__yLabel="Commit Count"
		__xUnits="th"
		__lines="1"
		;;

	'hour')
		__title="$__title Commits Per Given Hour"
		__xLabel="Hour"
		__yLabel="Commit Count"
		__xUnits="th"
		__lines="1"
		;;

	'minute')
		__title="$__title Commits Per Given Minute"
		__xLabel="Minute"
		__yLabel="Commit Count"
		__xUnits="th"
		__lines="1"
		;;

	*)
		# Was none just specified or just the wrong one?
		if [ "$__dataset" == "" ]
		then
			echo "No dataset was specified..." 1>&2
		else
			echo "Unknown dataset '$__dataset'..." 1>&2
		fi
		
		__help
		exit 4
		;;
esac

# Try to make less files if things happen to fail
if [ "$PPID" != "" ] && [ "$PPID" -ne "0" ]
then
	__pid="$PPID"
else
	__pid="$$"
fi

# The graph needs to go somewhere
__tempDir="/tmp"
__tempBase="$__tempDir/.$__pid"
__tempXpl="$__tempBase.xpl"
__tempGpl="$__tempBase.gpl"
__tempTimeline="$__tempBase.timeline"
__tempComments="$__tempBase.comments"
__tempStamps="$__tempBase.timestamps"

# Remove any old files, if they exist
rm -f -- "$__tempXpl" "$__tempXpl.lx" "$__tempXpl.ly" "$__tempGpl" \
	"$__tempTimeline" "$__tempComments" "$__tempStamps"

# Setup base XPL, if desired
if [ "$__xpl" -ne "0" ]
then
	# Note it
	echo "Initializing XPL..." 1>&2
	
	# Fill out the file
	printf 'unsigned unsigned\n' > "$__tempXpl"
	printf 'title\n' >> "$__tempXpl"
	printf '%s\n' "$__title" >> "$__tempXpl"
	printf 'xlabel\n' >> "$__tempXpl"
	printf '%s\n' "$__xLabel" >> "$__tempXpl"
	printf 'ylabel\n' >> "$__tempXpl"
	printf '%s\n' "$__yLabel" >> "$__tempXpl"
	printf 'xunits\n' >> "$__tempXpl"
	printf '%s\n' "$__xUnits" >> "$__tempXpl"
	printf 'invisible %d %d\n' "$__origX" "$__origY" >> "$__tempXpl"
fi

# Setup base GPL, if desired
if [ "$__gpl" -ne "0" ]
then
	# Note it
	echo "Initializing GPL..." 1>&2
	
	# Fill out the file
	printf 'set title "%s"\n' "$__title" > "$__tempGpl"
	printf 'set xlabel "%s"\n' "$__xLabel" > "$__tempGpl"
	printf 'set ylabel "%s"\n' "$__yLabel" > "$__tempGpl"
	printf 'set format x "%.0d"\n' >> "$__tempGpl"
	printf 'set format y "%.0d"\n' >> "$__tempGpl"
	printf 'set nokey\n' >> "$__tempGpl"
	#printf 'set autoscale keepfixed\n' >> "$__tempGpl"
	#printf 'set offsets graph %d, %d\n' "$__origX" "$__origY" >> "$__tempGpl"
	
	# Luckily we can just inline data with gnuplot
	# http://gnuplot.info/docs_6.1/loc3589.html
	printf '$Squeak << EOD\n' >> "$__tempGpl"
fi

# Put the full timeline somewhere, so it is not dumped all the time
echo "Dumping timeline..." 1>&2
fossil timeline -type ci --full -n 0 > "$__tempTimeline"

# Do we care about comments?
if [ "$__dataset" = "comments" ]
then
	# Note it
	echo "Extracting comments..." 1>&2
	
	# Now do it
	grep 'Comment:' < "$__tempTimeline" | \
		sed 's/Comment: *//g' > "$__tempComments"
		
# Or the date?
elif [ "$__dataset" = "year" ] || [ "$__dataset" = "month" ] || \
	[ "$__dataset" = "day" ] || [ "$__dataset" = "hour" ] || \
	[ "$__dataset" = "minute" ]
then
	# Note it
	echo "Extracting timestamps..." 1>&2
	
	# Now do it
	grep 'Date:' < "$__tempTimeline" | \
		sed 's/Date: *//g' > "$__tempStamps"
fi
	
# This will take awhile
echo -n "Processing '$__title'..." 1>&2

# Extract the data
case "$__dataset" in
	'comments')
		# These intervals should make sense
		__old="0"
		__add="5"
		__i="$__add"
		while [ "$__i" -le "2200" ]
		do
			# Progress...
			echo -n "." 1>&2
		
			# How many commits have comments that are this long?
			__count="$(grep '^.\{'"$__old"','"$__i"'\}$' < \
				"$__tempComments" | wc -l)"
			
			# Plot it, if not zero
			if [ "$__count" -ne "0" ]
			then
				__plot "$__xpl" "$__gpl" "$__tempXpl" "$__tempGpl" \
					"$__i" "$__count" "$__lines"
			fi
			
			# Bump up so commits in the old group are not counted and
			# the count itself
			__old="$__i"
			__i="$(expr "$__i" + "$__add")"
		done
		;;

	year)
		# Only care about then to now
		for __year in $(seq 2016 $(date -- +%Y) )
		do
			__timeGrep "$__xpl" "$__gpl" "$__tempXpl" "$__tempGpl" \
				"$__year" "$__lines" "$__tempStamps" \
				'^'"$__year"-.*'$'
		done
		;;

	month)
		# There are only 12 months in the year
		for __month in $(seq 1 12)
		do
			__timeGrep "$__xpl" "$__gpl" "$__tempXpl" "$__tempGpl" \
				"$__month" "$__lines" "$__tempStamps" \
				'^....-0\{0,1\}'"$__month"'-.*$'
		done
		;;

	day)
		# There are only up to 31 days in a month
		for __day in $(seq 1 31)
		do
			__timeGrep "$__xpl" "$__gpl" "$__tempXpl" "$__tempGpl" \
				"$__day" "$__lines" "$__tempStamps" \
				'^....-..-0\{0,1\}'"$__day"'.*$'
		done
		;;

	hour)
		# There are only up to 24 hours in a day
		for __hour in $(seq 0 23)
		do
			__timeGrep "$__xpl" "$__gpl" "$__tempXpl" "$__tempGpl" \
				"$__hour" "$__lines" "$__tempStamps" \
				'^....-..-.. 0\{0,1\}'"$__hour"':.*$'
		done
		;;

	minute)
		# There are only 60 minutes in an hour
		for __minute in $(seq 0 59)
		do
			__timeGrep "$__xpl" "$__gpl" "$__tempXpl" "$__tempGpl" \
				"$__minute" "$__lines" "$__tempStamps" \
				'^....-..-.. ..:0\{0,1\}'"$__minute"':.*$'
		done
		;;

	*)
		echo "NOT IMPLEMENTED '$__dataset'!" 1>&2
		;;
esac

# Done
echo "Done!" 1>&2

# These are no longer needed
rm -f -- "$__tempTimeline" "$__tempComments"

# Note this
echo "Finalizing plots..." 1>&2

# XPL is much simpler
if [ "$__xpl" -ne "0" ]
then
	printf 'go\n' >> "$__tempXpl"
fi

# End of plot is needed for GPL
if [ "$__gpl" -ne "0" ]
then
	# We need to actually end the inline data but also tell gnuplot to use
	# this data
	printf 'EOD\n' >> "$__tempGpl"
	
	# Lines or points?
	if [ "$__lines" -ne "0" ]
	then
		printf 'plot "$Squeak" index 0 with lines;\n' >> "$__tempGpl"
	else
		printf 'plot "$Squeak" index 0 with points ps 2;\n' >> "$__tempGpl"
	fi
	
	printf 'replot\n' >> "$__tempGpl"
	printf 'pause -1;\n' >> "$__tempGpl"
fi

# Previewing the graph?
if [ "$__preview" -ne "0" ]
then
	# Note this
	echo "Previewing plots..." 1>&2
	
	# xplot.org preview?
	if [ "$__xpl" -ne "0" ]
	then
		xplot.org "$__tempXpl"
	fi
	
	# gnuplot preview?
	if [ "$__gpl" -ne "0" ]
	then
		gnuplot "$__tempGpl"
	fi
fi

# Note this
echo "Outputting..." 1>&2

# Prefer outputting gnuplot over xplot.org
if [ "$__gpl" -ne "0" ]
then
	__tempFile="$__tempGpl"
else
	__tempFile="$__tempXpl"
fi

# Going just to stdout?
if [ "$__outFile" = "-" ]
then
	cat "$__tempFile"
	
# If we actually care for the output, move it over
elif [ "$__outFile" != "" ]
then
	# Move it
	mv -f -- "$__tempFile" "$__outFile"
	
else
	# Otherwise delete, we just wanted a preview
	rm -f "$__tempFile"
fi

# Cleanup other temp files
rm -f -- "$__tempXpl" "$__tempXpl.lx" "$__tempXpl.ly" "$__tempGpl" \
	"$__tempTimeline" "$__tempComments" "$__tempStamps"

# Success!
exit 0
