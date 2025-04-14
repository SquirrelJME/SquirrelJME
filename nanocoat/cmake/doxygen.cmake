# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Support for Doxygen document generation

# Need to find doxygen first
find_package(Doxygen)

# Did we find it?
if(DOXYGEN_FOUND)
	function(squirreljme_doxygen target)
		# Configure Doxygen inputs and outputs
		configure_file("${CMAKE_SOURCE_DIR}/cmake/Doxyfile.in"
			"${CMAKE_CURRENT_BINARY_DIR}/Doxyfile")

		# Add target to build all the documentation
		add_custom_target(${target}Doxygen
			COMMAND "${DOXYGEN_EXECUTABLE}"
				"${CMAKE_CURRENT_BINARY_DIR}/Doxyfile"
			DEPENDS ${target}
			SOURCES "${CMAKE_SOURCE_DIR}/cmake/Doxyfile.in"
				"${CMAKE_CURRENT_BINARY_DIR}/Doxyfile"
			BYPRODUCTS "@CMAKE_CURRENT_BINARY_DIR@/docs/")

		# Do not run this by default
		set_target_properties(${target}Doxygen PROPERTIES
			EXCLUDE_FROM_ALL TRUE
			EXCLUDE_FROM_DEFAULT_BUILD TRUE)
	endfunction()
else()
	function(squirreljme_doxygen target)
	endfunction()
endif()
