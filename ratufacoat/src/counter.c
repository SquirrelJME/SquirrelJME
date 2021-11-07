/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "counter.h"

sjme_jboolean sjme_counterDown(sjme_counter* counter, sjme_jboolean* outActive,
	sjme_error* error)
{
	sjme_jint oldValue;
	
	if (counter == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Reduce the value then check for consistency. */
	oldValue = sjme_atomicIntGetThenAdd(&counter->count, -1);
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
		sjme_atomicIntSet(&counter->count, -200);
		counter->collectData = NULL;
		counter->collect = NULL;
		
		/* Successful count down. */
		return sjme_true;
	}
	
	/* It has not. */
	if (outActive != NULL)
		*outActive = sjme_true;
	return sjme_true;
}
