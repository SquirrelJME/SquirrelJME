# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: System and architecture mappings

# Properties used for all targets
define_property(TARGET PROPERTY SQUIRRELJME_SYSTEM
	BRIEF_DOCS "The target SquirrelJME system.")
define_property(TARGET PROPERTY SQUIRRELJME_ARCH
	BRIEF_DOCS "The target SquirrelJME architecture.")
define_property(TARGET PROPERTY SQUIRRELJME_TEST_LEVEL
	BRIEF_DOCS "The test level of the target.")
define_property(TARGET PROPERTY SQUIRRELJME_CLUTTER_LEVEL
	BRIEF_DOCS "The clutter level of the target.")

# Virtual Machines which are available
list(APPEND SQUIRRELJME_JVM_MAP
	"Hosted!hosted"
	"SpringCoat!springcoat"
	"NanoCoat!nanocoat")
list(SORT SQUIRRELJME_JVM_MAP)

# Clutter levels which are available
list(APPEND SQUIRRELJME_CLUTTER_MAP
	"Release!release!fast"
	"Debug!debug!slow")
list(SORT SQUIRRELJME_CLUTTER_MAP)

# Testing level
list(APPEND SQUIRRELJME_TEST_LEVEL_MAP
	"!"
	"Test!test")
list(SORT SQUIRRELJME_TEST_LEVEL_MAP)

# Architecture mappings (SquirrelJME!gcc)
list(APPEND SQUIRRELJME_ARCH_MAP
	"ia32!i386"
	"ia32!i486"
	"ia32!i586"
	"ia32!i686"
	"ia32!x86"
	"ia32!x86_32"
	"ia64!ia64"
	"ia64!itanium"
	"ia64!itanic"
	"amd64!amd64"
	"amd64!x86_64"
	"amd64!em64t"
	"mips32b!mips"
	"mips32l!mipsel"
	"mips64b!mips64"
	"mips64l!mips64el"
	"mips32b6!mipsisa32r6"
	"mips32l6!mipsisa32r6el"
	"mips64b6!mipsisa64r6"
	"mips64l6!mipsisa64r6el"
	"riscv64!riscv64"
	"arm64l!aarch64"
	"powerpc32b!powerpc"
	"powerpc32l!powerpcle"
	"powerpc64b!powerpc64"
	"powerpc64l!powerpc64le")
list(SORT SQUIRRELJME_ARCH_MAP)

# System mappings (SquirrelJME!gcc)
list(APPEND SQUIRRELJME_SYSTEM_MAP
	"linux!linux"
	"linux!linux-gnu"
	"linux!linux-gnueabi"
	"linux!linux-gnueabihf"
	"linux!linux-gnuabi64"
	"windows!w64-mingw32"
	"windows!w64-mingw32ucrt"
	"windows!win16"
	"windows!win32"
	"windows!win64"
	"beos!beos"
	"bsd!bsd"
	"bsd!openbsd"
	"bsd!freebsd"
	"bsd!netbsd"
	"cygwin!cygwin"
	"cygwin!msys2"
	"dos!dos"
	"dos!freedos"
	"emscripten!emscripten"
	"macosx!macosx"
	"macosx!darwin"
	"3ds!3ds"
	"windowsce!wince-cegcc"
	"palmos!palmos")
list(SORT SQUIRRELJME_SYSTEM_MAP)

# Unmap from mapping fields
function(squirreljme_unmap destVar index source)
	# Convert to actual list
	string(REPLACE "!" ";" sourceList "${source}")

	# Extract the index
	list(GET sourceList ${index} destResult)

	# Return it
	set(${destVar} "${destResult}" PARENT_SCOPE)
endfunction()

# Grab GCC defines from GCC compiler
function(squirreljme_defines_gcc gccDefines gccExe)
	# Run the compiler and request all the preprocessor defines
	set(gccOutput "")
	execute_process(
		COMMAND "${gccExe}" "-E" "-dM"
			"${CMAKE_CURRENT_FUNCTION_LIST_DIR}/tryMain.c"
		OUTPUT_VARIABLE gccOutputRaw
		RESULT_VARIABLE gccResult
		OUTPUT_STRIP_TRAILING_WHITESPACE)

	# Did this actually work?
	set(gccOutDefines)
	if("${gccResult}" EQUAL "0")
		# Remap everything to lines
		string(REGEX REPLACE "[\r\n]" ";" gccOutput "${gccOutputRaw}")

		# Need to go through everything and extract the defines
		foreach(gccLineRaw IN LISTS gccOutput)
			# Split by space
			string(REGEX REPLACE "[\t ]" ";" gccLine "${gccLineRaw}")

			# The second field is the define
			list(GET gccLine 1 gccOnlyDefine)
			if(NOT "${gccOnlyDefine}" STREQUAL "")
				# Add it to the result
				list(APPEND gccOutDefines "${gccOnlyDefine}")
			endif()
		endforeach()

		# Return the resultant values
		set(${gccDefines} "${gccOutDefines}" PARENT_SCOPE)
	else()
		message(WARNING "Could not get defines from ${gccExe}: ${gccResult}")
	endif()
