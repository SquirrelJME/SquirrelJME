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
		return SJME_ERROR_NOT_IMPLEMENTED;
	
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
	
	/* Not implemented? */
	if (inState->impl->windowNew == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
		
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inState->pool,
		sizeof(*result), NULL, NULL, &result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Pre-initialize. */
	if (sjme_error_is(error = inState->intern->initComponent(inState,
		&result->component, SJME_JNI_FALSE,
		SJME_SCRITCHUI_TYPE_WINDOW)))
		goto fail_preInit;
	
	/* Forward call. */
	if (sjme_error_is(error = inState->impl->windowNew(inState,
		result)))
		goto fail_newWidget;
	
	/* Post-initialize. */
	if (sjme_error_is(error = inState->intern->initComponent(inState,
		&result->component, SJME_JNI_TRUE,
		SJME_SCRITCHUI_TYPE_WINDOW)))
		goto fail_postInit;
	
	/* Set close listener to use. */
	if (inState->impl->windowSetCloseListener != NULL)
		if (sjme_error_is(error = inState->impl->windowSetCloseListener(
			inState, result, sjme_scritchui_baseCloseListener, NULL)))
			goto fail_postCloseSet;
	
	/* Success! */
	*outWindow = result;
	return SJME_ERROR_NONE;

fail_postCloseSet:
fail_postInit:
fail_newWidget:
fail_preInit:
fail_alloc:
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
	sjme_scritchui_listener_close* infoUser;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get listener information. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inWindow, close);
	
	/* The core listener is always set, so we can just set this here */
	/* and any future size calls will use this callback. */
	infoUser->callback = inListener;
	if (copyFrontEnd != NULL)
		memmove(&infoUser->frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Success! */
	return SJME_ERROR_NONE;
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
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Clear menu bar first. */
	if (sjme_error_is(error = inState->impl->windowSetMenuBar(inState,
		inWindow, NULL)))
		return sjme_error_default(error);
	
	/* Set cleared state. */
	inWindow->menuBar = NULL;
	
	/* Set new menu bar? */
	if (inMenuBar != NULL)
	{
		/* Set new bar. */
		inWindow->menuBar = inMenuBar;
		
		/* Forward call. */
		if (sjme_error_is(error = inState->impl->windowSetMenuBar(
			inState, inWindow, inMenuBar)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
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
		return SJME_ERROR_NOT_IMPLEMENTED;
	
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
