# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Identify SquirrelJME version

# Load version number
file(STRINGS "${CMAKE_CURRENT_SOURCE_DIR}/squirreljme-version"
	SQUIRRELJME_VERSION LIMIT_COUNT 1)
message(STATUS
	"Building for SquirrelJME ${SQUIRRELJME_VERSION}!")

# Make Windows compatible version
set(SQUIRRELJME_VERSION_WINDOWS "${SQUIRRELJME_VERSION}.0")
string(REGEX REPLACE "\\." "," SQUIRRELJME_VERSION_WINDOWS_RC
	"${SQUIRRELJME_VERSION_WINDOWS}")

# Put down the configure time
string(TIMESTAMP SQUIRRELJME_VERSION_ID_TIME "%Y-%m-%dT%H:%M:%SZ" UTC)
message(STATUS
	"Configure time is ${SQUIRRELJME_VERSION_ID_TIME}.")

# Load Fossil manifest, if possible
if(EXISTS "${CMAKE_CURRENT_SOURCE_DIR}/../manifest.uuid" AND
	NOT IS_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/../manifest.uuid")
	file(STRINGS "${CMAKE_CURRENT_SOURCE_DIR}/../manifest.uuid"
		SQUIRRELJME_VERSION_ID_FOSSIL LIMIT_COUNT 1)
	set(SQUIRRELJME_VERSION_ID "fossil:${SQUIRRELJME_VERSION_ID_FOSSIL}")
else()
	# Try to get version from Git Commit
	execute_process(COMMAND git rev-parse HEAD
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		ERROR_QUIET
		RESULT_VARIABLE SQUIRRELJME_VERSION_ID_GIT_CODE
		OUTPUT_VARIABLE SQUIRRELJME_VERSION_ID_GIT
		ERROR_VARIABLE SQUIRRELJME_VERSION_GIT_ERROR
		OUTPUT_STRIP_TRAILING_WHITESPACE)

	if("${SQUIRRELJME_VERSION_ID_GIT_CODE}" EQUAL 0)
		set(SQUIRRELJME_VERSION_ID "git:${SQUIRRELJME_VERSION_ID_GIT}")
	else()
		# Otherwise use a basic timestamp
		set(SQUIRRELJME_VERSION_ID "unknown:${SQUIRRELJME_VERSION_ID_TIME}")
	endif()
endif()

# Show ID version
message(STATUS
	"Version ID: ${SQUIRRELJME_VERSION_ID}")
