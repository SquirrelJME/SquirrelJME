# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: IEEE1275 Open Firmware Interface

# Defines the system
set(CMAKE_SYSTEM_NAME ieee1275)
set(CMAKE_SYSTEM_PROCESSOR sparc)

# Compilers to use
find_program(CMAKE_ASM_COMPILER sparc-elf-as)
find_program(CMAKE_C_COMPILER sparc-elf-gcc)

# Flags for compilation
set(CMAKE_C_FLAGS "")
set(CMAKE_C_FLAGS_DEBUG "")
set(CMAKE_C_FLAGS_DEBUG_INIT "")
set(CMAKE_ASM_FLAGS "" CACHE STRING "ASM Flags")
set(CMAKE_ASM_FLAGS_DEBUG "" CACHE STRING "ASM Flags Debug")
set(CMAKE_ASM_FLAGS_DEBUG_INIT "" CACHE STRING "ASM Debug Init")

# Just try a static library as we have no C library
set(CMAKE_TRY_COMPILE_TARGET_TYPE "STATIC_LIBRARY")

# Parameters
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)
