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
#include "sjme/alloc.h"
#include "lib/scritchui/core/coreGeneric.h"

static sjme_errorCode sjme_scritchui_baseCloseListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow)
{
	sjme_scritchui_listener_close* infoUser;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get listener information. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inWindow, close);
	
	/* Call user callback if set. */
	if (infoUser->callback != NULL)
		return infoUser->callback(inState, inWindow);
	
	/* Do nothing, which destroys the window. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (width <= 0 || height <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Not implemented? */
	if (inState->impl->windowContentMinimumSize == NULL)
		return sjme_error_notImplemented(0);
	
	/* Store dimension set in the window. */
	inWindow->min.width = width;
	inWindow->min.height = height;
	
	/* Forward call. */
	return inState->impl->windowContentMinimumSize(inState, inWindow, width,
		height);
}

sjme_errorCode sjme_scritchui_core_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow)
{
	sjme_errorCode error;
	sjme_scritchui_uiWindow result;
	
	if (inState == NULL || outWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Use generic function. */
	result = NULL;
	if (sjme_error_is(error = sjme_scritchui_coreGeneric_componentNew(
		inState,
		(sjme_scritchui_uiComponent*)&result,
		sizeof(*result),
		SJME_SCRITCHUI_TYPE_WINDOW,
		(sjme_scritchui_coreGeneric_componentNewImplFunc)
			inState->impl->windowNew,
		NULL)) || result == NULL)
		goto fail_genericInit;
	
	/* Set close listener to use. */
	if (inState->impl->windowSetCloseListener != NULL)
		if (sjme_error_is(error = inState->impl->windowSetCloseListener(
			inState, result,
			sjme_scritchui_baseCloseListener, NULL)))
			goto fail_postCloseSet;
	
	/* Success! */
	*outWindow = result;
	return SJME_ERROR_NONE;

fail_postCloseSet:
fail_genericInit:
	if (result != NULL)
	{
		sjme_alloc_free(result);
		result = NULL;
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_windowSetCloseListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close))
{
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return inState->intern->setSimpleListener(
		inState,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			inWindow, close),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd);
}

sjme_errorCode sjme_scritchui_core_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar)
{
	sjme_errorCode error;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->windowSetMenuBar == NULL)
		return sjme_error_notImplemented(0);
	
	/* Setting a menu bar? */
	if (inMenuBar != NULL)
	{
		/* In the same exact window? */
		if (inMenuBar->window == inWindow && inWindow->menuBar == inMenuBar)
			return SJME_ERROR_NONE;
		
		/* Cannot add a menu bar that is attached to another window. */
		if (inMenuBar->window != NULL)
			return SJME_ERROR_ALREADY_IN_CONTAINER;
	}
	
	/* Clear menu bar first. */
	if (sjme_error_is(error = inState->impl->windowSetMenuBar(inState,
		inWindow, NULL)))
		return sjme_error_default(error);
	
	/* Set cleared state. */
	if (inWindow->menuBar != NULL)
		inWindow->menuBar->window = NULL;
	inWindow->menuBar = NULL;
	
	/* Set new menu bar? */
	if (inMenuBar != NULL)
	{
		/* Set new bar. */
		inWindow->menuBar = inMenuBar;
		inMenuBar->window = inWindow;
		
		/* Forward call. */
		if (sjme_error_is(error = inState->impl->windowSetMenuBar(
			inState, inWindow, inMenuBar)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_windowSetMenuItemActivateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(menuItemActivate))
{
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return inState->intern->setSimpleListener(
		inState,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			inWindow, menuItemActivate),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd);
}

sjme_errorCode sjme_scritchui_core_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->windowSetVisible == NULL)
		return sjme_error_notImplemented(0);
	
	/* Forward call. */
	return inState->impl->windowSetVisible(inState, inWindow, isVisible);
}

sjme_errorCode sjme_scritchui_core_intern_updateVisibleWindow(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Windows are container components, so we do not need to do anything */
	/* unique at all. */
	return inState->intern->updateVisibleContainer(inState,
		(sjme_scritchui_uiComponent)inWindow, isVisible);
}
