/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "counter.h"

/** Value to set for invalid counter values. */
#define SJME_INVALID_COUNTER_VALUE SJME_JINT_C(-200)

sjme_jboolean sjme_counterDown(sjme_counter* counter, sjme_jboolean* outActive,
	sjme_error* error)
{
	sjme_jint oldValue;
	
	if (counter == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Reduce the value then check for consistency. */
	oldValue = sjme_memIo_atomicIntGetThenAdd(&counter->count, -1);
	if (oldValue <= 0)
	{
		if (outActive != NULL)
			*outActive = sjme_false;
		
		sjme_setError(error, SJME_ERROR_INVALID_COUNTER_STATE, oldValue);
		
		return sjme_false;
	}
	
	/* Has this been garbage collected? */
	else if (oldValue == 1)
	{
		/* Say no otherwise. */
		if (outActive != NULL)
			*outActive = sjme_false;
		
		/* Perform collection. */
		if (counter->collect != NULL)
			if (!counter->collect(counter, error))
				return sjme_false;
		
		/* Invalidate the state. */
		sjme_memIo_atomicIntSet(&counter->count,
			SJME_INVALID_COUNTER_VALUE);
		counter->dataPointer = NULL;
		counter->dataInteger = 0;
		counter->collect = NULL;
		
		/* Successful count down. */
		return sjme_true;
	}
	
	/* It has not. */
	if (outActive != NULL)
		*outActive = sjme_true;
	return sjme_true;
}

sjme_jboolean sjme_counterInit(sjme_counter* counter,
	sjme_counterCollectFunction collectFunc,
	void* dataPointer, int dataInteger, sjme_error* error)
{
	sjme_jint oldCount;
	
	if (counter == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* The initial count should always be zero! */
	oldCount = sjme_memIo_atomicIntGetThenAdd(&counter->count, 1);
	if (oldCount != 0)
	{
		/* Invalidate the counter. */
		sjme_memIo_atomicIntSet(&counter->count,
			SJME_INVALID_COUNTER_VALUE);
		
		sjme_setError(error, SJME_ERROR_INVALID_COUNTER_STATE, oldCount);
		
		return sjme_false;
	}
	
	/* Setup fields. */
	counter->collect = collectFunc;
	counter->dataPointer = dataPointer;
	counter->dataInteger = dataInteger;
	
	/* Count up was successful. */
	return sjme_true;
}

sjme_jboolean sjme_counterUp(sjme_counter* counter, sjme_error* error)
{
	sjme_jint oldCount;
	
	if (counter == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Count up! Ensure the counter is not in an invalid state. */
	oldCount = sjme_memIo_atomicIntGetThenAdd(&counter->count, 1);
	if (oldCount <= 0)
	{
		/* Invalidate the counter. */
		sjme_memIo_atomicIntSet(&counter->count,
			SJME_INVALID_COUNTER_VALUE);
		
		sjme_setError(error, SJME_ERROR_INVALID_COUNTER_STATE, oldCount);
		
		return sjme_false;
	}
	
	/* Count up was successful. */
	return sjme_true;
}
