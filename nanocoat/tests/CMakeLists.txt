# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Testing framework for the main library

# Setup main test execution wrapper
add_executable(TestExecutor
	main.c
	"$<TARGET_OBJECTS:Core>")

# Make sure we have our headers included accordingly
target_include_directories(TestExecutor PUBLIC
	"${CMAKE_SOURCE_DIR}/include")
