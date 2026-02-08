# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Used for testing compilers and generators more quickly...

# Should hopefully work for everything
cmake_minimum_required(VERSION 3.10)

# Easier include
set(SQUIRRELJME_NANOCOAT_SOURCE_DIR
	"${CMAKE_SOURCE_DIR}/../../../nanocoat")
include("../easier-includes.cmake")

# Only C
project(Simple
	LANGUAGES C)

# System mapping information and identification
squirreljme_include_nanocoat("system-map.cmake")

# Add a shared library target
add_library(Simple SHARED
	blank.c)
