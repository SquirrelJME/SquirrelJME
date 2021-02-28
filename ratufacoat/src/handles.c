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
	
	/** The reference count of the handle. */
	sjme_jint refCount;
};

sjme_returnFail sjme_initMemHandles(sjme_memHandles** out, sjme_error* error)
{
	sjme_todo("sjme_initMemHandles(%p, %p)", out, error);
}

sjme_returnFail sjme_destroyMemHandles(sjme_memHandles* in, sjme_error* error)
{
	sjme_todo("sjme_destroyMemHandles(%p, %p)", in, error);
}

