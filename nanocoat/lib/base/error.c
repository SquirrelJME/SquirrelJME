/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>

#include "sjme/error.h"

sjme_errorCode sjme_error_also(
	sjme_errorCode error, sjme_errorCode expression)
{
	/* Keep existing error. */
	if (sjme_error_is(error))
		return error;
	
	/* Return the error from the expression. */
	return expression;
}

sjme_errorCode sjme_error_alsoV(
	sjme_errorCode error, ...)
{
	va_list va;
	sjme_errorCode result, now;
	
	/* Start reading. */
	va_start(va, error);
	
	/* Read in until the end of sequence. */
	result = error;
	for (;;)
	{
		/* Read in next. */
		now = va_arg(va, sjme_errorCode);
		
		/* End? */
		if (now == SJME_NUM_ERROR_CODES)
			break;
		
		/* Perform the normal also call. */
		result = sjme_error_also(result, result);
	}
	
	/* End. */
	va_end(va);
	
	/* Return the given result. */
	return result;
}
	
sjme_errorCode sjme_error_alsoVEnd(void)
{
	/* The number of error codes is considered the end. */
	return SJME_NUM_ERROR_CODES;
}

sjme_jboolean sjme_error_is(
	sjme_errorCode error)
{
	return error < SJME_ERROR_NONE;
}

sjme_errorCode sjme_error_default(
	sjme_errorCode error)
{
	return sjme_error_defaultOr((error), SJME_ERROR_UNKNOWN);
}

sjme_errorCode sjme_error_defaultOr(
	sjme_errorCode error, sjme_errorCode otherwise)
{
	if (!sjme_error_is(error) || error == SJME_ERROR_UNKNOWN)
	{
		if (!sjme_error_is(otherwise))
			return SJME_ERROR_UNKNOWN;
		else
			return otherwise;
	}

	return error;
}
