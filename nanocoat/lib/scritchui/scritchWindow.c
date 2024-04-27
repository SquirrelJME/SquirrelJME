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
#include "sjme/alloc.h"

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
	if (sjme_error_is(error = sjme_alloc(inState->pool, sizeof(*result),
		&result)) || result == NULL)
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
	
	/* Success! */
	*outWindow = result;
	return SJME_ERROR_NONE;

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
