/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Atomic read/write support.
 * 
 * @file
 * @since 2024/01/08
 */

#ifndef SJME_C_ATOMIC_H
#define SJME_C_ATOMIC_H

#include "sjme/config.h"
#include "sjme/stdTypes.h"
#include "sjme/tokenUtils.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ATOMIC_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_ATOMIC_DARWIN)
	/** Emits a memory barrier. */
	#define sjme_atomic_barrier() OSMemoryBarrier()
#elif defined(SJME_CONFIG_HAS_ATOMIC_GCC) || \
	defined(SJME_CONFIG_HAS_ATOMIC_GCC_LEGACY)
	/** Emits a memory barrier. */
	#define sjme_atomic_barrier() __sync_synchronize()
#elif defined(SJME_CONFIG_HAS_ATOMIC_WIN32)
	/** Emits a memory barrier. */
	#define sjme_atomic_barrier() MemoryBarrier()
#else
	/** Emits a memory barrier. */
	#define sjme_atomic_barrier() do {} while(0)
#endif

/**
 * Determines the name of an atomic type.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/08
 */
#define SJME_ATOMIC_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_atomic_, SJME_TOKEN_PASTE_PP(type, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars)))

/**
 * Determines the name for an atomic function.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @param name The name to use.
 * @since 2024/01/08
 */
#define SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, name) \
	SJME_TOKEN_PASTE_PP(SJME_ATOMIC_NAME(type, numPointerStars), name)

/**
 * Prototype for the atomic compare and set function.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
	sjme_jboolean SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, \
		_compareSet) \
		(SJME_ATOMIC_NAME(type, numPointerStars)* atomic, \
		SJME_TOKEN_TYPE(type, numPointerStars) expected, \
		SJME_TOKEN_TYPE(type, numPointerStars) set)

/**
 * Prototype for the atomic get then add function.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars) \
	SJME_TOKEN_TYPE(type, numPointerStars) \
		SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, \
			_getAdd) \
		(SJME_ATOMIC_NAME(type, numPointerStars)* atomic, \
		SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
			SJME_TOKEN_TYPE(type, numPointerStars), intptr_t) add)

/**
 * Prototype for the atomic set function.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_ATOMIC_PROTOTYPE_SET(type, numPointerStars) \
	SJME_TOKEN_TYPE(type, numPointerStars) \
		SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, \
			_set) \
		(SJME_ATOMIC_NAME(type, numPointerStars)* atomic, \
		SJME_TOKEN_TYPE(type, numPointerStars) value)

/**
 * Prototype for the atomic get function.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_ATOMIC_PROTOTYPE_GET(type, numPointerStars) \
	SJME_TOKEN_TYPE(type, numPointerStars) \
		SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, \
			_get) \
		(SJME_ATOMIC_NAME(type, numPointerStars)* atomic)

/**
 * Common atomic function sets.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_ATOMIC_PROTOTYPE_COMMON(type, numPointerStars) \
	SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars); \
	SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars); \
	SJME_ATOMIC_PROTOTYPE_SET(type, numPointerStars); \
	SJME_ATOMIC_PROTOTYPE_GET(type, numPointerStars)

#if defined(SJME_CONFIG_HAS_ATOMIC_C11)

/**
 * Declares an atomic type.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/08
 */
#define SJME_ATOMIC_DECLARE(type, numPointerStars) \
	typedef volatile struct sjme_alignPointer \
		SJME_ATOMIC_NAME(type, numPointerStars) \
	{ \
		/** The atomic type. */ \
		SJME_TOKEN_TYPE(type, numPointerStars) \
			sjme_alignPointer _Atomic value; \
	} SJME_ATOMIC_NAME(type, numPointerStars); \
	SJME_ATOMIC_PROTOTYPE_COMMON(type, numPointerStars)

#elif defined(SJME_CONFIG_HAS_ATOMIC_DARWIN) || \
	defined(SJME_CONFIG_HAS_ATOMIC_WIN32) || \
	defined(SJME_CONFIG_HAS_ATOMIC_GCC) || \
	defined(SJME_CONFIG_HAS_ATOMIC_GCC_LEGACY) || \
	defined(SJME_CONFIG_HAS_ATOMIC_VOLATILE)

/**
 * Declares an atomic type.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/08
 */
