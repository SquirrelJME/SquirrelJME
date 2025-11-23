# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchUIWin32 SHARED
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUICore_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUIFB_FPIC}>
	win32Component.c
	win32Container.c
	win32Core.c
	win32Intern.c
	win32Label.c
	win32Laf.c
	win32Loop.c
	win32Menu.c
	win32Panel.c
	win32Pencil.c
	win32Screen.c
	win32ScrollPanel.c
	win32View.c
	win32Window.c)

# Always make it PIC
squirreljme_always_fpic(ScritchUIWin32)

# Include main headers
target_include_directories(ScritchUIWin32 PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIWin32
	"squirreljme-scritchui-win32")
squirreljme_target_binary_output(ScritchUIWin32
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Generate exports
squirreljme_target_shared_library_exports(ScritchUIWin32)
