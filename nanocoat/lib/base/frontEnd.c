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
	sjme_attrInOutNotNull sjme_frontEndBindable* frontEnd,
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
	sjme_attrInOutNotNull sjme_frontEndBindable* frontEnd)
{
	sjme_errorCode error;
	
	if (owner == NULL || frontEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The front end is always in the owner structure. */
	if ((sjme_intPointer)frontEnd < (sjme_intPointer)owner)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Lock the front end*/
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&frontEnd->bindLock)))
		return sjme_error_is(error);

	/* Only if there is data or a wrapper, can we do an unbind. */
	error = SJME_ERROR_NONE;
	if (frontEnd->bindHandler != NULL)
		if (frontEnd->base.data != NULL || frontEnd->base.wrapper != NULL)
		{
			/* Call handler. */
			error = frontEnd->bindHandler(owner, frontEnd,
				&frontEnd->base.data,
				SJME_FRONTEND_RELEASE);
		}

	/* Unlock! */
	if (sjme_thread_spinLockRelease(&frontEnd->bindLock, NULL))
		return sjme_error_default(error);

	/* Failed or success? */
	if (sjme_error_is(error))
		return sjme_error_default(error);
	return error;
}
