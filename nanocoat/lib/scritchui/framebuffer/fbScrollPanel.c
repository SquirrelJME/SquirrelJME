/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_fb_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScrollPanel inScrollPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiScrollPanel wrappedPanel;
	
	if (inState == NULL || inScrollPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Create a wrapped panel. */
	wrappedPanel = NULL;
	if (sjme_error_is(error = wrappedState->apiInThread->scrollPanelNew(
		wrappedState, &wrappedPanel)) ||
		wrappedPanel == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inScrollPanel, wrappedPanel)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
