# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Bring in GTK2
find_package(PkgConfig)
pkg_check_modules(GTK2 REQUIRED gtk+-2.0)

# Declare base library
add_library(ScritchUIGTK2 SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	gtk2Component.c
	gtk2Container.c
	gtk2Core.c
	gtk2Loop.c
	gtk2Panel.c
	gtk2Pencil.c
	gtk2Screen.c
	gtk2Window.c)

# Always make it PIC
set_property(TARGET ScritchUIGTK2
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Include main headers
target_include_directories(ScritchUIGTK2 PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${GTK2_INCLUDE_DIRS})

# Link in GTK2
target_link_directories(ScritchUIGTK2 PUBLIC
	${GTK2_LIBRARY_DIRS})
target_link_libraries(ScritchUIGTK2 PUBLIC
	${GTK2_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIGTK2
	"squirreljme-scritchui-gtk2")
