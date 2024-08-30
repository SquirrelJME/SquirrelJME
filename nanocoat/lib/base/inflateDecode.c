/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/inflate.h"
#include "sjme/debug.h"

/** Code length shuffled bit order, for "optimal" bit placement. */
static const sjme_jubyte sjme_inflate_shuffleBits[
	SJME_INFLATE_CODE_LEN_LIMIT] =
{
	16, 17, 18, 0, 8, 7, 9,
	6, 10, 5, 11, 4, 12, 3,
	13, 2, 14, 1, 15
};

sjme_errorCode sjme_inflate_decode(
	sjme_attrInNotNull sjme_inflate_state* inState)
{
	sjme_errorCode error;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Which step are we on? */
	switch (state->step)
	{
			/* Parse the block type. */
		case SJME_INFLATE_STEP_CHECK_BTYPE:
			return sjme_inflate_decodeBType(state);
			
			/* Literal uncompressed header. */
		case SJME_INFLATE_STEP_LITERAL_HEADER:
			return sjme_inflate_decodeLiteralHeader(state);
			
			/* Literal uncompressed data. */
		case SJME_INFLATE_STEP_LITERAL_DATA:
			return sjme_inflate_decodeLiteralData(state);
		
			/* Load in dynamic huffman table. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD:
			return sjme_inflate_decodeDynLoad(state);
	
			/* Load in dynamic huffman table: Code length tree. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_CODE_LEN:
			return sjme_inflate_decodeDynLoadCodeLen(state);
		
			/* Load in dynamic huffman table: Literal tree. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_LITERAL:
			if (sjme_error_is(error = sjme_inflate_decodeDynLoadLitDist(
				state,
				state->huffInit.litLen,
				&state->literalTree,
				SJME_INFLATE_NUM_LIT_LENS)))
				return sjme_error_default(error);
			
			/* Read in distance codes next. */
			state->step = SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_DISTANCE;
			return SJME_ERROR_NONE;
		
			/* Load in dynamic huffman table: Distance tree. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_DISTANCE:
			if (sjme_error_is(error = sjme_inflate_decodeDynLoadLitDist(
				state,
				state->huffInit.distLen,
				&state->distanceTree,
				SJME_INFLATE_NUM_DIST_LENS)))
				return sjme_error_default(error);
				
			/* Set the source for input codes. */
			state->step = SJME_INFLATE_STEP_INFLATE_FROM_TREE;
			state->readCode = sjme_inflate_readCodeDynamic;
			state->readDist = sjme_inflate_readDistDynamic;
			return SJME_ERROR_NONE;
		
			/* Fixed static huffman table. */
		case SJME_INFLATE_STEP_FIXED_TABLE_INFLATE:
			state->step = SJME_INFLATE_STEP_INFLATE_FROM_TREE;
			state->readCode = sjme_inflate_readCodeFixed;
			state->readDist = sjme_inflate_readDistFixed;
			return SJME_ERROR_NONE;
			
			/* Decode from the given huffman tree. */
		case SJME_INFLATE_STEP_INFLATE_FROM_TREE:
			return sjme_inflate_processCodes(state);
	}
	
	/* Should not be reached. */
	return SJME_ERROR_ILLEGAL_STATE;
}

