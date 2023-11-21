# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare library
add_library(LibRetro
	$<TARGET_OBJECTS:Core>
	loop.c)

# Include main headers
target_include_directories(LibRetro PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

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
