/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/inflate.h"


static sjme_errorCode sjme_inflate_buildTreeInsertNext(
	sjme_attrInNotNull sjme_inflate_huffTree* outTree,
	sjme_attrInNotNull sjme_inflate_huffTreeStorage* inStorage,
	sjme_attrOutNotNull sjme_inflate_huffNode** outNode)
{
	if (outTree == NULL || inStorage == NULL || outNode == NULL)
		return SJME_ERROR_NONE;
	
	/* Does the storage need initialization? */
	if (inStorage->next == NULL || inStorage->finalEnd == NULL)
	{
		inStorage->next = (sjme_inflate_huffNode*)
			&inStorage->storage[0];
		inStorage->finalEnd = (sjme_inflate_huffNode*)
			&inStorage->storage[SJME_INFLATE_HUFF_STORAGE_SIZE /
				sizeof(sjme_inflate_huffNode)];
	}
	
	/* Is the tree now full? */
	if ((sjme_intPointer)inStorage->next >=
		(sjme_intPointer)inStorage->finalEnd)
		return SJME_ERROR_INFLATE_HUFF_TREE_FULL;
	
	/* Move pointer up. */
	*outNode = (inStorage->next);
	inStorage->next = SJME_POINTER_OFFSET(inStorage->next,
		sizeof(*inStorage->next));
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_buildTreeInsert(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_huffTree* outTree,
	sjme_attrInNotNull sjme_inflate_huffTreeStorage* inStorage,
	sjme_attrInPositive sjme_juint code,
	sjme_attrInValue sjme_juint sym,
	sjme_attrInPositiveNonZero sjme_juint symMask)
{
	sjme_errorCode error;
	sjme_juint maskBitCount, sh;
	sjme_inflate_huffNode* atNode;
	sjme_inflate_huffNode** dirNode;
	sjme_jboolean one;
#if defined(SJME_CONFIG_DEBUG)
	sjme_cchar binary[40];
	sjme_cchar binary2[40];
#endif
	
	if (state == NULL || outTree == NULL)
		return SJME_ERROR_NONE;
	
	/* Make sure the correct inputs are used for adding to a tree. */
	maskBitCount = sjme_util_bitCountU(symMask);
	if ((sym & (~symMask)) != 0 ||
		maskBitCount == 0 ||
		maskBitCount != (32 - sjme_util_numLeadingZeroesU(symMask)) ||
		(symMask & 1) == 0)
		return SJME_ERROR_INVALID_ARGUMENT;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_util_intToBinary(binary, sizeof(binary),
		code, 10);
	sjme_util_intToBinary(binary2, sizeof(binary2),
		sym, maskBitCount);
	sjme_message("treeInsert %s (%d 0x%x) -> %s (%d 0x%x)",
		binary2, sym, sym,
		binary,
		code,
		code);
#endif
	
	/* If there is no root node, create it. */
	if (outTree->root == NULL)
	{
		/* Grab next node. */
		if (sjme_error_is(error = sjme_inflate_buildTreeInsertNext(
			outTree, inStorage, &outTree->root)) ||
			outTree->root == NULL)
			return sjme_error_default(error);
		
		/* Set as node type, pointers go nowhere currently. */
		outTree->root->type = SJME_INFLATE_NODE;
	}
	
	/* Start at the top of the shift and continue down the tree, */
	/* create any nodes as needed for leaf placement. */
	atNode = outTree->root;
	for (sh = sjme_util_highestOneBit(symMask); sh > 0; sh >>= 1)
	{
		/* Are we going right or left? */
		one = (sym & sh) != 0;
		
		/* Must always be a node! */
		if (atNode->type != SJME_INFLATE_NODE)
			return SJME_ERROR_INFLATE_HUFF_TREE_COLLISION;
		
		/* Get direction to go into. */
		dirNode = (one ? &atNode->data.node.one : &atNode->data.node.zero);
		
		/* If null, it needs to be created. */
		if ((*dirNode) == NULL)
		{
			if (sjme_error_is(error = sjme_inflate_buildTreeInsertNext(
				outTree, inStorage, dirNode)) || (*dirNode) == NULL)
				return sjme_error_default(error);
			
			/* Set to node, provided we are not at the last shift. */
			if (sh != 1)
				(*dirNode)->type = SJME_INFLATE_NODE;
		}
		
		/* Set new position. */
		atNode = (*dirNode);
	}
	
	/* We must be at an unknown node! */
	if (atNode->type != SJME_INFLATE_UNKNOWN)
		return SJME_ERROR_INFLATE_HUFF_TREE_COLLISION;
	
	/* Set leaf details. */
	atNode->type = SJME_INFLATE_LEAF;
	atNode->data.leaf.code = code;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_buildTree(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_huffParam* param,
	sjme_attrInNotNull sjme_inflate_huffTree* outTree,
	sjme_attrInNotNull sjme_inflate_huffTreeStorage* inStorage)
{
	sjme_errorCode error;
	sjme_jint i, code, len;
	sjme_juint blCount[SJME_INFLATE_CODE_LEN_MAX_BITS + 1];
	sjme_juint nextCode[SJME_INFLATE_CODE_LEN_MAX_BITS + 1];
	
	if (state == NULL || param == NULL || outTree == NULL || inStorage == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Wipe the target tree. */
	memset(outTree, 0, sizeof(*outTree));
	
	/* Determine the bit-length for the input counts. */
	memset(blCount, 0, sizeof(blCount));
	for (i = 0; i < param->count; i++)
		blCount[param->lengths[i]] += 1;
	blCount[0] = 0;
	
	/* Find the numerical value of the smallest code for each code length. */
	memset(nextCode, 0, sizeof(nextCode));
	code = 0;
	for (i = 1; i <= SJME_INFLATE_CODE_LEN_MAX_BITS; i++)
	{
		code = (code + blCount[i - 1]) << 1;
		nextCode[i] = code;
	}
	
	/* Assign values to codes and build the huffman tree. */
	for (i = 0; i < param->count; i++)
	{
		/* Ignore zero lengths. */
		len = param->lengths[i];
		if (len == 0)
			continue;
		
#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("param[%d] = %d", i, param->lengths[i]);
#endif
			
		/* Insert into the tree. */
		if (sjme_error_is(error = sjme_inflate_buildTreeInsert(
			state, outTree, inStorage, i,
			nextCode[len],
			(1 << len) - 1)))
			return sjme_error_default(error);
		
		/* Increment up the next code. */
		nextCode[len]++;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
