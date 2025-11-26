/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Atomic implementation.
 * 
 * @file
 * @since 2025/10/02
 */

#ifndef SJME_C_SQUIRRELJME_ATOMICIMPL_H
#define SJME_C_SQUIRRELJME_ATOMICIMPL_H

#include "sjme/atomic.h"

#if defined(SJME_CONFIG_HAS_ATOMIC_DARWIN)
	#include <libkern/OSAtomic.h>
#elif defined(SJME_CONFIG_HAS_ATOMIC_WIN32)
	#define WIN32_LEAN_AND_MEAN 1
	
	#include <windows.h>

	#undef WIN32_LEAN_AND_MEAN
#endif

#include "sjme/debug.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_ATOMICIMPL_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */

#if defined(SJME_CONFIG_HAS_ATOMIC_DARWIN)

	#define SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
		{ \
			if (OSAtomicCompareAndSwap32Barrier( \
				(sjme_jint)expected, \
				(sjme_jint)set, \
				(sjme_jint*)&atomic->value)) \
				return SJME_JNI_TRUE; \
			return SJME_JNI_FALSE; \
		}

	#define SJME_ATOMIC_FUNCTION_GET_ADD(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars) \
		{ \
			/* Returns the new value, so it must be reversed! */\
			return (SJME_TOKEN_TYPE(type, numPointerStars))\
				(OSAtomicAdd32Barrier((sjme_jint)add, \
				(sjme_jint*)&atomic->value) - (sjme_jint)add); \
		}

	#define SJME_ATOMIC_FUNCTION_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_SET(type, numPointerStars) \
		{ \
			/* There is no simple set, so sadly this is two */ \
			/* atomic operations. */ \
			sjme_jint temp; \
			OSMemoryBarrier(); \
			temp = OSAtomicXor32((sjme_jint)value, \
				(sjme_jint*)&atomic->value); \
			OSAtomicXor32Barrier((((sjme_jint)temp) ^ ((sjme_jint)value)), \
				(sjme_jint*)&atomic->value); \
			OSMemoryBarrier(); \
			return (SJME_TOKEN_TYPE(type, numPointerStars))\
				(((sjme_jint)temp) ^ ((sjme_jint)value)); \
		}

#elif defined(SJME_CONFIG_HAS_ATOMIC_GCC)

	/** Memory order used for GCC. */
	#define SJME_ATOMIC_GCC_MEMORY_ORDER __ATOMIC_SEQ_CST

	#define SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
		{ \
			if (__atomic_compare_exchange_n( \
					&atomic->value, \
					&expected, \
					set, 0, SJME_ATOMIC_GCC_MEMORY_ORDER, \
						SJME_ATOMIC_GCC_MEMORY_ORDER)) \
				return SJME_JNI_TRUE; \
			return SJME_JNI_FALSE; \
		}

	#define SJME_ATOMIC_FUNCTION_GET_ADD(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars) \
		{ \
			if (add == 0) \
				return __atomic_load_n(&atomic->value, \
					SJME_ATOMIC_GCC_MEMORY_ORDER); \
			return \
				__atomic_fetch_add(&atomic->value, \
				add, \
				SJME_ATOMIC_GCC_MEMORY_ORDER); \
		}

	#define SJME_ATOMIC_FUNCTION_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_SET(type, numPointerStars) \
		{ \
			return __atomic_exchange_n(&atomic->value, \
				(SJME_TOKEN_TYPE(type, numPointerStars))value, \
				SJME_ATOMIC_GCC_MEMORY_ORDER); \
		}

#elif defined(SJME_CONFIG_HAS_ATOMIC_GCC_LEGACY)
	
	#define SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
		{ \
			SJME_TOKEN_TYPE(type, numPointerStars) was; \
			 \
			was = __sync_val_compare_and_swap( \
				SJME_TYPEOF_IF_POINTER(type, numPointerStars, \
					(volatile sjme_pointer*))&atomic->value, \
				expected, set); \
			if (was == expected) \
				return SJME_JNI_TRUE; \
			return SJME_JNI_FALSE; \
		}

	#define SJME_ATOMIC_FUNCTION_GET_ADD(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars) \
		{ \
			return __sync_fetch_and_add( \
				SJME_TYPEOF_IF_POINTER(type, numPointerStars, \
					(volatile sjme_pointer*))&atomic->value, \
				add); \
		}

	#define SJME_ATOMIC_FUNCTION_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_SET(type, numPointerStars) \
		{ \
			return __sync_lock_test_and_set( \
				SJME_TYPEOF_IF_POINTER(type, numPointerStars, \
					(volatile sjme_pointer*))&atomic->value, \
				(SJME_TOKEN_TYPE(type, numPointerStars))value); \
		}

