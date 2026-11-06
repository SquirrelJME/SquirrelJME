/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/bytecode.h"
#include "sjme/util.h"
#include "sjme/stdGone.h"
#include "sjme/nvm/task.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"

/** The number of threads to grow by. */
#define SJME_NVM_SCHEDULE_GROW 8

/** The number of threads to clump together when scheduling. */
#define SJME_MAX_THREAD_CLUMP 16

static sjme_errorCode sjme_nvm_task_taskScheduleMove(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInRange(0, SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		sjme_nvm_threadScheduleMode modeFrom,
	sjme_attrInRange(0, SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		sjme_nvm_threadScheduleMode modeTo,
	sjme_attrInValue sjme_jboolean inject)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	sjme_nvm_threadSubSchedule* fromSub;
	sjme_nvm_threadSubSchedule* toSub;
	sjme_list(sjme_nvm_thread)* fromOrder;
	sjme_list(sjme_nvm_thread)* toOrder;
	sjme_nvm_threadScheduleMode wasMode;
	sjme_jint i, n, freeSlot;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (modeFrom < 0 || modeTo < 0 ||
		modeFrom >= SJME_NVM_THREAD_NUM_SCHEDULE_MODE ||
		modeTo >= SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* No change in schedule state? */
	wasMode = sjme_atomic_g(sjme_nvm_threadScheduleMode,
		&inThread->scheduleMode);
	if (wasMode == modeTo)
		return SJME_ERROR_NONE;

	/* We will be operating on this schedule. */
	schedule = inState->schedule;

	/* Is there an order to actually remove from? */
	fromSub = &schedule->mode[modeFrom];
	fromOrder = fromSub->order;
	if (modeFrom == SJME_NVM_THREAD_UNDEFINED_SCHEDULE ||
		modeFrom == SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		fromOrder = NULL;

	/* Remove from the old schedule first. */
	if (fromOrder != NULL && fromOrder->length > 0)
		for (i = 0, n = fromOrder->length; i < n; i++)
			if (fromOrder->elements[i] == inThread)
			{
				/* Set the schedule to undefined as it is not in one. */
				sjme_atomic_s(sjme_nvm_threadScheduleMode,
					&inThread->scheduleMode,
					SJME_NVM_THREAD_UNDEFINED_SCHEDULE);
				fromOrder->elements[i] = NULL;

				/* Move everything down so there are no gaps. */
				memmove(&fromOrder->elements[i],
					&fromOrder->elements[i + 1],
					sizeof(sjme_nvm_thread) * (n - i - 1));
				fromOrder->elements[n - 1] = NULL;

				/* Since we moved down, try this slot again. */
				i--;
			}

	/* Which schedule is this going into? */
	toSub = &schedule->mode[modeTo];
	toOrder = toSub->order;
	if (modeTo == SJME_NVM_THREAD_UNDEFINED_SCHEDULE ||
		modeTo == SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		goto skip_noPlace;

	/* Place into free slot, growing the list if needed. */
	if (sjme_error_is(error = sjme_list_injectGrow(inState->allocPool,
		SJME_NVM_SCHEDULE_GROW, &toOrder, &inThread, sjme_nvm_thread, 0)) ||
		toOrder == NULL)
		return sjme_error_default(error);

	/* If the list changed, update it. */
	if (toSub->order != toOrder)
		toSub->order = toOrder;

	/* Mark the thread as being scheduled in the given target. */
	sjme_atomic_s(sjme_nvm_threadScheduleMode,
		&inThread->scheduleMode, modeTo);

	/* Success! */
skip_noPlace:
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_taskScheduleDelete(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* No effect in multi-threading. */
	if (SJME_T_S(inThread)->threadModel == SJME_NVM_MLE_THREAD_MULTI)
		return SJME_ERROR_NONE;

	/* Cannot schedule explicit threads, they must be explicitly */
	/* executed when needed. */
	if ((inThread->flags & SJME_NVM_THREAD_IS_EXPLICIT) != 0)
		return SJME_ERROR_NONE;

	/* Ignore if already deleted. */
	if (sjme_atomic_g(sjme_nvm_threadScheduleMode, &inThread->scheduleMode) ==
		SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		return SJME_ERROR_NONE;

	/* Lock schedule. */
	schedule = inState->schedule;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&schedule->lock)))
		return sjme_error_default(error);
	
	/* Remove from both schedule states. */
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
		inThread, SJME_NVM_THREAD_SCHEDULED,
		SJME_NVM_THREAD_UNDEFINED_SCHEDULE,
		SJME_JNI_FALSE)))
		goto fail_remove;
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
		inThread, SJME_NVM_THREAD_UNSCHEDULED,
		SJME_NVM_THREAD_UNDEFINED_SCHEDULE,
		SJME_JNI_FALSE)))
		goto fail_remove;

	/* Unlock schedule. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&schedule->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_remove:
	sjme_thread_spinLockRelease(&schedule->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleIn(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* No effect in multi-threading. */
	if (SJME_T_S(inThread)->threadModel == SJME_NVM_MLE_THREAD_MULTI)
		return SJME_ERROR_NONE;

	/* Cannot schedule explicit threads, they must be explicitly */
	/* executed when needed. */
	if ((inThread->flags & SJME_NVM_THREAD_IS_EXPLICIT) != 0)
		return SJME_ERROR_NONE;

	/* Ignore if already scheduled. */
	if (sjme_atomic_g(sjme_nvm_threadScheduleMode, &inThread->scheduleMode) ==
			SJME_NVM_THREAD_SCHEDULED)
		return SJME_ERROR_NONE;

	/* Lock schedule. */
	schedule = inState->schedule;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&schedule->lock)))
		return sjme_error_default(error);
	
	/* Move to scheduled. */
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
		inThread, SJME_NVM_THREAD_UNSCHEDULED,
		SJME_NVM_THREAD_SCHEDULED,
		SJME_JNI_TRUE)))
		goto fail_move;

	/* Unlock schedule. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&schedule->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_move:
	sjme_thread_spinLockRelease(&schedule->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleNext(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_nvm_thread* runThread,
	sjme_attrOutNotNull sjme_jboolean* isTerminated)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	sjme_list(sjme_nvm_thread)* order;
	sjme_nvm_thread nextThread, checkThread, clumpThread;
	sjme_jboolean terminated, isRunning;
	sjme_jint i, n, mode, j, checkBonus;
	sjme_nvm_thread clump[SJME_MAX_THREAD_CLUMP];
	sjme_jint clumpAt;
	
	if (inState == NULL || runThread == NULL || isTerminated == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Default state. */
	nextThread = NULL;
	terminated = sjme_atomic_g(sjme_jint, &inState->terminating);
	
	/* Clear the clump. */
	memset(&clump, 0, sizeof(clump));
	clumpAt = 0;

	/* Terminating already? Or no-effect when multithreaded. */
	if (terminated || inState->threadModel == SJME_NVM_MLE_THREAD_MULTI)
	{
		*runThread = NULL;
		*isTerminated = terminated;
		return SJME_ERROR_NONE;
	}
	
	/* If no tasks are left alive, stop VM execution. */
	if (sjme_atomic_g(sjme_jint, &inState->numRunningTasks) <= 0)
	{
		*runThread = NULL;
		*isTerminated = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}

	/* This is used to determine what to get. */
	schedule = inState->schedule;
	
	/* Lock schedule. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&schedule->lock)))
		return sjme_error_default(error);

	/* Find next running, then waiting, thread that is scheduled. */
	for (mode = SJME_NVM_THREAD_SCHEDULED;
		mode > SJME_NVM_THREAD_UNDEFINED_SCHEDULE &&
		mode < SJME_NVM_THREAD_NUM_SCHEDULE_MODE; mode++)
	{
		/* Are there even any threads in this schedule set? */
		order = schedule->mode[mode].order;
		if (order == NULL || order->length <= 0)
			continue;
		
		/* Scan through the running or waiting set. */
		for (i = 0, n = order->length; i < n; i++)
		{
			/* There must be a thread here to check. */
			checkThread = order->elements[i];
			if (checkThread == NULL)
				continue;

			/* Can this thread even be run (not sleeping)? */
			isRunning = SJME_JNI_FALSE;
			if (sjme_error_is(error = sjme_nvm_task_taskScheduleYes(
				inState, checkThread, &isRunning)))
				goto fail_checkRunning;

			/* Thread is not actually running. */
			if (!isRunning)
			{
				/* If this thread is scheduled, move it into */
				/* unscheduled. */
				/* The thread should already be doing this, however */
				/* if this condition is met then we grabbed the lock before */
				/* that thread could move itself. */
				if (mode == SJME_NVM_THREAD_SCHEDULED)
					if (sjme_error_is(error =
						sjme_nvm_task_taskScheduleMove(inState,
							checkThread,
							SJME_NVM_THREAD_SCHEDULED,
							SJME_NVM_THREAD_UNSCHEDULED,
							SJME_JNI_FALSE)))
						goto fail_move;

				/* Try another thread. */
				continue;
			}
				
			/* This thread is being accepted, so clump it in. */
			if (clumpAt < SJME_MAX_THREAD_CLUMP)
				clump[clumpAt++] = checkThread;
				
			/* There are many threads running right now, so try to */
			/* evict one that has a lower schedule bonus. */
			else
			{
				/* We will be checking our own bonus much. */
				checkBonus = sjme_atomic_g(sjme_jint,
					&checkThread->scheduleBonus);
					
				/* Go through every clumped thread. */
				for (j = 0; j < SJME_MAX_THREAD_CLUMP; j++)
				{
					/* Skip blank slots, which should never happen. */
					clumpThread = clump[j];
					if (clumpThread == NULL)
						continue;
						
					/* Let a thread that has been waiting longer run */
					/* first. */
					if (checkBonus <= sjme_atomic_g(sjme_jint,
						&clumpThread->scheduleBonus))
						continue;
					
					/* The thread in the future clump run will never */
					/* get its schedule bonus bumped, so it has to */
					/* be done now otherwise a very unlucky low */
					/* priority thread will never run. */
					sjme_atomic_ga(sjme_jint,
						&clumpThread->scheduleBonus,
						sjme_atomic_g(sjme_jint,
							&clumpThread->priority));
							
					/* Replace it. */
					clump[j] = checkThread;
					break;
				}
			}
		}
	}
	
	/* Go through the clump again to find the best thread to run. */
	/* All the threads are in the run state, so we choose a thread with */
	/* the highest bonus. */
	nextThread = NULL;
	for (j = 0; j < clumpAt; j++)
	{
		/* Skip empty slots, should not happen but just in case. */
		clumpThread = clump[j];
		if (clumpThread == NULL)
			continue;
		
		/* Accept running this thread if there is no current thread */
		/* to run. Or if there is a higher bonus, which means */
		/* multiple threads can be run, they are lower priority. */
		if (nextThread == NULL || (nextThread != NULL &&
			sjme_atomic_g(sjme_jint,
				&clumpThread->scheduleBonus) > 
			sjme_atomic_g(sjme_jint,
				&nextThread->scheduleBonus)))
			nextThread = clumpThread;
	}

	/* A thread was selected to be run. */
	if (nextThread != NULL)
	{
		/* If we selected a thread, then move it to be scheduled. */
		/* Unless of course it is already scheduled. */
		if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
			nextThread,
			SJME_NVM_THREAD_UNSCHEDULED,
			SJME_NVM_THREAD_SCHEDULED,
			SJME_JNI_TRUE)))
			goto fail_move;
		
		/* Since this thread is about to run, it loses its entire bonus. */
		/* It also gets a penalty to its bonus so that scheduling is a */
		/* bit more fair as lower priority threads get their bonus */
		/* accumulated slower versus a higher priority thread which can */
		/* easily get back in. */
		sjme_atomic_s(sjme_jint, &nextThread->scheduleBonus,
			-sjme_atomic_g(sjme_jint, &nextThread->priority));
		
		/* Then every thread that could have run, which is not this thread */
		/* gets a schedule bonus so it gets a higher chance to run next */
		/* time. */
		for (j = 0; j < clumpAt; j++)
		{
			/* Skip empty slots, should not happen but just in case. */
			/* Naturally skip the thread we are about to run since it */
			/* just got its bonus wiped. */
			clumpThread = clump[j];
			if (clumpThread == NULL || clumpThread == nextThread)
				continue;
			
			/* This thread gets its priority as a bonus. */
			sjme_atomic_ga(sjme_jint,
				&clumpThread->scheduleBonus,
				sjme_atomic_g(sjme_jint,
					&clumpThread->priority));
		}
	}
	
	/* Release the schedule lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&schedule->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	*runThread = nextThread;
	*isTerminated = terminated;
	return SJME_ERROR_NONE;

fail_unlockState:
fail_lockState:
fail_move:
fail_checkRunning:
	sjme_thread_spinLockRelease(&schedule->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleOut(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInPositive sjme_jint msResting)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (msResting < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* No effect in multi-threading. */
	if (SJME_T_S(inThread)->threadModel == SJME_NVM_MLE_THREAD_MULTI)
		return SJME_ERROR_NONE;

	/* Cannot schedule explicit threads, they must be explicitly */
	/* executed when needed. */
	if ((inThread->flags & SJME_NVM_THREAD_IS_EXPLICIT) != 0)
		return SJME_ERROR_NONE;

	/* Ignore if already unscheduled. */
	if (sjme_atomic_g(sjme_nvm_threadScheduleMode, &inThread->scheduleMode) ==
			SJME_NVM_THREAD_UNSCHEDULED)
		return SJME_ERROR_NONE;

	/* Lock schedule. */
	schedule = inState->schedule;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&schedule->lock)))
		return sjme_error_default(error);
	
	/* Move to unscheduled. */
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
		inThread, SJME_NVM_THREAD_SCHEDULED,
		SJME_NVM_THREAD_UNSCHEDULED,
		SJME_JNI_TRUE)))
		goto fail_move;

	/* Unlock schedule. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&schedule->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_move:
	sjme_thread_spinLockRelease(&schedule->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleYes(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jboolean* isRunning)
{
	sjme_nvm_thread_startType start;
	sjme_nvm_task inTask;
	sjme_jint left;
	
	if (inState == NULL || inThread == NULL || isRunning == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Explicitly scheduled threads can never be scheduled. */
	if ((inThread->flags & SJME_NVM_THREAD_IS_EXPLICIT) != 0)
		goto skip_not;
	
	/* If this is a callback thread, only consider if it has at least */
	/* one actively running frame. */
	start = sjme_atomic_g(sjme_nvm_thread_startType, &inThread->start);
	if (start == SJME_NVM_THREAD_START_CALLBACK)
	{
		if (inThread->numFrames > 0)
			goto skip_yes;
	}

	/* If the thread is running, continue. */
	else if (start == SJME_NVM_THREAD_START_STANDARD)
		goto skip_yes;

	/* Thread is entering the finishing state, so it is stopping. */
	else if (start == SJME_NVM_THREAD_START_FINISHING)
	{
		/* Mark as finished. */
		sjme_atomic_cs(sjme_nvm_thread_startType, &inThread->start,
			SJME_NVM_THREAD_START_FINISHING,
			SJME_NVM_THREAD_START_FINISHED);

		/* This thread is awaiting termination. */
		inTask = SJME_T_K(inThread);
		sjme_atomic_ga(sjme_jint, 
			&inTask->numThreads[SJME_NVM_THREAD_COUNT_AWAIT_CLEANUP],
			1);

		/* If this is the main thread, then reduce count. */
		if (inThread->isMain)
			sjme_atomic_ga(sjme_jint, 
				&inTask->numThreads[SJME_NVM_THREAD_COUNT_MAIN], -1);

		/* Reduce total thread count. */
		sjme_atomic_ga(sjme_jint, 
			&inTask->numThreads[SJME_NVM_THREAD_COUNT_ALL], -1);

		/* Non-daemon or daemon thread? */
		if (SJME_NVM_THREAD_CHECK(inThread->flags, IS_DAEMON))
			sjme_atomic_ga(sjme_jint, 
				&inTask->numThreads[SJME_NVM_THREAD_COUNT_DAEMON], -1);
		else
		{
			/* How many threads are left? */
			left = sjme_atomic_ga(sjme_jint, 
				&inTask->numThreads[SJME_NVM_THREAD_COUNT_NORMAL], -1) - 1;

			/* If there are no threads left, then start termination. */
			if (left <= 0)
				sjme_atomic_cs(sjme_jint, &inTask->terminate,
					SJME_NVM_TERMINATE_NOT,
					SJME_NVM_TERMINATE_CLEANUP);
		}

		/* Cleanup must be called! */
		goto skip_yes;
	}

skip_not:
	*isRunning = SJME_JNI_FALSE;
	return SJME_ERROR_NONE;
	
skip_yes:
	*isRunning = SJME_JNI_TRUE;
	return SJME_ERROR_NONE;
}
