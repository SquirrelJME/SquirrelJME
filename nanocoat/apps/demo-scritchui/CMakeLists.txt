# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Setup base executable
add_executable(AppDemoScritchUI
	main.c)

# Include main headers
target_include_directories(AppDemoScritchUI PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Depend on all ScritchUI targets
add_dependencies(AppDemoScritchUI
	ScritchUI)

# Required libraries to link in
squirreljme_target_link_libraries_required(AppDemoScritchUI)
