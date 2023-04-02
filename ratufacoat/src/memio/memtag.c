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
	sjme_memIo_tagGroupInternal* internal;
	sjme_memIo_tagGroupInternal* parentInternal;
	sjme_memIo_spinLockKey selfKey;
	sjme_memIo_spinLockKey parentKey;
	sjme_errorCode fail;

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

	/* Fill in information about ourselves. */
	internal = (*result);
	internal->freeFunc = freeFunc;
	internal->checkKey = SJME_MEMIO_GROUP_CHECK_KEY;
	sjme_memIo_lockInit(&internal->lock, error);

	/* Later operations could fail, so revert accordingly. */
	fail = SJME_ERROR_NONE;

	/* If this is a subgroup, add to the parent. */
	if (parent != NULL)
	{
		/* Operate on an easier pointer. */
		parentInternal = (*parent);

		/* Lock ourselves because if anything happens with the parent while */
		/* this one is being worked on, we do not want to mess this one up. */
		memset(&selfKey, 0, sizeof(selfKey));
		if (sjme_memIo_lock(&internal->lock, &selfKey, error))
		{
			/* Perform locking on the parent to add this one. */
			memset(&parentKey, 0, sizeof(parentKey));
			if (sjme_memIo_lock(&parentInternal->lock, &parentKey,
				error))
			{
				sjme_todo("Implement this?");
				if (sjme_true)
					return sjme_false;

				/* Unlock before leaving. */
				if (!sjme_memIo_unlock(&parentInternal->lock,
					&parentKey, error))
					fail = SJME_ERROR_INVALID_LOCK;
			}

			/* Failed to lock properly. */
			else
				fail = SJME_ERROR_INVALID_LOCK;

			/* Unlock before leaving. */
			if (!sjme_memIo_unlock(&internal->lock, &selfKey,
					error))
				fail = SJME_ERROR_INVALID_LOCK;
		}

		/* Failed to lock properly. */
		else
			fail = SJME_ERROR_INVALID_LOCK;
	}

	/* Fail? */
	if (fail != SJME_ERROR_NONE)
	{
		/* Free allocated memory so it is not wasted. */
		if (sjme_memIo_taggedFree(&result, error))
			fail = SJME_ERROR_INVALID_FREE_MEMORY;

		/* Use whatever resultant error happened. */
		return sjme_keepErrorF(error, fail, 0);
	}

	/* Set output. */
	*outPtr = result;
	return sjme_true;
}

sjme_jboolean sjme_memIo_taggedFreeZ(void*** inPtr, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	sjme_memIo_memTagInternal* internal;
	sjme_memIo_directChunk* directCheck;
	sjme_memIo_tagGroupInternal* groupInternal;
	sjme_memIo_spinLockKey groupKey;
	sjme_errorCode fail;

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
	internal = (sjme_memIo_memTagInternal*)(*inPtr);
	directCheck = NULL;
	if (internal->allocSize <= 0 ||
		internal->checkKey != (sjme_jsize)(
			SJME_MEMIO_TAG_CHECK_KEY ^ internal->allocSize) ||
		!sjme_memIo_directGetChunk(internal, &directCheck,
			error) || directCheck == NULL ||
		(internal->inGroup != NULL &&
			(*internal->inGroup)->checkKey != SJME_MEMIO_GROUP_CHECK_KEY))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_MEM_VIOLATION,
			internal->checkKey);

	/* Call the free function, before everything. */
	if (internal->freeFunc != NULL)
		if (!internal->freeFunc(&(*internal->inGroup), *inPtr, error))
			return sjme_keepErrorF(error, SJME_ERROR_FREE_FUNC_FAIL, 0);

	/* Could possibly fail. */
	fail = SJME_ERROR_NONE;

	/* Remove from owning group. */
	if (internal->inGroup != NULL)
	{
		/* Get internal group representation. */
		groupInternal = *internal->inGroup;

		/* Lock group. */
		memset(&groupKey, 0, sizeof(groupKey));
		if (sjme_memIo_lock(&groupInternal->lock, &groupKey, error))
		{
			/* Unlink from head of link, if this is the head. */
			if (groupInternal->firstLink == &internal->link)
				groupInternal->firstLink = internal->link.next;

			/* Unlink from connection chains, if they exist. */
			if (internal->link.prev != NULL)
				internal->link.prev->next = internal->link.next;
			if (internal->link.next != NULL)
				internal->link.next->prev = internal->link.prev;

			/* Clear out current chain. */
			internal->link.prev = NULL;
			internal->link.next = NULL;

			/* Unlock before leaving. */
			if (!sjme_memIo_unlock(&groupInternal->lock, &groupKey, error))
				fail = SJME_ERROR_INVALID_LOCK;
		}

		/* Failed to lock. */
		else
			fail = SJME_ERROR_INVALID_LOCK;
	}

	/* Free the memory. */
	if (!sjme_memIo_directFree(&internal, error))
		return sjme_keepErrorF(error, SJME_ERROR_INVALID_FREE_MEMORY, 0);

	/* Free successful. */
	*inPtr = NULL;
	return (fail == SJME_ERROR_NONE ? sjme_true : sjme_false);
}

