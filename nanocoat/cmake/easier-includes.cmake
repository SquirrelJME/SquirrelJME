# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Easier include directives

# Non-optional include (local)
macro(squirreljme_include what)
	include("${CMAKE_SOURCE_DIR}/cmake/${what}"
		NO_POLICY_SCOPE)
endmacro()

# Optional include (local)
macro(squirreljme_include_optional what)
	include("${CMAKE_SOURCE_DIR}/cmake/${what}"
		OPTIONAL NO_POLICY_SCOPE)
endmacro()

# Non-optional include (NanoCoat)
macro(squirreljme_include_nanocoat what)
	if(DEFINED SQUIRRELJME_NANOCOAT_SOURCE_DIR AND
		NOT "${SQUIRRELJME_NANOCOAT_SOURCE_DIR}" STREQUAL "")
		include("${SQUIRRELJME_NANOCOAT_SOURCE_DIR}/cmake/${what}"
			NO_POLICY_SCOPE)
	elseif(squirreljme_bp_version_3_17 AND
		NOT "${CMAKE_CURRENT_FUNCTION_LIST_DIR}" STREQUAL "")
		include("${CMAKE_CURRENT_FUNCTION_LIST_DIR}/${what}"
			NO_POLICY_SCOPE)
	else()
		include("${CMAKE_SOURCE_DIR}/cmake/${what}"
			NO_POLICY_SCOPE)
	endif()
endmacro()
