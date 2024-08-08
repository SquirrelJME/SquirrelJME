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
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar,
	sjme_attrInNullable sjme_pointer ignored)
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
	if (sjme_error_is(error = wrappedState->apiInThread->menuBarNew(
		wrappedState, &wrapped)) || wrapped == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inMenuBar, wrapped)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiMenuKind wrappedIntoMenu;
	sjme_scritchui_uiMenuKind wrappedChildItem;
	
	if (inState == NULL || intoMenu == NULL || childItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedIntoMenu =
		intoMenu->common.handle[SJME_SUI_FB_H_WRAPPED];
	wrappedChildItem =
		childItem->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward. */
	return wrappedState->apiInThread->menuInsert(wrappedState,
		wrappedIntoMenu, atIndex, wrappedChildItem);
}

sjme_errorCode sjme_scritchui_fb_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem,
	sjme_attrInNotNull const sjme_scritchui_impl_initParamMenuItem* init)
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
	if (sjme_error_is(error = wrappedState->apiInThread->menuItemNew(
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
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu,
	sjme_attrInNullable sjme_pointer ignored)
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
	if (sjme_error_is(error = wrappedState->apiInThread->menuNew(
		wrappedState, &wrapped)) || wrapped == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inMenu, wrapped)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiMenuKind wrappedFromMenu;
	
	if (inState == NULL || fromMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedFromMenu =
		fromMenu->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward. */
	return wrappedState->apiInThread->menuRemove(wrappedState,
		wrappedFromMenu, atIndex);
}