endfunction()

# Identify the system based on the preprocessor defines
function(squirreljme_identify_by_defines_list outSystem outArch defines)
	# This is based on what is found in the define list
	## Determine system
	if("__APPLE__" IN_LIST defines AND "__MACH__" IN_LIST defines)
		set(hasSystem "macosx")
	elseif("_3DS" IN_LIST defines)
		set(hasSystem "3ds")
	elseif("BSD" IN_LIST defines OR
		"__FreeBSD__"  IN_LIST defines OR
		"__NetBSD__"  IN_LIST defines OR
		"__bsdi__"  IN_LIST defines OR
		"__DragonFly__"  IN_LIST defines OR
		"__MidnightBSD__"  IN_LIST defines)
		set(hasSystem "bsd")
	elseif("__CYGWIN__" IN_LIST defines)
		set(hasSystem "cygwin")
	elseif("MSDOS" IN_LIST defines OR
		"__MSDOS__" IN_LIST defines OR
		"_MSDOS" IN_LIST defines OR
		"__DOS__" IN_LIST defines)
		set(hasSystem "dos")
	elseif("EMSCRIPTEN" IN_LIST defines OR
		"__EMSCRIPTEN__" IN_LIST defines)
		set(hasSystem "emscripten")
	elseif("__linux__" IN_LIST defines OR
		"linux" IN_LIST defines OR
		"__linux" IN_LIST defines)
		set(hasSystem "linux")
	elseif("_WIN16" IN_LIST defines OR
		"_WIN32" IN_LIST defines OR
		"_WIN64" IN_LIST defines OR
		"__WIN32__" IN_LIST defines OR
		"__WINDOWS__" IN_LIST defines)
		set(hasSystem "windows")
	else()
		set(hasSystem "unknown")
	endif()

	## Determine architecture
	if("__amd64__" IN_LIST defines OR
		"__amd64" IN_LIST defines OR
		"__x86_64__" IN_LIST defines OR
		"__x86_64" IN_LIST defines OR
		"_M_X64" IN_LIST defines OR
		"_M_AMD64" IN_LIST defines)
		set(hasArch "amd64")
	elseif("__powerpc" IN_LIST defines OR
		"__powerpc__" IN_LIST defines OR
		"__POWERPC__" IN_LIST defines OR
		"__ppc__" IN_LIST defines OR
		"_ARCH_PPC" IN_LIST defines OR
		"_M_PPC" IN_LIST defines)
		if("__powerpc64__" IN_LIST defines OR
			"__ppc64__" IN_LIST defines OR
			"__PPC64__" IN_LIST defines OR
			"_ARCH_PPC64" IN_LIST defines OR
			"_LP64" IN_LIST defines OR
			"__LP64__" IN_LIST defines OR
			"_LLP64" IN_LIST defines OR
			"__LLP64__" IN_LIST defines)
			if("__BIG_ENDIAN__" IN_LIST defines)
				set(hasArch "powerpc64b")
			else()
				set(hasArch "powerpc64l")
			endif()
		else()
			# macOS on PowerPC is always Big Endian
			if("__BIG_ENDIAN__" IN_LIST defines OR
				"__APPLE__" IN_LIST defines OR
				"__MACH__" IN_LIST defines)
				set(hasArch "powerpc32b")
			else()
				set(hasArch "powerpc32l")
			endif()
		endif()
	elseif("_MIPS_ARCH_MIPS32R6" IN_LIST defines)
		if("MIPSEB" IN_LIST defines OR
			"_MIPSEB" IN_LIST defines OR
			"__MIPSEB" IN_LIST defines OR
			"__MIPSEB__" IN_LIST defines)
			set(hasArch "mips32b6")
		else()
			set(hasArch "mips32l6")
		endif()
	elseif("_MIPS_ARCH_MIPS64R6" IN_LIST defines)
		if("MIPSEB" IN_LIST defines OR
			"_MIPSEB" IN_LIST defines OR
			"__MIPSEB" IN_LIST defines OR
			"__MIPSEB__" IN_LIST defines)
			set(hasArch "mips64b6")
		else()
			set(hasArch "mips64l6")
		endif()
	elseif("__mips__" IN_LIST defines OR
		"__mips" IN_LIST defines OR
		"__MIPS__" IN_LIST defines)
		if("_LP64" IN_LIST defines OR
			"__LP64__" IN_LIST defines OR
			"_LLP64" IN_LIST defines OR
			"__LLP64__" IN_LIST defines)
			if("MIPSEB" IN_LIST defines OR
				"_MIPSEB" IN_LIST defines OR
				"__MIPSEB" IN_LIST defines OR
				"__MIPSEB__" IN_LIST defines)
				set(hasArch "mips64b")
			else()
				set(hasArch "mips64l")
			endif()
		else()
			if("MIPSEB" IN_LIST defines OR
				"_MIPSEB" IN_LIST defines OR
				"__MIPSEB" IN_LIST defines OR
				"__MIPSEB__" IN_LIST defines)
				set(hasArch "mips32b")
			else()
				set(hasArch "mips32l")
			endif()
		endif()
	elseif("_M_I86" IN_LIST defines)
		if("__386__" IN_LIST defines OR
			"_M_IX86" IN_LIST defines)
			set(hasArch "ia32")
		else()
			set(hasArch "ia16")
		endif()
	elseif("i386" IN_LIST defines OR
		"__i386" IN_LIST defines OR
		"__i386__" IN_LIST defines OR
		"__i486__" IN_LIST defines OR
		"__i586__" IN_LIST defines OR
		"__i686__" IN_LIST defines OR
		"__IA32__" IN_LIST defines OR
		"_M_I86" IN_LIST defines OR
		"_M_IX86" IN_LIST defines OR
		"__X86__" IN_LIST defines OR
		"_X86_" IN_LIST defines OR
		"__INTEL__" IN_LIST defines OR
		"__I86__" IN_LIST defines)
		set(hasArch "ia32")
	elseif("__AARCH64_SIMD__" IN_LIST defines OR
		"__aarch64__" IN_LIST defines OR
		"__arm64" IN_LIST defines OR
		"__arm64__" IN_LIST defines OR
		"_M_ARM64" IN_LIST defines)
		if("__AARCH64EB__" IN_LIST defines OR
			"__BIG_ENDIAN__" IN_LIST defines)
			set(hasArch "arm64b")
		else()
			set(hasArch "arm64l")
		endif()
	elseif("__arm__" IN_LIST defines OR
		"__arm32" IN_LIST defines OR
		"__arm32__" IN_LIST defines OR
		"_M_ARM" IN_LIST defines)
		if("__BIG_ENDIAN__" IN_LIST defines)
			set(hasArch "arm32b")
		else()
			set(hasArch "arm32l")
		endif()
	elseif("__riscv" IN_LIST defines)
		set(hasArch "riscv64")
	else()
		set(hasArch "unknown")
	endif()

	# Make sure the parent scope has the value
	set(${outSystem} "${hasSystem}" PARENT_SCOPE)
	set(${outArch} "${hasArch}" PARENT_SCOPE)
