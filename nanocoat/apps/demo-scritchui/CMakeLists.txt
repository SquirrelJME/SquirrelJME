# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Setup base executable
add_executable(AppDemoScritchUI
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	main.c)

# Include main headers
target_include_directories(AppDemoScritchUI PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Depend on all ScritchUI targets
add_dependencies(AppDemoScritchUI
	${SQUIRRELJME_LIB_Base_FPIC}
	ScritchUI)

# Output to the base directory
squirreljme_target_binary_output(AppDemoScritchUI
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")
