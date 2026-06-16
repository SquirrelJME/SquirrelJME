/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/core/coreSerial.h"

sjme_errorCode sjme_scritchui_core_intern_objectNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* outCommon,
	sjme_attrInPositiveNonZero sjme_jint outCommonSize,
	sjme_attrInRange(0, SJME_SCRITCHUI_NUM_UI_TYPES)
		sjme_scritchui_uiType uiType,
	sjme_attrInNotNull sjme_scritchui_core_intern_objectNewImplFunc implNew,
	sjme_attrInNullable sjme_pointer inData)
{
	sjme_errorCode error;
	sjme_scritchui_uiCommon result;

	if (inState == NULL || outCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outCommonSize <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (implNew == NULL)
		return sjme_error_notImplemented(0);
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inState->pool,
		outCommonSize, NULL, (void**)&result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Pre-initialize. */
	result->magic = SJME_SCRITCHUI_OBJECT_MAGIC;
	result->type = uiType;
	result->state = inState;
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		result, SJME_JNI_FALSE, uiType)))
		goto fail_preInit;
	
	/* Setup common item. */
	if (sjme_error_is(error = implNew(inState, result,
		inData)))
		goto fail_new;
	
	/* Post-initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		result, SJME_JNI_TRUE, uiType)))
		goto fail_postInit;
	
	/* Success! */
	*outCommon = result;
	return SJME_ERROR_NONE;

fail_postInit:
fail_new:
fail_alloc:
fail_preInit:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

