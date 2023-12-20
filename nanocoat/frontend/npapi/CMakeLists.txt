# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

add_library(npapi SHARED
	$<TARGET_OBJECTS:CorePIC>
	plugin.c)

# The base SquirrelJME core needs to be built
add_dependencies(libEmulator
	CorePIC)

# Always make this position independent
set_property(TARGET npapi
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# We need to find all of the includes and such
target_include_directories(libEmulator PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/npapi")
