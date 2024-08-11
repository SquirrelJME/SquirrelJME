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
#include "sjme/stream.h"

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
typedef struct sjme_seekableBase* sjme_seekable;

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
 * Functions for seekable implementations.
 * 
 * @since 2024/08/11
 */
typedef struct sjme_seekable_functions
{
	int todo;
} sjme_seekable_functions;

/**
 * Provides an input stream to read data from a seekable, note that
 * unlike @c sjme_seekable_regionLockAsInputStream there is no locking
 * involved and as such there may be a performance penalty or otherwise.
 *
 * @param seekable The seekable to access.
 * @param outStream The resultant stream.
 * @param base The base address within the seekable.
 * @param length The number of bytes to stream.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_seekable_asInputStream(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length);

/**
 * Closes the given seekable.
 * 
 * @param seekable The seekable to close. 
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
sjme_errorCode sjme_seekable_close(
	sjme_attrInNotNull sjme_seekable seekable);

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
sjme_errorCode sjme_seekable_fromMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull sjme_pointer base,
	sjme_attrInPositive sjme_jint length);

/**
 * Wraps a seekable and provides a sub-seekable within this.
 *
 * @param inSeekable The input seekable, to get the sub-seekable of.
 * @param outSeekable The output seekable.
 * @param base The base address to get.
 * @param length The length of the seekable range.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_seekable_fromSeekable(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length);

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
