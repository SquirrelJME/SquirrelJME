/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "memio/memdirect.h"
#include "debug.h"
#include "error.h"

sjme_jboolean sjme_memDirectFree(void** inPtr, sjme_error* error)
{
	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memDirectNew(void** outPtr, sjme_jsize size,
	sjme_error* error)
{
	if (outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	if (size <= 0)
		return sjme_setErrorF(error, SJME_ERROR_NEGATIVE_SIZE, 0);

	/* Should be a zero initialized pointer. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_NOT_NULL, 0);


	sjme_todo("Implement this?");
	return sjme_false;
}