sjme_errorCode sjme_inflate_decodeBType(
	sjme_attrInNotNull sjme_inflate_state* inState)
{
	sjme_bitStream_input* inBits;
	sjme_errorCode error;
	sjme_juint finalFlag;
	sjme_juint blockType;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If this is called after we hit the end, we are done decompressing */
	/* and there will be no more output. */
	if (state->finalHit)
	{
		/* No more output data! */
		state->output.hitEof = SJME_JNI_TRUE;
		
		/* Set next step to finished, which should never be run. */
		state->step = SJME_INFLATE_STEP_FINISHED;
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Can we actually read in the final flag and block type? */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_inflate_bitNeed(
		inBuffer, 3)))
		return sjme_error_default(error);
	
	/* Read in the final flag. */
	finalFlag = 0;
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		1, &finalFlag)))
		return sjme_error_default(error);
	
	/* If it was indicated, then flag it for later. */
	if (finalFlag != 0)
		state->finalHit = SJME_JNI_TRUE;
	
	/* Read in the type that this is. */
	blockType = 0;
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP, 2,
		&blockType)))
		return sjme_error_default(error);
	
	/* Switch state to what was indicated. */
	if (blockType == 0)
		state->step = SJME_INFLATE_STEP_LITERAL_HEADER;
	else if (blockType == 1)
		state->step = SJME_INFLATE_STEP_FIXED_TABLE_INFLATE;
	else if (blockType == 2)
		state->step = SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD;
	
	/* Invalid block. */
	else
	{
		state->invalidInput = SJME_JNI_TRUE;
		return SJME_ERROR_INFLATE_INVALID_BTYPE;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_decodeDynLoad(
	sjme_attrInNotNull sjme_inflate_state* inState)
{
	sjme_errorCode error;
	sjme_bitStream_input* inBits;
	sjme_inflate_huffInit* init;
	
	sjme_juint lit, dist, codeLen;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need 14 bits for all the combined lengths. */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_inflate_bitNeed(inBuffer,
		14)))
		return sjme_error_default(error);
	
	/* Clear out any previous tree state. */
	init = &state->huffInit;
	memset(init, 0, sizeof(*init));
	memset(&state->codeLenTree, 0, sizeof(state->codeLenTree));
	memset(&state->distanceTree, 0, sizeof(state->distanceTree));
	memset(&state->literalTree, 0, sizeof(state->literalTree));
	
	/* Load in tree parameters. */
	lit = -1;
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		5, &lit)) || lit < 0)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INFLATE_INVALID_TREE_LENGTH);
			
	dist = -1;
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		5, &dist)) || dist < 0)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INFLATE_INVALID_TREE_LENGTH);
	
	codeLen = -1;
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		4, &codeLen)) || codeLen < 0)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INFLATE_INVALID_TREE_LENGTH);
	
	/* There can only be so many codes. */
	if (codeLen + 4 > SJME_INFLATE_CODE_LEN_LIMIT)
		return SJME_ERROR_INFLATE_INVALID_CODE_LENGTH;
	
	/* Fill in tree parameters. */
	init->litLen = lit + 257;
	init->distLen = dist + 1;
	init->codeLen = codeLen + 4;
	
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Dynamic tree: litLen:%d; distLen:%d; codeLen:%d",
		init->litLen, init->distLen, init->codeLen);
#endif
	
	/* Start reading the code length tree. */
	state->step = SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_CODE_LEN;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_decodeDynLoadCodeLen(
	sjme_attrInNotNull sjme_inflate_state* inState)
{
	sjme_bitStream_input* inBits;
	sjme_inflate_huffInit* init;
	sjme_inflate_huffParam param;
	sjme_errorCode error;
	sjme_jint i;
	sjme_juint v;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* There is a limit of 19 codes. */
	init = &state->huffInit;
	if (init->codeLen > SJME_INFLATE_CODE_LEN_LIMIT)
		return SJME_ERROR_INFLATE_INVALID_CODE_LENGTH_COUNT;
	
	/* We need 3 bits for each length. */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_inflate_bitNeed(inBuffer,
		init->codeLen * 3)))
		return sjme_error_default(error);
	
	/* Clear the lengths before they are read in. */
	memset(init->rawCodeLens, 0, sizeof(init->rawCodeLens));
	
	/* Read in every raw code length, shuffling accordingly. */
	for (i = 0; i < init->codeLen; i++)
	{
		/* Read in bits and make sure it is valid. */
		v = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			3,
			&v)) || v >= 8)
			return sjme_error_default(error);
		
		/* Set. */
		init->rawCodeLens[sjme_inflate_shuffleBits[i]] = v;

#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("rawCodeLens %d (%d): %d",
			sjme_inflate_shuffleBits[i], i, v);
