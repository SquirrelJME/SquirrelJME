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

# Include backports
include("${CMAKE_SOURCE_DIR}/../../../nanocoat/cmake/cmake-bp.cmake"
	NO_POLICY_SCOPE)

# Declare project
project(Simple)

# Use this form as it is backwards compatible
enable_language(C)

# System mapping information and identification
include("${CMAKE_SOURCE_DIR}/../../../nanocoat/cmake/system-map.cmake"
	NO_POLICY_SCOPE)

# Add a shared library target
add_library(Simple SHARED
	blank.c)
