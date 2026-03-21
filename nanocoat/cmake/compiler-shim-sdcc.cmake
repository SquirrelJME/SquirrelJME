# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SDCC Shim

# Generic system
set(CMAKE_SYSTEM_NAME "Generic")

# Try compiling static libraries
set(CMAKE_TRY_COMPILE_TARGET_TYPE "STATIC_LIBRARY")

# Use specific flags
set(CMAKE_C_LINK_EXECUTABLE
	"<CMAKE_C_COMPILER> -o <TARGET> <LINK_LIBRARIES> <CMAKE_C_LINK_FLAGS> \
		<LINK_FLAGS> <OBJECTS>"
	CACHE STRING "" FORCE)
set(CMAKE_CXX_LINK_EXECUTABLE
	"<CMAKE_CXX_COMPILER> -o <TARGET> <LINK_LIBRARIES> <CMAKE_CXX_LINK_FLAGS> \
		<LINK_FLAGS> <OBJECTS>"
	CACHE STRING "" FORCE)

# Compile flags
# Errors for these, unfortunately all warnings become errors
# warning 21: stack exceeds 256 bytes for function 'sjme_alloc_formatR'
# warning 94: comparison is always true due to limited range of data type
# warning 94: comparison is always false due to limited range of data type
# warning 158: overflow in implicit constant conversion
set(CMAKE_C_FLAGS "--std-c99 --stack-auto --stack-auto --opt-code-size \
	--Werror"
	CACHE STRING "" FORCE)
set(CMAKE_CXX_FLAGS "${CMAKE_C_FLAGS}"
	CACHE STRING "" FORCE)

# Linker flags
set(CMAKE_C_LINK_FLAGS "--out-fmt-elf"
	CACHE STRING "" FORCE)
set(CMAKE_CXX_LINK_FLAGS "${CMAKE_CXX_LINK_FLAGS}"
	CACHE STRING "" FORCE)
