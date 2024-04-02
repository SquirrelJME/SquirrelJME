/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "sjme/alloc.h"

sjme_errorCode sjme_scritchui_gtk2_apiInit(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState)
{
	sjme_errorCode error;
	sjme_scritchui state;

	if (inPool == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate state. */
	state = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool, sizeof(*state),
		&state)) || state == NULL)
		return sjme_error_default(error);
	
	/* Return resultant state. */
	*outState = state;
	return SJME_ERROR_NONE;
}
