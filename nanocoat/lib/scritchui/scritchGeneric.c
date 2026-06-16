/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/core/coreGeneric.h"

sjme_errorCode sjme_scritchui_coreGeneric_componentNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiComponent* outComponent,
	sjme_attrInPositiveNonZero sjme_jint outComponentSize,
	sjme_attrInRange(0, SJME_SCRITCHUI_NUM_UI_TYPES)
		sjme_scritchui_uiType uiType,
	sjme_attrInNotNull sjme_scritchui_coreGeneric_componentNewImplFunc implNew,
	sjme_attrInNullable sjme_pointer inData)
{
	sjme_scritchui_uiComponent result;
	sjme_errorCode error;
	
	if (inState == NULL || outComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outComponentSize <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Missing? */
	if (implNew == NULL)
		return sjme_error_notImplemented(uiType);
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inState->pool,
		outComponentSize, NULL, (void**)&result, NULL)) ||
		result == NULL)
		goto fail_alloc;
	
	/* Pre-initialize. */
	if (sjme_error_is(error = inState->intern->initComponent(inState,
		result, SJME_JNI_FALSE, uiType)))
		goto fail_preInit;
	
	/* Setup native widget. */
	if (sjme_error_is(error = implNew(inState,
		result, inData)) ||
		result->common.handle[0] == NULL)
		goto fail_newWidget;
	
	/* Post-initialize. */
	if (sjme_error_is(error = inState->intern->initComponent(inState,
		result, SJME_JNI_TRUE, uiType)))
		goto fail_postInit;
	
	/* Success! */
	*outComponent = result;
	return SJME_ERROR_NONE;

fail_postInit:
fail_newWidget:
fail_alloc:
fail_preInit:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}
