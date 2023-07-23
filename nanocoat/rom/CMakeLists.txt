# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Try to see if there are ROM directories
file(GLOB potentialRomDirs
	LIST_DIRECTORIES true
	"*/")
foreach(potentialRomDir ${potentialRomDirs})
	# If it is a directory and it has a CMakeLists.txt in it, then include
	# it for processing...
	if(IS_DIRECTORY "${potentialRomDir}" AND
		EXISTS "${potentialRomDir}/CMakeLists.txt")
		message("Adding ROM ${potentialRomDir}...")

		add_subdirectory(${potentialRomDir})
	endif()
endforeach()
