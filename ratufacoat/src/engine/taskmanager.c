/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "engine/buffer.h"
#include "engine/taskmanager.h"
#include "memory.h"

/**
 * Helper to initialize all of the standard pipes accordingly.
 * 
 * @param stdInMode The mode for standard input.
 * @param stdOutMode The mode for standard output.
 * @param stdErrMode The mode for standard error.
 * @param error Any resultant error state.
 * @return If everything was a success.
 * @since 2022/05/22
 */
static sjme_jboolean sjme_taskManagerPipeInit(sjme_pipeRedirectType stdInMode,
	sjme_pipeRedirectType stdOutMode, sjme_pipeRedirectType stdErrMode,
	sjme_pipeInstance* (*terminalPipes)[SJME_NUM_STANDARD_PIPES],
	sjme_pipeInstance* (*outputPipes)[SJME_NUM_STANDARD_PIPES],
	sjme_buffer* (*outputBuffers)[SJME_NUM_STANDARD_PIPES],
	sjme_error* error)
{
	sjme_pipeRedirectType redirects[SJME_NUM_PIPE_REDIRECTS];
	sjme_jint i, j;
	sjme_jboolean weInit[SJME_NUM_PIPE_REDIRECTS];
	sjme_jboolean failing;

	/* These must be valid. */
	if (terminalPipes == NULL || outputPipes == NULL || outputBuffers == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}
	
	/* Setup standard keyed redirect types. */
	memset(redirects, 0, sizeof(redirects));
	redirects[SJME_STANDARD_PIPE_STDIN] = stdInMode;
	redirects[SJME_STANDARD_PIPE_STDOUT] = stdOutMode;
	redirects[SJME_STANDARD_PIPE_STDERR] = stdErrMode;

	/* Invalid parameters? */
	for (i = SJME_STANDARD_PIPE_STDIN; i < SJME_NUM_STANDARD_PIPES; i++)
		if (redirects[i] < SJME_PIPE_REDIRECT_DISCARD ||
			redirects[i] >= SJME_NUM_PIPE_REDIRECTS)
		{
			sjme_setError(error, SJME_ERROR_INVALID_ARGUMENT, redirects[i]);

			return sjme_false;
		}

	/* Initialize each instance in a loop. */
	failing = sjme_false;
	for (i = SJME_STANDARD_PIPE_STDIN; i < SJME_NUM_STANDARD_PIPES; i++)
	{
		/* If using terminal pipe, just use global pipe. */
		if (redirects[i] == SJME_PIPE_REDIRECT_TERMINAL)
		{
			/* This should not occur at all. */
			if ((*terminalPipes)[i] == NULL)
			{
				sjme_message("Missing terminalPipes[%d]?", i);
				
				failing = sjme_true;
				break;
			}
			
			(*outputPipes)[i] = (*terminalPipes)[i];
			continue;
		}

		/* Buffer pipes, will write to a given buffer accordingly. */
		else if (redirects[i] == SJME_PIPE_REDIRECT_BUFFER)
		{
			/* Allocate buffer? */
			if (!sjme_bufferNew(&(*outputBuffers)[i], -1, error))
			{
				failing = sjme_true;
				break;
			}

			/* Allocate pipe? */
			if (!sjme_pipeNewFromBuffer((*outputBuffers)[i],
					i == SJME_STANDARD_PIPE_STDIN,
					i != SJME_STANDARD_PIPE_STDIN,
					&(*outputPipes)[i], error))
			{
				failing = sjme_true;
				break;
			}
		}

		/* Discard? */
		else if (redirects[i] == SJME_PIPE_REDIRECT_DISCARD)
		{
			/* Setup discard pipe. */
			if (!sjme_pipeNewNull(i == SJME_STANDARD_PIPE_STDIN,
				i != SJME_STANDARD_PIPE_STDIN,
				&(*outputPipes)[i], error))
			{
				failing = sjme_true;
				break;
			}
		}
		
		/* Set that we initialized it, for potential cleanup on failure. */
		weInit[i] = sjme_true;
	}
	
	/* Cleanup on failure? */
	if (failing)
	{
		/* Wipe pipes we did not initialize. */
		for (j = SJME_STANDARD_PIPE_STDIN;
			j < SJME_NUM_STANDARD_PIPES; j++)
		{
			/* Skip pipes we did not initialize ourselves. */
			if (!weInit[i])
				continue;
			
			sjme_todo("Destroy pipe?");
			sjme_todo("Destroy buffer?");
		}
		
		return sjme_false;
	}
	
	/* Success! */
	return sjme_true;
}

