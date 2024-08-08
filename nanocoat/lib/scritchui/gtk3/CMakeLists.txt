# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Bring in GTK3
find_package(PkgConfig)
pkg_check_modules(GTK3 REQUIRED gtk+-3.0)

# Declare base library
add_library(ScritchUIGTK3 SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	$<TARGET_OBJECTS:ScritchUIFBPIC>
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
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchUIGTK3
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# Include main headers
target_include_directories(ScritchUIGTK3 PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${GTK2_INCLUDE_DIRS})

# Link in GTK
target_link_directories(ScritchUIGTK3 PUBLIC
	${GTK3_LIBRARY_DIRS})
target_link_libraries(ScritchUIGTK3 PUBLIC
	${GTK3_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIGTK3
	"squirreljme-scritchui-gtk3")
