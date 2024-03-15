# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake Helper for project

cmake_minimum_required(VERSION 3.0)

# Declare project
project(EmulatorBaseLib)

# We need JNI for this to work
find_package(JNI)

# Declare library
add_library(EmulatorBaseLib SHARED
	src/main/c/mle_debug.c
    src/main/c/mle_form.c
    src/main/c/mle_jar.c
    src/main/c/mle_math.c
    src/main/c/mle_midi.c
    src/main/c/mle_nativearchive.c
    src/main/c/mle_object.c
    src/main/c/mle_pencil.c
    src/main/c/mle_reflection.c
    src/main/c/mle_runtime.c
    src/main/c/mle_task.c
    src/main/c/mle_terminal.c
    src/main/c/mle_thread.c
    src/main/c/mle_type.c
    src/main/c/nativebinding.c
    src/main/c/utils.c)

# Includes for the build
target_include_directories(EmulatorBaseLib PUBLIC
	"src/main/headers"
	${JNI_INCLUDE_DIRS})

# Use a more basic name for the library
set_target_properties(EmulatorBaseLib PROPERTIES
	LIBRARY_OUTPUT_NAME
	"emulator-base"
	ARCHIVE_OUTPUT_NAME
	"emulator-base")

# Always make this position independent
set_property(TARGET EmulatorBaseLib
	PROPERTY POSITION_INDEPENDENT_CODE ON)
