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
	atomic.c
	bitStream.c
	charSeq.c
	circleBuffer.c
	closeable.c
	comparator.c
	debug.c
	dylib.c
	error.c
	fixed.c
	frontEnd.c
	inflate.c
	list.c
	listUtil.c
	multithread.c
	native.c
	path.c
	seekable.c
	seekableMemory.c
	stdgone.c
	stream.c
	streamByte.c
	streamInflate.c
	streamMemory.c
	streamSeekable.c
	traverse.c
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
