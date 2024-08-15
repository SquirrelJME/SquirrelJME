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
	set(libRetroBaseObjects $<TARGET_OBJECTS:Base>)
	set(libRetroCoreObjects $<TARGET_OBJECTS:Core>)
else()
	set(libRetroIsStatic OFF)
	set(libRetroBuildType SHARED)
	set(libRetroCore CorePIC)
	set(libRetroBaseObjects $<TARGET_OBJECTS:BasePIC>)
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
	${libRetroBaseObjects}
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
if(ANDROID OR (DEFINED ANDROID_PLATFORM AND ANDROID_PLATFORM))
	squirreljme_target_binary_name(squirreljme_libretro
		"squirreljme_libretro_android")
else()
	squirreljme_target_binary_name(squirreljme_libretro
		"squirreljme_libretro${LIBRETRO_SUFFIX}")
endif()

# Library location must be placed in the build output for the RetroArch
# build system to pick up the library
# Note that RUNTIME_OUTPUT_DIRECTORY is needed for the Windows build to output
# directories since .DLL files are output there and not where shared libraries
# go??? No idea really.
squirreljme_target_binary_output(squirreljme_libretro
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# RetroArch does not use any prefixes like "lib" for the cores
set_target_properties(squirreljme_libretro PROPERTIES
	PREFIX "")

# Include main headers
target_include_directories(squirreljme_libretro PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Exports for Windows, etc.
if(NOT libRetroIsStatic)
	squirreljme_target_shared_library_exports(npapi)
endif()

# Allow running the binary, as long as it is not static and we are on the
# same system...
find_program(retroArchExe "retroarch")
if(retroArchExe AND NOT libRetroIsStatic AND
	NOT EMSCRIPTEN AND
	NOT SQUIRRELJME_IS_CROSS_COMPILE)
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
