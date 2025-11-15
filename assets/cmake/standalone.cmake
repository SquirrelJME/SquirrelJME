# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Building of Standalone SquirrelJME

# Natives are needed for standalone
squirreljme_include("standalone-natives.cmake")

# Is a prebuilt Jar available through CI/CD?
if(DEFINED ENV{SQUIRRELJME_STANDALONE_JAR_BASE_PREBUILT_CICD} OR
	DEFINED SQUIRRELJME_STANDALONE_JAR_BASE_PREBUILT_CICD)

# Is a prebuilt Jar available through some other means?
elseif(DEFINED ENV{SQUIRRELJME_STANDALONE_JAR_BASE_PREBUILT} OR
	DEFINED SQUIRRELJME_STANDALONE_JAR_BASE_PREBUILT)

# Need to build the standalone Jar?
else(SQUIRRELJME_HAS_JAVA)
	# Standalone is available through building it directly
	set(SQUIRRELJME_HAS_STANDALONE_JAR_BASE YES)

	# The built Jar has to go somewhere
	set(outputPath "${CMAKE_BINARY_DIR}/standaloneBase/standalone.jar")

	# Native is needed for Gradle
	file(TO_NATIVE_PATH "${outputPath}"
		outputPathNative)

	# Add rule to build the Standalone Jar
	squirreljme_add_gradle_target(standaloneJarBase
		"-Dsquirreljme.standalone.path=${outputPathNative}"
		"-Psquirreljme.standalone.path=${outputPathNative}"
		":emulators:standalone:shadowJar")

	# Set some SquirrelJME specific properties
	set_target_properties(${targetName} PROPERTIES
		ADDITIONAL_CLEAN_FILES "${outputPath}"
		SQUIRRELJME_STANDALONE_JAR_PATH "${outputPath}")

	# Register CI/CD Task
	squirreljme_cicd_register(standaloneJarBase)
endif()

# If any Standalone Jar is available, then package all the natives together
# into it
if(SQUIRRELJME_HAS_STANDALONE_JAR_BASE)
	# The base Jar exists in some fashion, so the actual fully packaged
	# Standalone Jar with all natives also exists
	set(SQUIRRELJME_HAS_STANDALONE_JAR YES)
endif()
