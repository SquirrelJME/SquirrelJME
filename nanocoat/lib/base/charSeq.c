/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/charSeq.h"
#include "sjme/debug.h"

sjme_errorCode sjme_charSeq_charAt(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar* outChar)
{
	if (inSeq == NULL || outChar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inIndex < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (inSeq->api->charAt == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	return inSeq->api->charAt(inSeq, inIndex, outChar);
}

sjme_errorCode sjme_charSeq_deleteStatic(
	sjme_attrInNotNull sjme_charSeq* inOutSeq)
{
	sjme_errorCode error;
	
	if (inOutSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Already deleted? */
	if (inOutSeq->api == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* If there is a deletion function, then call it. */
	if (inOutSeq->api->delete != NULL)
		if (sjme_error_is(error = inOutSeq->api->delete(inOutSeq)))
			return sjme_error_default(error);
	
	/* Clear out. */
	memset(inOutSeq, 0, sizeof(*inOutSeq));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_length(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_jint* outLen)
{
	if (inSeq == NULL || outLen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inSeq->api->length == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	return inSeq->api->length(inSeq, outLen);
}
	
sjme_errorCode sjme_charSeq_newStatic(
	sjme_attrInNotNull sjme_charSeq* inOutSeq,
	sjme_attrInNotNull const sjme_charSeq_functions* inFunctions,
	sjme_attrInNullable sjme_pointer inOptContext,
	sjme_attrInNullable sjme_frontEnd* inOptFrontEnd)
{
	if (inOutSeq == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Clear state. */
	memset(inOutSeq, 0, sizeof(*inOutSeq));
	
	/* Fill in. */
	inOutSeq->api = inFunctions;
	inOutSeq->context = inOptContext;
	
	/* Copy front end data? */
	if (inOptFrontEnd != NULL)
		memmove(&inOutSeq->frontEnd, inOptFrontEnd,
			sizeof(*inOptFrontEnd));
	
	/* Success! */
	return SJME_ERROR_NONE;
}
