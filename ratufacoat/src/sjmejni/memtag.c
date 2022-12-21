/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjmejni/memtag.h"
#include "debug.h"
#include "error.h"

/** Lower bit protection for the size. */
#define SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT_LOW INT32_C(0x40000000)

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

sjme_jboolean sjme_memTaggedNewGroup(sjme_memTagGroup** outPtr,
	sjme_error* error)
{
	if (outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Should be a zero initialized pointer. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_NOT_NULL, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memTaggedNewZ(sjme_memTagGroup* group, void*** outPtr,
	sjme_jsize size, sjme_memTagType tagType, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	if (group == NULL || outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Protect values must be the same size as pointers, this is to detect */
	/* cases where tag pointers are not used correctly. */
	if (protectA != sizeof(void*) || protectB != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_TAG_VIOLATION, 0);

	/* Make sure the correct sizeof() is used and that the value is not */
	/* erroneously zero. */
	if (((size & SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT) !=
		SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT) ||
		(size & SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT_LOW) != 0)
		return sjme_setErrorF(error, SJME_ERROR_TAGGED_WRONG_SIZE_OF, 0);

	/* We should not smash a pointer that was here already. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_TAG_NOT_NULL, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}
