# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Setup base executable
add_executable(StdC
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:CorePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	$<TARGET_OBJECTS:ScritchUIFBPIC>
	stdcMain.c)

# These must be built first
add_dependencies(StdC
	BasePIC CorePIC
	ScritchUIFB ScritchUICorePIC)

# This is always "squirreljme"
squirreljme_target_binary_name(StdC
	"squirreljme")

# Include main headers
target_include_directories(StdC PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Strip on release
squirreljme_executable_strip(StdC)
