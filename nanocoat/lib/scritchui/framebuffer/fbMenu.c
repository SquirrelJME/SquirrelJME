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

sjme_errorCode sjme_scritchui_fb_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiMenuBar wrapped;
	
	if (inState == NULL || inMenuBar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Create a wrapped panel. */
	wrapped = NULL;
	if (sjme_error_is(error = wrappedState->api->menuBarNew(
		wrappedState, &wrapped)) || wrapped == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inMenuBar, wrapped)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiMenuItem wrapped;
	
	if (inState == NULL || inMenuItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Create a wrapped panel. */
	wrapped = NULL;
	if (sjme_error_is(error = wrappedState->api->menuItemNew(
		wrappedState, &wrapped)) || wrapped == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inMenuItem, wrapped)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiMenu wrapped;
	
	if (inState == NULL || inMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Create a wrapped panel. */
	wrapped = NULL;
	if (sjme_error_is(error = wrappedState->api->menuNew(
		wrappedState, &wrapped)) || wrapped == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inMenu, wrapped)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
