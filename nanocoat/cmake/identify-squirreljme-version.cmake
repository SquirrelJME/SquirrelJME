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
string(REPLACE "." ";" SQUIRRELJME_VERSION_LIST "${SQUIRRELJME_VERSION}")

# Is the version considered stable or unstable?
list(GET SQUIRRELJME_VERSION_LIST 1 midVersion)
math(EXPR midVersion "${midVersion} % 2")
if("${midVersion}" EQUAL "0")
	set(SQUIRRELJME_VERSION_STABILITY "stable")
else()
	set(SQUIRRELJME_VERSION_STABILITY "unstable")
endif()
unset(midVersion)

# Make Windows compatible version
set(SQUIRRELJME_VERSION_WINDOWS "${SQUIRRELJME_VERSION}.0")
string(REGEX REPLACE "\\." "," SQUIRRELJME_VERSION_WINDOWS_RC
	"${SQUIRRELJME_VERSION_WINDOWS}")

# Setup underscore version for Install4J
string(REPLACE "." "_" SQUIRRELJME_VERSION_UNDER "${SQUIRRELJME_VERSION}")

# Put down the configure time
string(TIMESTAMP SQUIRRELJME_VERSION_ID_TIME "%Y-%m-%dT%H:%M:%SZ" UTC)
message(STATUS
	"Configure time is ${SQUIRRELJME_VERSION_ID_TIME}.")

# Does `fossil info` work?
execute_process(COMMAND "fossil" "info" "-v"
	OUTPUT_FILE "${CMAKE_BINARY_DIR}/fossil.info"
	RESULT_VARIABLE fossilInfoResult)
if("${fossilInfoResult}" EQUAL "0" AND
	EXISTS "${CMAKE_BINARY_DIR}/fossil.info")
	# Handle each info item
	file(STRINGS "${CMAKE_BINARY_DIR}/fossil.info" fossilInfoOutput
		NO_HEX_CONVERSION)
	foreach(fossilInfo IN LISTS fossilInfoOutput)
		# Split into left and right, trim any whitespace
		string(FIND "${fossilInfo}" ":" colonDx)
		string(SUBSTRING "${fossilInfo}" 0 ${colonDx} left)
		string(STRIP "${left}" left)
		if(${colonDx} GREATER_EQUAL 0)
			math(EXPR colonDx "${colonDx} + 1")
			string(SUBSTRING "${fossilInfo}" ${colonDx} -1 right)
			string(STRIP "${right}" right)
		else()
			set(right "")
		endif()

		# Which information is being parsed?
		if("${left}" STREQUAL "local-root")
			file(TO_CMAKE_PATH "${right}" SQUIRRELJME_FOSSIL_ROOT)
		elseif("${left}" STREQUAL "checkout")
			# Only the first value matters on the right side
			string(REPLACE " " ";" right "${right}")
			list(GET right 0 right)
			string(STRIP "${right}" right)

			# Extract ID
			set(SQUIRRELJME_VERSION_ID_FOSSIL "${right}")
		endif()
	endforeach()

# Load Fossil manifest, if possible
elseif(EXISTS "${CMAKE_CURRENT_LIST_DIR}/../../manifest.uuid" AND
	NOT IS_DIRECTORY "${CMAKE_CURRENT_LIST_DIR}/../../manifest.uuid")
	file(STRINGS "${CMAKE_CURRENT_LIST_DIR}/../../manifest.uuid"
		SQUIRRELJME_VERSION_ID_FOSSIL LIMIT_COUNT 1)

	# Fossil root is assumed to be here
	set(SQUIRRELJME_FOSSIL_ROOT "${CMAKE_CURRENT_LIST_DIR}/../../")
else()
	# Try to get version from Git Commit
	execute_process(COMMAND git rev-parse HEAD
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		ERROR_QUIET
		RESULT_VARIABLE SQUIRRELJME_VERSION_ID_GIT_CODE
		OUTPUT_VARIABLE SQUIRRELJME_VERSION_ID_GIT
		ERROR_VARIABLE SQUIRRELJME_VERSION_GIT_ERROR
		OUTPUT_STRIP_TRAILING_WHITESPACE)
endif()

# Which version ID to use? Prefer Fossil first
if(DEFINED SQUIRRELJME_VERSION_ID_FOSSIL)
	set(SQUIRRELJME_VERSION_ID "fossil:${SQUIRRELJME_VERSION_ID_FOSSIL}")
elseif(DEFINED SQUIRRELJME_VERSION_ID_GIT)
	set(SQUIRRELJME_VERSION_ID "git:${SQUIRRELJME_VERSION_ID_GIT}")
else()
	set(SQUIRRELJME_VERSION_ID "date:${SQUIRRELJME_VERSION_ID_TIME}")
endif()

# Give full details on what is being built
message(STATUS
	"Building for SquirrelJME ${SQUIRRELJME_VERSION}!")
message(STATUS
	"Version ID: ${SQUIRRELJME_VERSION_ID}")
message(STATUS
	"Build ID (if any): ${SQUIRRELJME_VERSION_BUILD}")
message(STATUS
	"Stability: ${SQUIRRELJME_VERSION_STABILITY}")
