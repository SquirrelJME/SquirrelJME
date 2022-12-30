# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Architecture detection for Apple systems, so that there can
# be a cross build to PowerPC and whatnot via CMake

# Emit a notice to inform the user we are checking this
message("Checking available Mac OS architectures...")
message("If nothing is detected, the default host architecture is used.")

# Used for checking C compiler flags, need this for multi-arch
include(CheckCCompilerFlag)

# Remember old required flags for later restoration
if(DEFINED CMAKE_REQUIRED_LINK_OPTIONS)
	set(ORIG_CMAKE_REQUIRED_LINK_OPTIONS "${CMAKE_REQUIRED_LINK_OPTIONS}")
endif()

# PowerPC
set(CMAKE_REQUIRED_LINK_OPTIONS "-arch;ppc")
check_c_compiler_flag("-arch ppc" SQUIRRELJME_CLANG_SUPPORTS_POWERPC)

# x86
set(CMAKE_REQUIRED_LINK_OPTIONS "-arch;i686")
check_c_compiler_flag("-arch i686" SQUIRRELJME_CLANG_SUPPORTS_X86)

# x86_64
set(CMAKE_REQUIRED_LINK_OPTIONS "-arch;x86_64")
check_c_compiler_flag("-arch x86_64" SQUIRRELJME_CLANG_SUPPORTS_X86_64)

# M1
set(CMAKE_REQUIRED_LINK_OPTIONS "-arch;arm64")
check_c_compiler_flag("-arch arm64" SQUIRRELJME_CLANG_SUPPORTS_ARM64)

# Restore old flags
if(DEFINED ORIG_CMAKE_REQUIRED_LINK_OPTIONS)
	set(CMAKE_REQUIRED_LINK_OPTIONS "${ORIG_CMAKE_REQUIRED_LINK_OPTIONS}")
endif()

# PowerPC
if (SQUIRRELJME_CLANG_SUPPORTS_POWERPC)
	message(" - Enabling PowerPC!")
	list(APPEND CMAKE_OSX_ARCHITECTURES "ppc")
endif()

# x86
if (SQUIRRELJME_CLANG_SUPPORTS_X86)
	message(" - Enabling x86!")
	list(APPEND CMAKE_OSX_ARCHITECTURES "i686")
endif()

# x86_64
if (SQUIRRELJME_CLANG_SUPPORTS_X86_64)
	message(" - Enabling x86_64!")
	list(APPEND CMAKE_OSX_ARCHITECTURES "x86_64")
endif()

# ARM64
if (SQUIRRELJME_CLANG_SUPPORTS_ARM64)
	message(" - Enabling ARM64!")
	list(APPEND CMAKE_OSX_ARCHITECTURES "arm64")
endif()