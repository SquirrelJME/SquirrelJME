# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchUIJAWT SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	$<TARGET_OBJECTS:ScritchUIFBPIC>
	pureCore.c)

# Always make it PIC
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchUIJAWT
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# Include main headers
target_include_directories(ScritchUIJAWT PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${JAVA_AWT_INCLUDE_PATH}")

target_link_libraries(ScritchUIJAWT PUBLIC
	"${JAVA_AWT_LIBRARY}")

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIJAWT
	"squirreljme-scritchui-jawt")
