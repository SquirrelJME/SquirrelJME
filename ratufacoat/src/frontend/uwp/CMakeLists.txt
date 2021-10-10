# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Universal Windows Platform Support

# Force Windows Store Mode
set(CMAKE_SYSTEM_NAME WindowsStore)
set(CMAKE_SYSTEM_VERSION 10.0)

# UWP is entirely in C++ Mode
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED YES)

# Setup platform executable
add_executable(SquirrelJMEUWP WIN32
	main.cpp
	SquirrelJME.appxmanifest)

# This is a C++ Application
set_target_properties(SquirrelJMEUWP PROPERTIES LINKER_LANGUAGE CXX)

# Linking for UWP
target_link_libraries(SquirrelJMEUWP
	SquirrelJMECoreLib
	WindowsApp)