#define SJME_ATOMIC_DECLARE(type, numPointerStars) \
	typedef volatile sjme_attrOrder(struct, sjme_alignPointer) \
		SJME_ATOMIC_NAME(type, numPointerStars) \
	{ \
		/** The atomic value. */ \
		SJME_TOKEN_TYPE(type, numPointerStars) volatile value; \
	} SJME_ATOMIC_NAME(type, numPointerStars); \
	typedef SJME_ATOMIC_NAME(type, numPointerStars) \
		SJME_TOKEN_PASTE_PP(SJME_ATOMIC_NAME(type, numPointerStars), \
		_phantom); \
	typedef SJME_ATOMIC_NAME(type, numPointerStars) \
		SJME_TOKEN_PASTE_PP(SJME_ATOMIC_NAME(type, numPointerStars), \
		_nonCyclic); \
	SJME_ATOMIC_PROTOTYPE_COMMON(type, numPointerStars)

#else

#error Atomic definitions missing.

#endif

/** Atomic @link sjme_jint. */
SJME_ATOMIC_DECLARE(sjme_jint, 0);

/** Atomic @link sjme_juint. */
SJME_ATOMIC_DECLARE(sjme_juint, 0);

/** Atomic @link sjme_lpstr. */
SJME_ATOMIC_DECLARE(sjme_lpstr, 0);

/** Atomic @link sjme_lpcstr. */
SJME_ATOMIC_DECLARE(sjme_lpcstr, 0);

/** Atomic @link sjme_pointer. */
SJME_ATOMIC_DECLARE(sjme_pointer, 0);

/** Atomic pointer declaration. */
SJME_ATOMIC_DECLARE(sjme_intPointer , 0);

/** Atomic @link sjme_jobject. */
SJME_ATOMIC_DECLARE(sjme_jobject, 0);

/** Atomic @link sjme_jclass. */
SJME_ATOMIC_DECLARE(sjme_jclass, 0);

/** Atomic @link sjme_charSeq. */
SJME_ATOMIC_DECLARE(sjme_charSeq, 0);

/**
 * Atomic reference.
 *
 * @since 2025/10/11
 */
#define sjme_atomic(type) \
	SJME_TOKEN_PASTE_PP(sjme_atomic_, type)

/**
 * Phantom atomic reference.
 *
 * @param type The type to reference.
 * @since 2025/10/11
 */
#define sjme_phantom(type) \
	SJME_TOKEN_PASTE3_PP(sjme_atomic_, type, _phantom)

/**
 * Phantom pointer reference.
 * 
 * @param type The type to reference.
 * @param numPointerStars The number of pointer stars.
 * @since 2025/10/17
 */
#define sjme_phantomP(type, numPointerStars) \
	SJME_TOKEN_PASTE4_PP(sjme_atomic_, type, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars), _phantom)

/**
 * Non-cyclic atomic reference.
 *
 * @param type The type to reference.
 * @since 2025/10/27
 */
#define sjme_nonCyclic(type) \
	SJME_TOKEN_PASTE3_PP(sjme_atomic_, type, _nonCyclic)

/**
 * Non-cyclic pointer reference.
 * 
 * @param type The type to reference.
 * @param numPointerStars The number of pointer stars.
 * @since 2025/10/27
 */
#define sjme_nonCyclicP(type, numPointerStars) \
	SJME_TOKEN_PASTE4_PP(sjme_atomic_, type, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars), _nonCyclic)
	

/**
 * Chain gets from two atomic values which are in a structure with each
 * other.
 *
 * Equivalent to @code refTop->structIsh @endcode .
 * 
 * @param type The atomic type.
 * @param refTop The top reference.
 * @param structIsh The sub-structure member reference.
 * @return The resultant sub-structure member value.
 * @since 2025/10/02
 */
#define sjme_atomic_chainGetGet(type, refTop, structIsh) \
	sjme_atomic_g(type, (&sjme_atomic_g(type, (refTop)) structIsh))

/**
 * Chain gets from a sub-structure of an atomic, which is also an atomic,
 * then sets the atomic value.
 *
 * Equivalent to @code refTop->structIsh = set @endcode .
 * 
 * @param type The atomic type.
 * @param refTop The top reference.
 * @param structIsh The sub-structure member reference.
 * @param set The value to set.
 * @return The old value of the sub-structure member value.
 * @since 2025/10/02
 */
#define sjme_atomic_chainGetSet(type, refTop, structIsh, set) \
	sjme_atomic_s(type, (&sjme_atomic_g(type, (refTop)) structIsh), set)

/**
 * Copies the value of one atomic to another.
 * 
 * @param type The type of the atomic.
 * @param dst The destination atomic.
 * @param src The source atomic.
 * @return The old value.
 * @since 2025/10/02
 */
#define sjme_atomic_copy(type, dst, src) \
	sjme_atomic_s(type, dst, sjme_atomic_g(type, src))
	
