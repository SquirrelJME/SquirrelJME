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
			".a")
#		set(CMAKE_C_CREATE_STATIC_LIBRARY
#			"emcc -o <TARGET> -shared <LINK_FLAGS> <OBJECTS>")
#		set(CMAKE_CXX_CREATE_STATIC_LIBRARY
#			"emcc -o <TARGET> -shared <LINK_FLAGS> <OBJECTS>")
		set(EMSCRIPTEN_GENERATE_BITCODE_STATIC_LIBRARIES
			OFF)
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
	# The target location can be overridden, generally through the pipeline
	# build system
	if(DEFINED ENV{SQUIRRELJME_BINARY_OUTPUT_DIR})
		set(actualWhere "$ENV{SQUIRRELJME_BINARY_OUTPUT_DIR}")
	elseif(DEFINED SQUIRRELJME_BINARY_OUTPUT_DIR)
		set(actualWhere "${SQUIRRELJME_BINARY_OUTPUT_DIR}")
	else()
		set(actualWhere "${where}")
	endif()

	# Set properties for all binary types
	set_target_properties(${target} PROPERTIES
		RUNTIME_OUTPUT_DIRECTORY "${actualWhere}"
		LIBRARY_OUTPUT_DIRECTORY "${actualWhere}"
		ARCHIVE_OUTPUT_DIRECTORY "${actualWhere}")

	# Some generators have multiple configuration types
	foreach(outputConfig ${CMAKE_CONFIGURATION_TYPES})
		# Configuration types are always capitalized
		string(TOUPPER "${outputConfig}" outputConfig)

		# Set properties for all binary types
		set_target_properties(${target} PROPERTIES
			RUNTIME_OUTPUT_DIRECTORY_${outputConfig} "${actualWhere}"
			LIBRARY_OUTPUT_DIRECTORY_${outputConfig} "${actualWhere}"
			ARCHIVE_OUTPUT_DIRECTORY_${outputConfig} "${actualWhere}")
	endforeach()
endmacro()

# Generate exports, mostly for Windows
macro(squirreljme_target_shared_library_exports target)
	# The target location can be overridden, generally through the pipeline
	# build system
	if(DEFINED ENV{SQUIRRELJME_BINARY_OUTPUT_DIR})
		set(actualWhere "$ENV{SQUIRRELJME_BINARY_OUTPUT_DIR}")
	elseif(DEFINED SQUIRRELJME_BINARY_OUTPUT_DIR)
		set(actualWhere "${SQUIRRELJME_BINARY_OUTPUT_DIR}")
	else()
		# If there is a config used, just use the first one
		if(NOT "${CMAKE_CONFIGURATION_TYPES}" STREQUAL "")
			list(GET CMAKE_CONFIGURATION_TYPES 0 firstConfig)

			get_target_property(actualWhere
				${target} RUNTIME_OUTPUT_DIRECTORY_${firstConfig})
		endif()

		# If not specified, use whatever was used
		if(NOT actualWhere)
			get_target_property(actualWhere
				${target} RUNTIME_OUTPUT_DIRECTORY)
		endif()

		# If not set, use the default location that CMake uses
		if(NOT actualWhere)
			set(actualWhere "${CMAKE_CURRENT_BINARY_DIR}")
		endif()
	endif()

	# If there is a config used, just use the first one, we need to know
	# the binary name for the IMPLIB
	if(NOT "${CMAKE_CONFIGURATION_TYPES}" STREQUAL "")
		list(GET CMAKE_CONFIGURATION_TYPES 0 firstConfig)

		get_target_property(squirreljme_dylib_output_name
			${target} RUNTIME_OUTPUT_NAME_${firstConfig})
	endif()

	# If no configuration is used, then use the normal output name
	if(NOT squirreljme_dylib_output_name)
		get_target_property(squirreljme_dylib_output_name
			${target} RUNTIME_OUTPUT_NAME)
	endif()

	# MSVC requires that the implementation library also be specified otherwise
	# nothing will be able to properly link against the library
	if(MSVC)
		target_link_options(${target} PRIVATE
			"/IMPLIB:${actualWhere}/${squirreljme_dylib_output_name}.lib")
	endif()
endmacro()