#elif defined(SJME_CONFIG_HAS_ATOMIC_WIN32)

	#if SJME_CONFIG_HAS_POINTER == 64
		#define SJME_ATOMIC_WIN32_IA(type, numPointerStars) \
			SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
				InterlockedExchangeAdd, InterlockedExchangeAdd64)

		#define SJME_ATOMIC_WIN32_S(type, numPointerStars) \
			SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
				InterlockedExchange, InterlockedExchange64)
		
		#define SJME_ATOMIC_WIN32_X(type, numPointerStars) \
			SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
				InterlockedCompareExchange, InterlockedCompareExchange64)

		#define SJME_ATOMIC_WIN32_POINTER LONG64
	#else
		#define SJME_ATOMIC_WIN32_IA(type, numPointerStars) \
			InterlockedExchangeAdd

		#define SJME_ATOMIC_WIN32_S(type, numPointerStars) \
			InterlockedExchange

		#define SJME_ATOMIC_WIN32_X(type, numPointerStars) \
        	InterlockedCompareExchange

		#define SJME_ATOMIC_WIN32_POINTER LONG
	#endif

	/** The value type. */
	#define SJME_ATOMIC_WIN32_TYPE(type, numPointerStars) \
		SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
			LONG, SJME_ATOMIC_WIN32_POINTER)

	/** The value type for getAdd. */
	#define SJME_ATOMIC_WIN32_TYPEGA(type, numPointerStars) \
		SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
			LONG, SJME_ATOMIC_WIN32_POINTER)
	
	#define SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
		{ \
			SJME_ATOMIC_WIN32_TYPE(type, numPointerStars) was; \
			\
			/* Returns the value that was stored here. */ \
			was = SJME_ATOMIC_WIN32_X(type, numPointerStars)((volatile \
				SJME_ATOMIC_WIN32_TYPE(type, numPointerStars)*) \
				&atomic->value, \
				(SJME_ATOMIC_WIN32_TYPE(type, numPointerStars))set, \
				(SJME_ATOMIC_WIN32_TYPE(type, numPointerStars))expected); \
			\
			if ((SJME_TOKEN_TYPE(type, numPointerStars))was == \
				(SJME_TOKEN_TYPE(type, numPointerStars))expected) \
				return SJME_JNI_TRUE; \
			return SJME_JNI_FALSE; \
		}

	#define SJME_ATOMIC_FUNCTION_GET_ADD(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars) \
		{ \
			return (SJME_TOKEN_TYPE(type, numPointerStars)) \
				SJME_ATOMIC_WIN32_IA(type, numPointerStars) \
				((volatile \
				SJME_ATOMIC_WIN32_TYPEGA(type, \
					numPointerStars)*)&atomic->value, add); \
		}

	#define SJME_ATOMIC_FUNCTION_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_SET(type, numPointerStars) \
		{ \
			return (SJME_TOKEN_TYPE(type, numPointerStars)) \
				SJME_ATOMIC_WIN32_S(type, numPointerStars)( \
				(volatile SJME_ATOMIC_WIN32_TYPEGA(type, numPointerStars)*) \
					&atomic->value, \
					(SJME_ATOMIC_WIN32_TYPE(type, numPointerStars))value); \
		}

#elif defined(SJME_CONFIG_HAS_ATOMIC_VOLATILE)

	#define SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
		{ \
			sjme_jboolean result; \
			 \
			sjme_atomic_interruptsDisable(); \
			 \
			result = (atomic->value == expected); \
			if (result) \
				atomic->value = set; \
			 \
			sjme_atomic_interruptsEnable(); \
			 \
			return result; \
		}

	#define SJME_ATOMIC_FUNCTION_GET_ADD(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars) \
		{ \
			type result; \
			 \
			sjme_atomic_interruptsDisable(); \
			 \
			result = atomic->value; \
			atomic->value = (type)(((intptr_t)result) + add); \
			 \
			sjme_atomic_interruptsEnable(); \
			 \
			return result; \
		}

	#define SJME_ATOMIC_FUNCTION_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_SET(type, numPointerStars) \
		{ \
			type result; \
			 \
			sjme_atomic_interruptsDisable(); \
			 \
			result = atomic->value; \
			atomic->value = value; \
			 \
			sjme_atomic_interruptsEnable(); \
			 \
			return result; \
		}

#else

#error No atomic access functions.

#endif

#include "sjme/multithread.h"

#define SJME_ATOMIC_FUNCTION_GET(type, numPointerStars) \
	SJME_ATOMIC_PROTOTYPE_GET(type, numPointerStars) \
	{ \
		return SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, _getAdd) \
			(atomic, 0); \
	}

/* ------------------------------------------------------------------------ */
/* clang-format on */ /* @formatter:on */

/**
 * Common atomic function sets.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @param hasP Does this have a pointer?
 * @since 2024/01/09
 */
#define SJME_ATOMIC_FUNCTION(type, numPointerStars) \
	SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars) \
	SJME_ATOMIC_FUNCTION_GET_ADD(type, numPointerStars) \
	SJME_ATOMIC_FUNCTION_SET(type, numPointerStars) \
	SJME_ATOMIC_FUNCTION_GET(type, numPointerStars)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_ATOMICIMPL_H
}
#undef SJME_CXX_SQUIRRELJME_ATOMICIMPL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_ATOMICIMPL_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_ATOMICIMPL_H */
