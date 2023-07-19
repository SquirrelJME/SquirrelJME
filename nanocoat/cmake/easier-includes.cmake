# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Easier include directives

# Non-optional include
macro(squirreljme_include what)
	include("${CMAKE_SOURCE_DIR}/cmake/${what}"
		NO_POLICY_SCOPE)
endmacro()

# Optional include
macro(squirreljme_include_optional what)
	include("${CMAKE_SOURCE_DIR}/cmake/${what}"
		OPTIONAL NO_POLICY_SCOPE)
endmacro()
