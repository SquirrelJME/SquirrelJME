/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * NanoCoat Virtual Machine loop functions.
 * 
 * @file
 * @since 2025/01/05
 */

#ifndef SQUIRRELJME_LOOP_H
#define SQUIRRELJME_LOOP_H

#include "sjme/nvm/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_LOOP_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Never ending main loop.
 * 
 * @param inState The state to loop.
 * @param exitCode The exit code from the loop.
 * @return Any resultant error, if any.
 * @since 2025/07/15
 */
sjme_errorCode sjme_nvm_loop_main(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNullable sjme_jint* exitCode);
	
/**
 * Ticks the virtual machine.
 *
 * @param inState The virtual machine to tick.
 * @param maxTics The number of ticks to execute before returning,
 * a tick value of @c -1 means to tick forever. If the state has a tic limit
 * set then this will be capped to that limit, otherwise it will be infinite.
 * @param ticRemainder The output number of ticks remaining, this is optional.
 * @param isTerminated Optional output to check if the VM terminated.
 * @return Any resultant error, if any.
 * @since 2025/01/05
 */
sjme_errorCode sjme_nvm_loop_tick(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInValue sjme_attrInNegativeOnePositive sjme_jint maxTics,
	sjme_attrOutNullable sjme_jint* ticRemainder,
	sjme_attrOutNullable sjme_jboolean* isTerminated);

/**
 * Explicitely ticks a thread depending on the threading logic of the
 * virtual machine. Despite this being mostly for explicit threads, this can
 * be used without, however when used for a thread that is not explicit there
 * is a more extreme strict criteria that must be met.
 *
 * @param inThread The thread being ticked.
 * @param inFrame The minimum frame level to be ticked.
 * @param argRCopy The output argument copy, this is optional as it is only
 * a copy.
 * @return Any resultant error, if any.
 * @since 2026/10/05
 */
sjme_errorCode sjme_nvm_loop_tickExplicit(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrOutNullable sjme_jvalueTyped* argRCopy);

/**
 * Ticks the given virtual machine thread.
 *
 * @param inThread The thread to tick.
 * @param maxTics The number of ticks to execute before returning,
 * a tick value of @c -1 means to tick forever.
 * @param minFrameLevel The minimum frame level to run, this will execute all
 * frames that are at least this level or higher, @c -1 means that all frames
 * run regardless of level.
 * @param ticRemainder The output number of ticks remaining, this is optional.
 * @param isTerminated Optional output to check if the VM terminated.
 * @return Any resultant error, if any. If the thread has fallen below the
 * frame level, then @link SJME_ERROR_MINIMUM_FRAME_LEVEL @endlink is returned.
 * @since 2025/01/05
 */
sjme_errorCode sjme_nvm_loop_tickThread(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_attrInNegativeOnePositive sjme_jint maxTics,
	sjme_attrInValue sjme_attrInNegativeOnePositive sjme_jint minFrameLevel,
	sjme_attrOutNullable sjme_jint* ticRemainder,
	sjme_attrOutNullable sjme_jboolean* isTerminated);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_LOOP_H
}
#undef SJME_CXX_LOOP_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_LOOP_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LOOP_H */
