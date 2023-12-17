# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

list(APPEND coreSources
	alloc.c
	allocSizeOf.c
	boot.c
	bytecode.c
	debug.c
	except.c
	gfx.c
	list.c
	loop.c
	modelessStars.c
	multithread.c
	native.c
	nvm.c
	recycle.c
	rom.c
	softmmio.c
	task.c
	tread.c
	util.c
	vmConfig.c)

# The core library contains the main logic for the virtual machine
# For non-PIC:
add_library(Core OBJECT
	${coreSources})
# For PIC:
add_library(CorePIC OBJECT
	${coreSources})

# Needs to be position independent when included in other parts
set_property(TARGET CorePIC
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Include main headers
target_include_directories(Core PUBLIC
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(CorePIC PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Third party support libraries
add_subdirectory(3rdparty)
