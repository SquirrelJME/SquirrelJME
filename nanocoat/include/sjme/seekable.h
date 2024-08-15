/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Seekable buffers.
 * 
 * @since 2024/01/01
 */

#ifndef SQUIRRELJME_SEEKABLE_H
#define SQUIRRELJME_SEEKABLE_H

#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SEEKABLE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Core seekable structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_seekableBase sjme_seekableBase;

/**
 * Opaque seekable data.
 *
 * @since 2024/01/01
 */
typedef sjme_seekableBase* sjme_seekable;

/**
 * Seekable lock core structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_seekable_lockBase sjme_seekable_lockBase;

/**
 * Opaque locked seekable structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_seekable_lockBase* sjme_seekable_lock;

struct sjme_seekable_lockBase
{
	/** The owning seekable. */
	sjme_seekable seekable;

	/**
	 * The base address pointer.
	 *
	 * Depending on the implementation, if the memory within the lock is
	 * modified it may directly change the resultant memory or file.
	 */
	sjme_pointer base;

	/** The length of the lock. */
	sjme_jint length;
};

/**
 * This is the action that can change what happens when a locked region is
 * unlocked.
 *
 * @since 2024/01/01
 */
typedef enum sjme_seekable_unlockAction
{
	/** Discard any bytes that were written. */
	SJME_SEEKABLE_UNLOCK_ACTION_DISCARD,

	/**
	 * Write the data back to the seekable.
	 *
	 * Note that this only has an effect if the buffer in memory is a copy
	 * of the source seekable, in which case it was not directly mappable.
	 */
	SJME_SEEKABLE_UNLOCK_ACTION_WRITE_BACK,

	/** The number of unlock actions. */
	SJME_NUM_SEEKABLE_UNLOCK_ACTION
} sjme_seekable_unlockAction;

/**
 * Implementation state within seekables.
 * 
 * @since 2024/08/11
 */
typedef struct sjme_seekable_implState
{
	/** The pool this is in. */
	sjme_alloc_pool* inPool;
	
	/** Internal handle. */
	sjme_pointer handle;
	
	/** Internal index. */
	sjme_jint index;
	
	/** Internal length. */
	sjme_jint length;
} sjme_seekable_implState;

/**
 * Closes the seekable stream.
 * 
 * @param inSeekable The current seekable.
 * @param inImplState The implementation state.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_seekable_closeFunc)(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState);

/**
 * Initializes the new seekable.
 * 
 * @param inSeekable The current seekable.
 * @param inImplState The implementation state.
 * @param data Any passed in data through initialize.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_seekable_initFunc)(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrInNullable sjme_pointer data);

/**
 * Reads from the given seekable.
 * 
 * @param inSeekable The current seekable.
 * @param inImplState The implementation state.
 * @param outBuf The buffer to write to.
 * @param base The base address to read from.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_seekable_readFunc)(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositiveNonZero sjme_jint length);

/**
 * Returns the size of the seekable.
 * 
 * @param inSeekable The current seekable.
 * @param inImplState The implementation state.
 * @param outSize The resultant size of the seekable.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_seekable_sizeFunc)(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNull sjme_jint* outSize);

/**
 * Functions for seekable implementations.
 * 
 * @since 2024/08/11
 */
typedef struct sjme_seekable_functions
{
	/** Closes the stream. */
	sjme_seekable_closeFunc close;
	
	/** Initializes the stream. */
	sjme_seekable_initFunc init;
	
	/** Read from the given stream. */
	sjme_seekable_readFunc read;
	
	/** Return the size of the stream. */
	sjme_seekable_sizeFunc size;
} sjme_seekable_functions;

struct sjme_seekableBase
{
	/** Closeable. */
	sjme_closeableBase closable;
	
	/** Implementation state. */
	sjme_seekable_implState implState;
	
	/** Front end data. */
	sjme_frontEnd frontEnd;
	
	/** Functions for stream access. */
	const sjme_seekable_functions* functions;
	
	/** Spinlock for stream access. */
	sjme_thread_spinLock lock;
};

/**
 * Opens a generic stream.
 * 
 * @param inPool The pool to allocate within.
 * @param outSeekable The resultant seekable.
 * @param inFunctions The seekable functions.
 * @param data Any data to pass to the initialize function.
 * @param copyFrontEnd Front-end data as needed.
 * @return Any resultant error, if any,
 * @since 2024/08/11
 */
sjme_errorCode sjme_seekable_open(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull const sjme_seekable_functions* inFunctions,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd);

