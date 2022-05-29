# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Format support

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Define the library
add_library(SquirrelJMEFormat OBJECT
	detect.cxx
	format.cxx
	library.cxx
	memfile.cxx
	pack.cxx
	sqc.cxx
	sqclib.cxx
	sqcpack.cxx
	zip.cxx)

# C++!
set_target_properties(SquirrelJMEFormat PROPERTIES LINKER_LANGUAGE CXX)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMEFormat PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMEFormat
	PROPERTY POSITION_INDEPENDENT_CODE ON)
