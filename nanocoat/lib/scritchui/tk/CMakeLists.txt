# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchUITk SHARED
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUICore_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUIFB_FPIC}>
#	tkComponent.c
#	tkContainer.c
#	tkCore.c
#	tkIntern.c
#	tkLabel.c
#	tkLaf.c
#	tkLoop.c
#	tkMenu.c
#	tkPanel.c
#	tkPencil.c
#	tkScreen.c
#	tkScrollPanel.c
#	tkView.c
#	tkWindow.c
)

# Always make it PIC
squirreljme_always_fpic(ScritchUITk)

# Include main headers
target_include_directories(ScritchUITk PUBLIC
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(ScritchUITk SYSTEM PRIVATE
	${TK_INCLUDE_DIRS})

# Link in Tk
target_link_directories(ScritchUITk PUBLIC
	${TK_LIBRARY_DIRS})
target_link_libraries(ScritchUITk
	${TK_LIBRARIES})

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUITk
	"squirreljme-scritchui-tk")
squirreljme_target_binary_output(ScritchUITk
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Generate exports
squirreljme_target_shared_library_exports(ScritchUITk)