endfunction()

# Identify system and architecture by CMake variables
function(squirreljme_identify_by_cmake outSystem outArch inSystem inArch)
	# Operating System
	if("${inSystem}" STREQUAL "Darwin")
		set(hasSystem "macos")
	elseif("${inSystem}" STREQUAL "FreeBSD" OR
		"${inSystem}" STREQUAL "NetBSD" OR
		"${inSystem}" STREQUAL "OpenBSD")
		set(hasSystem "bsd")
	elseif("${inSystem}" STREQUAL "CYGWIN" OR
		"${inSystem}" STREQUAL "MSYS")
		set(hasSystem "cygwin")
	elseif("${inSystem}" STREQUAL "DOS")
		set(hasSystem "dos")
	elseif("${inSystem}" STREQUAL "Emscripten")
		set(hasSystem "emscripten")
	elseif("${inSystem}" STREQUAL "Linux")
		set(hasSystem "linux")
	elseif("${inSystem}" STREQUAL "Windows" OR
		"${inSystem}" STREQUAL "WindowsStore")
		set(hasSystem "windows")
	else()
		set(hasSystem "unknown")
	endif()

	# Architecture
	if("${inArch}" STREQUAL "AMD64" OR
		"${inArch}" STREQUAL "EM64T" OR
		"${inArch}" STREQUAL "x86_64" OR
		"${inArch}" STREQUAL "x86-64" OR
		"${inArch}" STREQUAL "amd64")
		set(hasArch "amd64")
	elseif("${inArch}" STREQUAL "armv6k" OR
		"${inArch}" STREQUAL "arm32" OR
		"${inArch}" STREQUAL "armel" OR
		"${inArch}" STREQUAL "armbe" OR
		"${inArch}" STREQUAL "arm" OR
		"${inArch}" STREQUAL "armv5l")
		if("${inArch}" STREQUAL "armbe")
			set(hasArch "arm32b")
		else()
			set(hasArch "arm32l")
		endif()
	elseif("${inArch}" STREQUAL "arm64" OR
		"${inArch}" STREQUAL "aarch64" OR
		"${inArch}" STREQUAL "aarch64_be")
		if("${inArch}" STREQUAL "aarch64_be")
			set(hasArch "arm64b")
		else()
			set(hasArch "arm64l")
		endif()
	elseif("${inArch}" STREQUAL "i286" OR
		"${inArch}" STREQUAL "I86" OR
		"${inArch}" STREQUAL "ia16")
		set(hasArch "ia16")
	elseif("${inArch}" STREQUAL "i386" OR
		"${inArch}" STREQUAL "i486" OR
		"${inArch}" STREQUAL "i586" OR
		"${inArch}" STREQUAL "i686" OR
		"${inArch}" STREQUAL "X86" OR
		"${inArch}" STREQUAL "x86" OR
		"${inArch}" STREQUAL "ia32")
		set(hasArch "ia32")
	elseif("${inArch}" STREQUAL "IA64" OR
		"${inArch}" STREQUAL "ia64" OR
		"${inArch}" STREQUAL "Itanium" OR
		"${inArch}" STREQUAL "Itanic")
		set(hasArch "ia64")
	elseif("${inArch}" STREQUAL "mips" OR
		"${inArch}" STREQUAL "mipseb" OR
		"${inArch}" STREQUAL "mips32" OR
		"${inArch}" STREQUAL "mips32eb")
		set(hasArch "mips32b")
	elseif("${inArch}" STREQUAL "mips64" OR
		"${inArch}" STREQUAL "mips64eb")
		set(hasArch "mips64b")
	elseif("${inArch}" STREQUAL "mipsel" OR
		"${inArch}" STREQUAL "mips32el")
		set(hasArch "mips32l")
	elseif("${inArch}" STREQUAL "mips64el")
		set(hasArch "mips64l")
	elseif("${inArch}" STREQUAL "ppc" OR
		"${inArch}" STREQUAL "powerpc")
		set(hasArch "powerpc32b")
	elseif("${inArch}" STREQUAL "ppc64" OR
		"${inArch}" STREQUAL "powerpc64")
		set(hasArch "powerpc64b")
	else()
		set(hasArch "unknown")
	endif()

	# Make sure the parent scope has the value
	set(${outSystem} "${hasSystem}" PARENT_SCOPE)
	set(${outArch} "${hasArch}" PARENT_SCOPE)