#endif
	}
	
	/* Clear the huffman node storage as we are recreating the tree. */
	memset(&state->huffStorage, 0, sizeof(state->huffStorage));
	
	/* We can now parse the code length tree, which needs no input bits! */
	/* This is pretty magical. */
	memset(&param, 0, sizeof(param));
	param.lengths = &init->rawCodeLens[0];
	param.count = SJME_INFLATE_CODE_LEN_LIMIT;
	if (sjme_error_is(error = sjme_inflate_buildTree(state,
		&param,
		&state->codeLenTree,
		&state->huffStorage)))
		return sjme_error_default(error);
	
	/* Load the literal tree now that we have the code lengths. */
	state->step = SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_LITERAL;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_decodeDynLoadLitDist(
	sjme_attrInNotNull sjme_inflate_state* inState,
	sjme_attrInPositive sjme_juint count,
	sjme_attrInNotNull sjme_inflate_huffTree* outTree,
	sjme_attrInPositiveNonZero sjme_juint maxCount)
{
	sjme_bitStream_input* inBits;
	sjme_inflate_huffTree* codeLenTree;
	sjme_errorCode error;
	sjme_juint index, v;
	sjme_juint* lengths;
	sjme_inflate_huffParam param;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (maxCount <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* This cannot be empty or exceed the maximum ever. */
	if (count <= 0 || count >= maxCount)
		return SJME_ERROR_INFLATE_INVALID_TREE_LENGTH;
	
	/* Allocate stack memory for the lengths we need to process. */
	lengths = sjme_alloca(sizeof(*lengths) * maxCount);
	if (lengths == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(lengths, 0, sizeof(*lengths) * maxCount);
	
	/* Read in all code values. */
	inBuffer = &state->input;
	codeLenTree = &state->codeLenTree;
	for (index = 0; index < count;)
		if (sjme_error_is(error = sjme_inflate_bitInCodeLen(
			inBuffer, codeLenTree, &index, lengths,
			maxCount)))
		{
			if (error == SJME_ERROR_TOO_SHORT)
				return SJME_ERROR_ILLEGAL_STATE;
			return sjme_error_default(error);
		}
	
	/* Build tree from this. */
	memset(&param, 0, sizeof(param));
	param.lengths = lengths;
	param.count = maxCount;
	if (sjme_error_is(error = sjme_inflate_buildTree(
		state, &param, outTree, &state->huffStorage)))
		return sjme_error_default(error);
	
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_decodeLiteralData(
	sjme_attrInNotNull sjme_inflate_state* inState)
{
	sjme_bitStream_input* inBits;
	sjme_bitStream_output* outBits;
	sjme_errorCode error;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_inflate_decodeLiteralHeader(
	sjme_attrInNotNull sjme_inflate_state* inState)
{
	sjme_bitStream_input* inBits;
	sjme_errorCode error;
	sjme_juint len, nel;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can we actually read in the literal data header? */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_inflate_bitNeed(
		inBuffer, 32)))
		return sjme_error_default(error);
	
	/* Read in the length and their complement. */
	len = INT32_MAX;
	nel = INT32_MAX;
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		16, &len)) || len == INT32_MAX)
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		16, &nel)) || nel == INT32_MAX)
		return sjme_error_default(error);
	
	/* Debug. */
	sjme_message("Literal %d/%d %08x/%08x",
		len, nel, len, nel);
	
	/* These should be the inverse of each other. */
	if (len != (nel ^ 0xFFFF))
		return SJME_ERROR_INFLATE_INVALID_INVERT;
	
	/* Setup for next step. */
	state->step = SJME_INFLATE_STEP_LITERAL_DATA;
	state->literalLeft = len;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_bitInCodeLen(
	sjme_attrInNotNull sjme_bitStream_input* inBits,
	sjme_attrInNotNull sjme_inflate_huffTree* codeLenTree,
	sjme_attrInOutNotNull sjme_juint* index,
	sjme_attrOutNotNull sjme_juint* outLengths,
	sjme_attrInPositive sjme_juint count)
{
	sjme_errorCode error;
	sjme_juint code, repeatCode, repeatCount, i;
	
	if (inBuffer == NULL || codeLenTree == NULL || index == NULL ||
		outLengths == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (count < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Read in the next code. */
	code = INT32_MAX;
	if (sjme_error_is(error = sjme_inflate_bitInTree(inBuffer,
		codeLenTree, &code)) || code == INT32_MAX)
		return sjme_error_default(error);

#if defined(SJME_CONFIG_DEBUG)
	sjme_message("inCodeLen %d: %d",
		(*index), code);
#endif
	
	/* Literal value? */
	if (code >= 0 && code < 16)
	{
		/* Make sure we do not write out of the length buffer. */
		if ((*index) >= count)
			return SJME_ERROR_INFLATE_INDEX_OVERFLOW;
		
		/* Store in value. */
		outLengths[(*index)++] = code;
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Repeat 3-6 times. */
	if (code == 16)
	{
		/* Cannot be the first entry. */
		if ((*index) == 0)
			return SJME_ERROR_INFLATE_INVALID_FIRST_REPEAT;
		
		/* The code is just the previous entry. */
		repeatCode = outLengths[(*index) - 1];
		
		/* The input specifies how much to repeat for. */
		repeatCode = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			2, &repeatCode)) || repeatCode == INT32_MAX)
			return sjme_error_default(error);
		
		/* Base of three. */
		repeatCount += 3;
	}
	
	/* Repeat zero 3-10 times. */
	else if (code == 17)
	{
		/* The zero code is repeated. */
		repeatCode = 0;
		
		/* The input specifies how much to repeat for. */
		repeatCode = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			3, &repeatCode)) || repeatCode == INT32_MAX)
			return sjme_error_default(error);
		
		/* Base of three. */
		repeatCount += 3;
	}
	
	/* Repeat zero for 11-138 times. */
	else if (code == 18)
	{
		/* The zero code is repeated. */
		repeatCode = 0;
		
		/* The input specifies how much to repeat for. */
		repeatCode = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			7, &repeatCode)) || repeatCode == INT32_MAX)
			return sjme_error_default(error);
		
		/* Base of three. */
		repeatCount += 11;
	}
	
	/* Not valid. */
	else
		return SJME_ERROR_INFLATE_INVALID_CODE;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("Code %d -> repeat %d * %d",
		code, repeatCode, repeatCount);
