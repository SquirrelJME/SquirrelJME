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

static sjme_errorCode sjme_traverse_traverse(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrOutNotNull sjme_traverse_node** outNode,
	sjme_attrOutNullable sjme_traverse_node** outParent,
	sjme_attrInValue sjme_jboolean create,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits)
{
	sjme_errorCode error;
	sjme_traverse_node* at;
	sjme_traverse_node* atParent;
	sjme_traverse_node** next;
	sjme_juint sh;
	sjme_jboolean finalShift;
	
	if (traverse == NULL || outNode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Go through all bits in the shift. */
	at = traverse->root;
	atParent = traverse->root;
	for (sh = sjme_util_intOverShiftU(1, numBits) - 1;
		sh >= 1 && at != NULL; sh >>= 1)
	{
		/* Is this the final shift? */
		finalShift = (sh == 1);
		
		/* Are we at a leaf? We should never be on one. */
		if (at->leaf.key == SJME_TRAVERSE_LEAF_KEY)
			return (create ? SJME_ERROR_TREE_COLLISION :
				SJME_ERROR_NO_SUCH_ELEMENT);
		
		/* Are we going zero or one? */
		next = ((bits & sh) != 0 ? &at->node.one : &at->node.zero);
		
		/* If there is no node here, create it, possibly. */
		if ((*next) == NULL)
		{
			/* Not creating, so fail here. */
			if (!create)
				return SJME_ERROR_NO_SUCH_ELEMENT;
			
			/* Allocate node. */
			if (sjme_error_is(error = sjme_traverse_next(traverse,
				next)) || (*next) == NULL)
				return sjme_error_default(error);
			
			/* If this is the last item, it becomes a leaf. */
			if (finalShift)
				(*next)->leaf.key = SJME_TRAVERSE_LEAF_KEY;
		}
		
		/* Go the next node. */
		atParent = at;
		at = (*next);
	}
	
	/* Nothing here? This means the tree might be corrupted, or it may */
	/* actually be missing. */
	if (at == NULL)
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* Return the target node. */
	*outNode = at;
	if (outParent != NULL)
		*outParent = atParent;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_traverse_clear(
	sjme_attrInNotNull sjme_traverse traverse)
{
	if (traverse == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Reset nodes to initial state. */
	traverse->root = NULL;
	traverse->next = traverse->start;
	memset(&traverse->storage[0], 0, traverse->storageBytes);
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	
	/* Start traversal. */
	memset(iterator, 0, sizeof(*iterator));
	iterator->atNode = traverse->root;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_traverse_iterateNextR(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrOutNotNull sjme_traverse_iterator* iterator,
	sjme_attrOutNotNull sjme_pointer* leafValue,
	sjme_attrInPositiveNonZero sjme_jint leafLength,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits)
{
	sjme_traverse_node* at;
	sjme_traverse_node** next;
	sjme_juint sh;
	sjme_jboolean finalShift, set;
	
	if (traverse == NULL || iterator == NULL || leafValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (leafLength <= 0 || numBits <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Wrong length for this tree? */
	if (traverse->leafLength != leafLength)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Invalid? */
	at = iterator->atNode;
	if (at == NULL)
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* We cannot iterate past a leaf. */
	if ((sjme_intPointer)at == SJME_TRAVERSE_LEAF_KEY)
		return SJME_ERROR_NO_SUCH_ELEMENT; 
		
	/* Continue where this was left off. */
	for (sh = sjme_util_intOverShiftU(1, numBits) - 1;
		sh >= 1 && at != NULL; sh >>= 1)
	{
		/* Is this the final shift? */
		finalShift = (sh == 1);
		
		/* Is this set? */
		set = ((bits & sh) != 0);
		
		/* We cannot iterate past a leaf. */
		if ((sjme_intPointer)at == SJME_TRAVERSE_LEAF_KEY)
			return SJME_ERROR_NO_SUCH_ELEMENT; 
		
		/* Are we going zero or one? */
		next = (set ? &at->node.one : &at->node.zero);
		
		/* Nothing here? */
		if ((*next) == NULL)
			return SJME_ERROR_NO_SUCH_ELEMENT;
		
		/* Next is a leaf? */
		if ((sjme_intPointer)((*next)->leaf.key) == SJME_TRAVERSE_LEAF_KEY)
		{
			/* Must be the final bit! */
			if (!finalShift)
				return SJME_ERROR_NO_SUCH_ELEMENT;
			
			/* Set leaf value. */
			*leafValue = &((*next)->leaf.value[0]);
		}
		
		/* In too deep? */
		if (iterator->bitCount >= 32)
			return SJME_ERROR_TREE_TOO_DEEP;
		
		/* Shift in bit. */
		iterator->bits <<= 1;
		iterator->bits |= (set ? 1 : 0);
		iterator->bitCount += 1;
		
		/* Go the next node. */
		at = (*next);
	}
	
	/* Not valid? */
	if (at == NULL)
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* Set at this position. */
	iterator->atNode = at;
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	
	/* Traverse the tree. */
	at = NULL;
	if (sjme_error_is(error = sjme_traverse_traverse(
		traverse, &at, NULL,
		SJME_JNI_TRUE, bits, numBits)) ||
		at == NULL)
		return sjme_error_default(error);
	
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
	sjme_errorCode error;
	sjme_traverse_node* at;
	sjme_traverse_node* atParent;
	
	if (traverse == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (numBits <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Traverse the tree. */
	at = NULL;
	atParent = NULL;
	if (sjme_error_is(error = sjme_traverse_traverse(
		traverse, &at, &atParent,
		SJME_JNI_FALSE, bits, numBits)))
		return sjme_error_default(error);
	
	/* Nothing is here? */
	if (at == NULL)
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* Clear it out. */
	if (atParent != NULL)
	{
		if (atParent->node.zero == at)
			atParent->node.zero = NULL;
		else if (atParent->node.one == at)
			atParent->node.one = NULL;
	}
	
	/* Wipe as long as this is not a leaf. */
	if ((sjme_intPointer)at != SJME_TRAVERSE_LEAF_KEY)
		memset(at, 0, sizeof(*at));
	
	/* Success! */
	return SJME_ERROR_NONE;
}
