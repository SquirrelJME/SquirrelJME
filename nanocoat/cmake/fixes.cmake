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
include(CheckIncludeFile)
include(CheckLibraryExists)
include(CheckSymbolExists)

# Debugging
message(STATUS "Library Path: ${CMAKE_LIBRARY_PATH}")
message(STATUS "Prefix Path: ${CMAKE_PREFIX_PATH}")
message(STATUS "Library Path (System): ${CMAKE_SYSTEM_LIBRARY_PATH}")
message(STATUS "Prefix Path (System): ${CMAKE_SYSTEM_PREFIX_PATH}")

# Debugging? SDCC does not like extra debugging info
if(SQUIRRELJME_IS_RELEASE OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "sdcc")
	add_compile_definitions(SJME_CONFIG_RELEASE=1)
elseif(SQUIRRELJME_IS_DEBUG)
	add_compile_definitions(SJME_CONFIG_DEBUG=1)
endif()

# Do not install with RPATH, CMake does relinking in build/install which
# we do not want as we give away whatever executes and such
set(CMAKE_SKIP_RPATH YES)
set(CMAKE_BUILD_WITH_INSTALL_RPATH NO)
set(CMAKE_SKIP_INSTALL_RPATH YES)

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

# Are implibs used?
if("${SQUIRRELJME_SYSTEM}" STREQUAL "windows")
	set(SQUIRRELJME_HAS_IMPLIB YES)
else()
	set(SQUIRRELJME_HAS_IMPLIB NO)
endif()

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

# Determine the directory of a library
function(squirreljme_library_dir result target)
	# Try to find the output directory
	get_target_property(dylibDirNoneR ${target}
		RUNTIME_OUTPUT_DIRECTORY)
	get_target_property(dylibDirConfR ${target}
		RUNTIME_OUTPUT_DIRECTORY_$<CONFIG>)
	get_target_property(dylibDirNone ${target}
		LIBRARY_OUTPUT_DIRECTORY)
	get_target_property(dylibDirConf ${target}
		LIBRARY_OUTPUT_DIRECTORY_$<CONFIG>)

	# Use configuration first
	if(NOT "${dylibDirConfR}" STREQUAL "dylibDirConfR-NOTFOUND")
		set(dylibDir "${dylibDirConfR}")
	elseif(NOT "${dylibDirNoneR}" STREQUAL "dylibDirNoneR-NOTFOUND")
		set(dylibDir "${dylibDirNoneR}")
	elseif(NOT "${dylibDirConf}" STREQUAL "dylibDirConf-NOTFOUND")
		set(dylibDir "${dylibDirConf}")
	elseif(NOT "${dylibDirNone}" STREQUAL "dylibDirNone-NOTFOUND")
		set(dylibDir "${dylibDirNone}")
	else()
		set(dylibDir ".")
	endif()

	# Build output
	set(${result} "${dylibDir}" PARENT_SCOPE)
endfunction()

# Determine the path of a library
function(squirreljme_library_path result target)
	# Get the library directory
	squirreljme_library_dir(dylibDir ${target})

	# Try to find the library name
	get_target_property(dylibNameNoneR ${target}
		RUNTIME_OUTPUT_NAME)
	get_target_property(dylibNameConfR ${target}
		RUNTIME_OUTPUT_NAME_$<CONFIG>)
	get_target_property(dylibNameNone ${target}
		LIBRARY_OUTPUT_NAME)
	get_target_property(dylibNameConf ${target}
		LIBRARY_OUTPUT_NAME_$<CONFIG>)

	# Use configuration first
	if(NOT "${dylibNameConfR}" STREQUAL "dylibNameConfR-NOTFOUND")
		set(dylibName "${dylibNameConfR}")
	elseif(NOT "${dylibNameNoneR}" STREQUAL "dylibNameNoneR-NOTFOUND")
		set(dylibName "${dylibNameNoneR}")
	elseif(NOT "${dylibNameConf}" STREQUAL "dylibNameConf-NOTFOUND")
		set(dylibName "${dylibNameConf}")
	elseif(NOT "${dylibNameNone}" STREQUAL "dylibNameNone-NOTFOUND")
		set(dylibName "${dylibNameNone}")
	else()
		set(dylibName "${target}")
	endif()

	# Build output
	set(baseName "")
	string(APPEND baseName
		"${CMAKE_SHARED_LIBRARY_PREFIX}"
		"${dylibName}"
		"${CMAKE_SHARED_LIBRARY_SUFFIX}")
	set(${result} "${dylibDir}/${baseName}" PARENT_SCOPE)
endfunction()

