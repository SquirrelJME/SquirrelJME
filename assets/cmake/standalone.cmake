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
	set(SQUIRRELJME_STANDALONE_JAR_BASE_PATH "${outputPath}")

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

	# Where is the actual standalone Jar placed?
	set(inputJar "${SQUIRRELJME_STANDALONE_JAR_BASE_PATH}")
	set(outputJar "${CMAKE_BINARY_DIR}/standalone/standalone.jar")
	set(workPath "${CMAKE_BINARY_DIR}/standaloneWork")

	# Setup target to combine everything into a single Jar, older CMake does
	# not support append to archives so unfortunately we have to extract
	# everything and work that way
	file(MAKE_DIRECTORY "${workPath}")
	add_custom_target(standaloneJar
		COMMAND "${CMAKE_COMMAND}" "-E"
			"make_directory" "${workPath}"
		DEPENDS standaloneJarBase
			"${SQUIRRELJME_STANDALONE_NATIVE_RULES}")
	add_custom_command(TARGET standaloneJar
		COMMAND "${CMAKE_COMMAND}" "-E"
			"tar" "x" "${inputJar}" "--format=zip"
		WORKING_DIRECTORY "${workPath}")

	# Set properties to be later used by CI/CD, also set cleanup to be the
	# working directory and the Jar
	set_target_properties(standaloneJar PROPERTIES
		ADDITIONAL_CLEAN_FILES "${outputJar}"
		SQUIRRELJME_STANDALONE_JAR_PATH "${workPath};${outputJar}")

	# Go through all all natives and package them inside the Standalone Jar
	foreach(rule IN LISTS SQUIRRELJME_STANDALONE_NATIVE_RULES)
		# Where were the binaries and list files placed?
		get_target_property(coreNativePath ${rule}
			SQUIRRELJME_CORE_NATIVE_PATH)
		get_target_property(emulatorNativePath ${rule}
			SQUIRRELJME_EMULATOR_NATIVE_PATH)

		# Which system/arch does this target?
		get_target_property(systemNormal ${rule}
			SQUIRRELJME_SYSTEM)
		get_target_property(archNormal ${rule}
			SQUIRRELJME_ARCH)

		# Add the natives to the Standalone Jar
		add_custom_command(TARGET standaloneJar
			POST_BUILD
			COMMAND "${CMAKE_COMMAND}" "-E"
				"make_directory" "${workPath}/${systemNormal}/${archNormal}"
			COMMAND "${CMAKE_COMMAND}" "-E"
				"copy_directory"
				"${coreNativePath}/" "${emulatorNativePath}/"
				"${workPath}/${systemNormal}/${archNormal}"
			COMMENT "Embedding ${systemNormal}/${archNormal}...")
	endforeach()

	# Finalize packaging of the Jar
	add_custom_command(TARGET standaloneJar
		POST_BUILD
		COMMAND "${CMAKE_COMMAND}" "-E"
			"tar" "c" "${outputPath}" "--format=zip" "--" "."
		BYPRODUCTS "${outputPath}"
		WORKING_DIRECTORY "${workPath}"
		COMMENT "Finalizing ${outputJar}...")
endif()
