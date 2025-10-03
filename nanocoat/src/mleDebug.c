/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <sjme/nvm/cleanup.h>

#include "sjme/config.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_FUNCTION_DECL(traceStack)
{
	sjme_errorCode error;
	sjme_nvm_thread inThread;
	sjme_jint count, i, into;
	sjme_jarray result;
	sjme_nvm_frame atFrame;
	sjme_list_sjme_nvm_frame* frames;
	sjme_jbracketTrace point;

	/* Which thread is being operated one? */
	inThread = SJME_F_T(inFrame);

	/* Allocate an array with the frame count. */
	count = inThread->numFrames;
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(inThread,
		&result, sjme_nvm_task_commonClassR(inThread,
			SJME_NVM_TASK_COMMON_CLASS_TRACE_POINT), count)) || result == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Create trace point objects mapped to frames. */
	frames = inThread->frames;
	for (i = count - 1, into = 0; i >= 0; i--, into++)
	{
		/* Which frame is here? */
		atFrame = frames->elements[i];
		if (atFrame == NULL)
			continue;

		/* Is there a phantom trace point here? */
		point = (sjme_jbracketTrace)sjme_atomic_pg(
			&atFrame->phantomTracePoint);
		if (point != NULL && sjme_nvm_isAR(point,
			SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE))
		{
			/* If the point's ID is valid for the frame, we do not need */
			/* to actually recreate it as it still points to the same frame. */
			if (point->frame == atFrame && point->id == atFrame->id)
			{
				/* Store into the array. */
				result->e.l[into] = SJME_AS_JOBJECT(point);
				
				/* No need to create. */
				continue;
			}

			/* Needs to be created. */
			point = NULL;
		}
		
		/* It needs to be created. */
		point = NULL;
		if (sjme_error_is(error = sjme_nvm_instance_objectNewBracket(inThread,
			SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE,
			SJME_AS_JOBJECTP(&point))) || point == NULL)
			goto fail_allocBracket;

		/* Set ID of the trace and what it points to. */
		point->frame = atFrame;
		point->id = atFrame->id;
		point->baseIndex = i;

		/* Capture a copy of the frame information as it will go away. */
		point->capture.inClass = atFrame->inClass;
		point->capture.inCode = atFrame->inCode;
		point->capture.lastPc = atFrame->lastPc;
		point->capture.lastIv = atFrame->lastIv;
		
		/* Store into the array. */
		result->e.l[into] = SJME_AS_JOBJECT(point);
	}

	/* Return the trace point array. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = SJME_AS_JOBJECT(result);
	return SJME_ERROR_NONE;

fail_allocBracket:
	/* Count down the array so it gets GCed. */
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	
	return sjme_error_vmError(inFrame, error);
}

SJME_NVM_MLE_FUNCTION_DECL(traceThrowable)
{
	sjme_errorCode error;
	sjme_jthrowable throwable;
	sjme_jvalueTyped result;

	/* Must be a basic object. */
	throwable = (sjme_jthrowable)argV[0].v.l;
	if (!sjme_nvm_isAR(throwable, SJME_NVM_STRUCT_OBJECT_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Must be of the throwable class! */
	if (!sjme_nvm_vmClass_isAssignableFrom(SJME_F_T(inFrame),
		sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
			SJME_NVM_TASK_COMMON_CLASS_THROWABLE), throwable->object.isClass))
		return SJME_ERROR_MLE_CALL;

	/* We can just call the above. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_nvm_mleFunc_traceStack_none(inFrame,
		&result, 0, NULL)))
		return sjme_error_default(error);

	/* Set the throwable special. */
	sjme_atomic_s(sjme_intPointer, &throwable->object.special,
		(sjme_intPointer)result.v.l);

	/* Set the return value. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = result.v.l;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(DebugShelf) =
{
	SJME_NVM_MLE_DEFINE(traceStack,
		SJME_MD(SJME_MD_A(SJME_MD_TRACE), ),
		"L", ),
	SJME_NVM_MLE_DEFINE(traceThrowable,
		SJME_MD(SJME_MD_A(SJME_MD_TRACE), SJME_MD_THROWABLE),
		"L", "L"),
	SJME_NVM_MLE_STOP()
};
