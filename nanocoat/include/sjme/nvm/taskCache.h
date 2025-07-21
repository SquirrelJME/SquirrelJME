/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Stack cached interpreter support.
 * 
 * @since 2025/07/21
 */

#ifndef SJME_C_TASKCACHE_H
#define SJME_C_TASKCACHE_H

#include "sjme/nvm/task.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_TASKCACHE_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * A full slot within the stack and/or locals.
 *
 * @since 2025/07/21
 */
typedef struct sjme_nvm_cache_slot sjme_nvm_cache_slot;

typedef union sjme_nvm_cache_value
{
	/** Reference to another slot, this caches another value. */
	sjme_nvm_cache_slot* ref;

	/** The value in this slot. */
	sjme_nvm_rawFieldValue v;
} sjme_nvm_cache_value;

struct sjme_nvm_cache_slot
{
	sjme_alignPointer sjme_nvm_cache_value v;
	
	/** The number of references to this slot. */
	sjme_jint refCount;
};

/**
 * A single argument within an argument flood.
 *
 * @since 2026/07/21
 */
typedef struct sjme_nvm_cache_floodArg
{
	/** The raw value this points to, will always be an isolate. */
	const sjme_nvm_rawFieldValue* v;
	
	/** The reference this points to. */
	sjme_nvm_cache_slot* ref;
} sjme_nvm_cache_floodArg;

/**
 * A flood of values which are passed to a method or elsewhere, when these
 * are no longer needed they must be @c sjme_nvm_cache_opDeleteFlood() . This
 * is needed so that if there are any values which are isolated and are fully
 * passed to whatever operation used them, they can be properly dropped
 * and garbage collected when finished.
 *
 * @since 2026/07/21
 */
typedef struct sjme_nvm_cache_flood
{
	/** The number of flood arguments. */
	sjme_jint numArgs;
	
	/** Arguments within the flood. */
	sjme_nvm_cache_floodArg a[sjme_flexibleArrayCount];
} sjme_nvm_cache_flood;

/**
 * Copies the value of one slot to another.
 * 
 * @param inFrame The context frame of execution.
 * @param opIn The input slot.
 * @param opOut The output slot.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opCopy(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opIn,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opOut);
	
/**
 * Deletes the given slot.
 * 
 * @param inFrame The context frame of execution.
 * @param opInOut The slot to delete, this will be cleared on output.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opDelete(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opInOut);

/**
 * Deletes the given flood, this should be called when the flood from
 * a @c sjme_nvm_cache_opStackPop() is no longer needed.
 * 
 * @param inFrame The context frame of execution.
 * @param opInOut The flood to delete, all slots within will be deleted.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opDeleteFlood(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_cache_flood* opInOut);

/**
 * Evicts the given slot from the cache and forces it to be an isolate.
 * 
 * @param inFrame The context frame of execution.
 * @param opInOut The slot to evict and become an isolate.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opEvict(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opInOut);

/**
 * Evicts all slots within the given flood from the cache and forces all of
 * them isolates.
 * 
 * @param inFrame The context frame of execution.
 * @param opInOut The flood to turn all slots into isolates for.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opEvictFlood(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInOutNotNull sjme_nvm_cache_flood** opInOut);

/**
 * Sets a local variable to an isolate value.
 * 
 * @param inFrame The context frame of execution.
 * @param opOut The resultant cached slot.
 * @param localIndex The local index to set.
 * @param inValue The value to set.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opIsoLocal(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrOutNotNull sjme_nvm_cache_slot** opOut,
	sjme_attrInRange(0, INT32_MAX) sjme_jint localIndex,
	sjme_attrInNotNull sjme_jvalueTyped* inValue);

/**
 * Pushes a stack value as an isolate value.
 * 
 * @param inFrame The context frame of execution.
 * @param opOut The resultant cached slot.
 * @param inValue The value to set.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opIsoStackPush(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrOutNotNull sjme_nvm_cache_slot** opOut,
	sjme_attrInNotNull sjme_jvalueTyped* inValue);

/**
 * Pushes a value from a local variable to the stack.
 * 
 * @param inFrame The context frame of execution.
 * @param opIn The input slot.
 * @param opOut The output slot. 
 * @param localIndex The index being pushed.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opLocalPush(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrOutNullable sjme_nvm_cache_slot** opIn,
	sjme_attrOutNullable sjme_nvm_cache_slot** opOut,
	sjme_attrInRange(0, INT32_MAX) sjme_jint localIndex);

/**
 * Peeks the top-most of the stack into the given flood, which can be used
 * to process an instruction or forward to another method. This does not
 * remove any slots from the stack.
 * 
 * @param inFrame The context frame of execution.
 * @param opInOut The flood to read values into.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opStackPeek(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInOutNotNull sjme_nvm_cache_flood* opInOut);

/**
 * Pops the top-most of the stack into the given flood, which can be used
 * to process an instruction or forward to another method. This does remove
 * slots from the stack and should be followed
 * by @c sjme_nvm_cache_opDeleteFlood() when no longer needed.
 * 
 * @param inFrame The context frame of execution.
 * @param opInOut The flood to read values into.
 * @return Any resultant error, if any.
 * @since 2025/07/21
 */
sjme_errorCode sjme_nvm_cache_opStackPop(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInOutNotNull sjme_nvm_cache_flood* opInOut);

/**
 * Allocates a flood variable on the stack.
 * 
 * @param n The number of items to allocate.
 * @param var The variable to store into.
 * @since 2025/07/21
 */
#define sjme_nvm_cache_floodAlloc(n, var) \
	do \
	{ \
		(*(var)) = sjme_alloca(sizeof(sjme_nvm_cache_flood) + \
			offsetof(sjme_nvm_cache_flood, a) + \
			(sizeof(sjme_nvm_cache_floodArg) * (n))); \
		if ((*(var)) != NULL) \
			memset((*(var)), 0, sizeof(sjme_nvm_cache_flood) + \
				offsetof(sjme_nvm_cache_flood, a) + \
				(sizeof(sjme_nvm_cache_floodArg) * (n))); \
	} while (0)

/**
 * Is the given reference count an isolated value?
 * 
 * @param refCount The reference count.
 * @since 2025/07/21
 */
#define sjme_nvm_cache_refCountIsIso(refCount) \
	((refCount) >= 0)

/**
 * Is the given reference count a reference?
 * 
 * @param refCount The reference count.
 * @since 2025/07/21
 */
#define sjme_nvm_cache_refCountIsRef(refCount) \
	((refCount) < 0)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_TASKCACHE_H
}
#undef SJME_CXX_TASKCACHE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_TASKCACHE_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_TASKCACHE_H */
