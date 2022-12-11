/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Reference pointer implementation, using compiler specific magic to implement
 * this with some required words.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_REFPTR_H
#define SQUIRRELJME_REFPTR_H

#include "sjmerc.h"
#include "memory.h"
#include "memoryintern.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_REFPTR_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Ref pointer detection key. */
#define SJME_REF_DETECT__ (UINTPTR_MAX ^ UINT32_C(0xCAFEBABE))

/**
 * Performs reference cleanup.
 *
 * @param refStruct The reference struct to cleanup.
 * @param error Any error state, if any.
 * @return Any potential error state.
 * @since 2022/12/11
 */
sjme_jboolean sjme_refPtrCleanup__(void* refStruct, sjme_error* error);

/* Clang format gets messy here... */
/* clang-format off */

#if defined(_MSC_VER)
	/** Make type compatible with reference pointers. */
	#define SJME_REFPTR_MAKE(type) \
		typedef __pragma(pack(push, 1)) struct sjme_refptr__##type \
		{ \
            sjme_refPtr_ownerList__* owners; \
			type* ptr; \
		} __pragma(pack(pop, 1)) sjme_refptr__##type

	/** Start of function for reference pointers */
	#define SJME_REFPTR_CODE_BEGIN() \
		__try {

	/** End of function for reference pointers. */
	#define SJME_REFPTR_CODE_END(error) \
		} __finally { sjme_refPtrCleanup__(&refs, error); }

	/** Start of variables. */
	#define SJME_REFPTR_VAR_BEGIN() \
		__pragma(pack(push, 1)) struct {

	/** Define variable. */
	#define SJME_REFPTR_VAR(type, name) \
		sjme_refptr__##type name /* NOLINT */

	/** End of variables. */
	#define SJME_REFPTR_VAR_END() \
          uintptr_t detect__; \
		} __pragma(pack(pop, 1)) refs; \
        /* For end of refs detection. */ \
        refs.detect__ = SJME_REF_DETECT__; \
        /* MSVC requires the memory here be initialized properly... */ \
		memset(&refs, 0, sizeof(refs))

	/** Get reference variable. */
	#define SJME_UNREF(x) (*(refs.x.ptr))
#else
	/* Not supported so needs to be added. */
	#error No RefPtr Implementation!
#endif

/* Start using Clang format again */
/* clang-format on */

/** Reference pointer for @c sjme_jboolean. */
SJME_REFPTR_MAKE(sjme_jboolean);

/** Reference pointer for @c sjme_jbyte. */
SJME_REFPTR_MAKE(sjme_jbyte);

/** Reference pointer for @c sjme_jubyte. */
SJME_REFPTR_MAKE(sjme_jubyte);

/** Reference pointer for @c sjme_jshort. */
SJME_REFPTR_MAKE(sjme_jshort);

/** Reference pointer for @c sjme_jushort. */
SJME_REFPTR_MAKE(sjme_jushort);

/** Reference pointer for @c sjme_jint. */
SJME_REFPTR_MAKE(sjme_jint);

/** Reference pointer for @c sjme_juint. */
SJME_REFPTR_MAKE(sjme_juint);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_REFPTR_H
}
		#undef SJME_CXX_SQUIRRELJME_REFPTR_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_REFPTR_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_REFPTR_H */
