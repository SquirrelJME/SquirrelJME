#!/bin/sh -e
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Configure script
# This script is non-Bash POSIX compliant and should work on any POSIX system!

#############################################################################

# Export any additional PATH entries for binaries that are not normally
# included in job schedulers.
#export PATH="$PATH:/somewhere:$HOME/.local/bin"

# The Fossil command, this generally never needs to change unless there is
# for example multiple Fossil versions installed and a specific one is
# required
__fossilCommand="fossil"

# The Fossil user to act as when running Fossil commands, note that this is
# not the current user to run as
__fossilUser="squirreljme"

# The path to the Fossil repository
__fossilRepo="$HOME/squirreljme.fossil"

# The path to the Fossil checkout
__fossilCheckout="$HOME/squirreljme"

# The Fossil reference to update to
__fossilRef="trunk"

# The build directory for CMake, be sure to _NOT_ mess this one up as this will
# be `rm -rf`ed!
__buildDir="/tmp/squirreljme-build"

# Set to `true` to indicate that a very old CMake version is being used and
# that the command structure should be different. You will know this is the
# case if `cmake -B buildDir -S sourceDir` does not work. This is generally
# for CMake before 3.13. 
__oldCMake="false"

# The CMake command to use
__cMakeCommand="cmake"

# Should Gradle be killed before a build runs? This generally is recommended
# as with specific makefile systems and Gradle, these daemons sitting around
# can cause issues for example with GNU Make.
__killGradle="true"

# The CMake Generator to use for the build, this can be listed via
# `cmake -G`. It is recommended to always use Ninja unless you cannot.
#__cMakeGenerator="Ninja"

# Build the Standalone Jar, or at least try?
__buildStandalone="true"

# The CMake Fossil upload target to use, if unspecified then this means
# all. This information is in `building.mkd`.
__cMakeUpload=""
#__cMakeUpload=".install4j"
#__cMakeUpload=".onlyNatives"
#__cMakeUpload=".sourceTar"
#__cMakeUpload=".sourceZip"

#############################################################################

# If no CMake Generator was set...
if [ "$__cMakeGenerator" = "" ]
then
	# Always prefer Ninja
	if which ninja 2>&1 > /dev/null
	then
		__cMakeGenerator="Ninja"
		
	# Otherwise, fallback to basic makefiles
	else
		__cMakeGenerator="Unix Makefiles"
	fi
fi

# Normalize paths
__fossilCommand="$(which -- "$__fossilCommand")"
__cMakeCommand="$(which -- "$__cMakeCommand")"
__fossilRepo="$(readlink -f -- "$__fossilRepo")"
__fossilCheckout="$(readlink -f -- "$__fossilCheckout")"

# Fossil does not exist, or cannot be executed?
if [ ! -f "$__fossilCommand" ] || \
	[ ! -x "$__fossilCommand" ]
then
	echo "Could not find Fossil: $___fossilCommand." 1>&2
	exit 2
fi

# CMake does not exist, or cannot be executed?
if [ ! -f "$__cMakeCommand" ] || \
	[ ! -x "$__cMakeCommand" ]
then
	echo "Could not find CMake: $__cMakeCommand." 1>&2
	exit 3
fi

# Checkout does not exist?
if [ ! -d "$__fossilCheckout" ] || \
	[ ! -f "$__fossilCheckout/squirreljme-version" ]
then
	echo "Fossil repository checkout directory does not exist!" 1>&2
	exit 4
fi

# Repository does not exist?
if [ ! -f "$__fossilRepo" ]
then
	echo "Fossil repository does not exist!" 1>&2
	exit 5
fi

# Normalize the Fossil upload target
if [ "$__cMakeUpload" = "" ]
then
	__cMakeUpload="fossilUpload"
else
	__cMakeUpload="fossilUpload.$__cMakeUpload"
fi

# Echo the current date, for log purposes
echo "*** CONFIGURE $(date) ***" 1>&2
