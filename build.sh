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
		echo "Failed to build hairball." 2>&1
		exit 1
	fi
	
	# Launch hairball
	"$0" "launch" "hairball"
	exit $?
fi

# Launch hairball
if [ "$1" = "launch" ]
then
	# Need arguments?
	if [ "$#" -le "1" ]
	then
		echo "Usage: $0 $1 (package)" 2>&1
		exit 1
	fi
	
	# The package to launch
	__pack="$2"
	
	# Launch the given command
	java -cp "$("$0" "launchclasspath" "$__pack")" \
		net.multiphasicapps.hairball.Main $*
	exit $?

# Calculate dependenices for a package to launch it
elif [ "$1" = "launchclasspath" ]
then
	# Need arguments?
	if [ "$#" -le "1" ]
	then
		echo "Usage: $0 $1 (package)" 2>&1
		exit 1
	fi
	
	# The package to get dependencies for
	__pack="$2"
	
	# Get the dependent class path
	__dcp="$("$0" dependsclasspath "$__pack")"
	
	# If there are none, then just print this package
	if [ -z "$__dcp" ]
	then
		echo "$__pack.jar"
	
	# Otherwise add ours on top
	else
		echo "$__dcp:$__pack.jar"
	fi

# Calculate the dependent classpath for a specific package
elif [ "$1" = "dependsclasspath" ]
then
	# Need arguments?
	if [ "$#" -le "1" ]
	then
		echo "Usage: $0 $1 (package)" 2>&1
		exit 1
	fi
	
	# The package to get dependencies for
	__pack="$2"
	
	# Get dependencies
	__i=0
	"$0" depends "$__pack" | while read __line
	do
		# Flagged?
		if [ "$__i" -ne "0" ]
		then
			echo -n ":"
		fi
		
		# Set to use colon next time
		__i=1
		
		# Print it
		echo -n "$__line.jar"
	done

# Get all package dependencies without recursion
elif [ "$1" = "dependsnorecurse" ]
then
	# Need arguments?
	if [ "$#" -le "1" ]
	then
		echo "Usage: $0 $1 (package)" 2>&1
		exit 1
	fi
	
	# The package to get dependencies for
	__pack="$2"

	# Needs a manifest
	__manf="$__exedir/src/$__pack/META-INF/MANIFEST.MF"
	if [ ! -f "$__manf" ]
	then
		echo "The package $__pack does not have a manifest." 2>&1
		exit 1
	fi
	
	# Echo them out
	(for __dep in $(tr '\n' '\v' < $__manf | sed 's/\v //g' | tr '\v' '\n' |
		grep '^X-Hairball-Depends[ \t]*:' | sed 's/^[^:]*://g')
	do
		# Print this dependency
		echo "$__dep"
	done) | sort | uniq

# Get all package dependencies (recursive)
elif [ "$1" = "depends" ]
then
	# Need arguments?
	if [ "$#" -le "1" ]
	then
		echo "Usage: $0 $1 (package)" 2>&1
		exit 1
	fi
	
	# The package to get dependencies for
	__pack="$2"
	
	# Call non-recursive getter
	("$0" "dependsnorecurse" "$__pack" | while read __dep
	do
		# Print this dependency
		echo "$__dep"
		
		# Print dependencies of that dependency
		if ! "$0" "depends" "$__dep"
		then
			echo "Failed to print dependency $__dep for package $__pack."
			exit 1
		fi
	done) | sort | uniq
	
# Building a package
elif [ "$1" = "build" ]
then
	# Need arguments?
	if [ "$#" -le "1" ]
	then
		echo "Usage: $0 $1 (package)" 2>&1
		exit 1
	fi
	
	# The package to compile
	__pack="$2"

	# Needs a manifest
	__manf="$__exedir/src/$__pack/META-INF/MANIFEST.MF"
	if [ ! -f "$__manf" ]
	then
		echo "The package $__pack does not have a manifest." 2>&1
		exit 1
	fi

	# Build package dependencies
	"$0" "dependsnorecurse" "$__pack" | while read __dep
	do
		# Build dependency
		if ! "$0" "build" "$__dep"
		then
			echo "Failed to build dependency $__dep for $__pack." 2>&1
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
		"$0" "dependsnorecurse" "$__pack" | while read __dep
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
		# Delete the output JAR first
		rm -f "$__ojar"
		
		# List source code which needs compilation
		find "$__isrc" -type f | grep '\.java$' > /tmp/$$
		
		# Place code here
		mkdir -p "/tmp/$$.$__pack"
		
		# Note it
		echo "*** Compiling $__pack ***" 2>&1
		
		# Calculate stuff to run it with
		__cp="$("$0" "dependsclasspath" "$__pack")"
		
		# Compile code but only if there is source code to actually compile
		if [ -s "/tmp/$$" ]
		then
			if ! javac -d "/tmp/$$.$__pack" -source 1.7 -target 1.7 -g \
				-bootclasspath "$__cp" @/tmp/$$
			then
				# Note
				echo "Failed to compile $__pack." 2>&1
			
				# Delete temporaries
				rm -rf "/tmp/$$.$__pack"
			
				# Failed
				exit 1
			fi
		fi
		
		# Package it up
		if ! jar cf "$__ojar" -C "/tmp/$$.$__pack" .
		then
			# Note
			echo "Failed to package $__pack." 2>&1
			
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
	echo "Unknown sub-command $1." 2>&1
	exit 1
fi

