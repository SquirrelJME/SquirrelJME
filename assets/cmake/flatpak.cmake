# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Building of the Flatpak package on Linux, not supported:
# - After the Flatpak is built, it needs to be installed before it is bundled.
# - The runtimes change too often and just disappear, the Eclipse group is
#   seriously keeping Java 8 under LTS and that is 11 years old, you can keep
#   LTS releases of ancient Ubuntu around as developers like myself are who
#   they are for.
# - Annoying deprecation warnings which confuse users.
# - For a proper Flatpak build you cannot have the internet or any network
#   access, thus Gradle is rather impossible without hacking it to be in
#   offline mode with pre-downloaded dependencies and an alternative cache.
# - Since SquirrelJME does not have a Java compiler built-in, yet(!) although
#   I do plan to eventually write one because of insanity, it is not possible
#   to have a completely independent self-contained build.
# - Lack of support for the Fossil DVCS.

# Locate Flatpak
find_program(Flatpak_EXECUTABLE
	NAMES flatpak)
find_program(FlatpakBuilder_EXECUTABLE
	NAMES flatpak-builder)

# Was it found?
message(STATUS "flatpak        : ${Flatpak_EXECUTABLE}")
message(STATUS "flatpak-builder: ${FlatpakBuilder_EXECUTABLE}")

# If it was found, build the single bundled flatpak
if(Flatpak_EXECUTABLE AND FlatpakBuilder_EXECUTABLE)
	# Where is this going?
	set(buildDir "${CMAKE_BINARY_DIR}/flatpak-build-dir")
	set(bundlePath "${CMAKE_BINARY_DIR}/squirreljme.flatpak")

	# Add target to build
	add_custom_target(flatpakBundle
		COMMAND "${FlatpakBuilder_EXECUTABLE}"
			"${buildDir}"
			"${CMAKE_SOURCE_DIR}/cc.squirreljme.SquirrelJME.yml"
		COMMAND "${FlatpakBuilder_EXECUTABLE}"
			"--user"
			"--install"
			"${buildDir}"
			"${CMAKE_SOURCE_DIR}/cc.squirreljme.SquirrelJME.yml"
		COMMAND "${Flatpak_EXECUTABLE}"
			"build-bundle"
			"${bundlePath}"
			"cc.squirreljme.SquirrelJME"
		COMMAND_EXPAND_LISTS
		DEPENDS standaloneJar
		SOURCES "${CMAKE_SOURCE_DIR}/cc.squirreljme.SquirrelJME.yml")

	# Set properties for the target
	set_target_properties(flatpakBundle PROPERTIES
		SQUIRRELJME_OUTPUT_PATH "${bundlePath}"
		SQUIRRELJME_OUTPUT_TYPE "flatpak")

	# This does get uploaded to Fossil
	list(APPEND SQUIRRELJME_UPLOAD_TARGETS
		flatpakBundle)
endif()
