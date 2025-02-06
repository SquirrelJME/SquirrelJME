/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/frontEnd.h"
#include "sjme/debug.h"

sjme_errorCode sjme_frontEnd_bind(
	sjme_attrInNotNull sjme_pointer owner,
	sjme_attrInOutNotNull sjme_frontEnd* frontEnd,
	sjme_attrOutNotNull sjme_pointer* resultData)
{
	if (owner == NULL || frontEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The front end is always in the owner structure. */
	if ((sjme_intPointer)frontEnd < (sjme_intPointer)owner)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_frontEnd_release(
	sjme_attrInNotNull sjme_pointer owner,
	sjme_attrInOutNotNull sjme_frontEnd* frontEnd)
{
	if (owner == NULL || frontEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The front end is always in the owner structure. */
	if ((sjme_intPointer)frontEnd < (sjme_intPointer)owner)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
