/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "memio/atomic.h"

#if defined(SJME_MEMIO_ATOMIC_WIN32)
	#include <windows.h>
#endif

#if defined(SJME_MEMIO_ATOMIC_GCC)
	#define MEMORY_ORDER __ATOMIC_SEQ_CST
#endif

sjme_jboolean sjme_memIo_atomicIntCompareThenSet(sjme_memIo_atomicInt* atomic,
	sjme_jint check, sjme_jint set)
{
#if defined(SJME_MEMIO_ATOMIC_C11)
	if (atomic_compare_exchange_strong(&atomic->value, &check, set))
		return sjme_true;
	return sjme_false;
#elif defined(SJME_MEMIO_ATOMIC_GCC)
	if (__atomic_compare_exchange_n(&atomic->value, &check, set, 0,
		MEMORY_ORDER, MEMORY_ORDER))
		return sjme_true;
	return sjme_false;
#elif defined(SJME_MEMIO_ATOMIC_WIN32)
	LONG was;
	
	/* Returns the value that was stored here. */
	was = InterlockedCompareExchange((volatile LONG*)&atomic->value,
		set, check);
	
	if (was == check)
		return sjme_true;
	return sjme_false;
#elif defined(SJME_MEMIO_ATOMIC_OLD)
	sjme_jboolean result;

	/* Disable interrupts before continuing. */
	sjme_memIo_interruptsDisable();

	/* Set the value. */
	result = sjme_false;
	if (atomic->value == check)
	{
		atomic->value = set;
		result = sjme_true;
	}

	/* Enable interrupts before leaving. */
	sjme_memIo_interruptsEnable();
	
	return result;
#else
	#error No sjme_memIo_atomicIntCompareAndSet
#endif
}

sjme_jint sjme_memIo_atomicIntGet(sjme_memIo_atomicInt* atomic)
{
#if defined(SJME_MEMIO_ATOMIC_C11)
	return atomic_load(&atomic->value);
#elif defined(SJME_MEMIO_ATOMIC_GCC)
	return __atomic_load_n(&atomic->value, MEMORY_ORDER);
#elif defined(SJME_MEMIO_ATOMIC_OLD)
	sjme_jint result;

	/* Disable interrupts before continuing. */
	sjme_memIo_interruptsDisable();

	/* Read value. */
	result = atomic->value;

	/* Enable interrupts before leaving. */
	sjme_memIo_interruptsEnable();

	return result;
#else
	return sjme_memIo_atomicIntGetThenAdd(atomic, 0);
#endif
}

sjme_jint sjme_memIo_atomicIntSet(sjme_memIo_atomicInt* atomic,
	sjme_jint value)
{
#if defined(SJME_MEMIO_ATOMIC_C11)
	return atomic_exchange(&atomic->value, value);
#elif defined(SJME_MEMIO_ATOMIC_GCC)
	return __atomic_exchange_n(&atomic->value, value, MEMORY_ORDER);
#elif defined(SJME_MEMIO_ATOMIC_WIN32)
	return (sjme_jint)InterlockedExchange(
		(volatile LONG*)&atomic->value, value);
#elif defined(SJME_MEMIO_ATOMIC_OLD)
	sjme_jint oldValue;

	/* Disable interrupts before continuing. */
	sjme_memIo_interruptsDisable();

	/* Get and then unconditionally set the value. */
	oldvalue = atomic->value;
	atomic->value = value;

	/* Enable interrupts before leaving. */
	sjme_memIo_interruptsEnable();
	
	return oldValue; 
#else
	#error No sjme_memIo_atomicIntSet
#endif
}

sjme_jint sjme_memIo_atomicIntGetThenAdd(sjme_memIo_atomicInt* atomic,
	sjme_jint add)
{
#if defined(SJME_MEMIO_ATOMIC_C11)
	return atomic_fetch_add(&atomic->value, add);
#elif defined(SJME_MEMIO_ATOMIC_GCC)
	return __atomic_fetch_add(&atomic->value, add, MEMORY_ORDER);
#elif defined(SJME_MEMIO_ATOMIC_WIN32)
	/* This performs an add and get, however to do a get and add we need */
	/* to subtract what we just added to get the original value. */
	return InterlockedAdd((volatile LONG*)&atomic->value, add) - add;
#elif defined(SJME_MEMIO_ATOMIC_OLD)
	sjme_jint oldValue;

	/* Disable interrupts before continuing. */
	sjme_memIo_interruptsDisable();

	/* Get the old value, then set the new value accordingly. */
	oldValue = atomic->value;
	atomic->value = oldValue + add;

	/* Enable interrupts before leaving. */
	sjme_memIo_interruptsEnable();
	
	return oldValue;
#else
	#error No sjme_memIo_atomicIntGetAndAdd
#endif
}

