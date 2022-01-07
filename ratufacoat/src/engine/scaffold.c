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

const sjme_engineScaffold* const sjme_engineScaffolds[] =
{
	&sjme_engineScaffoldSpringCoat,
	
	NULL
};

sjme_jboolean sjme_engineDestroy(sjme_engineState* state, sjme_error* error)
{
	if (state == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_engineNew(const sjme_engineConfig* config,
	sjme_engineState** outState, sjme_error* error)
{
	if (config == NULL || outState == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	sjme_todo("Implement this?");
	return sjme_false;
}
