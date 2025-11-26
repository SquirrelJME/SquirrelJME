# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Defines the base project and the versioning info

# Allow a dynamic range of CMake versions to allow older systems
if(${CMAKE_VERSION} VERSION_LESS "3.7")
	cmake_minimum_required(VERSION 3.0)
elseif(${CMAKE_VERSION} VERSION_LESS "3.13")
	cmake_minimum_required(VERSION 3.7)
else()
	cmake_minimum_required(VERSION 3.13)
endif()

# This is always needed
project(CheckFindJNI
	LANGUAGES)

# Try the normal package find here
find_package(JNI QUIET)
