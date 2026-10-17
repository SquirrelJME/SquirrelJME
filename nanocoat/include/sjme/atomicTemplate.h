/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Atomic template, do not include this directly unless you are wishing to
 * declare an atomic type.
 * 
 * @since 2026/10/15
 */

/* ReSharper disable once CppMissingIncludeGuard */
/*--------------------------------------------------------------------------*/

/* Only declare templates once, if needed. */
#ifndef SJME_C_ATOMIC_TEMPLATE_H_DECL
#define SJME_C_ATOMIC_TEMPLATE_H_DECL

/* These are definitely needed. */
#include "sjme/tokenUtils.h"

#endif /* SJME_C_ATOMIC_TEMPLATE_H_DECL */

/* This is required. */
#if !defined(sjme_atomic_type)
	#error sjme_atomic_type undefined
#endif

/* If no refs, assume not a pointer type. */
#if !defined(sjme_atomic_refs)
	#define sjme_atomic_refs 0
#endif

/* Is this a pointer? */
#if sjme_atomic_refs > 0 || \
	SJME_TYPEOF_IS_POINTER(sjme_atomic_type, sjme_atomic_refs) || \
	SJME_TYPEOF_BASIC(sjme_atomic_type) == SJME_TYPEOF_BASIC_sjme_jobject
	#define sjme_atomic_isPointer 1
#endif

/* How wide is this type? */
#if sjme_atomic_isPointer == 1 || \
	SJME_TYPEOF_BASIC(sjme_atomic_type) == SJME_TYPEOF_BASIC_sjme_jobject
	#define sjme_atomic_typeWidth SJME_POINTER_BYTES
#elif SJME_TYPEOF_BASIC(sjme_atomic_type) == SJME_TYPEOF_BASIC_sjme_jint
	#define sjme_atomic_typeWidth 4
#elif SJME_TYPEOF_BASIC(sjme_atomic_type) == SJME_TYPEOF_BASIC_sjme_jlong
	#define sjme_atomic_typeWidth 8
#else
	#error Unknown or unsupported atomic type width. 
#endif

#if sjme_atomic_typeWidth == 8
	#define sjme_atomic_storageType sjme_jlongNative
#elif sjme_atomic_typeWidth == 4
	#define sjme_atomic_storageType sjme_jintNative
#else
	#error Unknown or unsupported atomic type width. 
#endif

/** Pointer stars represented with @code P @endcode characters. */
#define sjme_atomic_starsP \
	SJME_TOKEN_PASTE_PP(SJME_TOKEN_STARS_C, sjme_atomic_refs)

/** Pointer stars with actual stars. */
#define sjme_atomic_starsS \
	SJME_TOKEN_PASTE_PP(SJME_TOKEN_STARS_, sjme_atomic_refs)

/** The number of pointer stars used for dereferenced values. */
#define sjme_atomic_derefStars \
	SJME_TOKEN_PASTE_PP(SJME_TOKEN_STARS_M1_, sjme_atomic_refs)

/** The atomic subtype name. */
#define sjme_atomic_subName \
	SJME_TOKEN_PASTE_PP(sjme_atomic_type, sjme_atomic_starsP)

/** The full atomic type name. */
#define sjme_atomic_typeName \
	SJME_TOKEN_PASTE_PP(sjme_atomic_, sjme_atomic_subName)

/** The full atomic value type. */
#define sjme_atomic_valueName \
	sjme_atomic_type sjme_atomic_starsS

/* ReSharper disable once CppMissingIncludeGuard */
/*--------------------------------------------------------------------------*/

/* These are only important in headers. */
#if !defined(sjme_atomic_sourceImpl) && sjme_atomic_sourceImpl == 0

/**
 * Atomic value storage.
 * 
 * @since 2026/10/15
 */
typedef sjme_attrVolatileStruct struct sjme_alignPointerL sjme_atomic_typeName
{
#if defined(SJME_CONFIG_HAS_ATOMIC_C11)
	/** Atomic value. */
	sjme_alignPointerL sjme_atomic_valueName _Atomic value sjme_alignPointerR;
#else
#if sjme_atomic_refs > 0
	/** Atomic value. */
	sjme_alignPointerL sjme_atomic_valueName volatile value sjme_alignPointerR;
#else
	/** Atomic value. */
	sjme_alignPointerL volatile sjme_atomic_valueName value sjme_alignPointerR;
#endif
#endif
} sjme_atomic_typeName sjme_alignPointerR;

#if sjme_atomic_isPointer == 1
	/** Non-cyclic type. */
	typedef sjme_atomic_typeName \
		SJME_TOKEN_PASTE_PP(sjme_atomic_typeName, _nonCyclic);

	/** Phantom type. */
	typedef sjme_atomic_typeName \
		SJME_TOKEN_PASTE_PP(sjme_atomic_typeName, _phantom);
#endif

/**
 * Compares the atomic with the @code expected @endcode value and if it is
 * said value, it will atomically be set to @code set @endcode.
 * 
 * @param atomic The atomic reference.
 * @param expected The expected value.
 * @param set The value to set.
 * @return If the value changed.
 * @since 2026/10/15
 */
sjme_jboolean SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _compareSet)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName expected,
	sjme_atomic_valueName set);

/**
 * Atomically adds the given value to the given atomic.
 * 
 * @param atomic The atomic reference.
 * @param add The value to add.
 * @return The previous value.
 * @since 2026/10/15
 */
sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _getAdd)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName add);

/**
 * Unconditionally sets the atomic to the given value.
 * 
 * @param atomic The atomic reference.
 * @param value The new value to set.
 * @return The previous value.
 * @since 2026/10/15
 */
sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _set)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName value);

/**
 * Atomically reads the value.
 * 
 * @param atomic The atomic reference.
 * @return The value.
 * @since 2026/10/15
 */
sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _get)(
	sjme_atomic_typeName* atomic);

#endif

/* ReSharper disable once CppMissingIncludeGuard */
/*--------------------------------------------------------------------------*/

/* Clear definitions for next include, if not source impl. */
#if !defined(sjme_atomic_sourceImpl) && sjme_atomic_sourceImpl == 0
	#undef sjme_atomic_type
	#undef sjme_atomic_refs
	#undef sjme_atomic_starsP
	#undef sjme_atomic_starsS
	#undef sjme_atomic_name
	#undef sjme_atomic_derefStars
	#undef sjme_atomic_subName
	#undef sjme_atomic_typeName
	#undef sjme_atomic_valueName
	#undef sjme_atomic_typeWidth
	#undef sjme_atomic_storageType

	#if defined(sjme_atomic_isPointer)
		#undef sjme_atomic_isPointer
	#endif
#endif
