# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: IEEE1275 Open Firmware Interface

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Setup executable
add_library(SquirrelJMEIEEE1275Lib OBJECT
	devtree.c
	stdc.c)

# This is written only in C!
set_target_properties(SquirrelJMEIEEE1275Lib PROPERTIES LINKER_LANGUAGE C)

# Position independent code always
set_property(TARGET SquirrelJMEIEEE1275Lib
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Define this for just this target
target_compile_definitions(SquirrelJMEIEEE1275Lib PUBLIC
	SJME_SYSTEM_IEEE1275=1)

# Includes for target
target_include_directories(SquirrelJMEIEEE1275Lib PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Needed by sub-targets
squirreljme_object_and_sources(SquirrelJMEIEEE1275LibTarget
	SquirrelJMEIEEE1275Lib)

# Include directories for the build
if(CMAKE_SYSTEM_NAME STREQUAL "ieee1275")
	# PowerPC
	if (CMAKE_SYSTEM_PROCESSOR STREQUAL "powerpc")
		message("IEEE1275: PowerPC")
		add_subdirectory(powerpc)
	endif()

	# SPARC
	if (CMAKE_SYSTEM_PROCESSOR STREQUAL "sparc")
		message("IEEE1275: SPARC")
		add_subdirectory(sparc)
	endif()

	# x86
	if (CMAKE_SYSTEM_PROCESSOR STREQUAL "x86")
		message("IEEE1275: x86")
		add_subdirectory(x86)
	endif()
else()
	message("IEEE1275: Not in current build")

	# Does the PowerPC ELF Compiler exist?
	find_program(SQUIRRELJME_IEEE1275_POWERPC_COMPILER powerpc-elf-gcc)
	if(SQUIRRELJME_IEEE1275_POWERPC_COMPILER)
		# Otherwise recursive build for PowerPC
		message("Recursively including IEEE1275 PowerPC")
		execute_process(COMMAND "${CMAKE_COMMAND}"
				-G "Unix Makefiles"
				-S "${PROJECT_SOURCE_DIR}"
				-B "${PROJECT_BINARY_DIR}/ieee1275-powerpc"
				--toolchain "${CMAKE_CURRENT_SOURCE_DIR}/powerpc/ieee1275-powerpc.toolchain.cmake"
			COMMAND_ECHO STDERR
			COMMAND_ERROR_IS_FATAL ANY)

		# Then add custom target for output of that build
		add_custom_target(SquirrelJMEIEEE1275PowerPC
			COMMAND "${CMAKE_COMMAND}"
				--build "${PROJECT_BINARY_DIR}/ieee1275-powerpc"
				-j "${CMAKE_BUILD_PARALLEL_LEVEL}"
				-t "SquirrelJMEIEEE1275PowerPC"
			VERBATIM COMMAND_EXPAND_LISTS)
	else()
		message("No powerpc-elf-gcc found, not supporting IEEE1275 PowerPC.")
	endif()
endif()