endfunction()

# Identify the system that GCC is based on the preprocessor defines
function(squirreljme_identify_by_gcc outSystem outArch gccExe)
	# Determine GCC defines
	squirreljme_defines_gcc(defines "${gccExe}")

	# Run define based identification
	squirreljme_identify_by_defines_list(hasSystem hasArch "${defines}")

	# Make sure these get to the parent scope properly
	set(${outSystem} "${hasSystem}" PARENT_SCOPE)
	set(${outArch} "${hasArch}" PARENT_SCOPE)
endfunction()

# Identify the system of the current compiler based on the compiler or
# preprocessor defines
function(squirreljme_identify_by_current outSystem outArch)
	# Start with blank values
	set(hasSystem "")
	set(hasArch "")

	# Name-likes
	string(FIND "${CMAKE_C_COMPILER}" "gcc" hasGccInName)

	# Unconfigured environment?
	if("${CMAKE_C_COMPILER_ID}" STREQUAL "" AND
		"${CMAKE_SYSTEM_NAME}" STREQUAL "" AND
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "")
		set(hasSystem "none")
		set(hasArch "none")

	# Is this GCC or GCC-like?
	elseif(CMAKE_COMPILER_IS_GNUCC OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "GNU" OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "Clang" OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "AppleClang" OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "ARMClang" OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "CrayClang" OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "FujitsuClang" OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "TIClang" OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "XLClang" OR
		"${CMAKE_C_COMPILER_ID}" STREQUAL "IBMClang" OR
		"${hasGccInName}" GREATER_EQUAL "0")
		squirreljme_identify_by_gcc(hasSystem hasArch
			"${CMAKE_C_COMPILER}")

	# Hope CMake has enough information about the target to determine what
	# it actually is
	else()
		squirreljme_identify_by_cmake(hasSystem hasArch
			"${CMAKE_SYSTEM_NAME}" "${CMAKE_SYSTEM_PROCESSOR}")

		# If the system is Windows, CMake reports the incorrect architecture
		# for the target system... *faceclaw*
		if("${hasSystem}" STREQUAL "windows")
			if("${CMAKE_GENERATOR_PLATFORM}" STREQUAL "Win32")
				set(hasSystem "ia32")
			elseif("${CMAKE_GENERATOR_PLATFORM}" STREQUAL "x64")
				set(hasSystem "amd64")
			endif()
		endif()
	endif()

	# If blank, set to unknown
	if("${hasSystem}" STREQUAL "")
		set(hasSystem "unknown")
	endif()

	if("${hasArch}" STREQUAL "")
		set(hasArch "unknown")
	endif()

	# Make sure these get to the parent scope properly
	set(${outSystem} "${hasSystem}" PARENT_SCOPE)
	set(${outArch} "${hasArch}" PARENT_SCOPE)
