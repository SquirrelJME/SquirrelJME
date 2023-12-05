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
	src/main/cpp/mle_debug.cpp
    src/main/cpp/mle_form.cpp
    src/main/cpp/mle_jar.cpp
    src/main/cpp/mle_math.cpp
    src/main/cpp/mle_object.cpp
    src/main/cpp/mle_pencil.cpp
    src/main/cpp/mle_reflection.cpp
    src/main/cpp/mle_runtime.cpp
    src/main/cpp/mle_task.cpp
    src/main/cpp/mle_terminal.cpp
    src/main/cpp/mle_thread.cpp
    src/main/cpp/mle_type.cpp
    src/main/cpp/nativebinding.cpp
    src/main/cpp/utils.cpp)

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
