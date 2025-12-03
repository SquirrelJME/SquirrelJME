# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Should be found via pkg-config
if(PKG_CONFIG_FOUND)
	pkg_check_modules(TK tk)
endif()

# If TK is found, enable it by default!
if(TK_FOUND)
	# Can we actually compile with Tk?
	try_compile(SQUIRRELJME_TK_VALID
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryTk.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
			"-DLINK_DIRECTORIES=${TK_LIBRARY_DIRS}"
			"-DINCLUDE_DIRECTORIES=${TK_INCLUDE_DIRS}"
		LINK_LIBRARIES "${TK_LIBRARIES}"
		OUTPUT_VARIABLE SQUIRRELJME_TK_VALID_DEBUG)

	# Does this work?
	if(SQUIRRELJME_TK_VALID)
		# Note it
		message(STATUS "Tk: Detected and linkable")

		# Enable it by default
		set(SQUIRRELJME_ENABLE_GUI_TK_DEFAULT YES)
	else()
		# Note it
		message(STATUS "Tk Detected, does not link!")
		message(STATUS ${SQUIRRELJME_TK_VALID_DEBUG})

		# Disable it by default, since it is broken
		set(SQUIRRELJME_ENABLE_GUI_TK_DEFAULT NO)

		# Make it so Tk was not found
		squirreljme_notfound_strip(TK_FOUND)
	endif()
else()
	set(SQUIRRELJME_ENABLE_GUI_TK_DEFAULT NO)
endif()

# Is Tk enabled by default?
option(SQUIRRELJME_ENABLE_GUI_TK "Enable Tk GUI"
	"${SQUIRRELJME_ENABLE_GUI_TK_DEFAULT}")
