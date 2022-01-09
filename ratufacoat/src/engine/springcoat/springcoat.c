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
#include "engine/springcoat/springcoat.h"

/**
 * Initializes the SpringCoat engine.
 * 
 * @param partialEngine The partially initialized engine state.
 * @param error The output error state.
 * @return Will return @c sjme_true if the engine was successfully
 * initialized. 
 * @since 2022/01/08
 */
static sjme_jboolean sjme_springCoat_initEngine(
	sjme_engineState* partialEngine, sjme_error* error)
{
	if (partialEngine == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	sjme_todo("Implement this?");
	return sjme_false;
}

/**
 * Checks if the SpringCoat engine is available or not.
 * 
 * @param why Optional output reason for why a ROM is not available.
 * @param partialEngine The partially loaded engine state.
 * @param error The error state.
 * @return Will return @c sjme_true if the engine is available.
 * @since 2022/01/08
 */
static sjme_jboolean sjme_springCoat_isAvailable(
		sjme_engineScaffoldUnavailableType* why,
		sjme_engineState* partialEngine, sjme_error* error)
{
	if (config == NULL || partialEngine == NULL ||
		partialEngine->romPack == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* If this was not flagged or is specifically marked as a SpringCoat
	 * library, then use it. */
	if (partialEngine->romPack->flags != 0 &&
		(partialEngine->romPack->flags & SJME_PACK_FLAG_IS_SPRINGCOAT) == 0)
	{
		if (why != NULL)
			*why = SJME_ENGINE_SCAFFOLD_INCOMPATIBLE_ROM;
		
		return sjme_false;
	}
	
	/* Is a SpringCoat library. */
	if (why != NULL)
		*why = SJME_ENGINE_SCAFFOLD_IS_AVAILABLE;
	
	return sjme_true;
}

const sjme_engineScaffold sjme_engineScaffoldSpringCoat =
{
	.name = "springcoat",
	.initEngine = sjme_springCoat_initEngine,
	.isAvailable = sjme_springCoat_isAvailable,
};
