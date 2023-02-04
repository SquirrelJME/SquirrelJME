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
	sjme_memIo_tagGroup** outPtr, sjme_error* error)
{
	sjme_memIo_tagGroup* result;

	if (outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Should be a zero initialized pointer. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_NOT_NULL, 0);

	/* Allocate memory group. */
	result = NULL;
	if (!sjme_memIo_directNew((void**)&result, sizeof(*result), error))
		return sjme_setErrorF(error, SJME_ERROR_NO_MEMORY, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memIo_taggedFreeZ(void*** inPtr, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	if (inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Prevent double free of memory. */
	if (*inPtr == NULL || **inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_DOUBLE_FREE, 0);

	/* Protect values must be the same size as pointers, this is to detect */
	/* cases where tag pointers are not used correctly. */
	if (protectA != sizeof(void*) || protectB != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memIo_taggedNewZ(sjme_memIo_tagGroup* group, void*** outPtr,
	sjme_jsize size, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	if (group == NULL || outPtr == NULL)
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

	sjme_todo("Implement this?");
	return sjme_false;
}
