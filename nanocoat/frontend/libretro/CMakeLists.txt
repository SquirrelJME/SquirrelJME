# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Which type of build is being used?
if(LIBRETRO_REALLY_STATIC)
	set(libRetroIsStatic ON)
	set(libRetroBuildType STATIC)
	set(libRetroCore Core)
	set(libRetroCoreObjects $<TARGET_OBJECTS:Core>)
else()
	set(libRetroIsStatic OFF)
	set(libRetroBuildType SHARED)
	set(libRetroCore CorePIC)
	set(libRetroCoreObjects $<TARGET_OBJECTS:CorePIC>)
endif()

# Include tests with the build as well?
if(SQUIRRELJME_IS_DEBUG AND SQUIRRELJME_ENABLE_TESTING)
	if(LIBRETRO_REALLY_STATIC)
		set(libRetroTestObjects $<TARGET_OBJECTS:BasicTestLib>)
	else()
		set(libRetroTestObjects $<TARGET_OBJECTS:BasicTestLibPIC>)
	endif()
else()
	set(libRetroTestObjects)
endif()

# Declare library
add_library(squirreljme_libretro ${libRetroBuildType}
	${libRetroCoreObjects}
	${libRetroTestObjects}
	audio.c
	boot.c
	env.c
	ext.c
	hooks.c
	input.c
	loop.c
	save.c
	shared.c
	video.c)

# Make this PIC if not static
if(NOT libRetroIsStatic)
	set_property(TARGET squirreljme_libretro
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# The base SquirrelJME core needs to be built
add_dependencies(squirreljme_libretro
	${libRetroCore})

# Name must be specifically set for RetroArch for it to work properly
if(ANDROID OR
	(DEFINED ANDROID_PLATFORM AND ANDROID_PLATFORM))
	set_target_properties(squirreljme_libretro PROPERTIES
		LIBRARY_OUTPUT_NAME
			"squirreljme_libretro_android"
		ARCHIVE_OUTPUT_NAME
			"squirreljme_libretro_android")
else()
	set_target_properties(squirreljme_libretro PROPERTIES
		LIBRARY_OUTPUT_NAME
			"squirreljme_libretro${LIBRETRO_SUFFIX}"
		ARCHIVE_OUTPUT_NAME
			"squirreljme_libretro${LIBRETRO_SUFFIX}")
endif()

# Library location must be placed in the build output for the RetroArch
# build system to pick up the library
# Note that RUNTIME_OUTPUT_DIRECTORY is needed for the Windows build to output
# directories since .DLL files are output there and not where shared libraries
# go??? No idea really.
set_target_properties(squirreljme_libretro PROPERTIES
	RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	ARCHIVE_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}")

# RetroArch does not use any prefixes like "lib" for the cores
set_target_properties(squirreljme_libretro PROPERTIES
	PREFIX "")

# Include main headers
target_include_directories(squirreljme_libretro PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Allow running the binary, as long as it is not static and we are on the
# same system...
find_program(retroArchExe "retroarch")
if(retroArchExe AND NOT libRetroIsStatic AND
	NOT EMSCRIPTEN AND
	NOT SQUIRRELJME_CROSS_BUILD)
	# Add target to run accordingly
	add_custom_target(LibRetroRun
		COMMAND "${retroArchExe}"
			"-L" "$<TARGET_FILE:squirreljme_libretro>"
			"--no-patch"
			"--appendconfig=${CMAKE_CURRENT_SOURCE_DIR}/test.config"
			"--log-file=${CMAKE_BINARY_DIR}/libretro.log"
		DEPENDS squirreljme_libretro
		USES_TERMINAL)

	# Do not try running this
	set_target_properties(LibRetroRun PROPERTIES
		EXCLUDE_FROM_ALL TRUE
		EXCLUDE_FROM_DEFAULT_BUILD TRUE)
endif()

# CPack
if(SQUIRRELJME_ENABLE_PACKING)
	cpack_add_component(LIBRETRO
		DISPLAY_NAME "SquirrelJME LibRetro"
		DESCRIPTION "SquirrelJME running on LibRetro.")
	install(TARGETS squirreljme_libretro
		LIBRARY)
	set(CPACK_DEBIAN_LIBRETRO_PACKAGE_DEPENDS
		"retroarch | libretro-frontend")
	set(CPACK_DEBIAN_PACKAGE_SUGGESTS
		"retroarch")
endif()
