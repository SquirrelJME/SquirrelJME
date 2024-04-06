/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_core_componentSetPaintListener(
	sjme_scritchui inState,
	sjme_scritchui_uiComponent inComponent,
	sjme_scritchui_paintListenerFunc inListener)
{
	sjme_errorCode error;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only certain types can be painted on. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_PANEL:
			((sjme_scritchui_uiPanel)inComponent)->paintListener = inListener;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Inform component of updated listener. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (inState->impl->componentSetPaintListener == NULL ||
		sjme_error_is(error = inState->impl->componentSetPaintListener(
			inState, inComponent, inListener)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
