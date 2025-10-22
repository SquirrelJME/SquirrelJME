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
	# Target to generate all documentation per each target
	add_custom_target(Doxygen)

	# Do not run this all target by default
	set_target_properties(Doxygen PROPERTIES
		EXCLUDE_FROM_ALL TRUE
		EXCLUDE_FROM_DEFAULT_BUILD TRUE)

	# Document for Doxygen
	function(squirreljme_doxygen target)
		# Get list of sources
		get_target_property(targetSourcesList ${target} SOURCES)
		list(JOIN targetSourcesList " " targetSources)

		# Configure Doxygen inputs and outputs
		configure_file("${CMAKE_SOURCE_DIR}/cmake/Doxyfile.in"
			"${CMAKE_CURRENT_BINARY_DIR}/Doxyfile")

		# Add target to build all the documentation
		add_custom_target(Doxygen${target}
			COMMAND "${DOXYGEN_EXECUTABLE}"
				"${CMAKE_CURRENT_BINARY_DIR}/Doxyfile"
			DEPENDS ${target}
			WORKING_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}"
			SOURCES "${CMAKE_SOURCE_DIR}/cmake/Doxyfile.in"
				"${CMAKE_CURRENT_BINARY_DIR}/Doxyfile"
			BYPRODUCTS "${CMAKE_CURRENT_BINARY_DIR}/docs/")

		# Make all documentation depend on this
		add_dependencies(Doxygen Doxygen${target})

		# Do not run this by default
		set_target_properties(Doxygen${target} PROPERTIES
			EXCLUDE_FROM_ALL TRUE
			EXCLUDE_FROM_DEFAULT_BUILD TRUE)
	endfunction()
else()
	function(squirreljme_doxygen target)
	endfunction()
endif()
