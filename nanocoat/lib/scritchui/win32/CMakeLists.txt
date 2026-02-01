# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
squirreljme_scritchany_add_library(ui Win32
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

# Standard properties
squirreljme_scritchany_standard_properties(ui Win32)

# Required libraries
squirreljme_scritchany_link_libraries(ui Win32
	"gdi32")
