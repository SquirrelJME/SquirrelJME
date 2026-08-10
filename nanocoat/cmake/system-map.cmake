# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: System and architecture mappings

# CMake 3.3+ Policies
if(squirreljme_bp_version_3_3 OR
	"${CMAKE_VERSION}" VERSION_GREATER "3.2")
	# Support new if() IN_LIST operator.
	message(STATUS "Setting policy CMP0057...")
	cmake_policy(SET CMP0057 NEW)
endif()

# Properties used for all targets
define_property(TARGET PROPERTY SQUIRRELJME_SYSTEM
	BRIEF_DOCS "The target SquirrelJME system."
	FULL_DOCS "The target SquirrelJME system.")
define_property(TARGET PROPERTY SQUIRRELJME_ARCH
	BRIEF_DOCS "The target SquirrelJME architecture."
	FULL_DOCS "The target SquirrelJME architecture.")
define_property(TARGET PROPERTY SQUIRRELJME_TEST_LEVEL
	BRIEF_DOCS "The test level of the target."
	FULL_DOCS "The test level of the target.")
define_property(TARGET PROPERTY SQUIRRELJME_CLUTTER_LEVEL
	BRIEF_DOCS "The clutter level of the target."
	FULL_DOCS "The clutter level of the target.")

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
	"macosx!apple-darwin8"
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
	# Where does the main test source exist?
	if(EXISTS "${CMAKE_CURRENT_FUNCTION_LIST_DIR}/tryMain.c")
		file(TO_NATIVE_PATH
			"${CMAKE_CURRENT_FUNCTION_LIST_DIR}/tryMain.c"
			gccMainSource)
	elseif(EXISTS "${SQUIRRELJME_BP_LIST_DIR}/tryMain.c")
		file(TO_NATIVE_PATH
			"${SQUIRRELJME_BP_LIST_DIR}/tryMain.c"
			gccMainSource)
	elseif(EXISTS "${CMAKE_SOURCE_DIR}/cmake/tryMain.c")
		file(TO_NATIVE_PATH
			"${CMAKE_SOURCE_DIR}/cmake/tryMain.c"
			gccMainSource)
	else()
		file(TO_NATIVE_PATH
			"${CMAKE_SOURCE_DIR}/nanocoat/cmake/tryMain.c"
			gccMainSource)
	endif()

	# Run the compiler and request all the preprocessor defines
	unset(gccOutputRaw)
	execute_process(
		COMMAND "${gccExe}"
			"-E" "-dM" ${CMAKE_C_FLAGS} ${CFLAGS} "-c" "${gccMainSource}"
		OUTPUT_VARIABLE gccOutputRaw
		RESULT_VARIABLE gccResult
		OUTPUT_STRIP_TRAILING_WHITESPACE)

	# Try again, but with no passed flags
	if(NOT "${gccResult}" EQUAL "0")
		execute_process(
			COMMAND "${gccExe}"
				"-E" "-dM" "-c" "${gccMainSource}"
			OUTPUT_VARIABLE gccOutputRaw
			RESULT_VARIABLE gccResult
			OUTPUT_STRIP_TRAILING_WHITESPACE)
	endif()

	# Did this actually work?
	set(gccOutDefines)
	if("${gccResult}" EQUAL "0")
		# Remap everything to lines
		string(REGEX REPLACE "[\r\n]" ";" gccOutput "${gccOutputRaw}")

		# Need to go through everything and extract the defines
		foreach(gccLineRaw IN LISTS gccOutput)
			# Split by space
			string(REGEX REPLACE "[\t ]" ";" gccLine "${gccLineRaw}")
			list(LENGTH gccLine gccLineLen)

			# The second field is the define
			list(GET gccLine 1 gccOnlyDefine)

			# The third field is the value
			if(gccLineLen GREATER_EQUAL 3)
				list(GET gccLine 2 gccOnlyValue)
			else()
				set(gccOnlyValue "1")
			endif()

			if(NOT "${gccOnlyDefine}" STREQUAL "" AND
				NOT "${gccOnlyValue}" STREQUAL "0")
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
	# CMake before 3.3 does not support IN_LIST, thus this cannot be used!
	if(NOT squirreljme_bp_version_3_3)
		message(WARNING "CMake is too old to use #define identification!")

		# Make sure the parent scope has the value
		set(${outSystem} "unknown" PARENT_SCOPE)
		set(${outArch} "unknown" PARENT_SCOPE)

		# Stop here
		return()
	endif()

	# This is based on what is found in the define list
	## Determine system
	### RETROARCH/EMBEDDED SHOULD BE FIRST! ###
	if("__ANDROID__" IN_LIST defines OR
		"__ANDROID_API__" IN_LIST defines)
		set(hasSystem "android")
	elseif("_3DS" IN_LIST defines)
		set(hasSystem "3ds")
	elseif("GEKKO" IN_LIST defines)
		if("HW_RVL" IN_LIST defines)
			if("WIIU" IN_LIST defines)
				set(hasSystem "wiiu")
			else()
				set(hasSystem "wii")
			endif()
		else()
			set(hasSystem "gamecube")
		endif()
	elseif("PS2" IN_LIST defines)
		set(hasSystem "playstation2")
	elseif("PSP" IN_LIST defines)
		set(hasSystem "psp")
	elseif("HAVE_LIBNX" IN_LIST defines)
		set(hasSystem "switch")
	elseif("IOS" IN_LIST defines)
		set(hasSystem "ios")
	elseif("PSP2_SDK_VERSION" IN_LIST defines OR
		"__vita__" IN_LIST defines)
		set(hasSystem "vita")
	###### PSEUDO SYSTEM/COMPILER TARGETS #####
	elseif("__WINE__" IN_LIST defines)
		set(hasSystem "wine")
	###########################################
	elseif("__APPLE__" IN_LIST defines)
		if("TARGET_IPHONE_SIMULATOR" IN_LIST defines OR
			"TARGET_OS_IPHONE" IN_LIST defines)
			set(hasSystem "ios")
		elseif("__MACH__" IN_LIST defines)
			set(hasSystem "macosx")
		else()
			set(hasSystem "macintosh")
		endif()
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
		"__DOS__" IN_LIST defines OR
		"__DJGPP__" IN_LIST defines)
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
	elseif("__SDCC" IN_LIST defines OR
		"SDCC" IN_LIST defines)
		set(hasSystem "sdcc")
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
		"_M_ARM" IN_LIST defines OR
		"ARM9" IN_LIST defines OR
		"ARM11" IN_LIST defines)
		if("__BIG_ENDIAN__" IN_LIST defines)
			set(hasArch "arm32b")
		else()
			set(hasArch "arm32l")
		endif()
	elseif("__riscv" IN_LIST defines)
		set(hasArch "riscv64")
	elseif("EMSCRIPTEN" IN_LIST defines OR
		"__EMSCRIPTEN__" IN_LIST defines)
		set(hasArch "emscripten")
	elseif("__SDCC_mcs51" IN_LIST defines)
		set(hasArch "mcs51")
	elseif("__SDCC_z80" IN_LIST defines)
		set(hasArch "z80")
	elseif("__SDCC_z180" IN_LIST defines)
		set(hasArch "z180")
	elseif("__SDCC_r2k" IN_LIST defines)
		set(hasArch "r2k")
	elseif("__SDCC_r2ka" IN_LIST defines)
		set(hasArch "r2ka")
	elseif("__SDCC_r3ka" IN_LIST defines)
		set(hasArch "r3ka")
	elseif("__SDCC_sm83" IN_LIST defines)
		set(hasArch "sm83")
	elseif("__SDCC_tlcs90" IN_LIST defines)
		set(hasArch "tlcs90")
	elseif("__SDCC_ez80_z80" IN_LIST defines)
		set(hasArch "ez80_z80")
	elseif("__SDCC_z80n" IN_LIST defines)
		set(hasArch "z80n")
	elseif("__SDCC_r800" IN_LIST defines)
		set(hasArch "r800")
	elseif("__SDCC_ds390" IN_LIST defines)
		set(hasArch "ds390")
	elseif("__SDCC_TININative" IN_LIST defines)
		set(hasArch "TININative")
	elseif("__SDCC_ds400" IN_LIST defines)
		set(hasArch "ds400")
	elseif("__SDCC_hc08" IN_LIST defines)
		set(hasArch "hc08")
	elseif("__SDCC_s08" IN_LIST defines)
		set(hasArch "s08")
	elseif("__SDCC_stm8" IN_LIST defines)
		set(hasArch "stm8")
	elseif("__SDCC_mos6502" IN_LIST defines)
		set(hasArch "mos6502")
	elseif("__SDCC_mos65c02" IN_LIST defines)
		set(hasArch "mos65c02")
	elseif("__SDCC_f8" IN_LIST defines)
		set(hasArch "f8")
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
		set(hasSystem "macosx")
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
	string(FIND "${CMAKE_C_COMPILER}" "gcc" isGcc)
	string(FIND "${CMAKE_C_COMPILER}" "cl.exe" isMsvc)
	string(FIND "${CMAKE_C_COMPILER}" "sdcc" isSDCC)

	# Unconfigured environment?
	if("${CMAKE_C_COMPILER_ID}" STREQUAL "" AND
		"${CMAKE_SYSTEM_NAME}" STREQUAL "" AND
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "")
		set(hasSystem "none")
		set(hasArch "none")

	# Is this GCC or GCC-like?
	# SDCC supports this as well!
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
		"${isGcc}" GREATER_EQUAL "0" OR
		"${isSDCC}" GREATER_EQUAL "0")
		message(STATUS "Identifying compiler via GCC...")
		squirreljme_identify_by_gcc(hasSystem hasArch
			"${CMAKE_C_COMPILER}")
	endif()

	# Hope CMake has enough information about the target to determine what
	# it actually is
	if("${hasSystem}" STREQUAL "" OR
		"${hasArch}" STREQUAL "" OR
		"${hasSystem}" STREQUAL "unknown" OR
		"${hasArch}" STREQUAL "unknown")
		# This sadly is more complicated due to issues with CMake and
		# Visual Studio oddities
		message(STATUS "Identifying compiler via CMake variables...")

		# If the system is Windows, CMake reports the incorrect architecture
		# for the target system for MSVC... *faceclaw*
		if("${CMAKE_SYSTEM_NAME}" STREQUAL "Windows" AND
			"${isMsvc}" GREATER_EQUAL "0")
			# If either are blank, we will have to run the compiler to
			# figure out the target system. This is the case for Ninja.
			if("${CMAKE_VS_PLATFORM_NAME}" STREQUAL "" OR
				"${CMAKE_GENERATOR_PLATFORM}" STREQUAL "")
				# Get help for the compiler
				execute_process(COMMAND "${CMAKE_C_COMPILER}" "/?"
					OUTPUT_FILE "${CMAKE_BINARY_DIR}/cl.out"
					ERROR_FILE "${CMAKE_BINARY_DIR}/cl.err")

				# Only care for the first line
				file(STRINGS "${CMAKE_BINARY_DIR}/cl.err" clLine
					LIMIT_COUNT 1)
				message(STATUS "Trying to identify MSVC with: ${clLine}")

				# Which words appear?
				string(FIND "${clLine}" "for x86" isMsvcIa32)
				string(FIND "${clLine}" "for x64" isMsvcAmd64)
				string(FIND "${clLine}" "for ARM" isMsvcArm)
				if("${isMsvcArm}" GREATER_EQUAL "0")
					set(hasSystem "windows")
					set(hasArch "arm32l")
				elseif("${isMsvcAmd64}" GREATER_EQUAL "0")
					set(hasSystem "windows")
					set(hasArch "amd64")
				else()
					set(hasSystem "windows")
					set(hasArch "ia32")
				endif()

			# 32-bit Windows
			elseif("${CMAKE_VS_PLATFORM_NAME}" STREQUAL "Win32" OR
				"${CMAKE_GENERATOR_PLATFORM}" STREQUAL "Win32")
				set(hasSystem "windows")
				set(hasArch "ia32")

			# Otherwise, assume 64-bit
			else()
				set(hasSystem "windows")
				set(hasArch "amd64")
			endif()

		# Use standard CMake variables defined after project()
		else()
			squirreljme_identify_by_cmake(hasSystem hasArch
				"${CMAKE_SYSTEM_NAME}" "${CMAKE_SYSTEM_PROCESSOR}")
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
if(NOT DEFINED SQUIRRELJME_SYSTEM OR
	NOT DEFINED SQUIRRELJME_ARCH)
	# Try to detect the current system
	squirreljme_identify_by_current(SQUIRRELJME_SYSTEM SQUIRRELJME_ARCH)

	# Emit detection message
	message(STATUS "Detected Target System: "
		"${SQUIRRELJME_SYSTEM}/${SQUIRRELJME_ARCH}")
