/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/cleanup.h"

sjme_errorCode sjme_nvm_enqueueHandler(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_objectInit(
	sjme_attrInNotNull sjme_nvm_common inCommon,
	sjme_attrInValue sjme_nvm_structType inType)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	
	if (inCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inType <= SJME_NVM_STRUCTTYPE_UNKNOWN ||
		inType >= SJME_NVM_NUM_STRUCTTYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Must be a weak pointer. */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefGet(inCommon,
		&weak)) || weak == NULL)
		return sjme_error_default(error);
	
	/* And must have enqueue cleanup. */
	if (weak->enqueue != sjme_nvm_enqueueHandler)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Set type information. */
	inCommon->type = inType;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

