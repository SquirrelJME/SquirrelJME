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
	
	# Outputting an XPL?
	if [ "$___xpl" -ne "0" ]
	then
		printf 'plus %d %d\n' "$___x" "$___y" >> "$___tempXpl"
	fi
	
	# Outputting a GPL?
	if [ "$___gpl" -ne "0" ]
	then
		printf '%d %d\n' "$___x" "$___y" >> "$___tempGpl"
	fi
	
	# Success!
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
case "$__dataset" in
	'comments')
		__title="$__title Commit Comment Lengths"
		__xLabel="Comment Length"
		__yLabel="Commit Count"
		__xUnits="B"
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

# Remove any old files, if they exist
rm -f -- "$__tempXpl" "$__tempGpl" "$__tempTimeline" "$__tempComments"

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
	printf 'invisible 0 0\n' >> "$__tempXpl"
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
	
	# Luckily we can just inline data with gnuplot
	# http://gnuplot.info/docs_6.1/loc3589.html
	printf '$Squeak << EOD\n' >> "$__tempGpl"
fi

# Put the full timeline somewhere, so it is not dumped all the time
echo "Dumping timeline..." 1>&2
fossil timeline --full -n 0 > "$__tempTimeline"

# Do we care about comments?
if [ "$__dataset" = "comments" ]
then
	# Note it
	echo "Extracting comments..." 1>&2
	
	# Now do it
	grep 'Comment:' < "$__tempTimeline" | \
		sed 's/Comment: *//g' > "$__tempComments"
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
					"$__i" "$__count"
			fi
			
			# Bump up so commits in the old group are not counted and
			# the count itself
			__old="$__i"
			__i="$(expr "$__i" + "$__add")"
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
	printf 'plot "$Squeak" index 0 with points pt 2;\n' >> "$__tempGpl"
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
rm -f -- "$__tempXpl" "$__tempGpl" "$__tempTimeline" "$__tempComments"

# Success!
exit 0
