# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare Base library
squirreljme_multilib_add_library(Base
	alloc.c
	allocSizeOf.c
	atomic.c
	charSeq.c
	comparator.c
	debug.c
	dylib.c
	error.c
	except.c
	fixed.c
	list.c
	multithread.c
	native.c
	recycle.c
	refcount.c
	seekable.c
	stream.c
	tree.c
	util.c
	zip.c)

# Include main headers
squirreljme_multilib_target_include_directories(Base
	"${CMAKE_SOURCE_DIR}/include")

# Base library goes here
squirreljme_multilib_target_binary_output(Base
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Dynamic linker library is needed
squirreljme_multilib_target_link_libraries(Base
	${CMAKE_DL_LIBS})
