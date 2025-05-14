/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/scritchaudio.h"
#include "lib/scritchaudio/scritchaudioIntern.h"
#include "lib/scritchaudio/softmix/softmixIntern.h"

static sjme_errorCode sjme_scritchaudio_core_initActual(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInNotNull const sjme_scritchaudio_implFunctions* inImplFunc,
	sjme_attrInNotNull sjme_scritchaudio wrappedStated)
{
	
	if (inPool == NULL || outState == NULL || inImplFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inImplFunc->apiInit == NULL)
		return sjme_error_notImplemented(0);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_core_destroy(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_core_init(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInNotNull const sjme_scritchaudio_implFunctions* inImplFunc)
{
	sjme_jboolean isSoftMixWrapper, needSoftMixWrapper;
	sjme_scritchaudio lower, higher;
	sjme_errorCode error;
	
	if (inPool == NULL || outState == NULL || inImplFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do we need the softmix wrapper? */
	isSoftMixWrapper = (inImplFunc == &sjme_scritchaudio_softmixFunctions);
	needSoftMixWrapper = !isSoftMixWrapper;
	
	/* Normal top-level initialization. */
	if (!needSoftMixWrapper)
		return sjme_scritchaudio_core_initActual(inPool, outState,
			initFrontEnd, inImplFunc, NULL);

	/* Initialize the lower level state. */
	lower = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_core_initActual(inPool,
		&lower, NULL, inImplFunc, NULL)) || lower == NULL)
		goto fail_initLower;

	/* Initialize upper wrapper. */
	higher = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_core_initActual(inPool,
		&higher, initFrontEnd, &sjme_scritchaudio_softmixFunctions, lower)) ||
		higher == NULL)
		goto fail_initHigher;

	/* Use the higher state. */
	*outState = higher;
	return SJME_ERROR_NONE;
	
fail_initHigher:
	if (higher != NULL)
		sjme_scritchaudio_core_destroy(higher);
fail_initLower:
	if (lower != NULL)
		sjme_scritchaudio_core_destroy(lower);
	return sjme_error_default(error);
}
