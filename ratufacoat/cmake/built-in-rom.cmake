# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Built-in ROM check and inclusion

# Which ROMs are available, both lists should have the same number of elements!
list(APPEND SQUIRRELJME_BUILTIN_ROMS_TEST
	"builtinTestSpringCoatDebug.c"
	"builtinTestSpringCoatRelease.c"
	"builtinTest.c")
list(APPEND SQUIRRELJME_BUILTIN_ROMS_RELEASE
	"builtinSpringCoatDebug.c"
	"builtinSpringCoatRelease.c"
	"builtin.c")

# Get size of both as they are checked concurrently
list(LENGTH SQUIRRELJME_BUILTIN_ROMS_TEST
	SQUIRRELJME_BUILTIN_ROMS_TEST_LENGTH)
list(LENGTH SQUIRRELJME_BUILTIN_ROMS_RELEASE
	SQUIRRELJME_BUILTIN_ROMS_RELEASE_LENGTH)

# Use the smaller size of the two
if(SQUIRRELJME_BUILTIN_ROMS_TEST_LENGTH LESS_EQUAL
	SQUIRRELJME_BUILTIN_ROMS_RELEASE_LENGTH)
	set(SQUIRRELJME_BUILTIN_ROMS_COUNT
		"${SQUIRRELJME_BUILTIN_ROMS_TEST_LENGTH}")
else()
	set(SQUIRRELJME_BUILTIN_ROMS_COUNT
		"${SQUIRRELJME_BUILTIN_ROMS_RELEASE_LENGTH}")
endif()

# Subtract one from list count because forEach is inclusive
message("Count: ${SQUIRRELJME_BUILTIN_ROMS_COUNT}")
math(EXPR SQUIRRELJME_BUILTIN_ROMS_COUNT
	"${SQUIRRELJME_BUILTIN_ROMS_COUNT} - 1")

# Go through each ROM type to check it, using the same order
foreach(checkIndex RANGE "${SQUIRRELJME_BUILTIN_ROMS_COUNT}")
	# Which ROMS are we looking at?
	list(GET SQUIRRELJME_BUILTIN_ROMS_TEST "${checkIndex}"
		checkTestBase)
	list(GET SQUIRRELJME_BUILTIN_ROMS_RELEASE "${checkIndex}"
		checkReleaseBase)

	# Note it
	message(
		"Built-in: Checking ${checkTestBase} and ${checkReleaseBase}")

	# Get path to actual ROMs
	set(checkTest
		"${PROJECT_SOURCE_DIR}/build/${checkTestBase}")
	set(checkRelease
		"${PROJECT_SOURCE_DIR}/build/${checkReleaseBase}")

	# Do we have either of the ROMs?
	if(SQUIRRELJME_USE_BUILTIN AND
		EXISTS "${checkTest}")
		set(SQUIRRELJME_HAS_BUILTIN ON)
		set(SQUIRRELJME_BUILTIN_FILE "${checkTest}")
		break()
	elseif(SQUIRRELJME_USE_BUILTIN AND
		EXISTS "${checkRelease}")
		set(SQUIRRELJME_HAS_BUILTIN ON)
		set(SQUIRRELJME_BUILTIN_FILE "${checkRelease}")
		break()
	endif()
endforeach()

if(SQUIRRELJME_USE_BUILTIN)
	if (NOT "${SQUIRRELJME_BUILTIN_FILE}" STREQUAL "")
		message("Built-in: ${SQUIRRELJME_BUILTIN_FILE}")
	else()
		message("Built-in: Not available.")

		set(SQUIRRELJME_HAS_BUILTIN OFF)
		set(SQUIRRELJME_BUILTIN_FILE)
	endif()
endif()
