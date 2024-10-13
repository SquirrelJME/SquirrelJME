# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake related fixes and build system related fixes
# The major purpose of this file is to make it so the main files are kept
# clean and pristine and patches are placed here because they affect the
# entire project.

# Needed for C compiler checks
include(CheckCCompilerFlag)

# Cross-compiling the build?
if(NOT "${CMAKE_HOST_SYSTEM_NAME}" STREQUAL "${CMAKE_SYSTEM_NAME}" OR
	NOT "${CMAKE_HOST_SYSTEM_PROCESSOR}" STREQUAL "${CMAKE_SYSTEM_PROCESSOR}")
	message(STATUS "Performing cross-build as "
		"${CMAKE_HOST_SYSTEM_NAME}/"
		"${CMAKE_HOST_SYSTEM_PROCESSOR} is not "
		"${CMAKE_SYSTEM_NAME}/${CMAKE_SYSTEM_PROCESSOR}.")
endif()

# LibRetro build for emscripten can never be static
if(SQUIRRELJME_IS_LIBRETRO)
	if(EMSCRIPTEN)
		unset(LIBRETRO_REALLY_STATIC)
		unset(LIBRETRO_REALLY_STATIC CACHE)

		# Linking needs to be fixed here
		set(CMAKE_STATIC_LIBRARY_SUFFIX
			".bc")
		set(CMAKE_C_CREATE_STATIC_LIBRARY
			"emcc -o <TARGET> -shared <LINK_FLAGS> <OBJECTS>")
		set(CMAKE_CXX_CREATE_STATIC_LIBRARY
			"emcc -o <TARGET> -shared <LINK_FLAGS> <OBJECTS>")
		set(EMSCRIPTEN_GENERATE_BITCODE_STATIC_LIBRARIES
			ON)
	elseif(LIBRETRO_STATIC)
		set(LIBRETRO_REALLY_STATIC ON)
	endif()
endif()

# If we cannot run the code we are building then we cannot actually test code
#if(NOT SQUIRRELJME_CROSS_BUILD)
#	include(CheckCSourceRuns)
#	set(CMAKE_REQUIRED_QUIET ON)
#	check_c_source_runs("${CMAKE_SOURCE_DIR}/cmake/utils/simple.c"
#		SQUIRRELJME_SIMPLE_SOURCE_RUNS)
#	if(NOT SQUIRRELJME_SIMPLE_SOURCE_RUNS)
#		# Note
#		message(WARNING
#			"Could not run simple utility ("
#			"${SQUIRRELJME_SIMPLE_SOURCE_RUNS}), disabling tests.")
#
#		# Disable testing
#		set(SQUIRRELJME_ENABLE_TESTING OFF)
#	endif()
#else()
#	# Different host, assume we cannot run the target code
#	set(SQUIRRELJME_ENABLE_TESTING OFF)
#endif()

# CMake 3.13 added many things!
if(${CMAKE_VERSION} VERSION_LESS_EQUAL "3.12")
	# Disable CPacking
	if(NOT DEFINED SQUIRRELJME_ENABLE_PACKING)
		message(WARNING "Disabling packing due to old CMake.")

		set(SQUIRRELJME_ENABLE_PACKING OFF)
	endif()
else()
	# Enable CPacking and
	if(NOT DEFINED SQUIRRELJME_ENABLE_PACKING)
		message(STATUS "Enabling packing...")

		set(SQUIRRELJME_ENABLE_PACKING ON)
	endif()
endif()

