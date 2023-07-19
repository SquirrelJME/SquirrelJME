# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# Define the project and the versioning info

# Easier includes
include("${CMAKE_SOURCE_DIR}/cmake/easier-includes.cmake" NO_POLICY_SCOPE)

# Needed for some ancient RetroArch toolchains
if(${CMAKE_VERSION} VERSION_LESS "3.13")
	message("Ancient CMake has been detected (${CMAKE_VERSION})")
	cmake_minimum_required(VERSION 3.7)

	# Needs to be this or newer
else()
	message("Detected CMake (${CMAKE_VERSION})")
	cmake_minimum_required(VERSION 3.13)
endif()

# Determine version information
squirreljme_include("identify-squirreljme-version.cmake")

# Define project
if(${CMAKE_VERSION} VERSION_LESS "3.12")
	project(SquirrelJME
		VERSION ${SQUIRRELJME_VERSION}
		LANGUAGES C )#CXX)
else()
	project(SquirrelJME
		VERSION ${SQUIRRELJME_VERSION}
		DESCRIPTION "SquirrelJME is a Java ME 8 \
Virtual Machine for embedded and Internet of Things devices."
		HOMEPAGE_URL https://squirreljme.cc/
		LANGUAGES C )#CXX)
endif()

# To Emily and Near...
squirreljme_include("i-miss-you.cmake")
