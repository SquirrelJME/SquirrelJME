# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Format support

# Define the library
add_library(SquirrelJMEFormat STATIC
	detect.c
	libraries.c
	library.c
	sqc.c
	zip.c)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMEFormat PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMEFormat PROPERTY POSITION_INDEPENDENT_CODE ON)