else()
	# This was forced!
	message(STATUS "Forced Target System: "
		"${SQUIRRELJME_SYSTEM}/${SQUIRRELJME_ARCH}")
endif()

# Then also detect the host system that we are on as well
squirreljme_identify_by_cmake(SQUIRRELJME_HOST_SYSTEM SQUIRRELJME_HOST_ARCH
	"${CMAKE_HOST_SYSTEM_NAME}" "${CMAKE_HOST_SYSTEM_PROCESSOR}")
message(STATUS "Detected Host System: "
	"${SQUIRRELJME_HOST_SYSTEM}/${SQUIRRELJME_HOST_ARCH}")

# Bare metal system?
if("${SQUIRRELJME_SYSTEM}" STREQUAL "bios" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "efi" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "ieee1275of" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "sdcc")
	set(SQUIRRELJME_IS_BARE_METAL YES)
else()
	set(SQUIRRELJME_IS_BARE_METAL NO)
endif()

### Historical, Ancient, and Retro (with the absurd) ###
# The years here are not from a product/business perspective, but more of a
# technical perspective of the best of the best that was widely available.
# For example, the Amiga 500 is from 1987 and the Macintosh 128K is from 1984
# however these two machines are very much in the same class when it comes to
# capabilities. The NeXT Computer released in 1988 just completely squashes
# those two systems in capabilities, despite the more limited CPU speed memory,
# peripheral, and disk wise it would would very much compete CPU wise with
# a Palm m515 with an SD Card. Another thing to consider is the operating
# system technology as well, at least the ease and capabilities of it. If there
# is no SDK, no C headers, and documentation is nothing except poke a bunch
# of interrupts then it gets bumped down a tier. Basically, I suppose the
# point here is modern practice separates historic and absurd from ancient
# and retro. Ancient is more like, "maybe we did not fully figure this out
# just yet". Absurd systems are not included in this logic, because that would
# just be absurd as the point is to just like Jurassic Park... just without the
# dinosaurs, hopefully. This does not give you free reign to port SquirrelJME
# to every bird in sight as you definitely will egret it.

