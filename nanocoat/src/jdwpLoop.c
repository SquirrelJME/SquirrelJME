/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/jdwp.h"

/** The limit to the number of tasks that can exist for a packet read. */
#define SJME_JDWP_WAITING_TASK_LATCH (SJME_JDWP_MAX_WAITING_TASKS - 4)

static sjme_errorCode sjme_jdwp_taskPoll(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrOutNotNull sjme_jdwp_taskItem* outTask)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_jdwp_taskItem* check;
	
	if (session == NULL || outTask == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Grab the task lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&session->taskLock)))
		return sjme_error_default(error);

	/* Go through and find a task to use. */
	for (i = 0; i < SJME_JDWP_MAX_WAITING_TASKS; i++)
	{
		/* Nothing here? */
		check = &session->tasks[i];
		if (check->function == NULL)
			continue;

		/* Copy it and wipe the old slot. */
		memmove(outTask, check, sizeof(*outTask));
		memset(check, 0, sizeof(*check));

		/* We found something, so stop! */
		break;
	}

	/* Release the task lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&session->taskLock,
		NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jdwp_sessionPoll(
	sjme_attrInNotNull sjme_jdwp session)
{
	sjme_errorCode error;
	sjme_jdwp_packet* nextPacket;
	sjme_jdwp_taskItem nextTask;
	
	if (session == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Read in next packet, if available. */
	/* But only if there are still task spaces available, so we do not */
	/* flood. */
	nextPacket = NULL;
	if (sjme_atomic_sjme_jint_get(
		&session->awaitingTasks) < SJME_JDWP_WAITING_TASK_LATCH)
		if (sjme_error_is(error = sjme_jdwp_commReceive(session, &nextPacket)))
			return sjme_error_default(error);

	/* Is there an actual packet to process? */
	if (nextPacket != NULL)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Perform any JDWP tasks that need to be done. */
	for (;;)
	{
		/* Grab the next task. */
		memset(&nextTask, 0, sizeof(nextTask));
		if (sjme_error_is(error = sjme_jdwp_taskPoll(session, &nextTask)))
			return sjme_error_default(error);

		/* Nothing to do? Stop then. */
		if (nextTask.function == NULL)
			break;

		/* Execute the task. */
		if (sjme_error_is(error = nextTask.function(session,
			nextTask.packet, nextTask.extraData)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jdwp_taskPush(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrInNotNull sjme_jdwp_taskFunction function,
	sjme_attrInNullable sjme_jdwp_packet* packet,
	sjme_attrInValue sjme_intPointer extraData)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_jdwp_taskItem* check;
	
	if (session == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Grab the task lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&session->taskLock)))
		return sjme_error_default(error);
	
	/* Go through and find a task to use. */
	for (i = 0; i < SJME_JDWP_MAX_WAITING_TASKS; i++)
	{
		/* Something here? */
		check = &session->tasks[i];
		if (check->function != NULL)
			continue;

		/* Set data here. */
		check->function = function;
		check->packet = packet;
		check->extraData = extraData;

		/* Stop processing. */
		break;
	}
	
	/* Release the task lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&session->taskLock,
		NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}
