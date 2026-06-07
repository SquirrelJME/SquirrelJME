# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Compiled Standalone Jar

# The Standalone Jar built by Gradle is in the build directory of the source
set(SQUIRRELJME_JAR_BUILD_DIR
	"${CMAKE_BINARY_DIR}/base/")
set(SQUIRRELJME_JAR_BUILD_PATH
	"${SQUIRRELJME_JAR_BUILD_DIR}/${SQUIRRELJME_JAR_BASENAME}")

# We can only build the standalone Jar if we have Java available
if(SQUIRRELJME_HAS_JAVA)
	# Native is needed for Gradle
	file(TO_NATIVE_PATH "${SQUIRRELJME_JAR_BUILD_PATH}"
		outputJarNative)

	# Add rule to build the Standalone Jar
	squirreljme_add_gradle_target(standalone.base.compiled
		"-Dsquirreljme.standalone.path=${outputJarNative}"
		"-Psquirreljme.standalone.path=${outputJarNative}"
		":emulators:standalone:shadowJar")

	# Set some SquirrelJME specific properties
	set_target_properties(standalone.base.compiled PROPERTIES
		EXCLUDE_FROM_ALL YES
		ADDITIONAL_CLEAN_FILES "${SQUIRRELJME_JAR_BUILD_PATH}"
		SQUIRRELJME_OUTPUT_PATH "${SQUIRRELJME_JAR_BUILD_PATH}"
		SQUIRRELJME_OUTPUT_TYPE "standalone")

	# Add to the order
	squirreljme_natives_append_rule(standalone.base.compiled
		"standalone" "base" "compiler")
endif()