# Absurd systems... "Yeah, but your scientists were so preoccupied with
# whether or not they could, they didn't stop to think if they should."
if ("${SQUIRRELJME_ARCH}" STREQUAL "mos6502" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "conwaylife" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "minecraft" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "msexcel" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "terraria" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "turingtape" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "xkcdrocks")
	set(SQUIRRELJME_IS_ABSURD YES)
else()
	set(SQUIRRELJME_IS_ABSURD NO)
endif()

# Historic system (Pre-1985)?
set(SQUIRRELJME_IS_HISTORIC NO)

# Ancient system (Pre-1995)? Historic systems are automatically ancient
if(SQUIRRELJME_IS_HISTORIC OR
	"${SQUIRRELJME_ARCH}" STREQUAL "ia16" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "amiga" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "dos" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "win16")
	set(SQUIRRELJME_IS_ANCIENT YES)
else()
	set(SQUIRRELJME_IS_ANCIENT NO)
endif()

# Retro system (Pre-2005)? Ancient systems are automatically retro
if(SQUIRRELJME_IS_ANCIENT OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "classicmac" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "palmos" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "wince")
	set(SQUIRRELJME_IS_RETRO YES)
else()
	set(SQUIRRELJME_IS_RETRO NO)
endif()

########################################################

# It's a Windows system?
if("${SQUIRRELJME_SYSTEM}" STREQUAL "reactos" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "windows" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "windowsce" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "wine")
	set(SQUIRRELJME_IS_WINDOWS YES)
