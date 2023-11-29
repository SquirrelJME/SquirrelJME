# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Valgrind Support on specific platforms

# Include pkg-config finder
find_package(PkgConfig)

# Was pkg-config found?
if(PKG_CONFIG_FOUND)
	# Look for Valgrind
	pkg_check_modules(VALGRIND valgrind)

	# Was Valgrind found?
	if(VALGRIND_FOUND)
		# Notice
		message(STATUS "Found Valgrind, including it!")

		# Valgrind is available
		add_compile_definitions(SJME_CONFIG_HAS_VALGRIND=1)

		# Include the valgrind includes
		include_directories(${VALGRIND_INCLUDE_DIRS})

		# Link against the Valgrind libraries
		list(APPEND SQUIRRELJME_EXEC_LINK_LIBRARIES
			${VALGRIND_LINK_LIBRARIES})
	endif()
endif()
