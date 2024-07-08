# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Soft patching for CMake.

# String joining
if(${CMAKE_VERSION} VERSION_LESS_EQUAL "3.11")
	macro(squirreljme_string_join sjGlue sjOut
		sjList)
		# Setup initial blank output
		set(sjResult "")

		# Go through list
		list(LENGTH "${sjList}" sjListLen)
		set(sjAt "0")
		while("${sjAt}" LESS "${sjListLen}")
			# Get list item
			set(sjTemp "")
			list(GET "${sjList}" "${sjAt}" sjTemp)

			# Append joiner
			string(APPEND sjResult "${sjGlue}")

			# Append string
			string(APPEND sjResult "${sjTemp}")

			# Move up
			math(EXPR sjAt "${sjAt} + 1")
		endwhile()

		# Set output
		set(${sjOut} "${sjResult}")
	endmacro()
else()
	macro(squirreljme_string_join sjGlue sjOut
		sjList)
		string(JOIN "${sjGlue}" ${sjOut}
			"${sjList}")
	endmacro()
endif()

# CMake 3.13 added many things!
if(${CMAKE_VERSION} VERSION_LESS_EQUAL "3.12")
	# Sort list of files
	macro(squirreljme_list_file_sort lfsList)
		list(SORT ${lfsList})
	endmacro()

	# Additional compiler settings
	macro(add_compile_definitions varVal)
		add_definitions("-D${varVal}")
	endmacro()

	# Additional linker options
	macro(target_link_options)
		# The target we are interested in...
		list(GET "${ARGV}" 0 tloTarget)

		# Is there a before?
		set(GET "${ARGV}" 1 tloMaybeBefore)
		if(tloMaybeBefore STREQUAL "BEFORE")
			# Mark as before
			set(tloBefore YES)

			# Start pivot point
			set(tloPivot 2)
		else()
			# Mark as not before
			set(tloBefore No)

			# Start pivot point
			set(tloPivot 1)
		endif()

		# Handle the remaining number of items
		set(tloAt "${tloPivot}")
		set(tloFlags)
		while(tloAt LESS ARGC)
			# Determine indexes
			math(EXPR tloAtI "${tloAt} + 0")
			math(EXPR tloAtL "${tloAt} + 1")

			# Extract sub-parameters
			list(GET "${ARGV}" "${tloAtI}" tloInstance)
			list(GET "${ARGV}" "${tloAtL}" tloFlag)

			# Add library, ignore the instance for it
			list(APPEND tloFlags "${tloFlag}")

			# Move indexes up for the next items
			math(EXPR tloAt "${tloAt} + 2")
		endwhile()

		# Get old link options to add in the list...
		get_target_property(tloOldLinkOpt ${tloTarget}
			LINK_FLAGS)
		squirreljme_string_join(" " tloStrOpt "${tloFlags}")
		if(tloBefore)
			set_target_properties(${tloTarget} PROPERTIES
				LINK_FLAGS "${tloStrOpt} ${tloOldLinkOpt}")
		else()
			set_target_properties(${tloTarget} PROPERTIES
				LINK_FLAGS "${tloOldLinkOpt} ${tloStrOpt}")
		endif()
	endmacro()
else()
	# Sorting file list
	macro(squirreljme_list_file_sort lfsList)
		list(SORT ${lfsList} COMPARE FILE_BASENAME)
	endmacro()
endif()
