# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SpringCoat Engine

# Declare base library
add_library(SquirrelJMEEngineSpringCoat STATIC
	springcoat.c)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMEEngineSpringCoat PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMEEngineSpringCoat
	PROPERTY POSITION_INDEPENDENT_CODE ON)
