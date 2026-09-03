#!/bin/sh -e
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Main build script
# This script is non-Bash POSIX compliant and should work on any POSIX system!

# Force C locale
export LC_ALL=C

# Load in the configuration script
__execDir="$(dirname -- "$0")"
if [ "$SQUIRRELJME_CICD_CONFIG_SCRIPT" != "" ] && \
	[ -x "$SQUIRRELJME_CICD_CONFIG_SCRIPT" ]
then
	. "$SQUIRRELJME_CICD_CONFIG_SCRIPT"
else
	. "$__execDir/build-config.sh"
fi

# Kill Gradle Daemons?
if [ "$__killGradle" = "true" ]
then
	# Kill java, in the event it is stuck
	echo "Killing any potentially alive Gradle daemons..." 1>&2
	
	# Hopefully PS is in a specific order...
	# Under FreeBSD
	# USER         PID  %CPU %MEM    VSZ    RSS TT  STAT STARTED      ...
	# root          11 395.7  0.0      0     64  -  RNL  21Aug26 45810...
	#
	# Under Linux
	# USER         PID %CPU %MEM    VSZ   RSS TTY      STAT START   TI...
	# root           1  0.0  0.0  27800 17676 ?        Ss   Aug27   2:...

	# Look for PIDs to kill
	ps aux | \
		grep 'java' | \
		grep 'gradle-launcher' | \
		grep -v 'grep' | \
		awk '{print $2}' | while read __pid
	do
		# Try a normal kill
		kill -- "$__pid" || echo "Ignoring failed kill $__pid..."
		
		# Hopefully enough time to let the Java daemon gracefully die...
		sleep 3
		
		# Then a force kill
		kill -9 -- "$__pid" || echo "Ignoring failed kill -9 $__pid..."
	done
fi

# Synchronize first so we get the latest changes
# Double-sync for intermittent database/network issues
"$__execDir/build-sync.sh" || "$__execDir/build-sync.sh"

# Notice
echo "*** CLEANING UP SOURCE DIRECTORY ***" 1>&2

# Completely clean the source tree of any files which should not be around
__was="$(pwd)"
cd -- "$__fossilCheckout"
"$__fossilCommand" clean -x -f -v
cd -- "$__was"

# Notice
echo "*** FOSSIL UPDATE ***" 1>&2

# We must jump to the checkout and perform an update accordingly, note that
# we need to be in the checkout directory as it is there. We do unfortunately
# have to juggle updates because Fossil can have a "different" idea of what
# a ref points to and `trunk` might be an out of date commit.
__was="$(pwd)"
cd -- "$__fossilCheckout"
"$__fossilCommand" update
"$__fossilCommand" update "$__fossilRef"
"$__fossilCommand" update
cd -- "$__was"

# Notice
echo "*** CLEANING UP BUILD DIRECTORY ***" 1>&2

# Remove the old build directory first
if [ -d "$__buildDir" ] && [ -f "$__buildDir/CMakeCache.txt" ]
then
	rm -rvf -- "$__buildDir"
fi

# Recreate the build directory
if ! mkdir -- "$__buildDir"
then
	mkdir -p -- "$__buildDir"
fi

# Notice
echo "*** CMAKE CONFIGURE ***" 1>&2

# Configure the build, with Old CMake
if [ "$__oldCMake" = "true" ]
then
	# The build context must always be in the build directory, as fully out
	# of tree and anywhere builds are in Modern CMake
	__was="$(pwd)"
	cd -- "$__buildDir"
	
	# Try with a generator
	if ! "$__cMakeCommand" -G "$__cMakeGenerator" "$__fossilCheckout"
	then
		# If that fails, then just do without the generator
		"$__cMakeCommand" "$__fossilCheckout"
		
		# Clear, just in case...
		__cMakeGenerator=""
	fi
	
	# Go back
	cd -- "$__was"
	
# Or with Modern CMake
else
	# Try with a generator
	if ! "$__cMakeCommand" -G "$__cMakeGenerator" \
		-B "$__buildDir" -S "$__fossilCheckout"
	then
		# If that fails, then just do without the generator
		cmake -B "$__buildDir" -S "$__fossilCheckout"
		
		# Clear, just in case...
		__cMakeGenerator=""
	fi
fi

# Extra arguments to pass to make? Note that no parallelism is used because
# of limitations with GNU Make and potential nested invocations of it through
# Gradle...
__extraMake=""
case "$__cMakeGenerator" in
	"Ninja")
		__extraMake="-k 0 -j1"
		;;
	
	"Unix Makefiles")
		__extraMake="-k -j1"
		;;
	
		# Unknown
	*)
		__extraMake=""
		;;
esac

# Build standalone.jar?
if [ "$__buildStandalone" = "true" ]
then
	# Notice
	echo "*** CMAKE BUILD STANDALONE ***" 1>&2
	
	# Fortunately, this is the same in old and modern CMake however the
	# options that can be specified and how they are specified is important
	"$__cMakeCommand" --build "$__buildDir" \
		--target standalone.jar -- $__extraMake
fi

# Notice
echo "*** CMAKE FOSSIL UPLOAD ***" 1>&2

# Fossil Upload Target, CMake invocation is as above
"$__cMakeCommand" --build "$__buildDir" \
	--target "$__cMakeUpload" -- $__extraMake

# Notice
echo "*** POST SYNC ***" 1>&2

# Finalize with a sync, to upload anything we built
# Double-sync for intermittent database/network issues
"$__execDir/build-sync.sh" || "$__execDir/build-sync.sh"

# Notice
echo "*** COMPLETED BUILD $(date) ***" 1>&2
