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
	allocAtomic.c
	atomic.c
	bitStream.c
	cbor.c
	charSeq.c
	circleBuffer.c
	closeable.c
	comparator.c
	debug.c
	dylib.c
	dylibExtra.c
	error.c
	fixed.c
	frontEnd.c
	inflate.c
	list.c
	listUtil.c
	multithread.c
	multithreadNone.c
	multithreadPosix.c
	multithreadWin32.c
	native.c
	nativeBsd.c
	nativeDarwin.c
	nativeLinux.c
	nativeNone.c
	nativePosix.c
	nativeSolaris.c
	nativeStdC.c
	nativeWin32.c
	path.c
	pathStyles.c
	seekable.c
	seekableMemory.c
	stdGone.c
	stream.c
	streamByte.c
	streamInflate.c
	streamMemory.c
	streamSeekable.c
	streamStdIo.c
	traverse.c
	tree.c
	util.c
	zip.c)

# Include main headers
squirreljme_multilib_include_directories(Base PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Base library goes here
squirreljme_multilib_binary_output(Base
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Required system libraries
squirreljme_multilib_link_libraries_required(Base NONE)

# The base library is rather useful
squirreljme_multilib_export(Base)
squirreljme_multilib_install(Base)