sjme_jboolean sjme_memIo_atomicPointerCompareThenSet(
	sjme_memIo_atomicPointer* atomic, void* check, void* set)
{
#if defined(SJME_MEMIO_ATOMIC_C11)
	if (atomic_compare_exchange_strong(&atomic->value, &check, set))
		return sjme_true;
	return sjme_false;
#elif defined(SJME_MEMIO_ATOMIC_GCC)
	if (__atomic_compare_exchange_n(&atomic->value,
		(volatile void**)&check, set, 0, MEMORY_ORDER, MEMORY_ORDER))
		return sjme_true;
	return sjme_false;
#elif defined(SJME_MEMIO_ATOMIC_WIN32)
	void* was;
	
	/* Returns the value that was stored here. */
	was = (void*)InterlockedCompareExchangePointer(
		(volatile PVOID*)&atomic->value, set, (PVOID)check);
	
	if (was == check)
		return sjme_true;
	return sjme_false;
#elif defined(SJME_MEMIO_ATOMIC_OLD)
	sjme_jboolean result;

	/* Disable interrupts before continuing. */
	sjme_memIo_interruptsDisable();

	/* Check then set the value. */
	result = sjme_false;
	if (atomic->value == check)
	{
		atomic->value = set;
		result = sjme_true;
	}

	/* Enable interrupts before leaving. */
	sjme_memIo_interruptsEnable();
	
	return result;
#else
	#error No sjme_memIo_atomicIntCompareAndSet
#endif
}

void* sjme_memIo_atomicPointerGet(sjme_memIo_atomicPointer* atomic)
{
#if defined(SJME_MEMIO_ATOMIC_C11)
	return atomic_load(&atomic->value);
#elif defined(SJME_MEMIO_ATOMIC_GCC)
	return (void*)__atomic_load_n(&atomic->value, MEMORY_ORDER);
#elif defined(SJME_MEMIO_ATOMIC_WIN32)
	#if SJME_POINTER == 64
		return (void*)InterlockedAdd64((volatile LONG64*)&atomic->value, 0);
	#else
		return (void*)InterlockedAdd((volatile LONG*)&atomic->value, 0);
	#endif
#elif defined(SJME_POINTER == 32)
	return (void*)sjme_memIo_atomicIntGetAndAdd(
		(sjme_memIo_atomicInt*)atomic, 0);
#elif defined(SJME_MEMIO_ATOMIC_OLD)
	void* result;

	/* Disable interrupts before continuing. */
	sjme_memIo_interruptsDisable();

	/* Read value. */
	result = (void*)atomic->value;

	/* Enable interrupts before leaving. */
	sjme_memIo_interruptsEnable();

	return result;
#else
	#error No sjme_memIo_atomicPointerGet
#endif
}

void* sjme_memIo_atomicPointerSet(sjme_memIo_atomicPointer* atomic,
	void* value)
{
#if defined(SJME_MEMIO_ATOMIC_C11)
	return atomic_exchange(&atomic->value, value);
#elif defined(SJME_MEMIO_ATOMIC_GCC)
	return (void*)__atomic_exchange_n(&atomic->value, value, MEMORY_ORDER);
#elif defined(SJME_MEMIO_ATOMIC_WIN32)
	return (void*)InterlockedExchangePointer(
		(volatile PVOID*)&atomic->value, (PVOID)value);
#elif defined(SJME_POINTER == 32)
	return sjme_memIo_atomicIntSet((sjme_memIo_atomicInt*)atomic, value);
#elif defined(SJME_MEMIO_ATOMIC_OLD)
	volatile void* oldValue;

	/* Disable interrupts before continuing. */
	sjme_memIo_interruptsDisable();

	/* Get then set value. */
	oldValue = atomic->value;
	atomic->value = value;

	/* Enable interrupts before leaving. */
	sjme_memIo_interruptsEnable();
	
	return (void*)oldValue;
#else
	#error No sjme_memIo_atomicPointerSet
#endif
}
