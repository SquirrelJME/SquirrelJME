/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/seekable.h"
#include "sjme/debug.h"

sjme_errorCode sjme_seekable_fromMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull void* base,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t rawBase;

	if (inPool == NULL || outSeekable == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Check for overflow. */
	rawBase = (uintptr_t)base;
	if (length < 0 || (rawBase + length) < rawBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