#endif
	
	/* Perform repeat sequence. */
	for (i = 0; i < repeatCount; i++)
	{
		/* Make sure we do not write out of the length buffer. */
		if ((*index) >= count)
			return SJME_ERROR_INFLATE_INDEX_OVERFLOW;
		
		/* Write repeated code. */
		outLengths[(*index)++] = repeatCode;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_bitInTree(
	sjme_attrInNotNull sjme_bitStream_input* inBits,
	sjme_attrInNotNull sjme_inflate_huffTree* fromTree,
	sjme_attrOutNotNull sjme_juint* outValue)
{
	sjme_inflate_huffNode* atNode;
	sjme_errorCode error;
	sjme_juint v;
#if defined(SJME_CONFIG_DEBUG)
	sjme_juint fullBits, fullBitCount;
	sjme_cchar binary[40];
	sjme_cchar binary2[40];
#endif
	
	if (inBuffer == NULL || fromTree == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Start at the root node. */
	atNode = fromTree->root;
	if (atNode == NULL)
		return SJME_ERROR_INFLATE_HUFF_TREE_INCOMPLETE;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug: clear the total bits. */
	fullBits = 0;
	fullBitCount = 0;
#endif
	
	/* Read in bits and go in the given direction. */
	while (atNode != NULL && atNode->type == SJME_INFLATE_NODE)
	{
		/* Read in single bit. */
		v = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			1, &v)) || v == INT32_MAX)
			return sjme_error_default(error);

#if defined(SJME_CONFIG_DEBUG)
		/* Debug: shift in full bits. */
		fullBits <<= 1;
		fullBits |= (v != 0);
		fullBitCount++;
#endif
		
		/* Traverse in the given direction. */
		atNode = (v != 0 ? atNode->data.node.one : atNode->data.node.zero);
	}
	
	/* If we stopped, then this is not even a complete/valid tree. */
	if (atNode == NULL || atNode->type != SJME_INFLATE_LEAF)
		return SJME_ERROR_INFLATE_HUFF_TREE_INCOMPLETE;
	
#if defined(SJME_CONFIG_DEBUG)
	sjme_util_intToBinary(binary, sizeof(binary) - 1,
		fullBits, fullBitCount);
	sjme_util_intToBinary(binary2, sizeof(binary2) - 1,
		atNode->data.leaf.code, 10);
	sjme_message("Give node %p %d %s (%d) -> %d %s",
		atNode, fullBits, binary, fullBitCount,
		atNode->data.leaf.code, binary2);
#endif
	
	/* Give the value here. */
	*outValue = atNode->data.leaf.code;
	return SJME_ERROR_NONE;
}
