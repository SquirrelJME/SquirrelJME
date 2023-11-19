/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/* Include Valgrind if it is available? */
#if defined(SJME_CONFIG_HAS_VALGRIND)
	#include <valgrind.h>
#endif

#include "sjme/alloc.h"
#include "sjme/debug.h"

jboolean sjme_alloc_poolMalloc(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInPositive jint size)
{
	sjme_todo("Implement this?");
	return JNI_FALSE;
}

jboolean sjme_alloc_poolStatic(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInNotNull void* baseAddr,
	sjme_attrInPositive jint size)
{
	sjme_todo("Implement this?");
	return JNI_FALSE;
}

jboolean sjme_alloc(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositive jint size,
	sjme_attrOutNotNull void** outAddr)
{
	sjme_todo("Implement this?");
	return JNI_FALSE;
}

jboolean sjme_allocFree(
	sjme_attrInNotNull void* addr)
{
	sjme_todo("Implement this?");
	return JNI_FALSE;
}

