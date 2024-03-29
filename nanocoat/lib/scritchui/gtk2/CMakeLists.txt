# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Bring in GTK2
pkg_check_modules(GTK2 gtk+-2.0)

# Declare base library
add_library(ScritchUIGTK2 SHARED
	gtk2Core.c)

# Always make it PIC
set_property(TARGET ScritchUIGTK2
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Include main headers
target_include_directories(ScritchUIGTK2 PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${GTK2_INCLUDE_DIR})

# Link in Motif
target_link_libraries(ScritchUIGTK2
	${GTK2_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIGTK2
	"squirreljme-scritchui-gtk2")
