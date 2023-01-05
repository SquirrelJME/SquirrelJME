/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "memio/memdirectinternal.h"
#include "debug.h"
#include "error.h"

sjme_jboolean sjme_memIo_directGetChunk(void* ptr,
	sjme_memIo_directChunk** outChunk, sjme_error* error)
{
	sjme_memIo_directChunk* possible;

	if (ptr == NULL || outChunk == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Need to be null, so it is not overwritten. */
	if (*outChunk != NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_IS_NULL, 0);

	/* Get the possible position of the chunk data. */
	possible = (sjme_memIo_directChunk*)((sjme_jpointer)ptr -
		(sjme_jpointer)sizeof(sjme_memIo_directChunk));

	/* Check for possible corruption. */
	if (possible->size <= 0 || possible->magic !=
			(sjme_jsize)(SJME_MEMIO_DIRECT_CHUNK_MAGIC ^ (~possible->size)) ||
		memcmp(&possible->magic,
			&possible->data[possible->size], 4) != 0)
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION, 0);

	/* Is okay. */
	*outChunk = possible;
	return sjme_true;
}

sjme_jboolean sjme_memIo_directFreeR(void** inPtr, sjme_error* error,
	sjme_jsize protect)
{
	sjme_memIo_directChunk* chunk;
	sjme_jsize totalSize;

	if (inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Protector value should be sizeof pointer. */
	if (protect != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION, 0);

	/* Cannot already be null. */
	if (*inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_IS_NULL, 0);

	/* Try to get the chunk. */
	chunk = NULL;
	if (!sjme_memIo_directGetChunk(*inPtr, &chunk, error))
		return sjme_setErrorF(error, sjme_getError(error,
			SJME_ERROR_PROTECTED_MEM_VIOLATION), 0);

	/* Clear the chunk information that is stored. */
	totalSize = (sjme_jsize)sizeof(sjme_memIo_directChunk) + chunk->size + 4;
	memset(chunk, 0, totalSize);

	/* Free it. */
	free(chunk);

	/* Remove old reference and be successful. */
	*inPtr = NULL;
	return sjme_true;
}

sjme_jboolean sjme_memIo_directNewR(void** outPtr, sjme_jsize size,
	sjme_error* error, sjme_jsize protect)
{
	sjme_memIo_directChunk* result;
	sjme_jsize totalSize;

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
	totalSize = (sjme_jsize)sizeof(sjme_memIo_directChunk) + size + 4;
	result = malloc(totalSize);
	if (result == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NO_MEMORY, size);

	/* Clear out memory. */
	memset(result, 0, totalSize);

	/* Setup chunk. */
	result->size = size;
	result->magic = SJME_MEMIO_DIRECT_CHUNK_MAGIC ^ (~size);
	memmove(&result->data[size], &result->magic, 4);

	/* Use result. */
	*outPtr = (void*)&result->data[0];
	return sjme_true;
}

