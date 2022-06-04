/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "error.h"

void sjme_clearError(sjme_error* error)
{
	if (error != NULL)
	{
		error->code = SJME_ERROR_NONE;
		error->value = 0;
		error->sourceFile = NULL;
		error->sourceLine = 0;
		error->sourceFunction = NULL;
	}
}

sjme_errorCode sjme_getError(sjme_error* error, sjme_errorCode ifMissing)
{
	if (error != NULL)
		return error->code;
	return ifMissing;
}

sjme_returnFail sjme_hasError(sjme_error* error)
{
	if (error != NULL && error->code != SJME_ERROR_NONE)
		return SJME_RETURN_FAIL;
	return SJME_RETURN_SUCCESS;
}

void sjme_setErrorR(sjme_error* error, sjme_errorCode code, sjme_jint value,
	const char* file, int line, const char* function)
{
	if (error != NULL)
	{
		error->code = code;
		error->value = value;
		error->sourceFile = file;
		error->sourceLine = line;
		error->sourceFunction = function;
	}

	/* Print error state being set. */
#if defined(SJME_DEBUG)
	sjme_messageR(NULL, -1, NULL, "setError(%d, %d) at %s:%d (%s()).",
		code, value, file, line, function);
#endif
}
