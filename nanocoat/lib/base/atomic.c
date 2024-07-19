/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/atomic.h"

#if defined(SJME_CONFIG_HAS_ATOMIC_WIN32)
	#define WIN32_LEAN_AND_MEAN 1
	
	#include <windows.h>

	#undef WIN32_LEAN_AND_MEAN
#endif

#if defined(SJME_CONFIG_HAS_ATOMIC_GCC)

	/** Memory order used for GCC. */
	#define SJME_ATOMIC_GCC_MEMORY_ORDER __ATOMIC_SEQ_CST

	#define SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars) \
		SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
		{ \
			if (__atomic_compare_exchange_n( \
					SJME_TYPEOF_IF_POINTER(type, numPointerStars, \
						(volatile sjme_pointer*))&atomic->value, \
					SJME_TYPEOF_IF_POINTER(type, numPointerStars, \
						(volatile sjme_pointer*)) &expected, \
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

#elif defined(SJME_CONFIG_HAS_ATOMIC_WIN32)

	/** The value type. */
	#define SJME_ATOMIC_WIN32_TYPE(type, numPointerStars) \
		SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, LONG, LONG64)
		
	/** The value type for getAdd. */
	#define SJME_ATOMIC_WIN32_TYPEGA(type, numPointerStars) \
		SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, LONG, LONG64)

	#if SJME_CONFIG_HAS_POINTER == 64
		#define SJME_ATOMIC_WIN32_IA(type, numPointerStars) \
			SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
				InterlockedAdd, InterlockedAdd64)

		#define SJME_ATOMIC_WIN32_S(type, numPointerStars) \
			SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
				InterlockedExchange, InterlockedExchange64)
		
		#define SJME_ATOMIC_WIN32_X(type, numPointerStars) \
			SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, \
				InterlockedCompareExchange, InterlockedCompareExchange64)
	#else
		#define SJME_ATOMIC_WIN32_IA(type, numPointerStars) \
			InterlockedAdd

		#define SJME_ATOMIC_WIN32_S(type, numPointerStars) \
			InterlockedExchange

		#define SJME_ATOMIC_WIN32_X(type, numPointerStars) \
        	InterlockedCompareExchange
	#endif
	
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
			if ((SJME_TOKEN_TYPE(type, numPointerStars))was == expected) \
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
					numPointerStars)*)&atomic->value, 0); \
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

#elif defined(SJME_CONFIG_HAS_ATOMIC_OLD)

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

SJME_ATOMIC_FUNCTION(sjme_jint, 0)

SJME_ATOMIC_FUNCTION(sjme_juint, 0)

SJME_ATOMIC_FUNCTION(sjme_lpstr, 0) /* NOLINT(*-non-const-parameter) */

SJME_ATOMIC_FUNCTION(sjme_lpcstr, 0) /* NOLINT(*-non-const-parameter) */

SJME_ATOMIC_FUNCTION(sjme_jobject, 0)

SJME_ATOMIC_FUNCTION(sjme_pointer, 0)

SJME_ATOMIC_FUNCTION(sjme_intPointer, 0)

SJME_ATOMIC_FUNCTION(sjme_thread, 0)
