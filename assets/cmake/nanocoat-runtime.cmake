# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: NanoCoat runtime libraries, only the Java sources

# NanoCoat Runtime needs Java to build
# romSpringCoatDebugNanoCoat
# romSpringCoatReleaseNanoCoat
# romTestSpringCoatDebugNanoCoat
# romTestSpringCoatReleaseNanoCoat
if(SQUIRRELJME_HAS_JAVA)
	# Pseudo target for NanoCoat Runtimes
	add_custom_target(rom)

	# Create rules for each type of ROM
	foreach(testMap IN ITEMS ${SQUIRRELJME_TEST_LEVEL_MAP})
		foreach(clutterMap IN ITEMS ${SQUIRRELJME_CLUTTER_MAP})
			# Extract levels
			squirreljme_unmap(testNoun 0 "${testMap}")
			squirreljme_unmap(testLow 1 "${testMap}")
			squirreljme_unmap(clutterNoun 0 "${clutterMap}")
			squirreljme_unmap(speedLow 2 "${clutterMap}")

			# Determine target name to use
			set(targetName rom${testNoun}${clutterNoun})

			# Where should this ROM be placed, and also be called?
			set(romDir "${CMAKE_BINARY_DIR}")
			if("${testLow}" STREQUAL "")
				file(TO_CMAKE_PATH
				"${romDir}/squirreljme-${SQUIRRELJME_VERSION}-${speedLow}.jar"
					romPath)
			else()
				file(TO_CMAKE_PATH
	"${romDir}/squirreljme-${SQUIRRELJME_VERSION}-${speedLow}-${testLow}.jar"
					romPath)
				endif()
			file(TO_NATIVE_PATH "${romPath}" romPathNative)

			# Add rule to build
			squirreljme_add_gradle_target(${targetName}
				"-Dsquirreljme.rom.path=${romPathNative}"
				"-Psquirreljme.rom.path=${romPathNative}"
				"rom${testNoun}SpringCoat${clutterNoun}NanoCoat")

			# Have the pseudo target pull this in
			add_dependencies(rom ${targetName})

			# Set target information
			set_target_properties(${targetName} PROPERTIES
				SQUIRRELJME_OUTPUT_PATH "${romPath}"
				SQUIRRELJME_OUTPUT_TYPE "rom"
				SQUIRRELJME_TEST_LEVEL "${testNoun}"
				SQUIRRELJME_CLUTTER_LEVEL "${clutterNoun}"
				ADDITIONAL_CLEAN_FILES "${romPath}")

				# These get uploaded into Fossil
				squirreljme_fossil_upload_register(${targetName})
		endforeach()
	endforeach()
endif()
