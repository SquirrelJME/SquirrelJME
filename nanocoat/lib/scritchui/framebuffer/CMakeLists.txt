# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

set(SJME_SCRITCHUIFB_SOURCES
	"fbCore.c")

# Declare library
if(SQUIRRELJME_ENABLE_FPIC)
	squirreljme_multilib_add_static_library(ScritchUIFB
		$<TARGET_OBJECTS:BasePIC>
		$<TARGET_OBJECTS:ScritchUICorePIC>
		${SJME_SCRITCHUIFB_SOURCES})
else()
	squirreljme_multilib_add_static_library(ScritchUIFB
		$<TARGET_OBJECTS:Base>
		$<TARGET_OBJECTS:ScritchUICore>
		${SJME_SCRITCHUIFB_SOURCES})
endif()

# Include main headers
target_include_directories(ScritchUIFB PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIFB
	"squirreljme-scritchui-fb")