/**
 * Compares the atomic to the given value then sets it if it is the given
 * value.
 * 
 * @param type The atomic type.
 * @param ref The atomic reference.
 * @param cmp The value to compare against.
 * @param set The value to set, if the existing value is the given value.
 * @return If the value matched then @link SJME_JNI_TRUE @endlink and the new value will
 * be set, otherwise @link SJME_JNI_FALSE @endlink and there are no changes.
 * @since 2025/10/02
 */
#define sjme_atomic_cs(type, ref, cmp, set) \
	SJME_TOKEN_PASTE3_PP(sjme_atomic_, type, _compareSet)((ref), (cmp), (set))

/**
 * Gets the atomic value.
 * 
 * @param type The atomic type.
 * @param ref The atomic reference.
 * @return The current value of the atomic.
 * @since 2025/10/02
 */
#define sjme_atomic_g(type, ref) \
	SJME_TOKEN_PASTE3_PP(sjme_atomic_, type, _get)((ref))

/**
 * Gets the atomic value.
 * 
 * @param type The atomic type.
 * @param numPointerStars The number of pointer stars.
 * @param ref The atomic reference.
 * @return The current value of the atomic.
 * @since 2025/10/17
 */
#define sjme_atomic_gP(type, numPointerStars, ref) \
	SJME_TOKEN_PASTE4_PP(sjme_atomic_, type, \
	SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars), _get)((ref))

/**
 * Gets the value of the atomic then adds to it. This is only valid for
 * non-pointer types.
 * 
 * @param type The atomic type.
 * @param ref The atomic reference.
 * @param add The value to add to the given type.
 * @return The previous value before the add.
 * @since 2025/10/02
 */
#define sjme_atomic_ga(type, ref, add) \
	SJME_TOKEN_PASTE3_PP(sjme_atomic_, type, _getAdd)((ref), (add))

/**
 * Compares the pointer atomic, without regards to type, to the given value
 * then sets it if it is the given value.
 * 
 * @param ref The atomic pointer reference.
 * @param cmp The value to compare against.
 * @param set The value to set, if the existing value is the given value.
 * @return If the value matched then @link SJME_JNI_TRUE @endlink and the new value will
 * be set, otherwise @link SJME_JNI_FALSE @endlink and there are no changes.
 * @since 2025/10/02
 */
#define sjme_atomic_pcs(ref, cmp, set) \
	sjme_atomic_cs(sjme_pointer, (sjme_atomic(sjme_pointer)*)(ref), (cmp), (set))
	
/**
 * Returns the pointer value of an atomic, disregarding type. This is mostly
 * a utility macro when a type is not needed.
 * 
 * @param ref The atomic reference.
 * @return The pointer value.
 * @since 2025/10/02
 */
#define sjme_atomic_pg(ref) \
	sjme_atomic_g(sjme_pointer, (sjme_atomic(sjme_pointer)*)(ref))

/**
 * Directly set pointer atomic to @c NULL .
 * 
 * @param ref The reference to set to NULL.
 * @return The old value.
 * @since 2025/10/02
 */
#define sjme_atomic_psNull(ref) \
	sjme_atomic_s(sjme_pointer, (sjme_atomic(sjme_pointer)*)(ref), NULL)
	
/**
 * Sets the new value of the atomic and returns the old value.
 * 
 * @param type The atomic type.
 * @param ref The atomic reference.
 * @param set The new value to set.
 * @return The old value of the atomic.
 * @since 2025/10/02
 */
#define sjme_atomic_s(type, ref, set) \
	SJME_TOKEN_PASTE3_PP(sjme_atomic_, type, _set)((ref), (set))
	
/**
 * Sets the new value of the atomic and returns the old value.
 * 
 * @param type The atomic type.
 * @param numPointerStars The number of pointer stars.
 * @param ref The atomic reference.
 * @param set The new value to set.
 * @return The old value of the atomic.
 * @since 2025/10/17
 */
#define sjme_atomic_sP(type, numPointerStars, ref, set) \
	SJME_TOKEN_PASTE4_PP(sjme_atomic_, type, \
	SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars), _set)((ref), (set))

#if defined(SJME_CONFIG_HAS_ATOMIC_VOLATILE)

/**
 * Disable interrupts.
 * 
 * @since 2024/07/07
 */
void sjme_atomic_interruptsDisable(void);

/**
 * Enable interrupts.
 * 
 * @since 2024/07/07
 */
void sjme_atomic_interruptsEnable(void);

#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ATOMIC_H
}
		#undef SJME_CXX_SQUIRRELJME_ATOMIC_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ATOMIC_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ATOMIC_H */
