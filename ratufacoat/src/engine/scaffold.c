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
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Count down the pack to hopefully destroy it. */
	if (state->romPack != NULL)
		isOkay &= sjme_counterDown(&state->romPack->counter,
			NULL, error);
	
	/* Free the final engine pointer. */
	isOkay &= sjme_free(state, error);
	
	/* Was destruction okay? */
	return isOkay;
}

sjme_jboolean sjme_engineNew(const sjme_engineConfig* inConfig,
	sjme_engineState** outState, sjme_error* error)
{
	sjme_engineState* result;
	const sjme_engineScaffold* tryScaffold;
	sjme_jint i;
	sjme_error subError;
	sjme_engineScaffoldUnavailableType whyUnavailable;
	
	if (inConfig == NULL || outState == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
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
	
	/* Perform base engine initialization. */
	if (!tryScaffold->initEngine(result, error))
	{
		/* Clean out. */
		sjme_engineDestroy(result, error);
		
		sjme_setError(error, SJME_ERROR_ENGINE_INIT_FAILURE,
			sjme_getError(error, 0));
		
		return sjme_false;
	}
	
	sjme_todo("Implement this?");
	
	/* Initialization complete! */
	*outState = result;
	return sjme_true;
}
