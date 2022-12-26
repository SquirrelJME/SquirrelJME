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
set(CMAKE_SYSTEM_PROCESSOR powerpc)

# Compilers to use
find_program(CMAKE_C_COMPILER powerpc-elf-gcc)
#set(CMAKE_CXX_COMPILER powerpc-elf-g++)

# Flags for compilation
set(CMAKE_C_FLAGS
	# Fixed Register 25 since it is IEEE1275 Specific
	-ffixed-r25)

# Just try a static library as we have no C library
set(CMAKE_TRY_COMPILE_TARGET_TYPE "STATIC_LIBRARY")

# Parameters
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)
