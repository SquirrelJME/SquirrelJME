# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Force do not use on certain systems
if(NOT SQUIRRELJME_SYSTEM STREQUAL "wine")
	# Check pkg-config first
	if(PKG_CONFIG_FOUND)
		pkg_check_modules(GTK2 gtk+-2.0)
	endif()

	# Otherwise, fallback to CMake detection
	if(NOT GTK2_FOUND)
		# Sets GTK2_INCLUDE_DIRS and GTK2_LIBRARIES
		find_package(GTK2 2.0 COMPONENTS gtk)
	endif()
endif()

# Additional checks for GTK2
if(GTK2_FOUND)
	# Can we actually compile with GTK2?
	try_compile(SQUIRRELJME_GTK2_VALID
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryGtk2.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
			"-DLINK_DIRECTORIES=${GTK2_LIBRARY_DIRS}"
			"-DINCLUDE_DIRECTORIES=${GTK2_INCLUDE_DIRS}"
		LINK_LIBRARIES "${GTK2_LIBRARIES}"
		OUTPUT_VARIABLE SQUIRRELJME_GTK2_VALID_DEBUG)

	# Does this work?
	if(SQUIRRELJME_GTK2_VALID)
		# Note it
		message(STATUS "GTK2: Detected and linkable")

		# Enable it by default
		set(SQUIRRELJME_ENABLE_GUI_GTK2_DEFAULT TRUE)
	else()
		# Note it
		message(STATUS "GTK2 Detected, does not link!")

		# Disable it by default, since it is broken
		set(SQUIRRELJME_ENABLE_GUI_GTK2_DEFAULT FALSE)

		# Make it so GTK2 was not found
		unset(GTK2_FOUND CACHE)
	endif()
endif()

# Enable GTK2 ScritchUi?
squirreljme_define_option(SQUIRRELJME_ENABLE_GUI_GTK2
	"Enable ScritchUI GTK2")
