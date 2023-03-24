/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "memio/memtag.h"
#include "debug.h"
#include "error.h"
#include "memio/memdirect.h"
#include "memio/memdirectinternal.h"
#include "memio/memtaginternal.h"

sjme_jboolean sjme_memIo_taggedGroupFree(sjme_memIo_tagGroup** inPtr,
	sjme_error* error)
{
	if (inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	if (*inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_DOUBLE_FREE, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memIo_taggedGroupNew(sjme_memIo_tagGroup* parent,
	sjme_memIo_tagGroup** outPtr, sjme_memIo_taggedFreeFuncType freeFunc,
	sjme_error* error)
{
	sjme_memIo_tagGroup* result;

	if (outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Should be a zero initialized pointer. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_NOT_NULL, 0);

	/* Allocate memory group. */
	result = NULL;
	if (!sjme_memIo_taggedNewUnowned(&result,
		sjme_memIo_taggedNewSizeOf(result), NULL, error))
		return sjme_keepErrorF(error, SJME_ERROR_NO_MEMORY, 0);

	sjme_todo("Implement this?");
	if (sjme_true)
		return sjme_false;

	/* Set output. */
	*outPtr = result;
	return sjme_true;
}

sjme_jboolean sjme_memIo_taggedFreeZ(void*** inPtr, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	sjme_memTagInternal* internal;
	sjme_memIo_directChunk* directCheck;

	if (inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Prevent double free of memory. */
	if (*inPtr == NULL || **inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_DOUBLE_FREE, 0);

	/* Protect values must be the same size as pointers, this is to detect */
	/* cases where tag pointers are not used correctly. */
	if (protectA != sizeof(void*) || protectB != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION, 0);

	/* Make sure the tag has not been corrupted. */
	internal = (sjme_memTagInternal*)(*inPtr);
	directCheck = NULL;
	if (internal->allocSize <= 0 ||
		internal->checkKey != (sjme_jsize)(
			SJME_MEMIO_TAG_CHECK_KEY ^ internal->allocSize) ||
		!sjme_memIo_directGetChunk(internal, &directCheck,
			error) || directCheck == NULL ||
		(internal->inGroup != NULL &&
			internal->inGroup->checkKey != SJME_MEMIO_GROUP_CHECK_KEY))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION,
			internal->checkKey);

	/* Call the free function, before everything. */
	if (internal->freeFunc != NULL)
		if (!internal->freeFunc(&internal->inGroup, *inPtr, error))
			return sjme_keepErrorF(error, SJME_ERROR_FREE_FUNC_FAIL, 0);

	/* Remove from owning group. */
	if (internal->inGroup != NULL)
	{
		sjme_todo("Implement this?");
		return sjme_false;
	}

	/* Free the memory. */
	if (!sjme_memIo_directFree(&internal, error))
		return sjme_keepErrorF(error, SJME_ERROR_INVALID_FREE_MEMORY, 0);

	/* Free successful. */
	*inPtr = NULL;
	return sjme_true;
}

sjme_jboolean sjme_memIo_taggedNewZ(sjme_memIo_tagGroup* group, void*** outPtr,
	sjme_jsize size, sjme_memIo_taggedFreeFuncType freeFunc, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	void** result;

	if (group == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Allocate unowned tag that will get bound into a group. */
	result = NULL;
	if (!sjme_memIo_taggedNewUnownedZ(&result, size, freeFunc, error,
		protectA, protectB) || result == NULL)
		return sjme_keepErrorF(error, SJME_ERROR_NO_MEMORY, 0);

	/* Fill in group information. */
	sjme_todo("Implement this?");
	if (sjme_true)
		return sjme_false;

	/* Set result. */
	*outPtr = result;
	return sjme_true;
}

sjme_jboolean sjme_memIo_taggedNewUnownedZ(void*** outPtr,
	sjme_jsize size, sjme_memIo_taggedFreeFuncType freeFunc, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	sjme_memTagInternal* internal;
	sjme_jsize fixedSize;

	if (outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Protect values must be the same size as pointers, this is to detect */
	/* cases where tag pointers are not used correctly. */
	if (protectA != sizeof(void*) || protectB != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION, 0);

	/* Make sure the correct sizeof() is used and that the value is not */
	/* erroneously zero. */
	if (((size & SJME_MEMIO_NEW_TAGGED_PROTECT) !=
			SJME_MEMIO_NEW_TAGGED_PROTECT) ||
		(size & SJME_MEMIO_NEW_TAGGED_PROTECT_LOW) != 0)
		return sjme_setErrorF(error, SJME_ERROR_TAGGED_WRONG_SIZE_OF, 0);

	/* We should not smash a pointer that was here already. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_TAG_NOT_NULL, 0);

	/* Allocate internal memory. */
	internal = NULL;
	fixedSize = sjme_memIo_taggedNewUnSizeOf(size);
	if (!sjme_memIo_directNew(&internal, sizeof(*internal) + fixedSize,
		error) || internal == NULL)
		return sjme_keepErrorF(error, SJME_ERROR_NO_MEMORY, 0);

	/* Fill in information. */
	sjme_memIo_atomicPointerSet(&internal->pointer,
		&internal->data[0]);
	internal->allocSize = fixedSize;
	internal->checkKey = SJME_MEMIO_TAG_CHECK_KEY ^ fixedSize;
	internal->freeFunc = freeFunc;

	/* Return this internal tag. */
	*outPtr = (void**)internal;
	return sjme_true;
}
