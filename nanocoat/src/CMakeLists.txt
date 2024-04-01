# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare library
squirreljme_multilib_add_library(Core
	alloc.c
	allocSizeOf.c
	atomic.c
	boot.c
	bytecode.c
	classy.c
	comparator.c
	debug.c
	descriptor.c
	dylib.c
	error.c
	except.c
	gfx.c
	jdwp.c
	list.c
	loop.c
	modelessStars.c
	multithread.c
	native.c
	nvm.c
	recycle.c
	refcount.c
	rom.c
	seekable.c
	stream.c
	task.c
	tread.c
	tree.c
	util.c
	vmConfig.c
	zip.c)

# Include main headers
squirreljme_multilib_target_include_directories(Core
	"${CMAKE_SOURCE_DIR}/include")

# Doxygen
squirreljme_doxygen(Core)
