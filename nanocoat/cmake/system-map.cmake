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

# Virtual Machines which are available
list(APPEND SQUIRRELJME_JVM_MAP
	"Hosted!hosted"
	"SpringCoat!springcoat"
	"NanoCoat!nanocoat")

# Clutter levels which are available
list(APPEND SQUIRRELJME_CLUTTER_MAP
	"Release!release"
	"Debug!debug")

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
	"mips32b!mips64"
	"mips32l!mips64el"
	"riscv64!riscv64"
	"arm64!aarch64"
	"powerpc32b!powerpc"
	"powerpc32l!powerpcle"
	"powerpc64b!powerpc64"
	"powerpc64l!powerpc64le")

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
	"3ds!3ds")

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
			"_LP64" IN_LIST defines OR OR
			"__LP64__" IN_LIST defines OR)
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

	# Is this GCC or GCC-like?
	string(FIND "${CMAKE_C_COMPILER}" "gcc" hasGccInName)
	if(CMAKE_COMPILER_IS_GNUCC OR
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
