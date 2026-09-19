/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/instanceProxy.h"

sjme_errorCode sjme_nvm_instance_proxyClassL(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_instance_proxyHandlerFunc handler,
	sjme_attrInNotNull sjme_list(sjme_jclass)* inInterfaces)
{
	if (contextThread == NULL || outClass == NULL || handler == NULL ||
		inInterfaces == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_proxyClassV(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_instance_proxyHandlerFunc handler,
	sjme_attrInNotNull sjme_jclass inInterfaces,
	sjme_attrInNotNull ...)
{
	if (contextThread == NULL || outClass == NULL || handler == NULL ||
		inInterfaces == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}