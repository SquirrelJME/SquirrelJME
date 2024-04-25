/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/core/core.h"
#include "sjme/atomic.h"
#include "sjme/debug.h"

typedef struct sjme_scritchui_core_waitData
{
	/** The signal to trigger on. */
	sjme_atomic_sjme_jint signal;
	
	/** The callback to execute. */
	sjme_thread_mainFunc callback;
	
	/** Value passed to the callback. */
	sjme_thread_parameter anything;
} sjme_scritchui_core_waitData;

static sjme_thread_result sjme_scritchui_core_waitAdapter(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui_core_waitData* waitData;
	sjme_thread_result result;
	
	/* Recover data. */
	waitData = (sjme_scritchui_core_waitData*)anything;
	if (waitData == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* Synchronous call. */
	result = waitData->callback(waitData->anything);
	
	/* Signal that wait is complete. */
	sjme_atomic_sjme_jint_set(&waitData->signal, 1);
	
	/* Use result from callback. */
	return result;
}

sjme_errorCode sjme_scritchui_core_loopExecute(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inState->impl->loopExecute == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward call. */
	return inState->impl->loopExecute(inState, callback, anything);
}

sjme_errorCode sjme_scritchui_core_loopExecuteWait(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_scritchui_core_waitData waitData;
	
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If executed wait is directly available we do not need a shim. */
	if (inState->impl->loopExecuteWait != NULL)
		return inState->impl->loopExecuteWait(inState, callback, anything);
	
	/* Initialize wait. */
	memset(&waitData, 0, sizeof(waitData));
	sjme_atomic_sjme_jint_set(&waitData.signal, 0);
	waitData.callback = callback;
	waitData.anything = anything;
	
	/* Execute call. */
	if (sjme_error_is(error = sjme_scritchui_core_loopExecute(
		inState, sjme_scritchui_core_waitAdapter,
		&waitData)))
		return sjme_error_default(error);
	
	/* Wait for termination. */
	for (;;)
	{
		/* Done? */
		if (0 != sjme_atomic_sjme_jint_get(&waitData.signal))
			break;
		
		/* Yield to let other threads run. */
		sjme_thread_yield();
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_loopIsInThread(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_jboolean* outInThread)
{
	sjme_errorCode error;
	sjme_thread self;
	
	if (inState == NULL || outInThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Check current thread. */
	self = SJME_THREAD_NULL;
	if (sjme_error_is(error = sjme_thread_current(&self)) ||
		self == SJME_THREAD_NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_THREAD_STATE);
	
	/* Are we in the loop? */
	*outInThread = sjme_thread_equal(self, inState->loopThread);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
