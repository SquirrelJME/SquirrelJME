/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "engine/scaffold.h"
#include "engine/taskmanager.h"
#include "frontend/frontfunc.h"
#include "memory.h"

const sjme_engineScaffold* const sjme_engineScaffolds[] =
{
	&sjme_engineScaffoldSpringCoat,
	
	NULL
};

sjme_jboolean sjme_engineDestroy(sjme_engineState* state, sjme_error* error)
{
	sjme_jboolean isOkay = sjme_true;
	
	if (state == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Count down the pack to hopefully destroy it. */
	if (state->romPack != NULL)
		isOkay &= sjme_counterDown(&state->romPack->counter,
			NULL, error);
	
	/* Free the final engine pointer. */
	isOkay &= sjme_free(state, error);
	
	/* Was destruction okay? */
	return isOkay;
}

/**
 * Enters the main entry point of the engine, this can be a specific class or
 * the specified main class on the command line.
 * 
 * @param engineState The state of the engine to load in.
 * @param error The error state.
 * @return If entering the main entry point was successful.
 * @since 2022/01/09
 */
static sjme_jboolean sjme_engineEnterMain(sjme_engineState* engineState,
	sjme_error* error)
{
	sjme_utfString* mainClass;
	sjme_mainArgs* mainArgs;
	sjme_classPath* classPath;
	
	if (engineState == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Use specific main class and starting arguments. */
	if (engineState->config.mainClass != NULL)
	{
		if (!sjme_charStarToUtf(&mainClass, 
			engineState->config.mainClass, error))
			return sjme_false;
		
		mainArgs = engineState->config.mainArgs;
		
		/* Resolve class path from a set of library strings. */
		if (!sjme_packClassPathFromCharStar(engineState->romPack,
			engineState->config.mainClassPath,
			&classPath, error))
			return sjme_keepErrorF(error, SJME_ERROR_INVALID_ARGUMENT, 0);
	}
	
	/* Use built-in launcher. */
	else
	{
		/* We need to resolve how we launch the launcher. */
		if (!sjme_packGetLauncherDetail(engineState->romPack,
			&mainClass, &mainArgs,
			&classPath, error))
		{
			sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 0);
			
			return sjme_false;
		}
	}
	
	/* If no arguments were set, use a default parameter. */
	if (mainArgs == NULL)
	{
		/* Attempt allocation. */
		mainArgs = sjme_malloc(sizeof(*mainArgs), error);
		if (mainArgs == NULL)
		{
					return sjme_setErrorF(error, SJME_ERROR_NO_MEMORY, 0);
		}
		
		/* Set no actual arguments used. */
		mainArgs->count = 0;
	}
	
	/* Initialize the main entry task and thread. */
	return sjme_engineTaskNew(engineState, classPath, mainClass, mainArgs,
		engineState->config.sysProps,
		SJME_PIPE_REDIRECT_TERMINAL,
		SJME_PIPE_REDIRECT_TERMINAL,
		SJME_PIPE_REDIRECT_TERMINAL,
		sjme_false,
		NULL, sjme_true, &engineState->mainTask,
		&engineState->mainThread,
		error);
}

sjme_jboolean sjme_engineNew(const sjme_engineConfig* inConfig,
	sjme_engineState** outState, sjme_error* error)
{
	sjme_engineState* result;
	const sjme_engineScaffold* tryScaffold;
	sjme_jint i, didPipes;
	sjme_error subError;
	sjme_engineScaffoldUnavailableType whyUnavailable;
	sjme_file* file;
	
	if (inConfig == NULL || outState == NULL ||
		inConfig->frontBridge == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Allocate base. */
	result = sjme_malloc(sizeof(*result), error);
	if (result == NULL)
		return sjme_false;
	
	/* Create a carbon copy of the config to use for everything. */
	result->config = *inConfig;
	
	/* Load the pack file, this way the engines need not do it themselves. */
	if (!sjme_packOpen(&result->romPack,
		result->config.romPointer, result->config.romSize, error))
	{
		/* Clean out. */
		sjme_engineDestroy(result, error);
		
		sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE,
			sjme_getError(error, 0));
		
		return sjme_false;
	}
	
	/* Go through each scaffold and attempt to use it. */
	for (i = 0;; i++)
	{
		/* Is there nothing left? */
		tryScaffold = sjme_engineScaffolds[i];
		if (tryScaffold == NULL)
			break;
		
		/* Not this named engine? */
		if (result->config.engineName != NULL &&
			0 != strcmp(result->config.engineName, tryScaffold->name))
			continue;
		
		/* Check if the engine is available before trying to use it. */
		memset(&subError, 0, sizeof(subError));
		whyUnavailable = SJME_ENGINE_SCAFFOLD_IS_AVAILABLE;
		if (tryScaffold->isAvailable == NULL ||
			tryScaffold->initEngine == NULL ||
			!tryScaffold->isAvailable(&whyUnavailable, result, &subError))
			continue;
		
		/* Should be available so stop here. */
		break;
	}
	
	/* Could not get an engine at all? */
	if (tryScaffold == NULL)
	{
		/* Clean out. */
		sjme_engineDestroy(result, error);
		
		sjme_setError(error, SJME_ERROR_ENGINE_NOT_FOUND,
			sjme_getError(error, 0));
		
		return sjme_false;
	}
	
	/* Initialize base pipes for the terminal output. */
	didPipes = 0;
	for (i = 0; i < SJME_NUM_STANDARD_PIPES; i++)
	{
		/* Open standard pipe file. */
		file = NULL;
		if (result->config.frontBridge->stdPipeFileOpen == NULL ||
			!result->config.frontBridge->stdPipeFileOpen(i,
				&file, error) || file == NULL)
		{
			if (!sjme_hasError(error))
				sjme_setError(error, SJME_ERROR_BAD_PIPE_INIT, i);
			
			break;
		}
		
		/* Open pipe that is associated with the given file. */
		if (!sjme_pipeNewFromFile(file, &result->stdPipes[i], error))
			break;
		
		/* Success! */
		didPipes++;
	}
	
	/* Perform base engine initialization and start the main task. */
	result->scaffold = tryScaffold;
	if (didPipes != SJME_NUM_STANDARD_PIPES ||
		!tryScaffold->initEngine(result, error) ||
		!sjme_engineEnterMain(result, error))
	{
		/* Clean out. */
		sjme_engineDestroy(result, error);
		
		sjme_setError(error, SJME_ERROR_ENGINE_INIT_FAILURE,
			sjme_getError(error, 0));
		
		return sjme_false;
	}
	
	/* Set initial ID for tasks and threads, so they do not start at zero. */
	sjme_memIo_atomicIntGetThenAdd(&result->nextTaskThreadId, 1);
	
	/* Initialize the standard pipes for terminal pipe usage. */
#if 0
	sjme_pipeInstance* stdPipes[SJME_NUM_STANDARD_PIPES];
#endif
	
	/* Initialization complete! */
	*outState = result;
	return sjme_true;
}
