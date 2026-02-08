# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
squirreljme_scritchany_add_library(ui X11
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

squirreljme_scritchany_standard_properties(ui X11)

# Include main headers
squirreljme_scritchany_include_directories(ui X11 SYSTEM PRIVATE
	${X11_INCLUDE_DIR}
	${X11_INCLUDE_DIRS})
