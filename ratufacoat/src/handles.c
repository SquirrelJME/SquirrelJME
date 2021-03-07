/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "handles.h"
#include "memory.h"

/**
 * Storage for all memory handles.
 * 
 * @since 2021/02/27
 */
struct sjme_memHandles
{
};

/**
 * Structure for a single memory handle.
 * 
 * @since 2021/02/27
 */
struct sjme_memHandle
{
	/** The identifier of the handle. */
	sjme_jint id;
	
	/** The kind of this handle. */
	sjme_memHandleKind kind;
	
	/** The reference count of the handle. */
	sjme_jint refCount;
	
	/** The length of this handle. */
	sjme_jint length;
};

sjme_returnFail sjme_memHandlesInit(sjme_memHandles** out, sjme_error* error)
{
	/* Cannot be null. */
	if (out == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return SJME_RETURN_FAIL;
	}
	
	sjme_todo("sjme_memHandlesInit(%p, %p)", out, error);
}

sjme_returnFail sjme_memHandlesDestroy(sjme_memHandles* handles,
	sjme_error* error)
{
	/* Cannot be null. */
	if (handles == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return SJME_RETURN_FAIL;
	}
	
	sjme_todo("sjme_memHandlesDestroy(%p, %p)", handles, error);
}

sjme_returnFail sjme_memHandleNew(sjme_memHandles* handles,
	sjme_memHandle** out, sjme_memHandleKind kind, sjme_jint size,
	sjme_error* error)
{
	/* Cannot be null. */
	if (handles == NULL || out == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return SJME_RETURN_FAIL;
	}
	
	/* Check size. */
	if (size < 0)
	{
		sjme_setError(error, SJME_ERROR_NEGATIVE_SIZE, size);
		return SJME_RETURN_FAIL;
	}
	
	/* Check kind. */
	if (kind <= SJME_MEMHANDLE_KIND_UNDEFINED ||
		kind >= SJME_MEMHANDLE_KIND_NUM_KINDS)
	{
		sjme_setError(error, SJME_ERROR_INVALID_MEMHANDLE_KIND, kind);
		return SJME_RETURN_FAIL;
	}
	
	sjme_todo("sjme_memHandleNew(%p, %p, %d, %d, %p)",
		handles, out, kind, size, error);
}

sjme_returnFail sjme_memHandleDelete(sjme_memHandles* handles,
	sjme_memHandle* handle, sjme_error* error)
{
	/* Cannot be null. */
	if (handles == NULL || handle == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return SJME_RETURN_FAIL;
	}
	
	sjme_todo("sjme_memHandleDelete(%p, %p, %p)",
		handles, handle, error);
}

sjme_returnFail sjme_memHandleInBounds(sjme_memHandle* handle, 
	sjme_jint offset, sjme_jint length, sjme_error* error)
{
	/* Cannot be null. */
	if (handle == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return SJME_RETURN_FAIL;
	}
	
	/* Check offset and length. */
	if (offset < 0 || length < 0 || (offset + length) > handle->length)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, offset);
		return SJME_RETURN_FAIL;
	}
	
	/* Is okay. */
	return SJME_RETURN_SUCCESS;
}

sjme_returnFail sjme_memHandleAccess(sjme_memHandle* handle,
	sjme_jboolean write, sjme_dataType type, sjme_jint* inOut,
	sjme_jint offset, sjme_error* error)
{
	/* Cannot be null. */
	if (handle == NULL || inOut == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return SJME_RETURN_FAIL;
	}
	
	sjme_todo("sjme_memHandleAccess(%p, %d, %d, %p, %d, %p)",
		handle, write, type, inOut, offset, error);
}

sjme_returnFail sjme_memHandleAccessWide(sjme_memHandle* handle,
	sjme_jboolean write, sjme_dataType type, sjme_jlong* inOut,
	sjme_jint offset, sjme_error* error)
{
	/* Cannot be null. */
	if (handle == NULL || inOut == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return SJME_RETURN_FAIL;
	}
	
	sjme_todo("sjme_memHandleAccess(%p, %d, %d, %p, %d, %p)",
		handle, write, type, inOut, offset, error);
}

