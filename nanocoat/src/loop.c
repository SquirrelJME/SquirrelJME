/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <sjme/nvm/task.h>

#include "sjme/nvm/nvm.h"
#include "sjme/nvm/loop.h"
#include "sjme/debug.h"

static sjme_errorCode sjme_nvm_loop_subSchedule(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInRange(0, SJME_NVM_THREAD_SCHEDULED)
		sjme_nvm_threadScheduleMode inMode)
{
#define SJME_NVM_SUB_GROW 8
	sjme_errorCode error;
	sjme_nvm_threadSubSchedule* sub;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inMode < 0 || inMode >= SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Get sub-schedule. */
	sub = &inState->schedule.mode[inMode];
	
	/* Lock thread. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inThread->common.lock)))
		return sjme_error_default(error);

	/* Ignore if already scheduled in this group. */
	if (inThread->schedule == inMode)
		goto skip_alreadyScheduled;

	/* Lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&sub->lock)))
		goto fail_lockSub;

	/* Need to grow the schedule list? */
	if (sub->order == NULL || (sub->count + 1) >= sub->order->length)
		if (sjme_error_is(error = sjme_list_replace(inState->allocPool,
			sub->count + SJME_NVM_SUB_GROW, &sub->order,
			sjme_nvm_thread, 0)))
			goto fail_growList;

	/* Place it at the end of the schedule queue. */
	sub->order->elements[sub->count] = inThread;
	sub->count++;

	/* Set new thread scheduling mode. */
	inThread->schedule = inMode;
	
	/* Release schedule lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&sub->lock, NULL)))
		goto fail_releaseSub;
	
	/* Release thread lock. */
skip_alreadyScheduled:
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inThread->common.lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_growList:
	sjme_thread_spinLockRelease(&sub->lock, NULL);
	
fail_releaseSub:
fail_lockSub:
	sjme_thread_spinLockRelease(&inThread->common.lock, NULL);

	return sjme_error_default(error);
#undef SJME_NVM_SUB_GROW
}

sjme_errorCode sjme_nvm_loop_schedule(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward. */
	return sjme_nvm_loop_subSchedule(inState,
		inThread,
		SJME_NVM_THREAD_SCHEDULED);
}
	
sjme_errorCode sjme_nvm_loop_tick(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInValue sjme_attrInNegativeOnePositive sjme_jint maxTics,
	sjme_attrOutNullable sjme_jint* ticRemainder,
	sjme_attrOutNullable sjme_jboolean* isTerminated)
{
	sjme_errorCode error;
	sjme_jint remaining;
	sjme_nvm_threadSubSchedule* sub;
	sjme_nvm_thread runThread;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (maxTics < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Calculate initial remaining tics. */
	remaining = (maxTics < 0 ? -1 : maxTics);

	/* Get threads scheduled to be run. */
	sub = &inState->schedule.mode[SJME_NVM_THREAD_SCHEDULED];

	/* Keep ticking until nothing left is to be done. */
	while (remaining == -1 || remaining > 0)
	{
		/* Lock schedule. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&sub->lock)))
			return sjme_error_default(error);

		/* Get the first thread to run. */
		runThread = NULL;
		if (sub->count > 0)
			runThread = sub->order->elements[0];
		
		/* Release schedule. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(&sub->lock,
			NULL)))
			return sjme_error_default(error);

		/* Nothing to run? Grab something from unscheduled. */
		if (runThread == NULL)
		{
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
		}

		/* Otherwise execute the single thread. */
		if (sjme_error_is(error = sjme_nvm_loop_tickThread(
			runThread, remaining, &remaining, isTerminated)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_loop_tickThread(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_attrInNegativeOnePositive sjme_jint maxTics,
	sjme_attrOutNullable sjme_jint* ticRemainder,
	sjme_attrOutNullable sjme_jboolean* isTerminated)
{
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (maxTics < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_message("Exec %p", inThread);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
