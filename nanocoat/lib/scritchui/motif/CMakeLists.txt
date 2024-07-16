# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# We need to know where Motif is
find_package(Motif)

# Declare base library
add_library(ScritchUIMotif SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	motifCore.c)

# Always make it PIC
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchUIMotif
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# Include main headers
target_include_directories(ScritchUIMotif PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${MOTIF_INCLUDE_DIR}
	${MOTIF_INCLUDE_DIRS})

# Link in Motif
target_link_libraries(ScritchUIMotif
	${MOTIF_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIMotif
	"squirreljme-scritchui-motif")
