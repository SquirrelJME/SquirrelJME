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
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opDelete(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opInOut)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opDeleteFlood(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInNotNull sjme_nvm_cache_flood* opInOut)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opEvict(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_slot** opInOut)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opEvictFlood(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_flood** opInOut)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opIsoLocal(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrOutNotNull sjme_nvm_cache_slot** opOut,
	sjme_attrInRange(0, INT32_MAX) sjme_jint localIndex,
	sjme_attrInNotNull sjme_jvalueTyped* inValue)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opIsoStackPush(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrOutNotNull sjme_nvm_cache_slot** opOut,
	sjme_attrInNotNull sjme_jvalueTyped* inValue)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opLocalPush(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrOutNullable sjme_nvm_cache_slot** opIn,
	sjme_attrOutNullable sjme_nvm_cache_slot** opOut,
	sjme_attrInRange(0, INT32_MAX) sjme_jint localIndex)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opStackPeek(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_flood* opInOut)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_cache_opStackPop(
	sjme_attrInNotNull sjme_nvm_cache_tread* inTread,
	sjme_attrInOutNotNull sjme_nvm_cache_flood* opInOut)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
