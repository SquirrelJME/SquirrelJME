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
		ADDITIONAL_CLEAN_FILES "${outputBaseJar}"
		SQUIRRELJME_OUTPUT_PATH "${outputBaseJar}"
		SQUIRRELJME_OUTPUT_TYPE "standalone")

	# Add to the order
	squirreljme_natives_append_rule(standalone.base.compiled
		"standalone" "base" "compiler")
endif()

## Is a prebuilt Jar available through CI/CD?
#if(DEFINED ENV{SQUIRRELJME_JAR_JAR_BASE_PREBUILT_CICD} OR
#	DEFINED SQUIRRELJME_JAR_JAR_BASE_PREBUILT_CICD)
#
## Is a prebuilt Jar available through some other means?
#elseif(DEFINED ENV{SQUIRRELJME_JAR_JAR_BASE_PREBUILT} OR
#	DEFINED SQUIRRELJME_JAR_JAR_BASE_PREBUILT)
#
## Need to build the standalone Jar?
#else(SQUIRRELJME_HAS_JAVA)
#	# Standalone is available through building it directly
#	set(SQUIRRELJME_HAS_STANDALONE_JAR_BASE YES)
#
#	# The built Jar has to go somewhere
#	set(outputBaseJar "${CMAKE_BINARY_DIR}/standaloneBase/standalone-base.jar")
#	set(SQUIRRELJME_JAR_JAR_BASE_PATH "${outputBaseJar}")
#
#	# Native is needed for Gradle
#	file(TO_NATIVE_PATH "${outputBaseJar}"
#		outputBaseJarNative)
#
#	# Add rule to build the Standalone Jar
#	squirreljme_add_gradle_target(standaloneJarBase
#		"-Dsquirreljme.standalone.path=${outputBaseJarNative}"
#		"-Psquirreljme.standalone.path=${outputBaseJarNative}"
#		":emulators:standalone:shadowJar")
#
#	# Set some SquirrelJME specific properties
#	set_target_properties(standaloneJarBase PROPERTIES
#		ADDITIONAL_CLEAN_FILES "${outputBaseJar}"
#		SQUIRRELJME_OUTPUT_PATH "${outputBaseJar}")
#endif()
#
## If any Standalone Jar is available, then package all the natives together
## into it
#if(SQUIRRELJME_HAS_STANDALONE_JAR_BASE)
#	# The base Jar exists in some fashion, so the actual fully packaged
#	# Standalone Jar with all natives also exists
#	set(SQUIRRELJME_HAS_STANDALONE_JAR YES)
#
#	# Where is the actual standalone Jar placed?
#	set(inputJar "${SQUIRRELJME_JAR_JAR_BASE_PATH}")
#	set(outputDir "${CMAKE_BINARY_DIR}/standalone")
#	set(outputJar
#		"${outputDir}/squirreljme-standalone-${SQUIRRELJME_VERSION}.jar")
#	set(workPath "${CMAKE_BINARY_DIR}/standaloneWork")
#
#	# Setup target to combine everything into a single Jar, older CMake does
#	# not support append to archives so unfortunately we have to extract
#	# everything and work that way
#	file(MAKE_DIRECTORY "${workPath}")
#	add_custom_target(standaloneJar
#		COMMAND "${CMAKE_COMMAND}" "-E"
#			"make_directory" "${workPath}"
#		DEPENDS standaloneJarBase
#			"${SQUIRRELJME_JAR_NATIVE_RULES}")
#	add_custom_command(TARGET standaloneJar
#		POST_BUILD
#		COMMAND "${CMAKE_COMMAND}" "-E"
#			"tar" "x" "${inputJar}" "--format=zip"
#		WORKING_DIRECTORY "${workPath}")
#
#	# Set properties to be later used by CI/CD, also set cleanup to be the
#	# working directory and the Jar
#	set_target_properties(standaloneJar PROPERTIES
#		ADDITIONAL_CLEAN_FILES "${workPath};${outputJar}"
#		SQUIRRELJME_OUTPUT_PATH "${outputJar}"
#		SQUIRRELJME_OUTPUT_TYPE "standalone")
#
#	# Go through all all natives and package them inside the Standalone Jar
#	foreach(rule IN LISTS SQUIRRELJME_JAR_NATIVE_RULES)
#		# Where were the binaries and list files placed?
#		get_target_property(coreNativePath ${rule}
#			SQUIRRELJME_CORE_NATIVE_PATH)
#		get_target_property(emulatorNativePath ${rule}
#			SQUIRRELJME_EMULATOR_NATIVE_PATH)
#
#		# Which system/arch does this target?
#		get_target_property(systemNormal ${rule}
#			SQUIRRELJME_SYSTEM)
#		get_target_property(archNormal ${rule}
#			SQUIRRELJME_ARCH)
#
##		# Remember which actual natives were put in
##		list(APPEND SQUIRRELJME_JAR_NATIVES_AVAILABLE
##			"${systemNormal}!${archNormal}")
#
#		# Add the natives to the Standalone Jar
#		add_custom_command(TARGET standaloneJar
#			POST_BUILD
#			COMMAND "${CMAKE_COMMAND}" "-E"
#				"make_directory"
#				"${workPath}/natives/${systemNormal}/${archNormal}"
#			COMMAND "${CMAKE_COMMAND}" "-E"
#				"copy_directory"
#				"${coreNativePath}/" "${emulatorNativePath}/"
#				"${workPath}/natives/${systemNormal}/${archNormal}"
#			COMMENT "Embedding ${systemNormal}/${archNormal}...")
#	endforeach()
#
#	# Finalize packaging of the Jar
#	add_custom_command(TARGET standaloneJar
#		POST_BUILD
#		COMMAND "${CMAKE_COMMAND}" "-E"
#			"make_directory" "${outputDir}"
#		COMMAND "${CMAKE_COMMAND}" "-E"
#			"tar" "c" "${outputJar}" "--format=zip" "--" "."
#		BYPRODUCTS "${outputJar}"
#		WORKING_DIRECTORY "${workPath}"
#		COMMENT "Finalizing ${outputJar}...")
#
#	# These get uploaded into Fossil
#	squirreljme_fossil_upload_register(standaloneJar)
#endif()
