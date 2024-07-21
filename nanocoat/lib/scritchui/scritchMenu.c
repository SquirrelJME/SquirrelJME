/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/core.h"
#include "sjme/alloc.h"

sjme_errorCode sjme_scritchui_core_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar)
{
	sjme_errorCode error;
	sjme_scritchui_uiMenuBar result;

	if (inState == NULL || outMenuBar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Missing? */
	if (inState->impl->menuBarNew == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inState->pool,
		sizeof(*result), NULL, NULL, &result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Pre-initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		result, SJME_JNI_FALSE,
		SJME_SCRITCHUI_TYPE_MENU_BAR)))
		goto fail_preInit;
	
	/* Setup native widget. */
	if (sjme_error_is(error = inState->impl->menuBarNew(inState,
		result)) || result->common.handle == NULL)
		goto fail_new;
	
	/* Post-initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		result, SJME_JNI_TRUE,
		SJME_SCRITCHUI_TYPE_MENU_BAR)))
		goto fail_postInit;
	
	/* Success! */
	*outMenuBar = result;
	return SJME_ERROR_NONE;

fail_postInit:
fail_new:
fail_alloc:
fail_preInit:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem)
{
	sjme_errorCode error;
	sjme_scritchui_uiMenuItem result;

	if (inState == NULL || outMenuItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Missing? */
	if (inState->impl->menuItemNew == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inState->pool,
		sizeof(*result), NULL, NULL, &result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Pre-initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		result, SJME_JNI_FALSE,
		SJME_SCRITCHUI_TYPE_MENU_ITEM)))
		goto fail_preInit;
	
	/* Setup native widget. */
	if (sjme_error_is(error = inState->impl->menuItemNew(inState,
		result)) || result->common.handle == NULL)
		goto fail_new;
	
	/* Post-initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		result, SJME_JNI_TRUE,
		SJME_SCRITCHUI_TYPE_MENU_ITEM)))
		goto fail_postInit;
	
	/* Success! */
	*outMenuItem = result;
	return SJME_ERROR_NONE;

fail_postInit:
fail_new:
fail_alloc:
fail_preInit:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu)
{
	sjme_errorCode error;
	sjme_scritchui_uiMenu result;

	if (inState == NULL || outMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Missing? */
	if (inState->impl->menuNew == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inState->pool,
		sizeof(*result), NULL, NULL, &result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Pre-initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		result, SJME_JNI_FALSE,
		SJME_SCRITCHUI_TYPE_MENU)))
		goto fail_preInit;
	
	/* Setup native widget. */
	if (sjme_error_is(error = inState->impl->menuNew(inState,
		result)) || result->common.handle == NULL)
		goto fail_new;
	
	/* Post-initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		result, SJME_JNI_TRUE,
		SJME_SCRITCHUI_TYPE_MENU)))
		goto fail_postInit;
	
	/* Success! */
	*outMenu = result;
	return SJME_ERROR_NONE;

fail_postInit:
fail_new:
fail_alloc:
fail_preInit:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}
