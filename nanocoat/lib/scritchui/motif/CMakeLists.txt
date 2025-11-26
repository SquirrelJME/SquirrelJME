# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# We need to know where Motif is
squirreljme_include("find-motif.cmake")

# Declare base library
add_library(ScritchUIMotif SHARED
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUICore_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUIFB_FPIC}>
	motifComponent.c
	motifContainer.c
	motifCore.c
	motifIntern.c
	motifLabel.c
	motifLaf.c
	motifLoop.c
	motifMenu.c
	motifPanel.c
	motifPencil.c
	motifScreen.c
	motifScrollPanel.c
	motifView.c
	motifWindow.c)

# Always make it PIC
squirreljme_always_fpic(ScritchUIMotif)

# Include main headers
target_include_directories(ScritchUIMotif PUBLIC
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(ScritchUIMotif SYSTEM PRIVATE
	${MOTIF_INCLUDE_DIR}
	${MOTIF_INCLUDE_DIRS})

# Link in Motif
target_link_libraries(ScritchUIMotif
	${MOTIF_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIMotif
	"squirreljme-scritchui-motif")
squirreljme_target_binary_output(ScritchUIMotif
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Generate exports
squirreljme_target_shared_library_exports(ScritchUIMotif)
