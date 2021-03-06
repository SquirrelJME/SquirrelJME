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
};

sjme_returnFail sjme_memHandlesInit(sjme_memHandles** out, sjme_error* error)
{
	sjme_todo("sjme_memHandlesInit(%p, %p)", out, error);
}

sjme_returnFail sjme_memHandlesDestroy(sjme_memHandles* in, sjme_error* error)
{
	sjme_todo("sjme_memHandlesDestroy(%p, %p)", in, error);
}


sjme_returnFail sjme_memHandleNew(sjme_memHandles* handles,
	sjme_memHandle** out, sjme_memHandleKind kind, sjme_jint size,
	sjme_error* error)
{
	sjme_todo("sjme_memHandleNew(%p, %p, %d, %d, %p)",
		handles, out, kind, size, error);
}

sjme_returnFail sjme_memHandleDelete(sjme_memHandles* handles,
	sjme_memHandle* handle, sjme_error* error)
{
	sjme_todo("sjme_memHandleDelete(%p, %p, %p)",
		handles, handle, error);
}
