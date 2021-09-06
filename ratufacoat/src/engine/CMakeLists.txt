# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Engine Base

# Define the library
add_library(SquirrelJMEEngineCore STATIC
		scaffold.c)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMEEngineCore PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Include all of our engines
target_link_libraries(SquirrelJMEEngineCore
	SquirrelJMEEngineSpringCoat)

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMECore PROPERTY POSITION_INDEPENDENT_CODE ON)

# Engine implementations
add_subdirectory(springcoat)