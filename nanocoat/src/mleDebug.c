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
	sjme_jobject object;
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
		object = sjme_atomic_sjme_jobject_get(&atFrame->phantomTracePoint);
		if (object != NULL && sjme_nvm_isAR(object,
			SJME_NVM_STRUCT_CLASS_TRACE_POINT_INSTANCE))
		{
			/* If the point's ID is valid for the frame, we do not need */
			/* to actually recreate it as it still points to the same frame. */
			if (point->frame == atFrame && point->id == atFrame->id)
			{
				/* Store into the array. */
				result->e.l[into] = object;
				
				/* No need to create. */
				continue;
			}

			/* Needs to be created. */
			object = NULL;
		}
		
		/* It needs to be created. */
		point = NULL;
		if (sjme_error_is(error = sjme_nvm_instance_objectNewBracket(inThread,
			SJME_NVM_STRUCT_CLASS_TRACE_POINT_INSTANCE,
			SJME_AS_JOBJECTP(&point))) || point == NULL)
			goto fail_allocBracket;

		/* Set ID of the trace and what it points to. */
		point->frame = atFrame;
		point->id = atFrame->id;
		point->baseIndex = i;
		
		/* Store into the array. */
		result->e.l[into] = object;
	}

	/* Return the trace point array. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argV->v.l = SJME_AS_JOBJECT(result);
	return SJME_ERROR_NONE;

fail_allocBracket:
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);

	return sjme_error_vmError(inFrame, error);
}

SJME_NVM_MLE_SHELF_DECLARE(DebugShelf) =
{
	SJME_NVM_MLE_DEFINE(traceStack,
		SJME_MD(SJME_MD_A(SJME_MD_TRACE), ),
		"L", ),
	SJME_NVM_MLE_STOP()
};