endfunction()

# Try to identify the platform as defined by SquirrelJME since we may not
# be able to actually compile and run any executables. This should be a more
# reliable way of determining the target system.
squirreljme_identify_by_current(SQUIRRELJME_SYSTEM SQUIRRELJME_ARCH)
message(STATUS "Detected Target System: "
	"${SQUIRRELJME_SYSTEM}/${SQUIRRELJME_ARCH}")

# Then also detect the host system that we are on as well
squirreljme_identify_by_cmake(SQUIRRELJME_HOST_SYSTEM SQUIRRELJME_HOST_ARCH
	"${CMAKE_HOST_SYSTEM_NAME}" "${CMAKE_HOST_SYSTEM_PROCESSOR}")
message(STATUS "Detected Host System: "
	"${SQUIRRELJME_HOST_SYSTEM}/${SQUIRRELJME_HOST_ARCH}")

# Is this cross compiled?
if (NOT "${SQUIRRELJME_HOST_SYSTEM}" STREQUAL "${SQUIRRELJME_SYSTEM}" OR
	NOT "${SQUIRRELJME_HOST_ARCH}" STREQUAL "${SQUIRRELJME_ARCH}")
	set(SQUIRRELJME_IS_CROSS_COMPILE YES)
else()
	set(SQUIRRELJME_IS_CROSS_COMPILE NO)
endif()

# Used for all builds to identify the system
add_compile_definitions(SQUIRRELJME_SYSTEM=${SQUIRRELJME_SYSTEM})
add_compile_definitions(SQUIRRELJME_ARCH=${SQUIRRELJME_ARCH})

# When compiling with mingw-w64, it is possible that CMake gets this wrong!
if("${SQUIRRELJME_SYSTEM}" STREQUAL "windows")
	# For EXEs
	set(CMAKE_EXECUTABLE_SUFFIX ".exe" CACHE STRING "" FORCE)
	set(CMAKE_EXECUTABLE_SUFFIX_ASM "${CMAKE_EXECUTABLE_SUFFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_EXECUTABLE_SUFFIX_C "${CMAKE_EXECUTABLE_SUFFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_EXECUTABLE_SUFFIX_CXX "${CMAKE_EXECUTABLE_SUFFIX}"
		CACHE STRING "" FORCE)

	# Ditto for DLL prefixes
	set(CMAKE_SHARED_LIBRARY_PREFIX "" CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_PREFIX_ASM "${CMAKE_SHARED_LIBRARY_PREFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_PREFIX_C "${CMAKE_SHARED_LIBRARY_PREFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_PREFIX_CXX "${CMAKE_SHARED_LIBRARY_PREFIX}"
		CACHE STRING "" FORCE)

	# Ditto for DLL suffixes
	set(CMAKE_SHARED_LIBRARY_SUFFIX ".dll" CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_SUFFIX_ASM "${CMAKE_SHARED_LIBRARY_SUFFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_SUFFIX_C "${CMAKE_SHARED_LIBRARY_SUFFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_SUFFIX_CXX "${CMAKE_SHARED_LIBRARY_SUFFIX}"
		CACHE STRING "" FORCE)
endif()

# Write the target system details into the build root
file(WRITE "${CMAKE_BINARY_DIR}/system.tgt" "${SQUIRRELJME_SYSTEM}")
file(WRITE "${CMAKE_BINARY_DIR}/arch__.tgt" "${SQUIRRELJME_ARCH}")
