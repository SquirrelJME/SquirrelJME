# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CI/CD Pipeline Support, for downloading/uploading artifacts

# CircleCI
if(DEFINED ENV{CIRCLECI} OR
	DEFINED ENV{CIRCLE_BRANCH} OR
	DEFINED ENV{CIRCLE_JOB})
	set(SQUIRRELJME_CICD "CircleCI")
	set(SQUIRRELJME_CICD_CIRCLECI YES)
	set(SQUIRRELJME_CICD_CONCOURSE NO)
	set(SQUIRRELJME_CICD_GITHUB NO)
	set(SQUIRRELJME_CICD_GITLAB NO)
	set(SQUIRRELJME_CICD_LOCAL NO)

# Concourse
elseif(DEFINED ENV{CONCOURSE_RUNTIME} OR
	DEFINED ENV{CONCOURSE_GARDEN} OR
	DEFINED ENV{CONCOURSE_GUARDIAN} OR
	DEFINED ENV{CONCOURSE_TAG} OR
	DEFINED ENV{CONCOURSE_NAME} OR
	DEFINED ENV{CONCOURSE_IMAGE_TAG} OR
	DEFINED ENV{CONCOURSE_IMAGE_NAME})
	set(SQUIRRELJME_CICD "Concourse")
	set(SQUIRRELJME_CICD_CIRCLECI NO)
	set(SQUIRRELJME_CICD_CONCOURSE YES)
	set(SQUIRRELJME_CICD_GITHUB NO)
	set(SQUIRRELJME_CICD_GITLAB NO)
	set(SQUIRRELJME_CICD_LOCAL NO)

# GitHub Actions
elseif(DEFINED ENV{GITHUB_ACTIONS} OR
	DEFINED ENV{GITHUB_ACTION} OR
	DEFINED ENV{GITHUB_JOB})
	set(SQUIRRELJME_CICD "GitHub Actions")
	set(SQUIRRELJME_CICD_CIRCLECI NO)
	set(SQUIRRELJME_CICD_CONCOURSE NO)
	set(SQUIRRELJME_CICD_GITHUB YES)
	set(SQUIRRELJME_CICD_GITLAB NO)
	set(SQUIRRELJME_CICD_LOCAL NO)

# GitLab
elseif(DEFINED ENV{GITLAB_CI} OR
	DEFINED ENV{GITLAB_FEATURES} OR
	DEFINED ENV{GITLAB_USER_ID})
	set(SQUIRRELJME_CICD "GitLab")
	set(SQUIRRELJME_CICD_CIRCLECI NO)
	set(SQUIRRELJME_CICD_CONCOURSE NO)
	set(SQUIRRELJME_CICD_GITHUB NO)
	set(SQUIRRELJME_CICD_GITLAB YES)
	set(SQUIRRELJME_CICD_LOCAL NO)

# Local environment, or unsupported
else()
	set(SQUIRRELJME_CICD "Local")
	set(SQUIRRELJME_CICD_CIRCLECI NO)
	set(SQUIRRELJME_CICD_CONCOURSE NO)
	set(SQUIRRELJME_CICD_GITHUB NO)
	set(SQUIRRELJME_CICD_GITLAB NO)
	set(SQUIRRELJME_CICD_LOCAL YES)
endif()

# Note the environment
message(STATUS "CI/CD Environment: ${SQUIRRELJME_CICD}")

# Registers the target on the CI/CD pipeline
function(squirreljme_cicd_register)
	# Add pre-build and post-build scripts
	foreach(target IN LISTS ARGN)
		# Add pre-build
		add_custom_target(${target}PreBuild
			COMMAND "${CMAKE_COMMAND}"
				"-D" "SQUIRRELJME_TARGET=${target}"
				"-P"
				"${CMAKE_CURRENT_LIST_DIR}/scripts/cicd-pipeline-job-prebuild.cmake"
			COMMENT "Pre-build ${target}...")
		add_dependencies(${target} ${target}PreBuild)

		# Add post-build
		add_custom_command(TARGET ${target} POST_BUILD
			COMMAND "${CMAKE_COMMAND}"
				"-D" "SQUIRRELJME_TARGET=${target}"
				"-P"
				"${CMAKE_CURRENT_LIST_DIR}/scripts/cicd-pipeline-job-postbuild.cmake"
			COMMENT "Post-build ${target}...")
	endforeach()

	# Add to the list
	list(APPEND SQUIRRELJME_CICD_TARGETS ${ARGN})
	list(REMOVE_DUPLICATES SQUIRRELJME_CICD_TARGETS)
	list(SORT SQUIRRELJME_CICD_TARGETS)

	# Force a cache-rewrite, only way to have a global unfortunately
	set(SQUIRRELJME_CICD_TARGETS "${SQUIRRELJME_CICD_TARGETS}"
		CACHE STRING "CI/CD Targets" FORCE)
endfunction()
