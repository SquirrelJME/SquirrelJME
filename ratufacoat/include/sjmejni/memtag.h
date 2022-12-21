/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Tagged memory management.
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

/**
 * Represents the type of tag that is used for memory.
 *
 * @since 2022/12/20
 */
typedef enum sjme_memTagType
{
	/** Static memory, never dynamically free. */
	SJME_MEM_TAG_STATIC,
} sjme_memTagType;

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

sjme_jboolean sjme_memNew(sjme_memTagType tagType, void** tag);

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
