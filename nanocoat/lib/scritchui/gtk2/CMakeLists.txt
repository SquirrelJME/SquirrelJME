# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Bring in GTK2
squirreljme_include("find-gtk2.cmake")

# Declare base library
squirreljme_scritchany_add_library(ui GTK2
	gtk2Component.c
	gtk2Container.c
	gtk2Core.c
	gtk2Intern.c
	gtk2Label.c
	gtk2Laf.c
	gtk2Loop.c
	gtk2Menu.c
	gtk2Panel.c
	gtk2Pencil.c
	gtk2Screen.c
	gtk2ScrollPanel.c
	gtk2View.c
	gtk2Window.c)

# Standard properties ScritchAny libraries
squirreljme_scritchany_standard_properties(ui GTK2)

# Include headers
squirreljme_scritchany_include_directories(ui GTK2 PRIVATE
	${GTK2_INCLUDE_DIR} ${GTK2_INCLUDE_DIRS})

# Link in GTK2
if(DEFINED GTK2_LIBRARY_DIRS)
	squirreljme_scritchany_link_directories(ui GTK2 PUBLIC
		${GTK2_LIBRARY_DIRS})
endif()

squirreljme_scritchany_link_libraries(ui GTK2 PUBLIC
	${GTK2_LIBRARIES})
