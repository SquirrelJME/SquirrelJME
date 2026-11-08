/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/pure/pure.h"

sjme_errorCode sjme_scritchui_pure_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward. */
	return inState->intern->loopQueuePush(inState,
		&inState->loopQueue, callback, anything);
}

sjme_errorCode sjme_scritchui_pure_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated)
{
	sjme_errorCode error;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Infinite loop when blocking. */
	do
	{
		/* Forward to standard loop drain. */
		if (sjme_error_is(error = inState->intern->loopQueueDrain(inState,
			&inState->loopQueue)))
			return sjme_error_default(error);
	} while (blocking);
	
	/* Terminated? */
	if (outHasTerminated != NULL)
		*outHasTerminated = (sjme_atomic_gl(sjme_jint,
			&inState->isTerminated) != 0);
	return SJME_ERROR_NONE;
}
