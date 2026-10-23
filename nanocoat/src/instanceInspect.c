/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/instance.h"
#include "sjme/nvm/instanceInspect.h"
#include "sjme/nvm/cleanup.h"

sjme_errorCode sjme_nvm_instance_arrayGet(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue)
{
	if (inArray == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (!sjme_nvm_isAR(inArray, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_CLASS_CAST;
	
	if (index < 0 || index >= inArray->e.length)
		return SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_arrayGetString(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_charSeq* outValue)
{
	if (inArray == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (!sjme_nvm_isAR(inArray, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_CLASS_CAST;
	
	if (index < 0 || index >= inArray->e.length)
		return SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_arraySet(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrInNotNull sjme_nvm_frame_gcCommit* commit,
	sjme_attrOutNotNull sjme_jvalueTyped* inValue)
{
	if (inArray == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (!sjme_nvm_isAR(inArray, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_CLASS_CAST;
	
	if (index < 0 || index >= inArray->e.length)
		return SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}