else()
	set(SQUIRRELJME_IS_WINDOWS NO)
endif()

# It's a UNIX system! I know this!
if(NOT SQUIRRELJME_IS_WINDOWS AND
	NOT SQUIRRELJME_IS_BARE_METAL AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "3ds" AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "dos" AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "gamecube" AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "macintosh" AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "palmos" AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "switch" AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "wii" AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "wiiu")
	set(SQUIRRELJME_IS_UNIX YES)
else()
	set(SQUIRRELJME_IS_UNIX NO)
endif()

# Is this on Android?
if(ANDROID OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "android" OR
	(DEFINED ANDROID_PLATFORM AND ANDROID_PLATFORM) OR
	(DEFINED ANDROID_ABI AND ANDROID_ABI))
	set(SQUIRRELJME_IS_ANDROID YES)
else()
	set(SQUIRRELJME_IS_ANDROID NO)
endif()

# Is this cross compiled?
# If this is targeting Android, always treat as cross-compiled even if we
# are running on Android due to all of the pseudo-Linux based systems
# available on the platform
# Bare metal is always cross compiled
if (NOT "${SQUIRRELJME_HOST_SYSTEM}" STREQUAL "${SQUIRRELJME_SYSTEM}" OR
	NOT "${SQUIRRELJME_HOST_ARCH}" STREQUAL "${SQUIRRELJME_ARCH}" OR
	SQUIRRELJME_IS_ANDROID OR
	SQUIRRELJME_IS_BARE_METAL)
	set(SQUIRRELJME_IS_CROSS_COMPILE YES)
