/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "atomic.h"

#if defined(SJME_ATOMIC_WIN32)
	#include <windows.h>
#endif

#if defined(SJME_ATOMIC_GCC)
	#define MEMORY_ORDER __ATOMIC_SEQ_CST
#endif

sjme_jboolean sjme_atomicIntCompareThenSet(sjme_atomicInt* atomic,
	sjme_jint check, sjme_jint set)
{
#if defined(SJME_ATOMIC_C11)
	if (atomic_compare_exchange_strong(&atomic->value, &check, set))
		return sjme_true;
	return sjme_false;
#elif defined(SJME_ATOMIC_GCC)
	if (__atomic_compare_exchange_n(&atomic->value, &check, set, 0,
		MEMORY_ORDER, MEMORY_ORDER))
		return sjme_true;
	return sjme_false;
#elif defined(SJME_ATOMIC_WIN32)
	LONG was;

	/* Returns the value that was stored here. */
	was = InterlockedCompareExchange((volatile LONG*)&atomic->value,
		set, check);

	if (was == check)
		return sjme_true;
	return sjme_false;
#elif defined(SJME_ATOMIC_RELAXED_NOT_ATOMIC)
	if (atomic->value == check)
	{
		atomic->value = set;
		return sjme_true;
	}

	return sjme_false;
#else
	#error No sjme_atomicIntCompareAndSet
#endif
}

sjme_jint sjme_atomicIntGet(sjme_atomicInt* atomic)
{
#if defined(SJME_ATOMIC_C11)
	return atomic_load(&atomic->value);
#elif defined(SJME_ATOMIC_GCC)
	return __atomic_load_n(&atomic->value, MEMORY_ORDER);
#else
	return sjme_atomicIntGetThenAdd(atomic, 0);
#endif
}

sjme_jint sjme_atomicIntSet(sjme_atomicInt* atomic, sjme_jint value)
{
#if defined(SJME_ATOMIC_C11)
	return atomic_exchange(&atomic->value, value);
#elif defined(SJME_ATOMIC_GCC)
	return __atomic_exchange_n(&atomic->value, value, MEMORY_ORDER);
#elif defined(SJME_ATOMIC_WIN32)
	return (sjme_jint)InterlockedExchange(
		(volatile LONG*)&atomic->value, value);
#elif defined(SJME_ATOMIC_RELAXED_NOT_ATOMIC)
	sjme_jint oldValue = atomic->value;

	atomic->value = value;

	return oldValue;
#else
	#error No sjme_atomicIntSet
#endif
}

sjme_jint sjme_atomicIntGetThenAdd(sjme_atomicInt* atomic, sjme_jint add)
{
#if defined(SJME_ATOMIC_C11)
	return atomic_fetch_add(&atomic->value, add);
#elif defined(SJME_ATOMIC_GCC)
	return __atomic_fetch_add(&atomic->value, add, MEMORY_ORDER);
#elif defined(SJME_ATOMIC_WIN32)
	/* This performs an add and get, however to do a get and add we need */
	/* to subtract what we just added to get the original value. */
	return InterlockedAdd((volatile LONG*)&atomic->value, add) - add;
#elif defined(SJME_ATOMIC_RELAXED_NOT_ATOMIC)
	sjme_jint oldValue = atomic->value;

	atomic->value = oldValue + add;

	return oldValue;
#else
	#error No sjme_atomicIntGetAndAdd
#endif
}

sjme_jboolean sjme_atomicPointerCompareThenSet(sjme_atomicPointer* atomic,
	void* check, void* set)
{
#if defined(SJME_ATOMIC_C11)
	if (atomic_compare_exchange_strong(&atomic->value, &check, set))
		return sjme_true;
	return sjme_false;
#elif defined(SJME_ATOMIC_GCC)
	if (__atomic_compare_exchange_n(&atomic->value, &check, set, 0,
		MEMORY_ORDER, MEMORY_ORDER))
		return sjme_true;
	return sjme_false;
#elif defined(SJME_ATOMIC_WIN32)
	void* was;

	/* Returns the value that was stored here. */
	was = (void*)InterlockedCompareExchangePointer(
		(volatile PVOID*)&atomic->value, set, (PVOID)check);

	if (was == check)
		return sjme_true;
	return sjme_false;
#elif defined(SJME_ATOMIC_RELAXED_NOT_ATOMIC)
	if (atomic->value == check)
	{
		atomic->value = set;
		return sjme_true;
	}

	return sjme_false;
#else
	#error No sjme_atomicIntCompareAndSet
#endif
}

void* sjme_atomicPointerGet(sjme_atomicPointer* atomic)
{
#if defined(SJME_ATOMIC_C11)
	return atomic_load(&atomic->value);
#elif defined(SJME_ATOMIC_GCC)
	return (void*)__atomic_load_n(&atomic->value, MEMORY_ORDER);
#elif defined(SJME_ATOMIC_WIN32)
	#if SJME_BITS == 64
		return (void*)InterlockedAdd64((volatile LONG64*)&atomic->value, 0);
	#else
		return (void*)InterlockedAdd((volatile LONG*)&atomic->value, 0);
	#endif
#elif defined(SJME_ATOMIC_RELAXED_NOT_ATOMIC)
	return atomic->value;
#else
	return sjme_atomicIntGetAndAdd(atomic, 0);
#endif
}

void* sjme_atomicPointerSet(sjme_atomicPointer* atomic, void* value)
{
#if defined(SJME_ATOMIC_C11)
	return atomic_exchange(&atomic->value, value);
#elif defined(SJME_ATOMIC_GCC)
	return (void*)__atomic_exchange_n(&atomic->value, value, MEMORY_ORDER);
#elif defined(SJME_ATOMIC_WIN32)
	return (void*)InterlockedExchangePointer(
		(volatile PVOID*)&atomic->value, (PVOID)value);
#elif defined(SJME_ATOMIC_RELAXED_NOT_ATOMIC)
	sjme_jint oldValue = atomic->value;

	atomic->value = value;

	return oldValue;
#else
	#error No sjme_atomicPointerSet
#endif
}
