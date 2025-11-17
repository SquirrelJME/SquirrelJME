# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Finding of various things

# Find Java
squirreljme_include("find-java.cmake")

# Find GCC
squirreljme_include("find-gcc.cmake")

# Find MSVC
squirreljme_include("find-msvc.cmake")

# Store the compiler mappings into the cache so reloads do not break
set(SQUIRRELJME_COMPILER_MAP "${SQUIRRELJME_COMPILER_MAP}"
	CACHE STRING "Available compiler mappings")
