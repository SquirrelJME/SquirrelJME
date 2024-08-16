# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchUIWin32 SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	$<TARGET_OBJECTS:ScritchUIFBPIC>
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
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchUIWin32
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# Include main headers
target_include_directories(ScritchUIWin32 PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# It is easier to find this when it is in the build root
squirreljme_target_binary_output(ScritchUIWin32
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIWin32
	"squirreljme-scritchui-win32")
