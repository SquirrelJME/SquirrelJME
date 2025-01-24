# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

add_library(npapi SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:CorePIC>
	plugin.c)

# The base SquirrelJME core needs to be built first
add_dependencies(npapi
	CorePIC)

# Always make this position independent
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET npapi
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# We need to find all of the includes and such
target_include_directories(npapi PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/npapi")

# Libraries needed to be linked with for the DyLib to function
target_link_libraries(npapi PUBLIC
	${SQUIRRELJME_LINK_LIBRARIES_Base}
	${SQUIRRELJME_LINK_LIBRARIES_Core})

# Exports for Windows, etc.
squirreljme_target_shared_library_exports(npapi)
