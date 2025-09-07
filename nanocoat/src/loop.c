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

static sjme_thread_result sjme_attrThreadCall sjme_nvm_loop_tickCrash(
	sjme_thread_parameter rawThread)
{
	sjme_nvm_thread inThread;

	/* Recover thread. */
	inThread = SJME_THREAD_PARAM_POINTER(rawThread);
	if (inThread == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NONE);

	/* Print stack trace. */
	sjme_nvm_task_stackTraceThread(inThread);
	
	/* No error generally. */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

sjme_errorCode sjme_nvm_loop_main(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNullable sjme_jint* exitCode)
{
	sjme_errorCode error;
	sjme_jboolean terminated;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Iterate the virtual machine loop. */
	for (terminated = SJME_JNI_FALSE; !terminated;)
	{
		/* Let other threads run. */
		sjme_thread_yield();
		
		/* Tick the virtual machine. */
		if (sjme_error_is(error = sjme_nvm_loop_tick(inState, -1,
			NULL, &terminated)))
		{
			/* Fail unless this was interrupted. */
			if (error == SJME_ERROR_INTERRUPTED)
				continue;
			goto fail_loop;
		}
	}

	/* Success! */
	if (exitCode != NULL)
		*exitCode = sjme_atomic_sjme_jint_get(&inState->lastExitCode);
	return SJME_ERROR_NONE;
	
fail_loop:
	if (exitCode != NULL)
		*exitCode = sjme_atomic_sjme_jint_get(&inState->lastExitCode);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_loop_tick(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInValue sjme_attrInNegativeOnePositive sjme_jint maxTics,
	sjme_attrOutNullable sjme_jint* ticRemainder,
	sjme_attrOutNullable sjme_jboolean* isTerminated)
{
	sjme_errorCode error;
	sjme_jint remaining;
	sjme_nvm_thread runThread;
	sjme_jboolean terminated;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (maxTics < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Calculate initial remaining tics. */
	remaining = (maxTics < 0 ? -1 : maxTics);

	/* Keep ticking until nothing left is to be done. */
	while (remaining == -1 || remaining > 0)
	{
		/* Grab next thread to run. */
		runThread = NULL;
		terminated = SJME_JNI_FALSE;
		if (sjme_error_is(error = sjme_nvm_task_taskScheduleNext(
			inState, &runThread, &terminated)))
			return sjme_error_default(error);

		/* Terminated? */
		if (terminated)
			break;

		/* Nothing to run? Give up CPU slice and re-run. */
		if (runThread == NULL)
		{
			sjme_thread_yield();
			continue;
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
	sjme_jint frameIndex, remaining, tossedLevel;
	sjme_nvm_frame currentFrame;
	sjme_nvm_class_codeInfo currentCode;
	sjme_byteCode* rawCode;
	sjme_byteCode* ev;
	sjme_byteCode iv;
	sjme_nvm_byteCode_pcNew pcNew, pcDefault;
	const sjme_nvm_byteCode_func (*lut)[SJME_NVM_NUM_JAVA_BYTECODES];
	sjme_nvm_byteCode_func lutFunc;
	sjme_jobject tossed;
	sjme_jboolean handled;
	sjme_jvalueTyped push;
	sjme_nvm_task inTask;
	sjme_nvm inState;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (maxTics < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Set crash context thread. */
	sjme_debug_crashContext(sjme_nvm_loop_tickCrash, inThread);

	/* The remaining execution count is always at the max tic count. */
	remaining = maxTics;

	/* Initialized to make the linter not noisy. */
	currentFrame = NULL;
	currentCode = NULL;
	rawCode = NULL;
	tossed = NULL;
	tossedLevel = -1;
	
	/* Continuous code execution. */
	inTask = SJME_T_K(inThread);
	inState = SJME_T_S(inThread);
	frameIndex = -2;
	memset(&pcNew, 0, sizeof(pcNew));
	while sjme_noLint(remaining == -1 || remaining > 0)
	{
		/* If this task is terminating, unwind everything. */
		if (sjme_atomic_sjme_jint_get(&inTask->terminate) !=
			SJME_NVM_TERMINATE_NOT)
		{
			/* Leave all thread frames. */
			while (inThread->numFrames > 0)
				if (sjme_error_is(error = sjme_nvm_task_threadLeave(inThread)))
					goto fail_any;

			/* All threads have been cleaned up? */
			if ((sjme_atomic_sjme_jint_getAdd(
				&inTask->numThreads[SJME_NVM_THREAD_COUNT_AWAIT_CLEANUP],
				-1) - 1) <= 0)
			{
				/* Mark task as terminated to the caller. */
				if (isTerminated)
					*isTerminated = SJME_JNI_TRUE;

				/* Flag task as cleaned up. */
				sjme_atomic_sjme_jint_compareSet(&inTask->terminate,
					SJME_NVM_TERMINATE_CLEANUP,
					SJME_NVM_TERMINATE_COMPLETE);

				/* Reduce the running task count. */
				/* This might be the final thread to reduce this to zero. */
				if ((sjme_atomic_sjme_jint_getAdd(&inState->numRunningTasks,
					-1) - 1) <= 0)
				{
					if (isTerminated)
						*isTerminated = SJME_JNI_TRUE;
				}
			}
			
			/* Do not execute actual byte code. */
			goto skip_done;
		}
		
		/* Thread has entered a sleeping state? */
		if (inThread->status != SJME_NVM_THREAD_STATUS_RUNNING)
			break;
		
		/* Tick down. */
		if (remaining > 0)
			remaining--;
		
		/* Get current top-most frame, if it changed. */
		if (frameIndex != (inThread->numFrames - 1))
		{
			/* Are there any actual frames left? */
			frameIndex = (inThread->numFrames - 1);
			if (frameIndex < 0)
				break;
			
			currentFrame = inThread->frames->elements[frameIndex];
			currentCode = currentFrame->inCode;
			rawCode = currentCode->rawCode;
		}

		/* Read instruction vector. */
		ev = &rawCode[currentFrame->pc];
		iv = *ev;

		/* Pre-determine default instruction offset. */
		pcDefault.type = SJME_NVM_BYTECODE_PC_DEFAULT;
		pcDefault.adjust = sjme_nvm_byteCode_lengths[iv];
		pcDefault.popFrame = SJME_JNI_FALSE;

		/* Default PC adjustment must be calculated? */
		if (pcDefault.adjust < SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_5)
			if (sjme_error_is(error = sjme_nvm_byteCode_calcLength(
				currentFrame, iv, ev, &pcDefault)))
				goto fail_any;

		/* The instruction length cannot run past the end of the code. */
		if (sjme_noLint(currentFrame)->pc + pcDefault.adjust >
			currentFrame->inCode->rawCodeLen)
		{
			error = SJME_ERROR_INVALID_INSTRUCTION;
			goto fail_any;
		}

		/* Which LUT table to use? */
		lut = sjme_nvm_byteCode_lutTable[iv];
		if (lut == NULL)
		{
			error = SJME_ERROR_INVALID_INSTRUCTION;
			goto fail_any;
		}

		/* The instruction should be valid for the LUT. */
		lutFunc = (*lut)[iv];
		if (lutFunc == NULL)
		{
			error = SJME_ERROR_INVALID_INSTRUCTION;
			goto fail_any;
		}
		
		/* Store last PC and IV, for trace purposes. */
		currentFrame->lastPc = sjme_noLint(currentFrame)->pc;
		currentFrame->lastIv = iv;

		/* Do not handle the instruction if there is an exception waiting. */
		/* But as long as we are still below the toss level. */
		tossed = sjme_atomic_sjme_jobject_get(&inThread->tossed);
		tossedLevel = sjme_atomic_sjme_jint_get(&inThread->tossedLevel);
		if (tossed != NULL && (tossedLevel < 0 ||
			inThread->numFrames < tossedLevel))
			goto skip_thrown;

#if defined(SJME_CONFIG_DEBUG_BYTECODES)
		/* Debug. */
		sjme_messageB("%2d@%3d: %s (%s.%s %s)", 
			frameIndex, currentFrame->pc, sjme_nvm_byteCode_names[iv],
			sjme_charSeq_tempUtf(currentFrame->inClass->binaryName),
			sjme_charSeq_tempUtf(currentFrame->inMethod->member.name->seq),
			sjme_charSeq_tempUtf(currentFrame->inMethod->member.type->seq));
#endif

		/* Execute handler. */
		memmove(&pcNew, &pcDefault, sizeof(pcNew));
		if (sjme_error_is(error = lutFunc(currentFrame, iv, ev, &pcNew)))
			goto fail_any;
		
		/* Has an exception been thrown? */
		tossed = sjme_atomic_sjme_jobject_get(&inThread->tossed);
		tossedLevel = sjme_atomic_sjme_jint_get(&inThread->tossedLevel);

		/* If recycling, we can skip handling exceptions here. */
		if (pcNew.type == SJME_NVM_BYTECODE_PC_RECYCLE)
			continue;
		
skip_thrown:
		/* Are we handling an exception, and we are running below any */
		/* implicit initialization frames? */
		if (tossed != NULL && (tossedLevel >= 0 &&
			inThread->numFrames <= tossedLevel))
		{
			/* We are finished running the constructor, so set the toss */
			/* level to a very high amount so any sub-calls are not done */
			/* freely. */
			sjme_atomic_sjme_jint_set(&inThread->tossedLevel, INT32_MAX);
			
			/* Find exception handler to jump to. */
			handled = SJME_JNI_FALSE;
			if (sjme_error_is(error = sjme_nvm_task_frameHandler(
				currentFrame, tossed, &handled, &pcNew)))
				goto fail_any;

			/* If handled, we need to prepare for the handler. */
			if (handled)
			{
				/* No longer handle the exception. */
				if (!sjme_atomic_sjme_jobject_compareSet(&inThread->tossed,
					tossed, NULL))
					goto fail_any;

				/* Clear the tossed level. */
				sjme_atomic_sjme_jint_set(&inThread->tossedLevel, -1);

				/* Clear the stack. */
				if (sjme_error_is(error = sjme_nvm_task_frameStackClear(
					currentFrame)))
					goto fail_any;

				/* Push the exception to the stack. */
				memset(&push, 0, sizeof(push));
				push.t = SJME_JAVA_TYPE_ID_OBJECT;
				push.v.l = tossed;
				if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
					currentFrame, &push)))
					goto fail_any;

				/* Lower the count as it is now on the stack and now gone */
				/* from tossed. */
				if (sjme_error_is(error = sjme_nvm_instance_countDown(tossed)))
					goto fail_any;
			}

			/* If not handled, pop the frame so we put the exception onto */
			/* the caller's stack. */
			else
			{
				pcNew.popFrame = SJME_JNI_TRUE;
			}
		}
		
		/* If recycling, do not commit to actually making progress. This is */
		/* here in the event we entered a default constructor for an */
		/* implicit exception, and it has to initialize a class. */
		if (pcNew.type == SJME_NVM_BYTECODE_PC_RECYCLE)
			continue;

		/* Popping the current frame? */
		if (pcNew.popFrame)
		{
			/* Pop the stack frame. */
			if (sjme_error_is(error = sjme_nvm_task_threadLeave(inThread)))
				goto fail_any;

			/* On the next one the frame index will be invalid. */
			continue;
		}

		/* Set new PC address, in non-exception cases. */
		if (pcNew.type == SJME_NVM_BYTECODE_PC_DEFAULT)
		{
			/* Default adjust can never go negative. */
			if (pcNew.adjust <= 0)
			{
				error = SJME_ERROR_INVALID_PC_ADJUST;
				goto fail_any;
			}

			/* Adjustment is valid. */
			sjme_noLint(currentFrame)->pc += pcNew.adjust;
		}
		else if (pcNew.type == SJME_NVM_BYTECODE_PC_RELATIVE)
			sjme_noLint(currentFrame)->pc += pcNew.adjust;
		else
			sjme_noLint(currentFrame)->pc = pcNew.adjust;
		
		/* PC address is not valid. */
		if (currentFrame->pc < 0 ||
			currentFrame->pc > sjme_noLint(currentCode)->rawCodeLen)
		{
			error = SJME_ERROR_INVALID_CODE_ADDRESS;
			goto fail_any;
		}
	}
	
	/* Clear crash context. */
skip_done:
	sjme_debug_crashContext(NULL, NULL);
	
	/* Give remaining, if requested. */
	if (ticRemainder != NULL)
		*ticRemainder = remaining;
	return SJME_ERROR_NONE;
	
fail_any:
	/* Clear crash context. */
	sjme_debug_crashContext(NULL, NULL);

	/* Fail. */
	return sjme_error_vmError(inThread, sjme_error_default(error));
}
