# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Used for testing compilers and generators more quickly...

# Use the best version possible
# Otherwise, fallback to the lowest viable version
if("${CMAKE_VERSION}" VERSION_GREATER "3.12")
	cmake_minimum_required(VERSION 3.13..3.31)
else()
	cmake_minimum_required(VERSION 3.1)
endif()

# Easier include
set(SQUIRRELJME_NANOCOAT_SOURCE_DIR
	"${CMAKE_SOURCE_DIR}/../../../nanocoat")
include("../easier-includes.cmake")

# Declare project
project(Simple)

# Use this form as it is backwards compatible
enable_language(C)

# System mapping information and identification
squirreljme_include_nanocoat("system-map.cmake")

# Add a shared library target
add_library(Simple SHARED
	blank.c)
