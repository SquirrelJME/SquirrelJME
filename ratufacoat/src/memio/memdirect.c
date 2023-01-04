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

sjme_jboolean sjme_memDirectFreeR(void** inPtr, sjme_error* error,
	sjme_jsize protect)
{
	if (inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Protector value should be sizeof pointer. */
	if (protect != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION, 0);

	/* Cannot already be null. */
	if (*inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_IS_NULL, 0);

	/* Free it. */
	free(*inPtr);

	/* Remove old reference and be successful. */
	*inPtr = NULL;
	return sjme_true;
}

sjme_jboolean sjme_memDirectNewR(void** outPtr, sjme_jsize size,
	sjme_error* error, sjme_jsize protect)
{
	void* result;

	/* Cannot be null. */
	if (outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Cannot be zero or negative. */
	if (size <= 0)
		return sjme_setErrorF(error, SJME_ERROR_NEGATIVE_SIZE, 0);

	/* Protector value should be sizeof pointer. */
	if (protect != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION, 0);

	/* Should be a zero initialized pointer. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_NOT_NULL, 0);

	/* Attempt data allocation. */
	result = malloc(size);
	if (result == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NO_MEMORY, size);

	/* Clear out memory. */
	memset(result, 0, size);

	/* Use result. */
	*outPtr = result;
	return sjme_true;
}

