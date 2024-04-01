# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Bring in GTK3
pkg_check_modules(GTK3 gtk+-3.0)

# Declare base library
add_library(ScritchUIGTK3 SHARED
	$<TARGET_OBJECTS:BasePIC>
	gtk3Core.c)

# Always make it PIC
set_property(TARGET ScritchUIGTK3
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Include main headers
target_include_directories(ScritchUIGTK3 PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${GTK2_INCLUDE_DIR})

# Link in GTK
target_link_libraries(ScritchUIGTK3
	${GTK3_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIGTK3
	"squirreljme-scritchui-gtk3")
