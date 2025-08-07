/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/taskCache.h"

sjme_errorCode sjme_nvm_cache_opCopy(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opIn,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opOut)
{
	if (inTread == NULL || opIn == NULL || opOut == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opDelete(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opInOut)
{
	if (inTread == NULL || opInOut == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opDeleteFlood(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInNotNull sjme_nvm_cache_flood* opInOut)
{
	if (inTread == NULL || opInOut == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opEvict(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opInOut)
{
	if (inTread == NULL || opInOut == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opEvictFlood(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_flood** opInOut)
{
	if (inTread == NULL || opInOut == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opIsoLocal(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrOutNotNull sjme_nvm_cache_slot** opOut,
	sjme_attrInRange(0, INT32_MAX) sjme_jint localIndex,
	sjme_attrInNotNull sjme_jvalueTyped* inValue)
{
	if (inTread == NULL || opOut == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (localIndex < 0)
		return SJME_ERROR_LOCAL_INDEX_INVALID;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opIsoStackPush(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrOutNotNull sjme_nvm_cache_slot** opOut,
	sjme_attrInNotNull sjme_jvalueTyped* inValue)
{
	if (inTread == NULL || opOut == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opLocalPush(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrOutNullable sjme_nvm_cache_slot** opIn,
	sjme_attrOutNullable sjme_nvm_cache_slot** opOut,
	sjme_attrInRange(0, INT32_MAX) sjme_jint localIndex)
{
	if (inTread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (localIndex < 0)
		return SJME_ERROR_LOCAL_INDEX_INVALID;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opStackPeek(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_flood* opInOut)
{
	if (inTread == NULL || opInOut == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opStackPop(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_flood* opInOut)
{
	if (inTread == NULL || opInOut == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_jvalueTyped* sjme_nvm_cache_slotValue(
	sjme_attrInNotNull sjme_nvm_cache_slot* inSlot)
{
	sjme_threadLocal(sjme_jvalueTyped, invalid);

	if (inSlot == NULL)
		goto fail_invalid;
	
	sjme_todo("Impl?");
	return &invalid;

	/* Always return a valid pointer somewhere. */
fail_invalid:
	memset(&invalid, 0, sizeof(invalid));
	invalid.t = SJME_JAVA_TYPE_ID_VOID;
	return &invalid;
}
