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

sjme_errorCode sjme_scritchui_fb_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiPanel wrappedPanel;
	
	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedPanel = inPanel->component.common.handle;
	
	/* Forward call. */
	return wrappedState->apiInThread->panelEnableFocus(wrappedState,
		wrappedPanel, enableFocus, defaultFocus);
}

sjme_errorCode sjme_scritchui_fb_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiPanel wrappedPanel;
	
	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Create a wrapped panel. */
	wrappedPanel = NULL;
	if (sjme_error_is(error = wrappedState->apiInThread->panelNew(
		wrappedState, &wrappedPanel)) ||
		wrappedPanel == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inPanel, wrappedPanel)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
