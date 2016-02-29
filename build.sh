#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Builds whatever is needed to support hairball and then invokes
# it using the host virtual machine.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# If no arguments, then assume hairball
if [ "$#" -le "0" ]
then
	# Build hairball
	if ! "$0" "build" "hairball"
	then
		echo "Failed to build hairball."
		exit 1
	fi
	
	exit 0
fi

# Building a package
if [ "$1" = "build" ]
then
	# Need arguments?
	if [ "$#" -le "1" ]
	then
		echo "Usage: $0 $1 (package)"
		exit 1
	fi
	
	# The package to compile
	__pack="$2"

	# Needs a manifest
	__manf="$__exedir/src/$__pack/META-INF/MANIFEST.MF"
	if [ ! -f "$__manf" ]
	then
		echo "The package $__pack does not have a manifest."
		exit 1
	fi

	# Build package dependencies
	__depends="$(tr '\n' '\v' < $__manf | sed 's/\v //g' | tr '\v' '\n' |
		grep '^X-Hairball-Depends[ \t]*:' | sed 's/^[^:]*://g')"
	for __dep in $__depends
	do
		# Build dependency
		if ! "$0" "build" "$__dep"
		then
			echo "Failed to build dependency $__dep for $__pack."
			exit 1
		fi
	done

	# Get the output JAR and source locations
	__ojar="$__pack.jar"
	__isrc="$__exedir/src/$__pack"

	# If the JAR exists, check the date against the sources
	rm -f /tmp/$$
	if [ -f "$__ojar" ]
	then
		# Compare the JAR date against all files
		find "$__isrc" -type f | while read __file
		do
			if [ "$__file" -nt "$__ojar" ]
			then
				touch /tmp/$$
				break
			fi
		done
	
		# Compare dependencies (if they exist)
		for __dep in $__depends
		do
			if [ -f "$__dep.jar" ] && [ "$__dep.jar" -nt "$__ojar" ]
			then
				touch /tmp/$$
				break
			fi
		done

	# Always out of date
	else
		touch /tmp/$$
	fi

	# Out of date?
	if [ -f /tmp/$$ ]
	then
		rm -f /tmp/$$
		__ood=1
	else
		__ood=0
	fi

	# Perform a rebuild?
	if [ "$__ood" -ne "0" ]
	then
		# List source code which needs compilation
		find "$__isrc" -type f | grep '\.java$' > /tmp/$$
		
		# Place code here
		mkdir -p "/tmp/$$.$__pack"
		
		# Note it
		echo "*** Compiling $__pack ***"
		
		# Calculate stuff to run it with
		__cp=""
		for __x in $__depends
		do
			# Add separator
			if [ -n "$__cp" ]
			then
				__cp="$__cp:"
			fi
			
			# Append
			__cp="$__cp$__x.jar"
		done
		
		# Compile code
		if ! javac -d "/tmp/$$.$__pack" -source 1.7 -target 1.7 -g \
			-bootclasspath "$__cp" @/tmp/$$
		then
			# Note
			echo "Failed to compile $__pack."
			
			# Delete temporaries
			rm -rf "/tmp/$$.$__pack"
			
			# Failed
			exit 1
		fi
		
		# Package it up
		if ! jar cf "$__ojar" -C "/tmp/$$.$__pack" .
		then
			# Note
			echo "Failed to package $__pack."
			
			# Delete
			rm -rf "/tmp/$$.$__pack"
			
			# Failed
			exit 1
		fi 
		
		# Cleanup
		rm -rf "/tmp/$$.$__pack"
	fi

# Unknown command
else
	echo "Unknown sub-command $1."
	exit 1
fi