# Determine the path of the implib
function(squirreljme_implib_path result target)
	# Determine the name of the library
	squirreljme_library_path(implibPath ${target})
	string(REPLACE
		"${CMAKE_SHARED_LIBRARY_SUFFIX}" "${CMAKE_STATIC_LIBRARY_SUFFIX}"
		implibPath "${implibPath}")

	# Set resultant path
	set(${result} "${implibPath}" PARENT_SCOPE)
endfunction()

# Generate exports, mostly for Windows
function(squirreljme_shared_library_exports target)
	# Determine the name that the implib should use
	squirreljme_implib_path(impLibPath ${target})

	# Import library is used?
	if(SQUIRRELJME_HAS_IMPLIB)
		# MSVC?
		if(MSVC)
			target_link_options(${target} PRIVATE
				"/IMPLIB:${impLibPath}")

		# Mingw32 or Mingw-w64
		elseif(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
			target_link_options(${target} PRIVATE
				"-Wl,--out-implib,${impLibPath}")
		endif()
	endif()
endfunction()

# VC8 and Older
if(MSVC AND "${MSVC_VERSION}" LESS_EQUAL 1400)
	# Make sure the multi-byte character set is used
	remove_definitions(-D_UNICODE)
	remove_definitions(-DUNICODE)
	add_definitions(-D_MBCS)
endif()

# Simplifies checking and setting a compiler flag
macro(squirreljme_check_set_compiler_flag lang flag yesDef)
	# Is this a valid flag?
	squirreljme_bp_check_compiler_flag(${lang}
		${flag}
		${yesDef})

	# Add compile definition to set this
	if(${yesDef})
		add_compile_options(${flag})
	endif()
endmacro()

if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
	# From the GCC manual: Control whether or not the compiler uses IEEE
	# floating-point comparisons. These correctly handle the case where the
	# result of a comparison is unordered.
	squirreljme_check_set_compiler_flag(C
		"-mieee-fp"
		SQUIRRELJME_HAS_GCC_MIEE_FP)

	# From the GCC manual: Set 80387 floating-point precision to 32, 64 or 80
	# bits.
	squirreljme_check_set_compiler_flag(C
		"-mpc64"
		SQUIRRELJME_HAS_GCC_MPC64)

	# From the GCC manual: Store float intermediates
	squirreljme_check_set_compiler_flag(C
		"-ffloat-store"
		SQUIRRELJME_HAS_GCC_FFLOAT_STORE)

	# Turn some warnings into errors
	squirreljme_check_set_compiler_flag(C
		"-Werror=implicit-function-declaration"
		SQUIRRELJME_HAS_GCC_WERROR_IMPLICIT)

	# Make symbols hidden by default in GCC, which may prefer them visible
	squirreljme_check_set_compiler_flag(C
		"-fvisibility=hidden"
		SQUIRRELJME_HAS_GCC_FVISIBILITY_HIDDEN)

	# Pedantic warnings?
	squirreljme_check_set_compiler_flag(C
		"-Wpedantic"
		SQUIRRELJME_HAS_WARN_PEDANTIC)

	# Can we set the no execute flag for the link?
	squirreljme_check_set_compiler_flag(C
		"-Wl,-z,noexecstack"
		SQUIRRELJME_HAS_NOEXECSTACK)
endif()

# Checks if the specific header exists
macro(squirreljme_check_include_file header yesDef noDef)
	# Run the check for it
	check_include_file("${header}" ${yesDef})

	# Note that this condition needs to be inverted due to CMake
	message(DEBUG "${header}: ${${yesDef}}")
	if(NOT ${yesDef})
		add_compile_definitions(${noDef}=1)
	else()
		add_compile_definitions(${yesDef}=1)
	endif()
endmacro()

# Quick compilation check
macro(squirreljme_try_compile noun source yesDef noDef)
	# Check compile of a specific symbol
	message(STATUS "Checking compile of ${noun}...")
	try_compile(${yesDef}
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/${source}.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
			"-DINCLUDE_DIRECTORIES=${CMAKE_SOURCE_DIR}/include"
		LINK_LIBRARIES ${CMAKE_THREAD_LIBS_INIT}
		OUTPUT_VARIABLE ${yesDef}_OUTPUT)

	# Note that this condition needs to be inverted due to CMake
	message(DEBUG "${noun}: ${${yesDef}_OUTPUT}")
	message(STATUS "${noun}: ${${yesDef}}")
	if(NOT ${yesDef})
		add_compile_definitions(
			${noDef}=1)
	else()
		add_compile_definitions(
			${yesDef}=1)
	endif()
endmacro()

# Do not set SONAME for a target
macro(squirreljme_no_soname target)
	set_target_properties(${target} PROPERTIES
		NO_SONAME YES
		NO_SYSTEM_FROM_IMPORTED YES)
endmacro()

# Used to remove any NOTFOUNDs from variables
macro(squirreljme_notfound_strip var)
	unset(${var}-NOTFOUND)
	unset(${var}-NOTFOUND CACHE)

	if (${var} MATCHES "-NOTFOUND$")
		unset(${var})
		unset(${var} CACHE)
	endif()

	if("${CMAKE_VERSION}" VERSION_GREATER_EQUAL "3.13")
		if("$CACHE{${var}}" MATCHES "-NOTFOUND$")
			unset(${var} CACHE)
		endif()
	endif()
endmacro()

# Statically link in libgcc?
if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
	# Plain variant
	squirreljme_bp_check_linker_flag(C "-static-libgcc"
		SJME_CONFIG_HAS_STATIC_LIBGCC)
	message(STATUS "-static-libgcc: ${SJME_CONFIG_HAS_STATIC_LIBGCC}")

	# -Wl variant
	squirreljme_bp_check_linker_flag(C "-Wl,-static-libgcc"
			SJME_CONFIG_HAS_STATIC_LIBGCC_WL)
	message(STATUS "-Wl,-static-libgcc: ${SJME_CONFIG_HAS_STATIC_LIBGCC_WL}")

	# LINKER: variant
	squirreljme_bp_check_linker_flag(C "LINKER:-static-libgcc"
		SJME_CONFIG_HAS_STATIC_LIBGCC_LINK)
	message(STATUS
		"LINKER:-static-libgcc: ${SJME_CONFIG_HAS_STATIC_LIBGCC_LINK}")
endif()

# Locate the math library, if applicable
# There are multiple ways to go about this
squirreljme_include_nanocoat("find-m.cmake")
message(STATUS "libm: ${SQUIRRELJME_LIBM}")

# Build required libraries into a list, as you may only call
# target_link_libraries() once!
unset(SQUIRRELJME_REQUIRED_LIBS)
## Dynamic Library
if(SQUIRRELJME_LIBDL)
	list(APPEND SQUIRRELJME_REQUIRED_LIBS
		"${SQUIRRELJME_LIBDL}")
endif()
## Math
if(SQUIRRELJME_LIBM)
	list(APPEND SQUIRRELJME_REQUIRED_LIBS
		"${SQUIRRELJME_LIBM}")
endif()
## Threads
if(DEFINED CMAKE_THREAD_LIBS_INIT)
	list(APPEND SQUIRRELJME_REQUIRED_LIBS
		"${CMAKE_THREAD_LIBS_INIT}")
endif()

# For debugging required libraries
message(STATUS "System Required Libraries: ${SQUIRRELJME_REQUIRED_LIBS}")

# Make a target always FPIC
function(squirreljme_always_fpic target)
	if(SQUIRRELJME_ENABLE_FPIC)
		set_target_properties(${target} PROPERTIES
			POSITION_INDEPENDENT_CODE ON)
	endif()
endfunction()

function(squirreljme_target_link_fixes target)
	# Make sure FPIC was properly set
	squirreljme_always_fpic(${target})

	# Static libgcc?
	if(SJME_CONFIG_HAS_STATIC_LIBGCC)
		target_link_options(${target} PRIVATE
			"-static-libgcc")
	# Static libgcc (-Wl)?
	elseif(SJME_CONFIG_HAS_STATIC_LIBGCC_WL)
		target_link_options(${target} PRIVATE
			"-Wl,-static-libgcc")
	# Static libgcc (LINKER:)?
	elseif(SJME_CONFIG_HAS_STATIC_LIBGCC_LINK)
		target_link_options(${target} PRIVATE
			"LINKER:-static-libgcc")
	endif()
endfunction()

# CMake does not support merging static libraries unfortunately, thus we need
# to do it ourselves
define_property(TARGET PROPERTY SQUIRRELJME_TARGET_OBJECTS
	BRIEF_DOCS "The collective objects for a target."
	FULL_DOCS "The collective objects for a target.")

# Depend on a library or target, with better debugging and supporting of
# targets accordingly
function(squirreljme_link_libraries target scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Get the type that the target is
	get_target_property(targetType ${target} TYPE)

	# Get list of libraries to link against
	set(libraries "${ARGV}")
	list(REMOVE_AT libraries 0)
	list(REMOVE_AT libraries 0)

	# Build a list of target files
	set(nonObjects)
	set(objects)

	# Process each library
	foreach(lib IN LISTS libraries)
		# Scope change?
		if("${lib}" STREQUAL "PRIVATE" OR
			"${lib}" STREQUAL "PUBLIC" OR
			"${lib}" STREQUAL "INTERFACE")
			list(APPEND items "${lib}")

		# Stop parsing entries?
		elseif("${lib}" STREQUAL "NONE")
			break()

		# Is this a target?
		elseif(TARGET "${lib}")
			# Get properties for the target
			get_target_property(targetObjs ${lib} SQUIRRELJME_TARGET_OBJECTS)
			get_target_property(targetType ${lib} TYPE)

			# If there are target objects, inherit everything
			if(NOT "${targetObjs}" STREQUAL "targetObjs-NOTFOUND")
				# Grab all objects
				list(APPEND objects "${targetObjs}")

			# Otherwise normal determination
			else()
				# Which type of library is this?
				get_target_property(type ${lib} TYPE)

				# Depend on this target
				add_dependencies(${target} ${lib})

				# Object?
				if("${type}" STREQUAL "OBJECT_LIBRARY")
					# Add all objects to be linked in
					list(APPEND objects
						"$<TARGET_GENEX_EVAL:${lib},$<TARGET_OBJECTS:${lib}>>")

				# Static or shared?
				elseif("${type}" STREQUAL "STATIC_LIBRARY" OR
					"${type}" STREQUAL "SHARED_LIBRARY")
					# Just link against the object
					list(APPEND nonObjects
						"$<TARGET_GENEX_EVAL:${lib},$<TARGET_FILE:${lib}>>")

				# Unknown target
				else()
					message(FATAL_ERROR "Cannot link target ${library} of "
						"type ${type} to ${target} (${scope})!")
				endif()
			endif()

		# Is this a direct path to a file?
		elseif(EXISTS "${lib}")
			# Directly add the file since it is known
			list(APPEND nonObjects "${lib}")

		# Linking to something else
		else()
			# Emit a warning if not in the required list!
			list(FIND SQUIRRELJME_REQUIRED_LIBS "${lib}" foundLib)
			if("${foundLib}" LESS "0")
				message(AUTHOR_WARNING
					"Indirectly referencing library ${lib}!")
			endif()

			# Add anyway
			list(APPEND nonObjects "${lib}")
		endif()
	endforeach()

	# Add objects to sources for this, since CMake will not link objects
	if(NOT "${objects}" STREQUAL "")
		# Build a collective list of objects, transitively
		get_target_property(targetObjs ${target} SQUIRRELJME_TARGET_OBJECTS)

		# This is very noisy but the debugging definitely helps
		message(DEBUG "${target} -> ${targetObjs} (${objects})")

		# Append objects, or initially set?
		if(NOT "${targetObjs}" STREQUAL "targetObjs-NOTFOUND")
			list(APPEND targetObjs "${objects}")
			set_target_properties(${target} PROPERTIES
				SQUIRRELJME_TARGET_OBJECTS "${targetObjs}")
		else()
			set_target_properties(${target} PROPERTIES
				SQUIRRELJME_TARGET_OBJECTS "${objects}")
		endif()

		# Add to sources
		target_sources(${target} ${scope}
			${objects})
	endif()

	# Link to any non-objects as CMake cannot link objects
	if(NOT "${scope}" STREQUAL "NONE")
		target_link_libraries(${target} ${scope}
			${nonObjects})
	endif()
endfunction()

# Link against required libraries
function(squirreljme_link_libraries_required target scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Grab all libraries
	set(libraries "${ARGV}")
	list(REMOVE_AT libraries 0)
	list(REMOVE_AT libraries 0)

	# No required libraries?
	if("${SQUIRRELJME_REQUIRED_LIBS}" STREQUAL "")
		# Add all of the previous required libs
		if(NOT "${libraries}" STREQUAL "")
			squirreljme_link_libraries(${target}
				${scope} ${libraries})
		endif()
	else()
		if(NOT "${libraries}" STREQUAL "")
			squirreljme_link_libraries(${target}
				PUBLIC ${SQUIRRELJME_REQUIRED_LIBS}
				${scope} ${libraries})
		else()
			squirreljme_link_libraries(${target}
				PUBLIC ${SQUIRRELJME_REQUIRED_LIBS})
		endif()
	endif()

	# For these to be used, linker fixes need to go in also
	squirreljme_target_link_fixes(${target})
endfunction()

# Do not use .lib suffix for Windows libraries for mingw32/mingw-w64
if("${SQUIRRELJME_SYSTEM}" STREQUAL "windows" AND
	NOT (CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX))
	set(SQUIRRELJME_WIN_LIB_SUFFIX ".lib")
else()
	set(SQUIRRELJME_WIN_LIB_SUFFIX "")
endif()

# Force Intel syntax to be used
if("${SQUIRRELJME_ARCH}" STREQUAL "ia32" OR
	"${SQUIRRELJME_ARCH}" STREQUAL "amd64")
	if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
		# Breaks with intel syntax
		if(NOT VALGRIND_FOUND)
			add_compile_definitions("SJME_CONFIG_HAS_ASM_INTEL=1")
			add_compile_options("-masm=intel")
		endif()
	endif()
endif()
