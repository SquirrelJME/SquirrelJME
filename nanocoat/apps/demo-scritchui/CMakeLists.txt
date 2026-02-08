# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Setup base executable
add_executable(AppDemoScritchUi
	main.c)

# Include main headers
target_include_directories(AppDemoScritchUi PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Link against the base libraries
squirreljme_link_libraries_required(AppDemoScritchUi PUBLIC
	libJvmStatic)

# Output to the base directory
squirreljme_target_binary_output(AppDemoScritchUi
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")
