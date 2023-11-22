# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare library
add_library(LibRetro SHARED
	$<TARGET_OBJECTS:CorePIC>
	audio.c
	boot.c
	env.c
	input.c
	loop.c
	save.c
	video.c)

# Name must be specifically set for RetroArch for it to work properly
set_target_properties(LibRetro PROPERTIES
	LIBRARY_OUTPUT_NAME "squirreljme_libretro")

# Include main headers
target_include_directories(LibRetro PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

find_program(RETROARCH "retroarch")
if(RETROARCH)
	# Add target to run accordingly
	add_custom_target(LibRetroRun
		COMMAND "${RETROARCH}"
			"-L" "$<TARGET_FILE:LibRetro>"
			"--no-patch"
			"--appendconfig=${CMAKE_CURRENT_SOURCE_DIR}/test.config"
			"--log-file=${CMAKE_CURRENT_BIN}/libretro.log"
		DEPENDS LibRetro
		USES_TERMINAL)

	# Do not try running this
	set_target_properties(LibRetroRun PROPERTIES
		EXCLUDE_FROM_ALL TRUE
		EXCLUDE_FROM_DEFAULT_BUILD TRUE)
endif()

# CPack
cpack_add_component(LIBRETRO
	DISPLAY_NAME "SquirrelJME LibRetro"
	DESCRIPTION "SquirrelJME running on LibRetro.")
install(TARGETS LibRetro
	LIBRARY)
set(CPACK_DEBIAN_LIBRETRO_PACKAGE_DEPENDS
	"retroarch | libretro-frontend")
set(CPACK_DEBIAN_PACKAGE_SUGGESTS
	"retroarch")
