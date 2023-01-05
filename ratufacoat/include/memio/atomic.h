/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Atomics which are needed for thread safety.
 * 
 * @since 2021/03/06
 */

#ifndef SQUIRRELJME_ATOMIC_H
#define SQUIRRELJME_ATOMIC_H

#include "sjmejni/ccfeatures.h"

#if defined(SJME_FEATURE_GCC) && !defined(SJME_HAS_SPARC) && \
	!defined(SJME_FEATURE_OLD_GCC)
	#define SJME_MEMIO_ATOMIC_GCC

	#if !defined(SJME_HAS_ATOMIC)
		#define SJME_HAS_ATOMIC
	#endif
#endif

/* Which atomic set is being used? */
#if defined(__STDC_VERSION__) && __STDC_VERSION__ >= 201112L && \
	!defined(__STDC_NO_ATOMICS__)
	#include <stdatomic.h>

	#define SJME_MEMIO_ATOMIC_C11

	#if !defined(SJME_HAS_ATOMIC)
		#define SJME_HAS_ATOMIC
	#endif
#elif defined(_WIN32) || defined(__WIN32__) || \
	defined(__WIN32) || defined(_WINDOWS)
	#define SJME_MEMIO_ATOMIC_WIN32

	#if !defined(SJME_HAS_ATOMIC)
		#define SJME_HAS_ATOMIC
	#endif
#endif

/* Fallback to non-atomic. */
#if !defined(SJME_HAS_ATOMIC)
	#define SJME_MEMIO_ATOMIC_RELAXED_NOT_ATOMIC
#endif

#include "sjmerc.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_ATOMIC_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Atomic integer.
 * 
 * @since 2021/03/06
 */
typedef struct sjme_memIo_atomicInt sjme_memIo_atomicInt;

#if defined(SJME_MEMIO_ATOMIC_C11)
	struct sjme_memIo_atomicInt
	{
		/** Atomic value. */
		_Atomic sjme_jint value;
	};
#else
	struct sjme_memIo_atomicInt
	{
		/** Atomic value. */
		volatile sjme_jint value;
	};
#endif

/**
 * Atomic pointer.
 * 
 * @since 2021/10/21
 */
typedef struct sjme_memIo_atomicPointer sjme_memIo_atomicPointer;

#if defined(SJME_MEMIO_ATOMIC_C11)
	struct sjme_memIo_atomicPointer
	{
		/** Atomic value. */
		void* _Atomic value;
	};
#else
	struct sjme_memIo_atomicPointer
	{
		/** Atomic value. */
		volatile void* volatile value;
	};
#endif

/**
 * Sets the value of the given atomic provided the check value is a match,
 * it then returns the old value atomically.
 * 
 * @param atomic The atomic to check and set.
 * @param check The value to check against, which if equal will set @c set.
 * @param set The value to set if @c check is equal.
 * @return If @c check matched and the atomic is set.
 * @since 2021/03/06
 */
sjme_jboolean sjme_memIo_atomicIntCompareThenSet(sjme_memIo_atomicInt* atomic,
	sjme_jint check, sjme_jint set)
	SJME_CODE_SECTION("memio");

/**
 * Gets the value of the atomic.
 * 
 * @param atomic The atomic to read from.
 * @return The read value.
 * @since 2021/03/06
 */
sjme_jint sjme_memIo_atomicIntGet(sjme_memIo_atomicInt* atomic)
	SJME_CODE_SECTION("memio");

/**
 * Sets the given atomic value.
 * 
 * @param atomic The atomic to set. 
 * @param value The value to set.
 * @return The old value in the atomic.
 * @since 2021/03/06
 */
sjme_jint sjme_memIo_atomicIntSet(sjme_memIo_atomicInt* atomic,
	sjme_jint value)
	SJME_CODE_SECTION("memio");

/**
 * Atomically reads the value then adds into the atomic.
 * 
 * @param atomic The atomic to add to.
 * @param add The value to add.
 * @return The value before adding.
 * @since 2021/03/06
 */
sjme_jint sjme_memIo_atomicIntGetThenAdd(sjme_memIo_atomicInt* atomic,
	sjme_jint add)
	SJME_CODE_SECTION("memio");

/**
 * Sets the value of the given atomic provided the check value is a match,
 * it then returns the old value atomically.
 * 
 * @param atomic The atomic to check and set.
 * @param check The value to check against, which if equal will set @c set.
 * @param set The value to set if @c check is equal.
 * @return If @c check matched and the atomic is set.
 * @since 2021/11/11
 */
sjme_jboolean sjme_memIo_atomicPointerCompareThenSet(
	sjme_memIo_atomicPointer* atomic, void* check, void* set)
	SJME_CODE_SECTION("memio");

/**
 * Gets the value of the atomic.
 * 
 * @param atomic The atomic to read from.
 * @return The read value.
 * @since 2021/10/21
 */
void* sjme_memIo_atomicPointerGet(sjme_memIo_atomicPointer* atomic)
	SJME_CODE_SECTION("memio");

/**
 * Reads an atomic pointer with the given type.
 * 
 * @param atomic The atomic to read from.
 * @param type The type to read the value as.
 * @return The read value.
 * @since 2021/10/21
 */
#define sjme_memIo_atomicPointerGetType(atomic, type) \
	((type)(sjme_memIo_atomicPointerGet(atomic)))

/**
 * Sets the given atomic value.
 * 
 * @param atomic The atomic to set. 
 * @param value The value to set.
 * @return The former atomic value.
 * @since 2021/10/21
 */
void* sjme_memIo_atomicPointerSet(sjme_memIo_atomicPointer* atomic,
	void* value)
	SJME_CODE_SECTION("memio");

/**
 * Sets an atomic pointer with the given type.
 * 
 * @param atomic The atomic to set. 
 * @param value The value to set.
 * @param type The type to read the old value as.
 * @return The old value.
 * @since 2021/10/21
 */
#define sjme_memIo_atomicPointerSetType(atomic, value, type) \
	((type)(sjme_memIo_atomicPointerSet(atomic, value)))

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_ATOMIC_H
}
#undef SJME_CXX_SQUIRRELJME_ATOMIC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_ATOMIC_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ATOMIC_H */
