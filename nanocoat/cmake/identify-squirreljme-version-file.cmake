# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Identify SquirrelJME version

# Which version file to use?
if(EXISTS "${CMAKE_CURRENT_LIST_DIR}/../../squirreljme-version")
	set(SQUIRRELJME_VERSION_FILE
		"${CMAKE_CURRENT_LIST_DIR}/../../squirreljme-version")
elseif(EXISTS "${CMAKE_SOURCE_DIR}/squirreljme-version")
	set(SQUIRRELJME_VERSION_FILE
		"${CMAKE_SOURCE_DIR}/squirreljme-version")
endif()

# Load version number
file(STRINGS "${SQUIRRELJME_VERSION_FILE}"
	SQUIRRELJME_VERSION LIMIT_COUNT 1)
