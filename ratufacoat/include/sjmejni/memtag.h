/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Tagged memory management, used for garbage collection and otherwise.
 * 
 * @since 2022/12/20
 */

#ifndef SQUIRRELJME_MEMTAG_H
#define SQUIRRELJME_MEMTAG_H

#include "sjmejni/sjmejni.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MEMTAG_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Protector value for using @c sjme_memTaggedNewSizeOf and not @c sizeof. */
#define SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT INT32_C(0x80000000)

/** Declares a tagged reference. */
#define SJME_DECL_TAGGED(x) typedef x* x##_tagged /* NOLINT */

/** Aliased tagged type, one that is always treated as tagged. */
#define SJME_DECL_TAGGED_ALIAS(x) typedef x x##_tagged /* NOLINT */

/** Utilizes a tagged type. */
#define SJME_TAGGED(x) x##_tagged

/** Tagged boolean. */
SJME_DECL_TAGGED(sjme_jboolean);

/** Tagged byte. */
SJME_DECL_TAGGED(sjme_jbyte);

/** Tagged character. */
SJME_DECL_TAGGED(sjme_jchar);

/** Tagged short. */
SJME_DECL_TAGGED(sjme_jshort);

/** Tagged integer. */
SJME_DECL_TAGGED(sjme_jint);

/** Tagged long. */
SJME_DECL_TAGGED(sjme_jlong);

/** Tagged float. */
SJME_DECL_TAGGED(sjme_jfloat);

/** Tagged double. */
SJME_DECL_TAGGED(sjme_jdouble);

/** Object type. */
SJME_DECL_TAGGED_ALIAS(sjme_jobject);

/** Class type. */
SJME_DECL_TAGGED_ALIAS(sjme_jclass);

/** String type. */
SJME_DECL_TAGGED_ALIAS(sjme_jstring);

/** Tagged Throwable. */
SJME_DECL_TAGGED_ALIAS(sjme_jthrowable);

/** Tagged Weak reference. */
SJME_DECL_TAGGED_ALIAS(sjme_jweakReference);

/** Tagged Array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jarray);

/** Tagged Boolean array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jbooleanArray);

/** Tagged Byte array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jbyteArray);

/** Tagged Character array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jcharArray);

/** Tagged Short array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jshortArray);

/** Tagged Integer array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jintArray);

/** Tagged Long array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jlongArray);

/** Tagged Float array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jfloatArray);

/** Tagged Double array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jdoubleArray);

/** Tagged Object array type. */
SJME_DECL_TAGGED_ALIAS(sjme_jobjectArray);

/**
 * Represents a memory tag group which is utilized by virtual machines to
 * collect all of the various pointers and also perform garbage collection
 * as needed.
 *
 * @since 2022/12/20
 */
typedef struct sjme_memTagGroup sjme_memTagGroup;

/**
 * Represents the type of tag that is used for memory.
 *
 * @since 2022/12/20
 */
typedef enum sjme_memTagType
{
	/** Static memory, never dynamically free. */
	SJME_MEM_TAG_STATIC,

	/** The number of available tags. */
	SJME_NUM_MEM_TAG_TYPES
} sjme_memTagType;

/**
 * Allocates memory directly, not using the tagging system.
 *
 * @param outPtr The output pointer.
 * @param size The size to allocate.
 * @param error The error if allocation failed.
 * @return If the allocation succeeded or not.
 * @since 2022/12/20
 */
sjme_jboolean sjme_memDirectNew(void** outPtr, sjme_jsize size,
	sjme_error* error);

/**
 * Allocates a group which contains tagged pointers which are used for
 * collection multiple allocations together within a single virtual machine
 * instance.
 *
 * @param outPtr The output for the group.
 * @param error Any resultant error state.
 * @return If the group was successfully allocated.
 * @since 2022/12/20
 */
sjme_jboolean sjme_memTaggedGroupNew(sjme_memTagGroup** outPtr,
	sjme_error* error);

/**
 * Frees a memory group and all of the memory that was previously allocated.
 *
 * @param inPtr The input group.
 * @param error Any resultant error state.
 * @return If the group was successfully freed.
 * @since 2022/12/20
 */
sjme_jboolean sjme_memTaggedGroupFree(sjme_memTagGroup** inPtr,
	sjme_error* error);

/**
 * Allocates tagged memory.
 *
 * @param outPtr The output pointer, should be a tagged pointer.
 * @param size The size of the data to allocate,
 * use @c sjme_memTaggedNewSizeOf().
 * @param tagType The type of tag used, represents how the value is to be
 * cached or otherwise.
 * @param error The resultant error if allocation failed.
 * @param protectA Should be @c sizeof(void*).
 * @param protectB Should be @c sizeof(void*).
 * @return If allocation was successful or not.
 * @since 2022/12/20
 */
sjme_jboolean sjme_memTaggedNewZ(sjme_memTagGroup* group, void*** outPtr,
	sjme_jsize size, sjme_memTagType tagType, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB);

/**
 * Macro to ensure that for tagged types, @c sizeof() is not used.
 *
 * @param ref The reference to get the size of.
 * @since 2022/12/20
 */
#define sjme_memTaggedNewSizeOf(ref) \
	(((sjme_jsize)sizeof(**(ref))) ^ SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT)

/**
 * Allocates tagged memory.
 *
 * @param group The group the tag belongs to.
 * @param outPtr The output pointer, should be a tagged pointer.
 * @param size The size of the data to allocate,
 * use @c sjme_memTaggedNewSizeOf().
 * @param tagType The type of tag used, represents how the value is to be
 * cached or otherwise.
 * @param error The resultant error if allocation failed.
 * @return If allocation was successful or not.
 * @since 2022/12/20
 */
#define sjme_memTaggedNew(group, outPtr, size, tagType, error) \
	sjme_memTaggedNewZ((group), (void***)(outPtr), (size), (tagType), \
		(error), \
		sizeof(*(outPtr)), \
		sizeof(**(outPtr))) /* NOLINT(bugprone-sizeof-expression) */

/**
 * Frees the memory tag and its resultant indirection is cleared as well.
 *
 * @param inPtr The input pointer to be freed.
 * @param error Any resultant error state.
 * @param protectA Should be @c sizeof(void*).
 * @param protectB Should be @c sizeof(void*).
 * @return If freeing the tagged pointer was a success.
 * @since 2022/12/20
 */
sjme_jboolean sjme_memTaggedFreeZ(void*** inPtr, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB);

/**
 * Frees the memory tag and its resultant indirection is cleared as well.
 *
 * @param inPtr The input pointer to be freed.
 * @param error Any resultant error state.
 * @return If freeing the tagged pointer was a success.
 * @since 2022/12/20
 */
#define sjme_memTaggedFree(inPtr, error) \
	sjme_memTaggedFreeZ((void***)inPtr, error, \
		sizeof(*(inPtr)), \
		sizeof(**(inPtr))) /* NOLINT(bugprone-sizeof-expression) */

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MEMTAG_H
}
		#undef SJME_CXX_SQUIRRELJME_MEMTAG_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMTAG_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMTAG_H */