/**
 * Initializes a seekable from the given memory range.
 *
 * @param inPool The pool to allocate within.
 * @param outSeekable The resultant seekable.
 * @param base The base memory address.
 * @param length The length of memory block.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_seekable_openMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull sjme_pointer base,
	sjme_attrInPositive sjme_jint length);

/**
 * Wraps a seekable and provides a sub-seekable within this.
 *
 * @param inPool The pool to allocate within.
 * @param inSeekable The input seekable, to get the sub-seekable of.
 * @param outSeekable The output seekable.
 * @param base The base address to get.
 * @param length The length of the seekable range.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_seekable_openSeekable(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length);

/**
 * Reads from the given seekable.
 * 
 * @param seekable The seekable to read from. 
 * @param outBuf The output buffer.
 * @param seekBase The base of the seekable to read from.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
sjme_errorCode sjme_seekable_read(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_buffer outBuf,
	sjme_attrInPositive sjme_jint seekBase,
	sjme_attrInPositive sjme_jint length);

/**
 * Reads from the given seekable in reverse byte order for every @c wordSize
 * that is read from the input. 
 * 
 * @param seekable The seekable to read from.
 * @param wordSize The word size for the read, every number of this many
 * bytes will be reversed.
 * @param outBuf The output buffer.
 * @param seekBase The base of the seekable to read from.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/08/13
 */
sjme_errorCode sjme_seekable_readReverse(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrInRange(2, 8) sjme_jint wordSize,
	sjme_attrOutNotNull sjme_buffer outBuf,
	sjme_attrInPositive sjme_jint seekBase,
	sjme_attrInPositive sjme_jint length);

#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)

/**
 * Reads big endian data from the given seekable.
 * 
 * @param seekable The seekable to read from.
 * @param wordSize The word size for the read, every number of bytes will
 * be possibly reversed if required.
 * @param outBuf The output buffer.
 * @param seekBase The base of the seekable to read from.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/08/13
 */
#define sjme_seekable_readBig(seekable, wordSize, outBuf, seekBase, \
	length) \
	(sjme_seekable_readReverse((seekable), (wordSize), (outBuf), (seekBase), \
	(length)))	

/**
 * Reads little endian data from the given seekable.
 * 
 * @param seekable The seekable to read from.
 * @param wordSize The word size for the read, every number of bytes will
 * be possibly reversed if required.
 * @param outBuf The output buffer.
 * @param seekBase The base of the seekable to read from.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/08/13
 */
#define sjme_seekable_readLittle(seekable, wordSize, outBuf, seekBase, \
	length) \
	(sjme_seekable_read((seekable), (outBuf), (seekBase), (length)))

#else

/**
 * Reads big endian data from the given seekable.
 * 
 * @param seekable The seekable to read from.
 * @param wordSize The word size for the read, every number of bytes will
 * be possibly reversed if required.
 * @param outBuf The output buffer.
 * @param seekBase The base of the seekable to read from.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/08/13
 */
#define sjme_seekable_readBig(seekable, wordSize, outBuf, seekBase, \
	length) \
	(sjme_seekable_read((seekable), (outBuf), (seekBase), (length)))

/**
 * Reads little endian data from the given seekable.
 * 
 * @param seekable The seekable to read from.
 * @param wordSize The word size for the read, every number of bytes will
 * be possibly reversed if required.
 * @param outBuf The output buffer.
 * @param seekBase The base of the seekable to read from.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/08/13
 */
#define sjme_seekable_readLittle(seekable, wordSize, outBuf, seekBase, \
	length) \
	(sjme_seekable_readReverse((seekable), (wordSize), (outBuf), (seekBase), \
	(length)))

#endif

/**
 * Locks a region of a seekable so that the data stored there can be accessed
 * directly via memory access. Depending on the seekable implementation, there
 * are multiple possibilities as to what may occur: if the seekable is
 * directly from memory it will just map to that pointer accordingly, if
 * the seekable is backed by a file then it will be memory mapped, otherwise
 * a buffer will be created with a copy of the bytes at the given region and
 * will stay as such until unlocked. Per the implementation, if the seekable
 * data is modified in memory it may change that actual memory or file.
 *
 * @param seekable The seekable to lock within.
 * @param outLock The resultant lock.
 * @param base The base address within the seekable to lock.
 * @param length The number of bytes to lock.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_seekable_regionLock(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_seekable_lock* outLock,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length);

/**
 * Similar to @c sjme_seekable_regionLock except that instead of returning a
 * lock it returns a stream.
 *
 * @param seekable The seekable to lock within.
 * @param outStream The resultant stream.
 * @param base The base address within the seekable to lock.
 * @param length The number of bytes to lock.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_seekable_regionLockAsInputStream(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length);

/**
 * Unlocks a locked seekable region, the resultant action may or may not
 * have an effect depending on the implementation.
 *
 * @param inLock The lock to unlock.
 * @param action The action to perform on the unlock.
 * @return Any resultant error, if any.
 * @see sjme_seekable_unlockAction
 * @since 2024/01/01
 */
sjme_errorCode sjme_seekable_regionUnlock(
	sjme_attrInNotNull sjme_seekable_lock inLock,
	sjme_attrInRange(0, SJME_NUM_SEEKABLE_UNLOCK_ACTION)
		sjme_seekable_unlockAction action);

/**
 * Returns the size of the given seekable.
 * 
 * @param seekable The seekable to get the size of. 
 * @param outSize The size of the seekable.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
sjme_errorCode sjme_seekable_size(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_jint* outSize);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SEEKABLE_H
}
		#undef SJME_CXX_SQUIRRELJME_SEEKABLE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SEEKABLE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SEEKABLE_H */
