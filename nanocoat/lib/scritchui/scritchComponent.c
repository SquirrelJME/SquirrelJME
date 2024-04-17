/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_core_componentSetPaintListener(
	sjme_scritchui inState,
	sjme_scritchui_uiComponent inComponent,
	sjme_scritchui_paintListenerFunc inListener,
	sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui_uiPaintableBase* paint;
	sjme_frontEnd oldFrontEnd;
	sjme_scritchui_paintListenerFunc oldListener;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Soft poll. */
	if (inState->impl->loopSoftPoll != NULL)
		inState->impl->loopSoftPoll(inState);
	
	/* Only certain types can be painted on. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_PANEL:
			paint = &((sjme_scritchui_uiPanel)inComponent)->paint;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Set new listener. */
	oldListener = paint->listener;
	paint->listener = inListener;
	
	/* Copy old front end in the event of an error. */
	memmove(&oldFrontEnd, &paint->frontEnd, sizeof(sjme_frontEnd));
	
	/* Replace with new front end data before the call. */
	if (copyFrontEnd != NULL)
		memmove(&paint->frontEnd, copyFrontEnd,
			sizeof(sjme_frontEnd));
	
	/* Inform component of updated listener. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (inState->impl->componentSetPaintListener == NULL ||
		sjme_error_is(error = inState->impl->componentSetPaintListener(
			inState, inComponent, inListener, paint, copyFrontEnd)))
	{
		/* Error, copy old value back. */
		paint->listener = oldListener;
		memmove(&paint->frontEnd, &oldFrontEnd,
			sizeof(sjme_frontEnd));
		
		return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