else()
	set(SQUIRRELJME_IS_CROSS_COMPILE NO)
endif()

# Used for all builds to identify the system
add_compile_definitions(SQUIRRELJME_SYSTEM=${SQUIRRELJME_SYSTEM})
add_compile_definitions(SQUIRRELJME_ARCH=${SQUIRRELJME_ARCH})

# Add fixed identifier for the target system/arch, which can be used in
# special cases as needed
string(MAKE_C_IDENTIFIER "SJME_CONFIG_IDENT_OS_${SQUIRRELJME_SYSTEM}"
	SJME_CONFIG_IDENT_OS)
string(MAKE_C_IDENTIFIER "SJME_CONFIG_IDENT_ARCH_${SQUIRRELJME_ARCH}"
	SJME_CONFIG_IDENT_ARCH)
string(TOUPPER "${SJME_CONFIG_IDENT_OS}" SJME_CONFIG_IDENT_OS)
string(TOUPPER "${SJME_CONFIG_IDENT_ARCH}" SJME_CONFIG_IDENT_ARCH)
add_compile_definitions(SJME_CONFIG_IDENT_OS=${SJME_CONFIG_IDENT_OS})
add_compile_definitions(SJME_CONFIG_IDENT_ARCH=${SJME_CONFIG_IDENT_ARCH})
add_compile_definitions(${SJME_CONFIG_IDENT_OS}=1)
add_compile_definitions(${SJME_CONFIG_IDENT_ARCH}=1)

# When compiling with mingw-w64, it is possible that CMake gets this wrong!
if("${SQUIRRELJME_SYSTEM}" STREQUAL "windows")
	# For EXEs
	set(CMAKE_EXECUTABLE_SUFFIX ".exe"
		CACHE STRING "" FORCE)
	set(CMAKE_EXECUTABLE_SUFFIX_ASM "${CMAKE_EXECUTABLE_SUFFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_EXECUTABLE_SUFFIX_C "${CMAKE_EXECUTABLE_SUFFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_EXECUTABLE_SUFFIX_CXX "${CMAKE_EXECUTABLE_SUFFIX}"
		CACHE STRING "" FORCE)

	# Ditto for DLL prefixes
	set(CMAKE_SHARED_LIBRARY_PREFIX ""
		CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_PREFIX_ASM "${CMAKE_SHARED_LIBRARY_PREFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_PREFIX_C "${CMAKE_SHARED_LIBRARY_PREFIX}"
		CACHE STRING "" FORCE)
	set(CMAKE_SHARED_LIBRARY_PREFIX_CXX "${CMAKE_SHARED_LIBRARY_PREFIX}"
		CACHE STRING "" FORCE)

	# Ditto for DLL suffixes
	set(CMAKE_SHARED_LIBRARY_SUFFIX ".dll"
		CACHE STRING "" FORCE)
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

# On by default if on Win32?
if(SQUIRRELJME_IS_WINDOWS)
	set(SQUIRRELJME_ON_IF_WIN32 ON)
	set(SQUIRRELJME_OFF_IF_WIN32 OFF)
else()
	set(SQUIRRELJME_ON_IF_WIN32 OFF)
	set(SQUIRRELJME_OFF_IF_WIN32 ON)
endif()