# VC8 and Older
if(MSVC AND "${MSVC_VERSION}" LESS_EQUAL 1400)
	# Make sure the multi-byte character set is used
	remove_definitions(-D_UNICODE)
	remove_definitions(-DUNICODE)
	add_definitions(-D_MBCS)
endif()

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

	# Pedantic warnings?
	check_c_compiler_flag("-Wpedantic" SQUIRRELJME_HAS_WARN_PEDANTIC)
	if(SQUIRRELJME_HAS_WARN_PEDANTIC)
		add_compile_options("-Wpedantic")
	endif()

	# Can we set the no execute flag for the link?
	check_c_compiler_flag("-Wl,-z,noexecstack" SQUIRRELJME_HAS_NOEXECSTACK)
	if(SQUIRRELJME_HAS_NOEXECSTACK)
		add_compile_options("-Wl,-z,noexecstack")
	endif()
endif()

# Quick compilation check
macro(squirreljme_try_compile noun target source cdef)
	message(NOTICE "Checking compile of ${noun}...")
	try_compile(${target}
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/${source}.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
			"-DINCLUDE_DIRECTORIES=${CMAKE_SOURCE_DIR}/include"
		LINK_LIBRARIES ${CMAKE_THREAD_LIBS_INIT}
		OUTPUT_VARIABLE ${target}_OUTPUT)

	message(DEBUG "${noun}: ${${target}_OUTPUT}")
	message("${noun}: ${${target}}")
	if(NOT ${target})
		add_compile_definitions(
			${cdef}=1)
	endif()
endmacro()

# Find headers
include(CheckIncludeFile)

# float.h available?
CHECK_INCLUDE_FILE("float.h" SJME_CONFIG_HAS_FLOAT_H)
if(NOT SJME_CONFIG_HAS_FLOAT_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_FLOAT_H=1)
endif()

# dlfcn.h available?
CHECK_INCLUDE_FILE("dlfcn.h" SJME_CONFIG_HAS_DLFCN_H)
if(NOT SJME_CONFIG_HAS_DLFCN_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_DLFCN_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_DLFCN_H=1)
endif()

# stdarg.h available?
CHECK_INCLUDE_FILE("stdarg.h" SJME_CONFIG_HAS_STDARG_H)
if(NOT SJME_CONFIG_HAS_STDARG_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_STDARG_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_STDARG_H=1)
endif()

# inttypes.h available?
CHECK_INCLUDE_FILE("inttypes.h" SJME_CONFIG_HAS_INTTYPES_H)
if(NOT SJME_CONFIG_HAS_INTTYPES_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_INTTYPES_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_INTTYPES_H=1)
endif()

# varargs.h available?
CHECK_INCLUDE_FILE("varargs.h" SJME_CONFIG_HAS_VARARGS_H)
if(NOT SJME_CONFIG_HAS_VARARGS_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_VARARGS_H=1)
else()
	add_compile_definitions(SJME_CONFIG_HAS_VARARGS_H=1)
endif()

# threads.h available?
CHECK_INCLUDE_FILE("threads.h" SJME_CONFIG_HAS_THREADS_H)
if(NOT SJME_CONFIG_HAS_THREADS_H)
	add_compile_definitions(SJME_CONFIG_HAS_NO_C11_THREADS=1)
endif()

# Is the SDK version header information available?
CHECK_INCLUDE_FILE("sdkddkver.h" WIN32_SDKDDKVER_INCLUDE)
if(WIN32_SDKDDKVER_INCLUDE)
	add_compile_definitions(SJME_CONFIG_HAS_SDKDDKVER_H=1)
endif()

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

# Can use thread local?
squirreljme_try_compile("sjme_threadLocal"
	SQUIRRELJME_C11_THREADS_TRY_THREAD_LOCAL
	"tryThreadLocal"
	SJME_CONFIG_HAS_NO_THREAD_LOCAL)

# Locate the math library, if applicable
find_library(SQUIRRELJME_LIBM m)
message(STATUS "libm: ${SQUIRRELJME_LIBM}")

# Link against required libraries
macro(squirreljme_target_link_libraries_required target)
	# Math library?
	if(SQUIRRELJME_LIBM)
		target_link_libraries(${target} PRIVATE
			"${SQUIRRELJME_LIBM}")
	endif()
endmacro()
