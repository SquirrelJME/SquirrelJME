/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/inflate.h"

static sjme_errorCode sjme_stream_inflateDecodeBType(
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
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
	if (sjme_error_is(error = sjme_stream_inflateBitNeed(
		inBuffer, 3)))
		return sjme_error_default(error);
	
	/* Read in the final flag. */
	finalFlag = 0;
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		1, &finalFlag)))
		return sjme_error_default(error);
	
	/* If it was indicated, then flag it for later. */
	if (finalFlag != 0)
		state->finalHit = SJME_JNI_TRUE;
	
	/* Read in the type that this is. */
	blockType = 0;
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
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

static sjme_errorCode sjme_stream_inflateDecodeLiteralHeader(
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_errorCode error;
	sjme_juint len, nel;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can we actually read in the literal data header? */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_stream_inflateBitNeed(
		inBuffer, 32)))
		return sjme_error_default(error);
	
	/* Read in the length and their complement. */
	len = INT32_MAX;
	nel = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		16, &len)) || len == INT32_MAX)
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
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

static sjme_errorCode sjme_stream_inflateDecodeLiteralData(
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateBuffer* outBuffer;
	sjme_errorCode error;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inflateDecodeDynLoad(
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_errorCode error;
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateHuffInit* init;
	
	sjme_juint lit, dist, codeLen;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need 14 bits for all the combined lengths. */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_stream_inflateBitNeed(inBuffer,
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
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		5, &lit)) || lit < 0)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INFLATE_INVALID_TREE_LENGTH);
			
	dist = -1;
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
		SJME_INFLATE_LSB, SJME_INFLATE_POP,
		5, &dist)) || dist < 0)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INFLATE_INVALID_TREE_LENGTH);
	
	codeLen = -1;
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
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

static sjme_errorCode sjme_stream_inflateDecodeDynLoadCodeLen(
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateHuffInit* init;
	sjme_stream_inflateHuffParam param;
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
	if (sjme_error_is(error = sjme_stream_inflateBitNeed(inBuffer,
		init->codeLen * 3)))
		return sjme_error_default(error);
	
	/* Clear the lengths before they are read in. */
	memset(init->rawCodeLens, 0, sizeof(init->rawCodeLens));
	
	/* Read in every raw code length, shuffling accordingly. */
	for (i = 0; i < init->codeLen; i++)
	{
		/* Read in bits and make sure it is valid. */
		v = INT32_MAX;
		if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			3,
			&v)) || v >= 8)
			return sjme_error_default(error);
		
		/* Set. */
		init->rawCodeLens[sjme_stream_inflateShuffleBits[i]] = v;

#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("rawCodeLens %d (%d): %d",
			sjme_stream_inflateShuffleBits[i], i, v);
#endif
	}
	
	/* Clear the huffman node storage as we are recreating the tree. */
	memset(&state->huffStorage, 0, sizeof(state->huffStorage));
	
	/* We can now parse the code length tree, which needs no input bits! */
	/* This is pretty magical. */
	memset(&param, 0, sizeof(param));
	param.lengths = &init->rawCodeLens[0];
	param.count = SJME_INFLATE_CODE_LEN_LIMIT;
	if (sjme_error_is(error = sjme_stream_inflateBuildTree(state,
		&param,
		&state->codeLenTree,
		&state->huffStorage)))
		return sjme_error_default(error);
	
	/* Load the literal tree now that we have the code lengths. */
	state->step = SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_LITERAL;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateDecodeDynLoadLitDist(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrInPositive sjme_juint count,
	sjme_attrInNotNull sjme_stream_inflateHuffTree* outTree,
	sjme_attrInPositiveNonZero sjme_juint maxCount)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateHuffTree* codeLenTree;
	sjme_errorCode error;
	sjme_juint index, v;
	sjme_juint* lengths;
	sjme_stream_inflateHuffParam param;
	
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
		if (sjme_error_is(error = sjme_stream_inflateBitInCodeLen(
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
	if (sjme_error_is(error = sjme_stream_inflateBuildTree(
		state, &param, outTree, &state->huffStorage)))
		return sjme_error_default(error);
	
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateDecode(
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_errorCode error;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Which step are we on? */
	switch (state->step)
	{
			/* Parse the block type. */
		case SJME_INFLATE_STEP_CHECK_BTYPE:
			return sjme_stream_inflateDecodeBType(state);
			
			/* Literal uncompressed header. */
		case SJME_INFLATE_STEP_LITERAL_HEADER:
			return sjme_stream_inflateDecodeLiteralHeader(state);
			
			/* Literal uncompressed data. */
		case SJME_INFLATE_STEP_LITERAL_DATA:
			return sjme_stream_inflateDecodeLiteralData(state);
		
			/* Load in dynamic huffman table. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD:
			return sjme_stream_inflateDecodeDynLoad(state);
	
			/* Load in dynamic huffman table: Code length tree. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_CODE_LEN:
			return sjme_stream_inflateDecodeDynLoadCodeLen(state);
		
			/* Load in dynamic huffman table: Literal tree. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_LITERAL:
			if (sjme_error_is(error = sjme_stream_inflateDecodeDynLoadLitDist(
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
			if (sjme_error_is(error = sjme_stream_inflateDecodeDynLoadLitDist(
				state,
				state->huffInit.distLen,
				&state->distanceTree,
				SJME_INFLATE_NUM_DIST_LENS)))
				return sjme_error_default(error);
				
			/* Set the source for input codes. */
			state->step = SJME_INFLATE_STEP_INFLATE_FROM_TREE;
			state->readCode = sjme_stream_inflateReadCodeDynamic;
			state->readDist = sjme_stream_inflateReadDistDynamic;
			return SJME_ERROR_NONE;
		
			/* Fixed static huffman table. */
		case SJME_INFLATE_STEP_FIXED_TABLE_INFLATE:
			state->step = SJME_INFLATE_STEP_INFLATE_FROM_TREE;
			state->readCode = sjme_stream_inflateReadCodeFixed;
			state->readDist = sjme_stream_inflateReadDistFixed;
			return SJME_ERROR_NONE;
			
			/* Decode from the given huffman tree. */
		case SJME_INFLATE_STEP_INFLATE_FROM_TREE:
			return sjme_stream_inflateProcessCodes(state);
	}
	
	/* Should not be reached. */
	return SJME_ERROR_ILLEGAL_STATE;
}
