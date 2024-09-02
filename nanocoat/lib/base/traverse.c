/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/traverse.h"
#include "sjme/debug.h"

sjme_errorCode sjme_traverse_clear(
	sjme_attrInNotNull sjme_traverse traverse)
{
	if (traverse == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_traverse_destroy(
	sjme_attrInNotNull sjme_traverse traverse)
{
	if (traverse == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_traverse_iterate(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrOutNotNull sjme_traverse_iterator* iterator)
{
	if (traverse == NULL || iterator == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_traverse_iterateNextR(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrOutNotNull sjme_traverse_iterator* iterator,
	sjme_attrOutNotNull sjme_pointer* leafValue,
	sjme_attrInPositiveNonZero sjme_jint leafLength,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits)
{
	if (traverse == NULL || iterator == NULL || leafValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (leafLength <= 0 || numBits <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_traverse_newR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_traverse* outTraverse,
	sjme_attrInPositiveNonZero sjme_jint elementSize,
	sjme_attrInPositiveNonZero sjme_jint maxElements)
{
	sjme_jint structSize, nodeSize, leafSize;
	sjme_jint storageSize, pointerBytes;
	sjme_traverse_storage* storage;
	
	if (inPool == NULL || outTraverse == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (elementSize <= 0 || maxElements <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Determine the size for nodes and leaves, then determine struct size. */
	nodeSize = sizeof(sjme_traverse_nodeNode);
	leafSize = sizeof(sjme_traverse_nodeLeaf) + elementSize;
	structSize = (nodeSize > leafSize ? nodeSize : leafSize);
	
	/* If not divisible by the pointer bits, round up. */
	/* The nodes themselves are pointers, but our container data might */
	/* also be pointers. */
	pointerBytes = (SJME_CONFIG_HAS_POINTER / 8);
	if ((structSize % pointerBytes) != 0)
		structSize += pointerBytes - (structSize % pointerBytes);
	
	/* Make sure calculated sizes are valid. */
	if (nodeSize <= 0 || leafSize <= 0 || structSize <= 0)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Then make sure we can actually store this many. */
	storageSize = structSize * maxElements;
	if (storageSize <= 0)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
	
sjme_errorCode sjme_traverse_putR(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrInNotNullBuf(leafLength) sjme_pointer leafValue,
	sjme_attrInPositiveNonZero sjme_jint leafLength,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits)
{
	if (traverse == NULL || leafValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (leafLength <= 0 || numBits <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_traverse_remove(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits)
{
	if (traverse == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (numBits <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
