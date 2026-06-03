# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Environment dependencies

# Locate an OCI program interface
find_program(OCI_EXECUTABLE
	NAMES podman docker)

# Locate Fossil SCM
find_program(Fossil_EXECUTABLE
	NAMES fossil)

# Locate Git SCM
find_program(Git_EXECUTABLE
	NAMES git)

# Print results of everything that was found
message(STATUS "OCI       : ${OCI_EXECUTABLE}")
message(STATUS "Fossil    : ${Fossil_EXECUTABLE}")
message(STATUS "Git       : ${Git_EXECUTABLE}")

# Is this a Fossil Repo?
if(Fossil_EXECUTABLE)
	# Request Fossil status
	execute_process(
		COMMAND "${Fossil_EXECUTABLE}"
			"status"
		WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
		RESULT_VARIABLE didFossilStatus)

	# Did this actually work?
	if("${didFossilStatus}" EQUAL "0")
		set(SQUIRRELJME_REPO_FOSSIL YES)
	else()
		set(SQUIRRELJME_REPO_FOSSIL NO)
	endif()
else()
	set(SQUIRRELJME_REPO_FOSSIL NO)
endif()

# Is this a Git repo?
if(Git_EXECUTABLE)
	# Request Git status
	execute_process(
		COMMAND "${Git_EXECUTABLE}"
			"status"
		WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
		RESULT_VARIABLE didGitStatus)

	# Did this actually work?
	if("${didGitStatus}" EQUAL "0")
		set(SQUIRRELJME_REPO_GIT YES)
	else()
		set(SQUIRRELJME_REPO_GIT NO)
	endif()
else()
	set(SQUIRRELJME_REPO_GIT NO)
endif()

# Is this an actual repo?
message(STATUS "Fossil Repository: ${SQUIRRELJME_REPO_FOSSIL}")
message(STATUS "Git Repository   : ${SQUIRRELJME_REPO_GIT}")

# In any repo?
if(SQUIRRELJME_REPO_FOSSIL OR SQUIRRELJME_REPO_GIT)
	set(SQUIRRELJME_REPO_ANY YES)
else()
	set(SQUIRRELJME_REPO_ANY NO)
endif()

# Preference...!
if(SQUIRRELJME_REPO_GIT AND Fossil_EXECUTABLE)
	if(SQUIRRELJME_REPO_FOSSIL)
		message(STATUS "Mix and matching Fossil and Git is very broken!")
	else()
		message(STATUS "You have Fossil installed, however you have the Git "
			"repository checked out. It is recommended to check out the "
			"Fossil repository when possible!")
	endif()
endif()
