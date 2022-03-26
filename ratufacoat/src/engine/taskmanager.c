/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "engine/taskmanager.h"
#include "memory.h"

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
	sjme_classPath* classPath,
	sjme_utfString* mainClass, sjme_mainArgs* mainArgs,
	sjme_engineSystemPropertySet* sysProps,
	sjme_taskPipeRedirectType stdOutMode,
	sjme_taskPipeRedirectType stdErrMode, sjme_jboolean forkThread,
	sjme_jboolean rootVm, sjme_profilerSnapshot* profiler,
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
	
	if (stdOutMode < 0 || stdOutMode >= NUM_SJME_TASK_PIPE_REDIRECTS ||
		stdErrMode < 0 || stdErrMode >= NUM_SJME_TASK_PIPE_REDIRECTS)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, 0);
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
	
	/* Setup actual task itself. */
	sjme_todo("Setup task process.");
	
	/* Bind task to engine. */
	sjme_todo("Bind task to engine.");
	
	/* Bind task into the VM. */
	
	/* If we are forking a thread, create it and start it, it runs always. */
	if (forkThread)
	{
		sjme_todo("Fork thread?");
	}
	
	sjme_todo("Implement this?");
	return sjme_false;
}
