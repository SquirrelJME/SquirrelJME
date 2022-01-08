/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "engine/scaffold.h"
#include "engine/springcoat/springcoat.h"

static sjme_jboolean sjme_springCoat_isAvailable(sjme_engineConfig* config,
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
		return sjme_false;
	
	/* Is a SpringCoat library. */
	return sjme_true;
}

const sjme_engineScaffold sjme_engineScaffoldSpringCoat =
{
	.name = "springcoat",
	.isAvailable = sjme_springCoat_isAvailable,
};
