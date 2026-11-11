# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Locates Java

# Locate Java 8
find_package(Java 1.8
	COMPONENTS Development)

# If javac was not found, try alternatives
if(NOT Java_Development_FOUND OR
	NOT Java_JAVAC_EXECUTABLE)
	# Remove these from the cache
	unset(Java_Development_FOUND CACHE)
	unset(Java_JAVAC_EXECUTABLE CACHE)

	# Try finding it through alternatives
	find_program(Java_JAVAC_EXECUTABLE
		NAMES javac ecj gcj)
endif()

# Which Java was found?
message(STATUS "java : ${Java_JAVA_EXECUTABLE}")
message(STATUS "javac: ${Java_JAVAC_EXECUTABLE}")

# Java build available?
if(Java_JAVA_EXECUTABLE AND Java_JAVAC_EXECUTABLE)
	set(SQUIRRELJME_HAS_JAVA_DEFAULT YES)
else()
	set(SQUIRRELJME_HAS_JAVA_DEFAULT NO)
endif()
option(SQUIRRELJME_HAS_JAVA "Java Build Capability"
	${SQUIRRELJME_HAS_JAVA_DEFAULT})

# Only possible when Java is available
if(SQUIRRELJME_HAS_JAVA)
	# Which Gradle is to be used?
	if("${CMAKE_HOST_SYSTEM_NAME}" STREQUAL "Windows")
		file(TO_CMAKE_PATH "${CMAKE_SOURCE_DIR}/gradlew"
			SQUIRRELJME_GRADLE_EXECUTABLE)
	else()
		file(TO_CMAKE_PATH "${CMAKE_SOURCE_DIR}/gradlew"
			SQUIRRELJME_GRADLE_EXECUTABLE)
	endif()

	# Add a target which executes Gradle
	function(squirreljme_add_gradle_target targetName ...)
		# Extract arguments for the call
		set(gradleArgs "${ARGV}")
		list(REMOVE_AT gradleArgs 0)

		# Now declare the target
		add_custom_target(${targetName}
			COMMAND "${CMAKE_COMMAND}"
				"-E" "env"
				"GRADLE_OPTS=-Dorg.gradle.daemon.idletimeout=5000"
				"JAVA_OPTS=-Dorg.gradle.daemon.idletimeout=5000"
				"--"
				"${SQUIRRELJME_GRADLE_EXECUTABLE}"
				# Do not make formatting nice looking
				"--console" "plain"
				# Continue without failure
				"--continue"
				# Parallel causes issues with GNU Makefile, so must be used
				# here just in case we are not using Ninja. This can also be
				# the case for other build systems as well.
				"--no-parallel"
				# We do not care about watching the filesystem for changes to
				# "optimize" the Gradle build
				"--no-watch-fs"
				# Note that the timeout is VERY important because the Gradle
				# daemon will cause builds to get stuck as some build systems
				# will wait until the Gradle daemon dies.
				# > Gradle Daemon will terminate itself after a specified
				# > number of idle milliseconds.
				# > Default is 10800000 (3 hours). <-- Bad idea
				"-Dorg.gradle.daemon.idletimeout=15000"
				"-Porg.gradle.daemon.idletimeout=15000"
				"-Dorg.gradle.jvmargs.org.gradle.daemon.idletimeout=5000"
				"-Porg.gradle.jvmargs.org.gradle.daemon.idletimeout=5000"
				# This does not do anything in modern Gradle other than
				# generally killing the daemon once the build completes.
				# It does not always work
				"--no-daemon"
				# We do not care for a scan to see how fast the build is,
				# as this is a build system run
				"--no-scan"
				# For debugging
				"--stacktrace"

				"${gradleArgs}"
			WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
			COMMAND_EXPAND_LISTS)

			# Set some SquirrelJME specific properties
			set_target_properties(${targetName} PROPERTIES
				SQUIRRELJME_GRADLE_BUILD YES
				SQUIRRELJME_TEST_RESULTS_DIR "${testResultsDir}")
	endfunction()
endif()