# Make static executable
macro(squirreljme_static_executable target)
	if(CMAKE_COMPILER_IS_GNUCC OR
		CMAKE_COMPILER_IS_GNUCXX OR
		CMAKE_C_COMPILER_ID STREQUAL "GNU" OR
		CMAKE_CXX_COMPILER_ID STREQUAL "Clang")
		target_link_options(${target} BEFORE PRIVATE
			"-static")
	elseif(MSVC OR
		CMAKE_C_COMPILER_ID STREQUAL "MSVC" OR
		CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
		# For MSVC, static linking is specified at compile time rather than
		# at link time, so everything has to be compiled this way to be static
		#target_compile_options(${target} BEFORE PRIVATE
		#	"/MT")

		# And as such we cannot specify a library to use here
		#target_link_options(${target} BEFORE PRIVATE
		#	"/NODEFAULTLIB:library")
	endif()
endmacro()

# Force a specific name for the output resultant binary
macro(squirreljme_target_binary_name target what)
	# Base properties
	set_target_properties(${target} PROPERTIES
		RUNTIME_OUTPUT_NAME "${what}"
		LIBRARY_OUTPUT_NAME "${what}"
		ARCHIVE_OUTPUT_NAME "${what}")

	# Then for each configuration
	foreach(outputConfig ${CMAKE_CONFIGURATION_TYPES})
		string(TOUPPER "${outputConfig}" outputConfig)

		set_target_properties(${target} PROPERTIES
			RUNTIME_OUTPUT_NAME_${outputConfig} "${what}"
			LIBRARY_OUTPUT_NAME_${outputConfig} "${what}"
			ARCHIVE_OUTPUT_NAME_${outputConfig} "${what}")
	endforeach()
endmacro()

# Need to set specific locations for output libraries?
# Note that RUNTIME_OUTPUT_DIRECTORY is needed for the Windows build to output
# directories since .DLL files are output there and not where shared libraries
# go??? No idea really.
macro(squirreljme_target_binary_output target where)
	set_target_properties(${target} PROPERTIES
		RUNTIME_OUTPUT_DIRECTORY "${where}"
		LIBRARY_OUTPUT_DIRECTORY "${where}"
		ARCHIVE_OUTPUT_DIRECTORY "${where}")

	foreach(outputConfig ${CMAKE_CONFIGURATION_TYPES})
		string(TOUPPER "${outputConfig}" outputConfig)

		set_target_properties(${target} PROPERTIES
			RUNTIME_OUTPUT_DIRECTORY_${outputConfig} "${where}"
			LIBRARY_OUTPUT_DIRECTORY_${outputConfig} "${where}"
			ARCHIVE_OUTPUT_DIRECTORY_${outputConfig} "${where}")
	endforeach()
endmacro()

# Generate exports, mostly for Windows
macro(squirreljme_target_shared_library_exports target)
	get_target_property(squirreljme_dylib_output_dir
		${target} RUNTIME_OUTPUT_DIRECTORY)
	get_target_property(squirreljme_dylib_output_name
		${target} RUNTIME_OUTPUT_NAME)

	if(MSVC)
		target_link_options(${target} PRIVATE
			"/IMPLIB:${squirreljme_dylib_output_dir}/${squirreljme_dylib_output_name}.lib")
	endif()
endmacro()

if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
	# Turn some warnings into errors
	check_c_compiler_flag("-Werror=implicit-function-declaration"
		SQUIRRELJME_HAS_GCC_WERROR_IMPLICIT)
	if (SQUIRRELJME_HAS_GCC_WERROR_IMPLICIT)
		add_compile_options("-Werror=implicit-function-declaration")
	endif()

	# Make symbols hidden by default in GCC, which may prefer them visible
	check_c_compiler_flag("-fvisibility=hidden"
		SQUIRRELJME_HAS_GCC_FVISIBILITY_HIDDEN)
	if(SQUIRRELJME_HAS_GCC_FVISIBILITY_HIDDEN)
		add_compile_options("-fvisibility=hidden")
	endif()
endif()

# Quick compilation check
macro(squirreljme_try_compile noun target source cdef)
	try_compile(${target}
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/${source}.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
		LINK_LIBRARIES ${CMAKE_THREAD_LIBS_INIT}
		OUTPUT_VARIABLE ${target}_OUTPUT)

	message(DEBUG "${noun}: ${${target}_OUTPUT}")
	message("${noun}: ${${target}}")
	if(NOT ${target})
		add_compile_definitions(
			${cdef}=1)
	endif()
endmacro()

# snprintf() available?
squirreljme_try_compile("snprintf()"
	SQUIRRELJME_SNPRINTF_TRY_VALID
	"trySNPrintF"
	SJME_CONFIG_HAS_NO_SNPRINTF)

# vsnprintf() available?
squirreljme_try_compile("vsnprintf() with stdarg.h"
	SQUIRRELJME_VSNPRINTFA_TRY_VALID
	"tryVSNPrintFA"
	SJME_CONFIG_HAS_NO_VSNPRINTFA)
squirreljme_try_compile("vsnprintf() with varargs.h"
	SQUIRRELJME_VSNPRINTFV_TRY_VALID
	"tryVSNPrintFV"
	SJME_CONFIG_HAS_NO_VSNPRINTFV)

# stdarg.h available?
squirreljme_try_compile("stdarg.h"
	SQUIRRELJME_STDARG_TRY_VALID
	"tryStdArgH"
	SJME_CONFIG_HAS_NO_STDARG)

# varargs.h available?
squirreljme_try_compile("varargs.h"
	SQUIRRELJME_VARARGS_TRY_VALID
	"tryVarArgsH"
	SJME_CONFIG_HAS_NO_VARARGS)

