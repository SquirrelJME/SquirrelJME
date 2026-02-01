# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Bring in GTK3
squirreljme_include("find-gtk3.cmake")

# Declare base library
add_library(ScritchUiGTK3 SHARED
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUiCore_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUiFB_FPIC}>
	gtk3Component.c
	gtk3Container.c
	gtk3Core.c
	gtk3Intern.c
	gtk3Label.c
	gtk3Laf.c
	gtk3Loop.c
	gtk3Menu.c
	gtk3Panel.c
	gtk3Pencil.c
	gtk3Screen.c
	gtk3ScrollPanel.c
	gtk3View.c
	gtk3Window.c)

# Always make it PIC
squirreljme_always_fpic(ScritchUiGTK3)

# Include main headers
target_include_directories(ScritchUiGTK3 PUBLIC
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(ScritchUiGTK3 SYSTEM PRIVATE
	${GTK3_INCLUDE_DIR} ${GTK3_INCLUDE_DIRS})

# Link in GTK
squirreljme_link_libraries(ScritchUiGTK3 PUBLIC
	${GTK3_LIBRARY_DIRS})
squirreljme_link_libraries(ScritchUiGTK3 PUBLIC
	${GTK3_LIBRARIES})

# ScritchUi requires a normalized library name
squirreljme_target_binary_name(ScritchUiGTK3
	"squirreljme-scritchui-gtk3")
squirreljme_target_binary_output(ScritchUiGTK3
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Generate exports
squirreljme_target_shared_library_exports(ScritchUiGTK3)
