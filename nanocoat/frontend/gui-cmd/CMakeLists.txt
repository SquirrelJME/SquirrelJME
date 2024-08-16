# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SquirrelJME emulator interface (Java SE)

# Setup executable
add_executable(GuiCmd
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:CorePIC>
	guiCmdMain.c)

# Always make this position independent
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET GuiCmd
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# The base SquirrelJME core needs to be built
add_dependencies(GuiCmd
	BasePIC CorePIC)

# We need to find all of the includes and such
target_include_directories(GuiCmd PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Libraries needed to be linked with for the DyLib to function
target_link_libraries(GuiCmd PUBLIC
	${SQUIRRELJME_LINK_LIBRARIES_Base}
	${SQUIRRELJME_LINK_LIBRARIES_Core})

# It is easier to find this when it is in the build root
squirreljme_target_binary_output(GuiCmd
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Standard Executable name
squirreljme_target_binary_name(GuiCmd
	"squirreljme-gui-cmd")
