# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Look for default ROM configurations in a basic order
foreach(checkClutterLevel "debug" "release")
	foreach(checkSourceSet "main" "testFixtures" "test")
		set(base "${checkSourceSet}_${checkClutterLevel}")
		set(baseDir
			"${CMAKE_CURRENT_SOURCE_DIR}/specific/${base}")

		# Does it actually exist and contains a CMakeLists?
		if(IS_DIRECTORY "${baseDir}" AND
			EXISTS "${baseDir}/CMakeLists.txt")
			# Indicate found
			message("Adding ROM ${base}...")

			# Add it
			add_subdirectory("${baseDir}")
		else()
			message("Did not find ROM ${base}...")
		endif()
	endforeach()
endforeach()
