# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# We need to know where X11 is
squirreljme_include("find-x11.cmake")

# Declare base library
add_library(ScritchUIX11 SHARED
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUICore_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUIFB_FPIC}>
	x11Component.c
	x11Container.c
	x11Core.c
	x11Intern.c
	x11Label.c
	x11Laf.c
	x11Loop.c
	x11Menu.c
	x11Panel.c
	x11Pencil.c
	x11Screen.c
	x11ScrollPanel.c
	x11View.c
	x11Window.c)

# Always make it PIC
squirreljme_always_fpic(ScritchUIX11)

# Include main headers
target_include_directories(ScritchUIX11 PUBLIC
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(ScritchUIX11 SYSTEM PRIVATE
	${X11_INCLUDE_DIR}
	${X11_INCLUDE_DIRS})

# Link in X11
target_link_directories(ScritchUIX11 PUBLIC
	${X11_LIBRARY_DIRS})
target_link_libraries(ScritchUIX11
	${X11_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIX11
	"squirreljme-scritchui-x11")
squirreljme_target_binary_output(ScritchUIX11
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Generate exports
squirreljme_target_shared_library_exports(ScritchUIX11)

