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

#define sjme_atomic_type sjme_jint
#include "sjme/atomicTemplate.h"

#define sjme_atomic_type sjme_juint
#include "sjme/atomicTemplate.h"

#define sjme_atomic_type sjme_lpstr
#include "sjme/atomicTemplate.h"

#define sjme_atomic_type sjme_lpcstr
#include "sjme/atomicTemplate.h"

#define sjme_atomic_type sjme_jobject
#include "sjme/atomicTemplate.h"

#define sjme_atomic_type sjme_jclass
#include "sjme/atomicTemplate.h"

#define sjme_atomic_type sjme_pointer
#include "sjme/atomicTemplate.h"

#define sjme_atomic_type sjme_intPointer
#include "sjme/atomicTemplate.h"

#define sjme_atomic_type sjme_charSeq
#include "sjme/atomicTemplate.h"

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
 * @return If the value matched then @link SJME_JNI_TRUE @endlink and the new 
 * value will be set, otherwise @link SJME_JNI_FALSE @endlink and there are 
 * no changes.
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
 * @return If the value matched then @link SJME_JNI_TRUE @endlink and the new 
 * value will be set, otherwise @link SJME_JNI_FALSE @endlink and there are 
 * no changes.
 * @since 2025/10/02
 */
#define sjme_atomic_pcs(ref, cmp, set) \
	sjme_atomic_cs(sjme_pointer, \
	(sjme_atomic(sjme_pointer)*)(ref), (cmp), (set))
	
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