sjme_jboolean sjme_engineTaskDestroy(sjme_engineTask* task, sjme_error* error)
{
	if (task == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_engineTaskNew(sjme_engineState* engineState,
	sjme_classPath* classPath, sjme_utfString* mainClass,
	sjme_mainArgs* mainArgs, sjme_systemPropertySet* sysProps,
	sjme_pipeRedirectType stdInMode, sjme_pipeRedirectType stdOutMode,
	sjme_pipeRedirectType stdErrMode, sjme_jboolean forkThread,
	sjme_profilerSnapshot* profiler, sjme_jboolean rootVm,
	sjme_engineTask** outTask, sjme_engineThread** outMainThread,
	sjme_error* error)
{
	sjme_engineTask* createdTask;
	
	if (engineState == NULL || classPath == NULL || mainClass == NULL ||
		mainArgs == NULL || outTask == NULL ||
		outMainThread == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	if (stdInMode < 0 || stdInMode >= SJME_NUM_PIPE_REDIRECTS ||
		stdOutMode < 0 || stdOutMode >= SJME_NUM_PIPE_REDIRECTS ||
		stdErrMode < 0 || stdErrMode >= SJME_NUM_PIPE_REDIRECTS)
	{
		sjme_setError(error, SJME_ERROR_INVALID_ARGUMENT, 0);
		return sjme_false;
	}
	
	/* If there are no system properties, just include nothing. */
	if (sysProps == NULL)
	{
		/* Allocate a blank one. */
		sysProps = sjme_malloc(SJME_SIZEOF_SYSTEM_PROPERTY_SET(0), error);
		
		/* Failed? */
		if (sysProps == NULL)
		{
			sjme_setError(error, SJME_ERROR_NO_MEMORY, 0);
			
			return sjme_false;
		}
	}
	
	/* Try to allocate the resultant object first. */
	createdTask = sjme_malloc(sizeof(*createdTask), error);
	if (createdTask == NULL)
	{
		sjme_setError(error, SJME_ERROR_NO_MEMORY, 0);
		return sjme_false;
	}
	
	/* Setup class path for our set of classes. */
	if (!sjme_classLoaderNew(&createdTask->classLoader,
		classPath, error))
	{
		/* Cleanup always as good as we can! */
		sjme_engineTaskDestroy(createdTask, error);
		
		return sjme_false;
	}
	
	/* Determine the ID for the task. */
	createdTask->id = sjme_memIo_atomicIntGetThenAdd(
		&engineState->nextTaskThreadId, 1);
	
	/* Use whatever profiler we specified. */
	createdTask->profiler = profiler;
	
	/* Setup every standard console pipe. */
	if (!sjme_taskManagerPipeInit(stdInMode, stdOutMode, stdErrMode,
		&engineState->stdPipes, &createdTask->stdPipes,
		&createdTask->stdBuffers, error))
	{
		/* Fail. */
		sjme_engineTaskDestroy(createdTask, error);
		
		return sjme_false;
	}
	
	sjme_todo("Setup task process.");
	
	/* Spawn initial thread. */
	sjme_todo("Spawn initial thread.");
	
	/* Bind task to engine. */
	sjme_todo("Bind task to engine.");
	
	/* If we are forking a thread, create it and start it, it runs always. */
	if (forkThread)
	{
		sjme_todo("Fork thread?");
	}
	
	sjme_todo("Implement this?");
	return sjme_false;
}
