/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/traverse.h"
#include "sjme/debug.h"
#include "sjme/alloc.h"
#include "sjme/util.h"

static sjme_errorCode sjme_traverse_next(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrOutNotNull sjme_traverse_node** outNode)
{
	sjme_traverse_node* result;
	
	if (traverse == NULL || outNode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Is the tree already full? */
	if ((intptr_t)traverse->next > (intptr_t)traverse->end)
		return SJME_ERROR_TRAVERSE_FULL;
	
	/* Next node is the actual next node, make sure it is cleared. */
	result = traverse->next;
	memset(result, 0, traverse->structSize);
	
	/* Then shift forward to the next node. */
	traverse->next = SJME_POINTER_OFFSET(result, traverse->structSize);
	
	/* Success! */
	*outNode = result;
	return SJME_ERROR_NONE;
}

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
	
	/* Wipe entire structure. */
	memset(traverse, 0, sizeof(*traverse));
	
	/* Then free it. */
	return sjme_alloc_free(traverse);
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
	sjme_attrInPositiveNonZero sjme_jint leafLength,
	sjme_attrInPositiveNonZero sjme_jint maxElements)
{
	sjme_errorCode error;
	sjme_jint structSize, nodeSize, leafSize;
	sjme_jint storageSize, pointerBytes;
	sjme_traverse result;
	
	if (inPool == NULL || outTraverse == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (leafLength <= 0 || maxElements <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Determine the size for nodes and leaves, then determine struct size. */
	nodeSize = sizeof(sjme_traverse_nodeNode);
	leafSize = sizeof(sjme_traverse_nodeLeaf) + leafLength;
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
	
	/* Allocate entire tree with storage. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool,
		offsetof(sjme_traverse_base, storage) + storageSize,
		(sjme_pointer*)&result)) || result == NULL)
		goto fail_allocResult;
	
	/* Setup tree parameters. */
	result->structSize = structSize;
	result->leafLength = leafLength;
	result->storageBytes = storageSize;
	result->start = (sjme_traverse_node*)&result->storage[0];
	result->end = (sjme_traverse_node*)SJME_POINTER_OFFSET(result->start,
		storageSize - structSize);
	result->next = result->start;
	
	/* Success! */
	*outTraverse = result;
	return SJME_ERROR_NONE;
	
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_traverse_putR(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrInNotNullBuf(leafLength) sjme_pointer leafValue,
	sjme_attrInPositiveNonZero sjme_jint leafLength,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits)
{
	sjme_errorCode error;
	sjme_traverse_node* at;
	sjme_traverse_node** next;
	sjme_juint sh;
	sjme_jboolean finalShift;
	
	if (traverse == NULL || leafValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (leafLength <= 0 || numBits <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Wrong length for this tree? */
	if (traverse->leafLength != leafLength)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Is a root node needed? */
	if (traverse->root == NULL)
		if (sjme_error_is(error = sjme_traverse_next(traverse,
			&traverse->root)) || traverse->root == NULL)
			return sjme_error_default(error);
	
	/* Go through all bits in the shift. */
	at = traverse->root;
	for (sh = sjme_util_intOverShiftU(1, numBits) - 1;
		sh >= 1; sh >>= 1)
	{
		/* Is this the final shift? */
		finalShift = (sh == 1);
		
		/* If not the final shift, this must be a node. */
		if (!finalShift && at->leaf.key == SJME_TRAVERSE_LEAF_KEY)
			return SJME_ERROR_TREE_COLLISION;
		
		/* Are we going zero or one? */
		next = ((bits & sh) != 0 ? &at->node.one : &at->node.zero);
		
		/* If there is no node here, create it. */
		if ((*next) == NULL)
		{
			/* Allocate node. */
			if (sjme_error_is(error = sjme_traverse_next(traverse,
				next)) || (*next) == NULL)
				return sjme_error_default(error);
			
			/* If this is the last item, it becomes a leaf. */
			if (finalShift)
				(*next)->leaf.key = SJME_TRAVERSE_LEAF_KEY;
		}
		
		/* Go the next node. */
		at = (*next);
	}
	
	/* Nothing here? This means the tree might be corrupted. */
	if (at == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* The leaf key must be set to be a leaf! */
	if (at->leaf.key != SJME_TRAVERSE_LEAF_KEY)
		return SJME_ERROR_TREE_COLLISION;
	
	/* Initialize leaf. */
	at->leaf.key = SJME_TRAVERSE_LEAF_KEY;
	memmove(&at->leaf.value[0], leafValue, traverse->leafLength);
	
	/* Success! */
	return SJME_ERROR_NONE;
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
