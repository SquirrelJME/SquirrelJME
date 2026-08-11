# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Pre-fixes before project()

# Debugging?
if(CMAKE_BUILD_TYPE STREQUAL "Debug" OR
	CMAKE_BUILD_TYPE STREQUAL "RelWithDebInfo")
	set(SQUIRRELJME_IS_DEBUG TRUE)
	set(SQUIRRELJME_IS_RELEASE FALSE)
else()
	set(SQUIRRELJME_IS_DEBUG FALSE)
	set(SQUIRRELJME_IS_RELEASE TRUE)
endif()

# For MSVC statically link against the runtime
if(MSVC)
	if(SQUIRRELJME_IS_DEBUG)
		set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreadedDebug")
	else()
		set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded")
	endif()
endif()