sjme_jboolean sjme_memIo_taggedNewZ(sjme_memIo_tagGroup* group, void*** outPtr,
	sjme_jsize size, sjme_memIo_taggedFreeFuncType freeFunc, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	void** result;
	sjme_memIo_spinLockKey groupKey;
	sjme_memIo_memTagInternal* internal;
	sjme_memIo_tagGroupInternal* groupInternal;
	sjme_errorCode fail;

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

	/* Allocate unowned tag that will get bound into a group. */
	result = NULL;
	if (!sjme_memIo_taggedNewUnownedZ(&result, size, freeFunc, error,
		protectA, protectB) || result == NULL)
		return sjme_keepErrorF(error, SJME_ERROR_NO_MEMORY, 0);

	/* Internal tag that is messed with. */
	internal = (sjme_memIo_memTagInternal*)result;
	groupInternal = *group;

	/* Later operations could fail, so revert accordingly. */
	fail = SJME_ERROR_NONE;

	/* Lock on the parent group to splice this in. */
	memset(&groupKey, 0, sizeof(groupKey));
	if (sjme_memIo_lock(&groupInternal->lock, &groupKey, error))
	{
		/* Set owner to parent group. */
		internal->inGroup = group;

		/* Set initial link chain. */
		internal->link.thisLink = internal;

		/* If the group has no links, then it gets the first one. */
		if (groupInternal->firstLink == NULL)
			groupInternal->firstLink = &internal->link;

		/* Otherwise place into the linked list at the head. */
		else
		{
			internal->link.next = groupInternal->firstLink;
			groupInternal->firstLink->prev = &internal->link;
			groupInternal->firstLink = &internal->link;
		}

		/* Unlock before leaving. */
		if (!sjme_memIo_unlock(&groupInternal->lock, &groupKey,
				error))
			fail = SJME_ERROR_INVALID_LOCK;
	}

	/* Failed to lock properly. */
	else
		fail = SJME_ERROR_INVALID_LOCK;

	/* Failed? Clean up. */
	if (fail != SJME_ERROR_NONE)
	{
		/* Free allocated memory so it is not wasted. */
		if (sjme_memIo_taggedFree(&result, error))
			fail = SJME_ERROR_INVALID_FREE_MEMORY;

		/* Use whatever resultant error happened. */
		return sjme_keepErrorF(error, fail, 0);
	}

	/* Set result. */
	*outPtr = result;
	return sjme_true;
}

sjme_jboolean sjme_memIo_taggedNewUnownedZ(void*** outPtr,
	sjme_jsize size, sjme_memIo_taggedFreeFuncType freeFunc, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	sjme_memIo_memTagInternal* internal;
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
