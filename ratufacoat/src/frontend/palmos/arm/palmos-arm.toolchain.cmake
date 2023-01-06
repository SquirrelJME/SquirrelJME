# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: ARM Palm OS (PNOlets)

# Defines the system
set(CMAKE_SYSTEM_NAME palmos)
set(CMAKE_SYSTEM_PROCESSOR arm)

# Compilers to use
#find_program(CMAKE_ASM_COMPILER arm-palmos-as)
find_program(CMAKE_ASM_COMPILER arm-palmos-gcc)
find_program(CMAKE_C_COMPILER arm-palmos-gcc)

# Flags for compilation
set(CMAKE_C_FLAGS "-std=gnu9x -Os -g0")
set(CMAKE_C_FLAGS_DEBUG "-std=gnu9x -Os -g0")
set(CMAKE_C_FLAGS_DEBUG_INIT "-std=gnu9x -Os -g0")
set(CMAKE_ASM_FLAGS "")
set(CMAKE_ASM_FLAGS_DEBUG "")
set(CMAKE_ASM_FLAGS_DEBUG_INIT "")

# Just try a static library as we have no C library
set(CMAKE_TRY_COMPILE_TARGET_TYPE "STATIC_LIBRARY")

# Parameters
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)

# Using compilation depends, breaks the build
set(CMAKE_DEPENDS_USE_COMPILER NO)

