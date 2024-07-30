# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# We need to know where Motif is
pkg_check_modules(X11 x11)

# Declare base library
add_library(ScritchUIX11 SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
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
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchUIX11
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# Include main headers
target_include_directories(ScritchUIX11 PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${X11_INCLUDE_DIR})

# Link in Motif
target_link_libraries(ScritchUIX11
	${X11_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIX11
	"squirreljme-scritchui-x11")
