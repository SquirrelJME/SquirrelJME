/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/task.h"
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
	sjme_errorCode error;
	sjme_jint frameIndex, remaining;
	sjme_nvm_frame currentFrame;
	sjme_nvm_class_codeInfo currentCode;
	sjme_byteCode* rawCode;
	sjme_byteCode* ev;
	sjme_byteCode iv;
	sjme_nvm_byteCode_pcNew pcNew;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (maxTics < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Initialized to make the linter not noisy. */
	currentFrame = NULL;
	rawCode = NULL;
	
	/* Continuous code execution. */
	frameIndex = -2;
	remaining = maxTics;
	memset(&pcNew, 0, sizeof(pcNew));
	while sjme_noLint(remaining == -1 || remaining > 0)
	{
		/* Thread has entered a sleeping state? */
		if (inThread->status != SJME_NVM_THREAD_STATUS_RUNNING)
			break;
		
		/* Tick down. */
		if (remaining > 0)
			remaining--;
		
		/* Get current top-most frame, if it changed. */
		if (frameIndex != (inThread->numFrames - 1))
		{
			frameIndex = (inThread->numFrames - 1);
			currentFrame = inThread->frames->elements[frameIndex];
			currentCode = currentFrame->inCode;
			rawCode = currentCode->rawCode;
		}

		/* Read instruction vector. */
		ev = &rawCode[currentFrame->pc];
		iv = *ev;

		/* Execute narrow handler. */
		memset(&pcNew, 0, sizeof(pcNew));
		if (sjme_error_is(error =
			sjme_nvm_byteCode_slowNarrowFunctions[iv](currentFrame,
				iv, ev, &pcNew)))
			return sjme_error_vmError(inThread, error);

		/* Popping the current frame? */
		if (pcNew.popFrame)
		{
			/* Pop the stack frame. */
			if (sjme_error_is(error = sjme_nvm_task_threadLeave(inThread)))
				return sjme_error_vmError(inThread, error);

			/* On the next one the frame index will be invalid. */
			continue;
		}

		/* Set new PC address, in non-exception cases. */
		if (pcNew.type == SJME_NVM_BYTECODE_PC_RELATIVE)
			currentFrame->pc += pcNew.adjust;
		else
			currentFrame->pc = pcNew.adjust;
	}
	
	/* Give remaining, if requested. */
	if (ticRemainder != NULL)
		*ticRemainder = remaining;
	return SJME_ERROR_NONE;
}
