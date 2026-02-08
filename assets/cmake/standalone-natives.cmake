# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Handling of building standalone natives for both emulator-base
# and NanoCoat, these are for later repackaging
# There are three methods for natives:
# - compiled: Compiled for the current and other systems that are found, then
#             uploaded to Fossil.
# - download: Download from the Fossil UV space.
# - cached: Exists in the output directory already.

# The download directory
file(TO_CMAKE_PATH "${CMAKE_BINARY_DIR}/download"
	"SQUIRRELJME_DOWNLOADS")

# The native combinations which are known, available in UV space as:
# unstable/0.3.0/natives-${systemNormal}-${archNormal}.zip
# unstable/0.3.0/natives-${systemNormal}-${archNormal}.zip.mkd
set(SQUIRRELJME_KNOWN_NATIVES)
list(APPEND SQUIRRELJME_KNOWN_NATIVES
	"linux!amd64"
	"linux!arm64l"
	"linux!ia32"
	"linux!mips32b"
	"linux!mips32b6"
	"linux!mips32l"
	"linux!mips32l6"
	"linux!mips64b"
	"linux!mips64b6"
	"linux!mips64l"
	"linux!mips64l6"
	"linux!powerpc32b"
	"linux!powerpc64l"
	"linux!riscv64"
	"macosx!arm64l"
	"macosx!amd64l"
	"macosx!ia32"
	"macosx!powerpc32b"
	"windows!amd64"
	"windows!ia32")

# Targets which are part of the standalone merge set
define_property(GLOBAL PROPERTY SQUIRRELJME_STANDALONE_MERGE_SET
	BRIEF_DOCS "Targets which are part of the standalone merge set."
	FULL_DOCS "Targets which are part of the standalone merge set.")
define_property(GLOBAL PROPERTY SQUIRRELJME_STANDALONE_SYSTEM_SET
	BRIEF_DOCS "Systems which are part of the standalone merge set."
	FULL_DOCS "Systems which are part of the standalone merge set.")

# Appends a native rule for a given method with the given system and
# architecture
function(squirreljme_natives_append_rule newRule systemNormal archNormal
	method)
	# Determine the names
	squirreljme_natives_order_name(orderName ${systemNormal} ${archNormal})
	squirreljme_natives_rule_name(ruleName ${systemNormal} ${archNormal})

	# Make sure the target has the system and architecture and set
	set_target_properties(${newRule} PROPERTIES
		SQUIRRELJME_SYSTEM "${systemNormal}"
		SQUIRRELJME_ARCH "${archNormal}"
		SQUIRRELJME_NATIVES_METHOD "${method}")

	# Obtain the order list
	unset(orderList)
	get_property(orderList GLOBAL PROPERTY ${orderName})

	# Is this the first in the order/
	if("${orderList}" STREQUAL "")
		set(firstOrder YES)
	else()
		set(firstOrder NO)
	endif()

	# Append to the order list
	list(APPEND orderList
		"${method}!${newRule}")
	set_property(GLOBAL PROPERTY ${orderName} "${orderList}")

	# If this is the first order, then set it as the default rule
	if(firstOrder)
		# Note it
		message(STATUS "Target ${systemNormal}/${archNormal}: "
			"${newRule} (default)")

		# Add target and its properties
		add_custom_target(${ruleName}
			ALL
			DEPENDS ${newRule})

		# Make sure it really depends on this
		add_dependencies(${ruleName}
			${newRule})

		# Always build defaults
		set_target_properties(${ruleName} PROPERTIES
			EXCLUDE_FROM_ALL NO)

		# And copy all of its properties
		squirreljme_copy_properties(${newRule} ${ruleName}
			SQUIRRELJME_CORE_NATIVE_PATH
			SQUIRRELJME_EMULATOR_NATIVE_PATH
			SQUIRRELJME_SYSTEM
			SQUIRRELJME_ARCH
			SQUIRRELJME_OUTPUT_PATH
			SQUIRRELJME_OUTPUT_TYPE)

		# Add it to the standalone and system merge set
		get_property(mergeSet GLOBAL PROPERTY
			SQUIRRELJME_STANDALONE_MERGE_SET)
		get_property(systemSet GLOBAL PROPERTY
			SQUIRRELJME_STANDALONE_SYSTEM_SET)
		if(NOT "${mergeSet}" STREQUAL "mergeSet-NOTFOUND")
			list(APPEND mergeSet "${newRule}")
			set_property(GLOBAL PROPERTY SQUIRRELJME_STANDALONE_MERGE_SET
				"${mergeSet}")

			list(APPEND systemSet "${systemNormal}!${archNormal}")
			set_property(GLOBAL PROPERTY SQUIRRELJME_STANDALONE_SYSTEM_SET
				"${systemSet}")
		else()
			set_property(GLOBAL PROPERTY SQUIRRELJME_STANDALONE_MERGE_SET
				"${newRule}")
			set_property(GLOBAL PROPERTY SQUIRRELJME_STANDALONE_SYSTEM_SET
				"${systemNormal}!${archNormal}")
		endif()

		# Upload the default to Fossil
		string(FIND "${method}" "generator." hasGenerator)
		if(NOT "${archNormal}" STREQUAL "base")
			if("${method}" STREQUAL "compiler" OR
				"${hasGenerator}" EQUAL "0")
				squirreljme_fossil_upload(${newRule})
			endif()
		endif()
	# Not first order
	else()
		# Note it
		message(STATUS "Target ${systemNormal}/${archNormal}: "
			"${newRule}")
	endif()
endfunction()

# Add rules and detection steps for the three
squirreljme_include("standalone-natives-compiled.cmake")
squirreljme_include("standalone-natives-download.cmake")
squirreljme_include("standalone-natives-cached.cmake")
