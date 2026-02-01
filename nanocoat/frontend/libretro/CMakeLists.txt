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
else()
	set(libRetroIsStatic OFF)
	set(libRetroBuildType SHARED)
endif()

# Declare library
add_library(squirreljme_libretro ${libRetroBuildType}
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
	squirreljme_always_fpic(squirreljme_libretro)
endif()

# Link against base libraries, statically!!
squirreljme_link_libraries_required(squirreljme_libretro PRIVATE
	CoreStatic
	ScritchAudioCoreStatic
	ScritchUiCoreStatic
	ScritchUiPureStatic)

# Name must be specifically set for RetroArch for it to work properly
if(SQUIRRELJME_IS_ANDROID)
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
target_include_directories(squirreljme_libretro SYSTEM PRIVATE
	"${CMAKE_SOURCE_DIR}/include/3rdparty/libretro")

# Exports for Windows, etc.
if(NOT libRetroIsStatic)
	squirreljme_target_shared_library_exports(squirreljme_libretro)
endif()

# Duplicate binary
if(EMSCRIPTEN)
	add_custom_command(TARGET squirreljme_libretro POST_BUILD
		BYPRODUCTS
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}/squirreljme_libretro_emscripten.bc"
		COMMAND "${CMAKE_COMMAND}" -E "copy"
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}/squirreljme_libretro_emscripten.a"
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}/squirreljme_libretro_emscripten.bc")
endif()

# Locate the RetroArch binary
find_program(retroArchExe "retroarch" NO_CACHE
	PATHS "C:/RetroArch-Win64"
		"C:/RetroArch"
		"$ENV{APPDATA}/RetroArch")

# Set system directoy for RetroArch
file(REMOVE "${CMAKE_CURRENT_BINARY_DIR}/run.config")
file(COPY "${CMAKE_CURRENT_SOURCE_DIR}/test.config"
	DESTINATION "${CMAKE_CURRENT_BINARY_DIR}/")
file(APPEND "${CMAKE_CURRENT_BINARY_DIR}/test.config"
	"\n")
file(APPEND "${CMAKE_CURRENT_BINARY_DIR}/test.config"
	"system_directory = \"${CMAKE_SOURCE_DIR}/rom\"\n")

# Allow running the binary, as long as it is not static and we are on the
# same system...
find_program(gdbExe "gdbserver")
if(retroArchExe AND NOT libRetroIsStatic AND
	NOT EMSCRIPTEN AND
	NOT SQUIRRELJME_IS_CROSS_COMPILE)
	# Add target to run accordingly
	add_custom_target(RunSquirreljme_libretro
		COMMAND "${retroArchExe}"
			"-v"
			"-L" "$<GENEX_EVAL:$<TARGET_FILE:squirreljme_libretro>>"
			"--no-patch"
			"--appendconfig=${CMAKE_CURRENT_BINARY_DIR}/test.config"
			"--log-file=${CMAKE_BINARY_DIR}/libretro.log"
		DEPENDS squirreljme_libretro
		USES_TERMINAL)

	# Do not try running this
	set_target_properties(RunSquirreljme_libretro PROPERTIES
		EXCLUDE_FROM_ALL TRUE
		EXCLUDE_FROM_DEFAULT_BUILD TRUE)

	# Extra target for debugging?
	if(gdbExe)
		# Add target to run accordingly
		add_custom_target(GdbSquirreljme_libretro
			COMMAND "${gdbExe}" "localhost:2345" "${retroArchExe}"
			"-L" "$<GENEX_EVAL:$<TARGET_FILE:squirreljme_libretro>>"
			"--no-patch"
			"--appendconfig=${CMAKE_CURRENT_BINARY_DIR}/test.config"
			"--log-file=${CMAKE_BINARY_DIR}/libretro.log"
			DEPENDS squirreljme_libretro
			USES_TERMINAL)

		# Do not try running this
		set_target_properties(GdbSquirreljme_libretro PROPERTIES
			EXCLUDE_FROM_ALL TRUE
			EXCLUDE_FROM_DEFAULT_BUILD TRUE)
	endif()
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
