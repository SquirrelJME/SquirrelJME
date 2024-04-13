# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base executable
add_executable(StandardJava
	$<TARGET_OBJECTS:BasePIC>
	home.c
	main.c)

# Base dependencies
add_dependencies(StandardJava
	BasePIC
	libJvm)

# We need all of our base headers
target_include_directories(StandardJava PUBLIC
	"${CMAKE_CURRENT_SOURCE_DIR}"
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")

# Just use this name as it is rather standard
squirreljme_target_binary_name(StandardJava
	"java")
