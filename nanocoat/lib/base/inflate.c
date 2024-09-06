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
#include "sjme/util.h"

/** Shuffled code length bits. */
static const sjme_jubyte sjme_inflate_shuffleBits[SJME_INFLATE_NUM_CODE_LEN] =
{
	16, 17, 18, 0, 8, 7, 9, 6,
	10, 5, 11, 4, 12, 3, 13,
	2, 14, 1, 15
};

static sjme_errorCode sjme_inflate_bitFill(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_jint avail, readCount;
	sjme_jubyte* buffer;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Pointless if the input hit EOF. */
	if (inState->inputEof)
		return SJME_ERROR_NONE;
		
	/* How much space is left in the input buffer? */
	avail = INT32_MAX;
	if (sjme_error_is(error = sjme_circleBuffer_available(
		inState->inputBuffer, &avail)) ||
		avail == INT32_MAX)
		return sjme_error_default(error);
	
	/* It is as full as it can get. */
	if (avail <= 0)
		return SJME_ERROR_NONE;
	
	/* Setup target buffer to read into. */
	buffer = sjme_alloca(avail);
	if (buffer == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(buffer, 0, avail);
	
	/* Read everything in as much as possible. */
	readCount = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadFully(
		inState->source, &readCount, buffer, avail)) ||
		readCount == INT32_MAX)
		return sjme_error_default(error);
	
	/* EOF? */
	if (readCount < 0)
	{
		inState->inputEof = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}
	
	/* Push onto the buffer. */
	if (sjme_error_is(error = sjme_circleBuffer_push(
		inState->inputBuffer,
		buffer, readCount,
		SJME_CIRCLE_BUFFER_TAIL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_bitRead(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrOutNotNull sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_inflate* inState;
	sjme_jint limit;
	
	inState = functionData;
	if (inStream == NULL || inState == NULL || readCount == NULL ||
		outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Always fill in before read. */
	if (sjme_error_is(error = sjme_inflate_bitFill(inState)))
		return sjme_error_default(error);
	
	/* How much is in the input buffer? */
	limit = INT32_MAX;
	if (sjme_error_is(error = sjme_circleBuffer_stored(
		inState->inputBuffer, &limit)) ||
		limit == INT32_MAX)
		return sjme_error_default(error);
	
	/* If this is zero then we do not have enough to read from. */
	if (limit == 0)
	{
		/* However if EOF was hit, we stop. */
		if (inState->inputEof)
		{
			*readCount = -1;
			return SJME_ERROR_NONE;
		}
		
		/* Otherwise, we need more data! */
		return SJME_ERROR_TOO_SHORT;
	}
	
	/* Do not exceed the storage limit. */
	if (limit > length)
		limit = length;
	
	/* Pop from the buffer. */
	if (sjme_error_is(error = sjme_circleBuffer_pop(
		inState->inputBuffer, outBuf, limit,
		SJME_CIRCLE_BUFFER_HEAD)))
		return sjme_error_default(error);
	
	/* Success! */
	*readCount = limit;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_bitWrite(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrInNotNullBuf(length) sjme_buffer writeBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_inflate* inState;
	
	inState = functionData;
	if (outStream == NULL || inState == NULL || writeBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Push onto the window. */
	if (sjme_error_is(error = sjme_circleBuffer_push(
		inState->window, writeBuf, length,
		SJME_CIRCLE_BUFFER_TAIL)))
		return sjme_error_default(error);
	
	/* And onto the output buffer. */
	if (sjme_error_is(error = sjme_circleBuffer_push(
		inState->outputBuffer, writeBuf, length,
		SJME_CIRCLE_BUFFER_TAIL)))
		return sjme_error_default(error);
	
	/* Debug. */
	sjme_message_hexDump(writeBuf, length);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_bufferSaturation(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrInPositiveNonZero sjme_jint bitsNeeded)
{
	sjme_errorCode error;
	sjme_jint ready;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitsNeeded <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
			
	/* The output buffer is saturated? */
	if (inState->outputBuffer->ready >= SJME_INFLATE_IO_BUFFER_SATURATED)
		return SJME_ERROR_BUFFER_SATURATED;
	
	/* How many bits are already in the window? */
	ready = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_bitsReady(
		SJME_AS_BITSTREAM(inState->input),
		&ready)) || ready == INT32_MAX)
		return sjme_error_default(error);
	
	/* Add in all the input buffer bytes. */
	ready += (inState->inputBuffer->ready * 8);
	
	/* Do we have enough? */
	if (ready >= bitsNeeded)
		return SJME_ERROR_NONE;
	return SJME_ERROR_TOO_SHORT;
}

static sjme_errorCode sjme_inflate_copyWindow(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrInValue sjme_jint length,
	sjme_attrInValue sjme_jint dist)
{
	sjme_errorCode error;
	sjme_jint maxLen, i, at;
	sjme_jubyte* buf;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The maximum length that is valid is the smaller of the two values, */
	/* essentially the length could be 5 but the distance 2, we cannot */
	/* read past the end of the window, we can only read 2 bytes. */
	maxLen = (length < dist ? length : dist);
	
	/* Allocate window buffer. */
	buf = sjme_alloca(maxLen);
	if (buf == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(buf, 0, maxLen);
	
	/* Read in from the window. */
	if (sjme_error_is(error = sjme_circleBuffer_get(inState->window,
		buf, maxLen,
		SJME_CIRCLE_BUFFER_TAIL, dist)))
		return sjme_error_default(error);
	
	/* Write in bytes from the window, this always wraps around the buffer. */
	for (i = 0, at = 0; i < length; i++)
	{
		/* Write single byte. */
		if (sjme_error_is(error = sjme_bitStream_outputWrite(
			inState->output, SJME_BITSTREAM_LSB,
			buf[at], 8)))
			return sjme_error_default(error);
		
		/* Overflowing? */
		if ((++at) >= maxLen)
			at = 0;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_drain(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrInOutNotNull sjme_jint* drainOff,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_jint stored, limit;
	
	if (inState == NULL || drainOff == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* How much is in the output buffer? */
	stored = INT32_MAX;
	if (sjme_error_is(error = sjme_circleBuffer_stored(
		inState->outputBuffer, &stored)) ||
		stored == INT32_MAX)
		return sjme_error_default(error);
	
	/* How much can be drained? */
	limit = length - (*drainOff);
	if (limit > stored)
		limit = stored;
		
	/* Nothing to do? */
	if (limit == 0)
		return SJME_ERROR_NONE;
	
	/* Pop off the head. */
	if (sjme_error_is(error = sjme_circleBuffer_pop(
		inState->outputBuffer,
		SJME_POINTER_OFFSET(outBuf, (*drainOff)),
		limit, SJME_CIRCLE_BUFFER_HEAD)))
		return sjme_error_default(error);
	
	/* Move up the drain offset, which is also the cumulative total output. */
	(*drainOff) += limit;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_dynamicBitIn(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrInNotNull sjme_traverse_sjme_jint fromTree,
	sjme_attrOutNotNull sjme_juint* outValue)
{
	sjme_errorCode error;
	sjme_traverse_iterator iterator;
	sjme_juint bit;
	sjme_juint* result;
	
	if (inState == NULL || fromTree == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Start tree iteration. */
	memset(&iterator, 0, sizeof(iterator));
	if (sjme_error_is(error = sjme_traverse_iterate(
		SJME_AS_TRAVERSE(fromTree), &iterator)))
		return sjme_error_default(error);
	
	/* Constantly read in bits until a leaf is hit. */
	for (;;)
	{
		/* Read in bit. */
		bit = INT32_MAX;
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input,
		 	SJME_BITSTREAM_LSB, &bit, 1)) ||
			bit == INT32_MAX)
			return sjme_error_default(error);
		
		/* Traverse in the tree. */
		result = NULL;
		if (sjme_error_is(error = sjme_traverse_iterateNext(
			fromTree, &iterator, &result, bit, 1, sjme_jint, 0)))
			return sjme_error_default(error);
		
		/* If a value was read, we are at a leaf. */
		if (result != NULL)
		{
			*outValue = *result;
			return SJME_ERROR_NONE;
		}
	}
	
	/* Fail if no value was read. */
	return SJME_ERROR_INFLATE_INVALID_CODE;
}

static sjme_errorCode sjme_inflate_dynamicBuildReadCode(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_juint* codes,
	sjme_attrInOutNotNull sjme_jint* i,
	sjme_attrInPositiveNonZero sjme_jint count,
	sjme_attrInPositiveNonZero sjme_jint maxCount)
{
	sjme_errorCode error;
	sjme_traverse_sjme_jint treeCodeLen;
	sjme_juint code, repFor, repVal;
	sjme_jint x;
	
	if (inState == NULL || codes == NULL || i == NULL)
		return SJME_ERROR_NONE;
	
	if (count <= 0 || maxCount <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Need the code length tree. */
	treeCodeLen = inState->treeCodeLen;
	if (treeCodeLen == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Read in code from the tree. */
	code = INT32_MAX;
	if (sjme_error_is(error = sjme_inflate_dynamicBitIn(
		inState, treeCodeLen, &code)) || code == INT32_MAX)
		return sjme_error_default(error);
	
	/* Input is used literally. */
	if (code >= 0 && code < 16)
	{
		if ((*i) >= maxCount)
			return SJME_ERROR_INFLATE_INDEX_OVERFLOW;
		
		/* Uses the same input code. */
		codes[(*i)++] = code;
		return SJME_ERROR_NONE;
	}
	
	/* Repeat previous length 3-6 times. */
	repVal = INT32_MAX;
	repFor = INT32_MAX;
	if (code == 16)
	{
		/* Cannot be the first entry. */
		if ((*i) <= 0)
			return SJME_ERROR_INFLATE_INVALID_CODE_LENGTH;
		
		/* Repeat this... */
		repVal = codes[(*i) - 1];
		
		/* This many times. */
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input, SJME_BITSTREAM_LSB,
			&repFor, 2)) || repFor == INT32_MAX)
			return sjme_error_default(error);
		repFor += 3;
	}
	
	/* Repeat zero for 3-10 times */
	else if (code == 17)
	{
		/* Repeat zero... */
		repVal = 0;
		
		/* This many times. */
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input, SJME_BITSTREAM_LSB,
			&repFor, 3)) || repFor == INT32_MAX)
			return sjme_error_default(error);
		repFor += 3;
	}
	
	/* Repeat zero for 11-138 times */
	else if (code == 18)
	{
		/* Repeat zero... */
		repVal = 0;
		
		/* This many times. */
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input, SJME_BITSTREAM_LSB,
			&repFor, 7)) || repFor == INT32_MAX)
			return sjme_error_default(error);
		repFor += 11;
	}
	
	/* Invalid. */
	if (repFor == INT32_MAX || repVal == INT32_MAX)
		return SJME_ERROR_INFLATE_INVALID_CODE;
	
	/* Store in repeated values. */
	for (x = 0; x < repFor; x++)
	{
		if ((*i) >= maxCount)
			return SJME_ERROR_INFLATE_INDEX_OVERFLOW;
		
		/* Repeat the requested code. */
		codes[(*i)++] = repVal;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_dynamicBuildTree(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_traverse_sjme_jint* outTree,
	sjme_attrInNotNullBuf(count) sjme_juint* codeLens,
	sjme_attrInPositiveNonZero sjme_jint count,
	sjme_attrInPositiveNonZero sjme_jint maxCount)
{
#if defined(SJME_CONFIG_DEBUG)
	sjme_cchar binary[40];
#endif
	sjme_errorCode error;
	sjme_juint blCount[SJME_INFLATE_CODE_LEN_MAX_BITS + 1];
	sjme_juint nextCode[SJME_INFLATE_CODE_LEN_MAX_BITS + 1];
	sjme_juint code, len, nextCodeLen;
	sjme_jint i;
	
	if (inState == NULL || outTree == NULL || codeLens == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (count <= 0 || maxCount <= 0 || count > maxCount)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Need to allocate the tree? */
	if ((*outTree) == NULL)
		if (sjme_error_is(error = sjme_traverse_new(
			inState->inPool, outTree, maxCount, sjme_jint, 0)) ||
			(*outTree) == NULL)
			return sjme_error_default(error);
	
	/* Wipe working arrays. */
	memset(blCount, 0, sizeof(blCount));
	memset(nextCode, 0, sizeof(nextCode));
	
	/* Determine the bit length count for all the inputs */
	for (i = 0; i < count; i++)
		blCount[codeLens[i]]++;
	blCount[0] = 0;
	
	/* Find the numerical value of the smallest code for each code length. */
	code = 0;
	for (i = 1; i <= SJME_INFLATE_CODE_LEN_MAX_BITS; i++)
	{
		code = (code + blCount[i - 1]) << 1;
		nextCode[i] = code;
	}
	
	/* Clear the target tree. */
	if (sjme_error_is(error = sjme_traverse_clear(
		SJME_AS_TRAVERSE((*outTree)))))
		return sjme_error_default(error);
	
	/* Assign all values to codes. */
	for (i = 0; i < count; i++)
	{
		/* Add code length to the huffman tree */
		len = codeLens[i];
		if (len != 0)
		{
			nextCodeLen = (nextCode[len])++;
			
#if defined(SJME_CONFIG_DEBUG)
			sjme_util_intToBinary(
				binary, sizeof(binary) - 1,
				nextCodeLen, len);
			sjme_message("Tree -> %s = %d",
				binary, i);
#endif
			
			if (sjme_error_is(error = sjme_traverse_putM(
				SJME_AS_TRAVERSE((*outTree)),
				0/*SJME_TRAVERSE_BRANCH_REPLACE*/,
				&i, nextCodeLen, len,
				sjme_jint, 0)))
				return sjme_error_default(error);
		}
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_dynamicReadCode(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_juint* outCode)
{
	sjme_errorCode error;
	sjme_juint code;
	
	if (inState == NULL || outCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in code. */
	code = INT32_MAX;
	if (sjme_error_is(error = sjme_inflate_dynamicBitIn(
		inState, inState->treeLit, &code)) ||
		code == INT32_MAX)
		return sjme_error_default(error);
	
	/* Give the code. */
	*outCode = code;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_dynamicReadDist(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	sjme_errorCode error;
	sjme_juint code;
	
	if (inState == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in code. */
	code = INT32_MAX;
	if (sjme_error_is(error = sjme_inflate_dynamicBitIn(
		inState, inState->treeDist, &code)) ||
		code == INT32_MAX)
		return sjme_error_default(error);
	
	/* Give the code. */
	*outDist = code;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_fixedReadCode(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_juint* outCode)
{
	sjme_errorCode error;
	sjme_juint nine, nextBits, codeBase, nineBase, fragment;
	
	if (inState == NULL || outCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure we at least have 7 bits available. */
	if (sjme_error_is(error = sjme_inflate_bufferSaturation(inState,
		7)))
		return sjme_error_default(error);
	
	/* Read in the first, or only, seven bits. */
	nine = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input,
		SJME_BITSTREAM_MSB, &nine, 7)) ||
		nine == INT32_MAX)
		return sjme_error_default(error);
	
	/* Make this nine bits for consistency. */
	nine <<= 2;
	
	/* Based on the current bit set, determine the code and remaining */
	/* bits we can read. */
	/* The mask is {upper range - lower range}, and is added to the base. */
	codeBase = INT32_MAX;
	nextBits = INT32_MAX;
	nineBase = INT32_MAX;
		
	/* 256 - 279 / 0000000.. - 0010111.. */
	if (nine >= 0x000 && nine <= 0x05F)
	{
		codeBase = 256;
		nextBits = 0;
		nineBase = 0x000;
	}
	
	/*   0 - 143 / 00110000. - 10111111. */
	else if (nine >= 0x060 && nine <= 0x17F)
	{
		codeBase = 0;
		nextBits = 1;
		nineBase = 0x060;
	}
		
	/* 280 - 287 / 11000000. - 11000111. */
	else if (nine >= 0x180 && nine <= 0x18F)
	{
		codeBase = 280;
		nextBits = 1;
		nineBase = 0x180;
	}
	
	/* 144 - 255 / 110010000 - 111111111 */
	else if (nine >= 0x190 && nine <= 0x1FF)
	{
		codeBase = 144;
		nextBits = 2;
		nineBase = 0x190;
	}
	
	/* Invalid code? */
	if (codeBase == INT32_MAX || nextBits == INT32_MAX ||
		nineBase == INT32_MAX)
		return SJME_ERROR_INFLATE_INVALID_CODE;
	
	/* If there are more bits to read, read them in now. */
	fragment = 0;
	if (nextBits != 0)
	{
		/* Read in bits. */
		fragment = INT32_MAX;
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input, SJME_BITSTREAM_MSB,
			&fragment, nextBits)) || fragment == INT32_MAX)
			return sjme_error_default(error);
	}
	
	/* Recompose the code, remember that we shifted left twice to make */
	/* a consistent 9 bits, if we did not need to do that, then undo it. */
	*outCode = codeBase + ((((nine - nineBase) >> (2 - nextBits)) | fragment));
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_fixedReadDist(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	sjme_errorCode error;
	sjme_juint result;
	
	if (inState == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Fixed huffman distance codes are always 5 bits. */
	result = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input, SJME_BITSTREAM_MSB,
		&result, 5)) || result == INT32_MAX)
		return sjme_error_default(error);
	
	/* Success! */
	*outDist = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_readDistance(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	sjme_errorCode error;
	sjme_juint code, result, i, group;
	
	if (inState == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in the base distance code. */
	code = INT32_MAX;
	if (sjme_error_is(error = inState->sub.huffman.readDist(
		inState, &code)) || code == INT32_MAX)
		return sjme_error_default(error);
	
	/* Distance codes can only be so high. */
	if (code > 29)
		return SJME_ERROR_INFLATE_INVALID_CODE;
		
	/* Distances always start at 1. */
	result = 1;
	
	/* Determine if any bits repeat. */
	for (i = 0; i < code; i++)
	{
		/* Too short to repeat? */
		if (i < 2)
		{
			result++;
			continue;
		}
		
		/* Otherwise, repeats in groups of two. */
		group = ((i / 2) - 1);
		result += (1 << group);
	}
	
	/* Do we need to read in any extra bits to the distance value? */
	if (code >= 4)
	{
		/* How many bits to read? */
		group = (code / 2) - 1;
		
		/* Read in those extra bits. */
		i = INT32_MAX;
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input, SJME_BITSTREAM_LSB,
			&i, group)) || i == INT32_MAX)
			return sjme_error_default(error);
		
		/* Add in those bits. */
		result += i;
	}
	
	/* Success! */
	*outDist = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_readLength(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrInRange(257, 285) sjme_juint code,
	sjme_attrOutNotNull sjme_juint* outLength)
{
	sjme_errorCode error;
	sjme_juint result, base, i, group;
	
	if (inState == NULL || outLength == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (code < 257 || code > 285)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* The max code is 258, note the fifty-eight and not eighty-five. */
	/* Previously in my Java inflate code, getting these wrong was bad. */
	if (code == 285)
	{
		*outLength = 258;
		return SJME_ERROR_NONE;
	}
	
	/* Resultant length always starts at three. */
	result = 3;
	
	/* Determine if there are any repeating bits. */
	for (base = code - 257, i = 0; i < base; i++)
	{
		/* Is too short to be in a group. */
		if (i < 8)
		{
			result++;
			continue;
		}
		
		/* Code lengths are in groups of 4, which get shifted up by their */
		/* group index. */
		group = ((i / 4) - 1);
		result += (1 << group);
	}
	
	/* Do we need to read in any extra bits to the length value? */
	if (base >= 8)
	{
		/* How many bits to read? */
		group = (base / 4) - 1;
		
		/* Read in those extra bits. */
		i = INT32_MAX;
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input, SJME_BITSTREAM_LSB,
			&i, group)) || i == INT32_MAX)
			return sjme_error_default(error);
		
		/* Add in those bits. */
		result += i;
	}
	
	/* Success! */
	*outLength = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_stepBType(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_juint isFinal, blockType;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If final was hit, then we are done. */
	if (inState->finalHit)
	{
		inState->step = SJME_INFLATE_STEP_FINISHED;
		return SJME_ERROR_NONE;
	}
		
	/* Can we actually read the block? */
	if (sjme_error_is(error = sjme_inflate_bufferSaturation(
		inState, 3)))
		return sjme_error_default(error);
	
	/* Read in final block flag. */
	isFinal = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input,
		SJME_BITSTREAM_LSB, &isFinal, 1)) ||
		isFinal == INT32_MAX)
		return sjme_error_default(error);
	
	/* Read in block type. */
	blockType = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input,
		SJME_BITSTREAM_LSB, &blockType, 2)) ||
		blockType == INT32_MAX)
		return sjme_error_default(error);
	
	/* Invalid. */
	if (blockType == 3)
		return SJME_ERROR_INFLATE_INVALID_BTYPE;
	
	/* Was the final block hit? */
	if (isFinal)
		inState->finalHit = SJME_JNI_TRUE;
	
	/* Which step to transition to? */
	if (blockType == 0)
		inState->step = SJME_INFLATE_STEP_LITERAL_SETUP;
	else if (blockType == 1)
		inState->step = SJME_INFLATE_STEP_FIXED_SETUP;
	else
		inState->step = SJME_INFLATE_STEP_DYNAMIC_SETUP;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_stepLiteralData(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_jint left;
	sjme_juint raw;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Copy in what we can, provided the buffers are not saturated */
	/* and we have input data. */
	left = inState->sub.literalLeft;
	while (left > 0)
	{
		/* Can we actually read a byte? */
		if (sjme_error_is(error = sjme_inflate_bufferSaturation(
			inState, 8)))
			goto fail_saturated;
		
		/* Read in byte. */
		raw = INT32_MAX;
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input,
			SJME_BITSTREAM_LSB, &raw, 8)) ||
			raw == INT32_MAX)
			goto fail_read;
		
		/* Write out bits. */
		if (sjme_error_is(error = sjme_bitStream_outputWrite(
			inState->output,
			SJME_BITSTREAM_LSB, raw, 8)))
			goto fail_write;
		
		/* We consumed a byte. */
		left--;
	}
	
	/* Store for next run. */
	inState->sub.literalLeft = left;
	
	/* Did we read in all the data? Go back to the block type parse. */
	if (left == 0)
		inState->step = SJME_INFLATE_STEP_CHECK_BTYPE;
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_write:
fail_read:
fail_saturated:
	/* Store for next run. */
	inState->sub.literalLeft = left;
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_inflate_stepLiteralSetup(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_juint len, nel;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Literal uncompressed data starts at the byte boundary. */
	if (sjme_error_is(error = sjme_bitStream_inputAlign(
		inState->input,
		8, NULL)))
		return sjme_error_default(error);
		
	/* Can we actually read the lengths? */
	if (sjme_error_is(error = sjme_inflate_bufferSaturation(
		inState, 32)))
		return sjme_error_default(error);
	
	/* Read both the length and its complement. */
	len = INT32_MAX;
	nel = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input, SJME_BITSTREAM_LSB,
		&len, 16)))
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input, SJME_BITSTREAM_LSB,
		&nel, 16)))
		return sjme_error_default(error);
	
	/* Both of these should be the complement to each other. */
	if (len != (nel ^ 0xFFFF))
		return SJME_ERROR_INFLATE_INVALID_INVERT;
	
	/* Setup for next step. */
	memset(&inState->sub, 0, sizeof(inState->sub));
	inState->sub.literalLeft = len;
	inState->step = SJME_INFLATE_STEP_LITERAL_DATA;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_stepDynamicSetup(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_juint rawCodeLen[SJME_INFLATE_NUM_CODE_LEN];
	sjme_juint rawLit[SJME_INFLATE_NUM_LIT_LENS];
	sjme_juint rawDist[SJME_INFLATE_NUM_DIST_LENS];
	sjme_juint hLit, hDist, hcLen, raw;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in literal count. */
	hLit = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input, SJME_BITSTREAM_LSB,
		&hLit, 5)) ||
		hLit == INT32_MAX)
		return sjme_error_default(error);
		
	/* Read in distance count. */
	hDist = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input, SJME_BITSTREAM_LSB,
		&hDist, 5)) ||
		hDist == INT32_MAX)
		return sjme_error_default(error);
		
	/* Read in code length count. */
	hcLen = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input, SJME_BITSTREAM_LSB,
		&hcLen, 4)) ||
		hcLen == INT32_MAX)
		return sjme_error_default(error);
	
	/* Determine their actual values. */
	hLit += 257;
	hDist += 1;
	hcLen += 4;
	
	/* Read in raw code lengths. */
	memset(rawCodeLen, 0, sizeof(rawCodeLen));
	for (i = 0; i < hcLen; i++)
	{
		/* Read in raw code length. */
		raw = INT32_MAX;
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input, SJME_BITSTREAM_LSB,
			&raw, 3)) || raw == INT32_MAX)
			return sjme_error_default(error);
		
		/* Shuffle it in appropriately, the shuffle is "optimized" according */
		/* to RFC1951 so the most importing bits are set first. */
		rawCodeLen[sjme_inflate_shuffleBits[i]] = raw;
	}
	
	/* Build code length tree. */
	if (sjme_error_is(error = sjme_inflate_dynamicBuildTree(
		inState, &inState->treeCodeLen,
		&rawCodeLen[0],
		SJME_INFLATE_NUM_CODE_LEN,
		SJME_INFLATE_NUM_CODE_LEN)))
		return sjme_error_default(error);
	
	/* Read literal values. */
	memset(rawLit, 0, sizeof(rawLit));
	for (i = 0; i < hLit;)
	{
		/* Read in raw code. */
		if (sjme_error_is(error = sjme_inflate_dynamicBuildReadCode(
			inState, &rawLit[0], &i,
			hLit, SJME_INFLATE_NUM_LIT_LENS)))
			return sjme_error_default(error);
	}
	
	/* Build literal tree. */
	if (sjme_error_is(error = sjme_inflate_dynamicBuildTree(
		inState, &inState->treeLit,
		&rawLit[0],
		hLit,
		SJME_INFLATE_NUM_LIT_LENS)))
		return sjme_error_default(error);
	
	/* Read distance values. */
	memset(rawDist, 0, sizeof(rawDist));
	for (i = 0; i < hDist;)
	{
		/* Read in raw code. */
		if (sjme_error_is(error = sjme_inflate_dynamicBuildReadCode(
			inState, &rawDist[0], &i, hDist,
			SJME_INFLATE_NUM_DIST_LENS)))
			return sjme_error_default(error);
	}
	
	/* Build Distance tree. */
	if (sjme_error_is(error = sjme_inflate_dynamicBuildTree(
		inState, &inState->treeDist,
		&rawDist[0],
		hDist,
		SJME_INFLATE_NUM_DIST_LENS)))
		return sjme_error_default(error);
	
	/* Set state for code reading. */
	memset(&inState->sub, 0, sizeof(inState->sub));
	inState->sub.huffman.readCode = sjme_inflate_dynamicReadCode;
	inState->sub.huffman.readDist = sjme_inflate_dynamicReadDist;
	
	/* Move onto decompression stage. */
	inState->step = SJME_INFLATE_STEP_INFLATE_FROM_TREE;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_stepFixedSetup(
	sjme_attrInNotNull sjme_inflate* inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup for new state, which is very simple. */
	memset(&inState->sub, 0, sizeof(inState->sub));
	inState->sub.huffman.readCode = sjme_inflate_fixedReadCode;
	inState->sub.huffman.readDist = sjme_inflate_fixedReadDist;
	
	/* Move onto decompression stage. */
	inState->step = SJME_INFLATE_STEP_INFLATE_FROM_TREE;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_stepInflateFromTree(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_juint code, length, dist;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Continual code reading loop, provided not saturated. */
	for (;;)
	{
		/* Read in next code. */
		code = INT32_MAX;
		if (sjme_error_is(error = inState->sub.huffman.readCode(inState,
			&code)) || code == INT32_MAX)
			return sjme_error_default(error);
		
		/* Invalid. */
		if (code > 285)
			return SJME_ERROR_INFLATE_INVALID_CODE;
		
		/* Stop decompression. */
		else if (code == 256)
		{
			/* Read the next block type. */
			inState->step = SJME_INFLATE_STEP_CHECK_BTYPE;
			break;
		}
		
		/* Literal byte value. */
		else if (code >= 0 && code <= 255)
		{
			/* Standard write. */
			if (sjme_error_is(error = sjme_bitStream_outputWrite(
				inState->output, SJME_BITSTREAM_LSB,
				code, 8)))
				return sjme_error_default(error);
			
			continue;
		}
		
		/* Read the length value, which is based on the code and may */
		/* read in more values. */
		length = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_readLength(inState,
			code, &length)) ||
			length == INT32_MAX)
			return sjme_error_default(error);
		
		/* Read in the distance. */
		dist = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_readDistance(inState,
			&dist)) || dist == INT32_MAX)
			return sjme_error_default(error);
		
		/* Copy from the window. */
		if (sjme_error_is(error = sjme_inflate_copyWindow(
			inState, (sjme_jint)length, (sjme_jint)dist)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_destroy(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Close the input bit stream. */
	if (inState->input != NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inState->input))))
			return sjme_error_default(error);
		
		inState->input = NULL;
	}
	
	/* Close the output bit stream. */
	if (inState->output != NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inState->output))))
			return sjme_error_default(error);
		
		inState->output = NULL;
	}
	
	/* Close the source stream. */
	if (inState->source != NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inState->source))))
			return sjme_error_default(error);
		
		inState->source = NULL;
	}
	
	/* Destroy the input buffer. */
	if (inState->inputBuffer != NULL)
	{
		if (sjme_error_is(error = sjme_circleBuffer_destroy(
			inState->inputBuffer)))
			return sjme_error_default(error);
		
		inState->inputBuffer = NULL;
	}
	
	/* Destroy the output buffer. */
	if (inState->outputBuffer != NULL)
	{
		if (sjme_error_is(error = sjme_circleBuffer_destroy(
			inState->outputBuffer)))
			return sjme_error_default(error);
		
		inState->outputBuffer = NULL;
	}
	
	/* Destroy the output window. */
	if (inState->window != NULL)
	{
		if (sjme_error_is(error = sjme_circleBuffer_destroy(
			inState->window)))
			return sjme_error_default(error);
		
		inState->window = NULL;
	}
	
	/* Destroy code length tree. */
	if (inState->treeCodeLen != NULL)
	{
		if (sjme_error_is(error = sjme_traverse_destroy(
			SJME_AS_TRAVERSE(inState->treeCodeLen))))
			return sjme_error_default(error);
		
		inState->treeCodeLen = NULL;
	}
	
	/* Destroy literal tree. */
	if (inState->treeLit != NULL)
	{
		if (sjme_error_is(error = sjme_traverse_destroy(
			SJME_AS_TRAVERSE(inState->treeLit))))
			return sjme_error_default(error);
		
		inState->treeLit = NULL;
	}
	
	/* Destroy distance tree. */
	if (inState->treeDist != NULL)
	{
		if (sjme_error_is(error = sjme_traverse_destroy(
			SJME_AS_TRAVERSE(inState->treeDist))))
			return sjme_error_default(error);
		
		inState->treeDist = NULL;
	}
	
	/* Free self. */
	sjme_alloc_free(inState);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_inflate(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_jint drainOff;
	
	if (inState == NULL || readCount == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* If this previously failed, then recover it. */
	if (sjme_error_is(inState->failed))
		return sjme_error_default(inState->failed);
	
	/* Constant inflation loop. */
	drainOff = 0;
	for (;;)
	{
		/* Can anything be drained? */
		if (inState->outputBuffer->ready > 0)
			if (sjme_error_is(error = sjme_inflate_drain(inState, &drainOff,
				outBuf, length)))
			{
				inState->failed = error;
				return sjme_error_default(error);
			}
		
		/* Did we actually finish? */
		if (inState->step == SJME_INFLATE_STEP_CHECK_BTYPE &&
			inState->finalHit)
			inState->step = SJME_INFLATE_STEP_FINISHED;
		
		/* Finished? */
		if (inState->step == SJME_INFLATE_STEP_FINISHED)
		{
			/* There is still data that can be read. */
			if (drainOff != 0 || inState->outputBuffer->ready > 0)
				break;
			
			/* True EOF */
			*readCount = -1;
			return SJME_ERROR_NONE;
		}
		
		/* Fill in the input buffer as much as possible, this will */
		/* reduce any subsequent disk activity as it is done in bulk. */
		if (sjme_error_is(error = sjme_inflate_bitFill(inState)))
		{
			inState->failed = error;
			return sjme_error_default(error);
		}
		
		/* Check the ready state, stop if not yet ready */
		if (sjme_error_is(sjme_inflate_bufferSaturation(inState,
			1)))
		{
			if (error == SJME_ERROR_TOO_SHORT)
				continue;
			
			if (error == SJME_INFLATE_IO_BUFFER_SATURATED)
				break;
			
			inState->failed = error;
			return sjme_error_default(error);
		}
		
		/* Which step at inflation are we at? */
		error = SJME_ERROR_UNKNOWN;
		switch (inState->step)
		{
			case SJME_INFLATE_STEP_CHECK_BTYPE:
				error = sjme_inflate_stepBType(inState);
				break;
				
			case SJME_INFLATE_STEP_LITERAL_DATA:
				error = sjme_inflate_stepLiteralData(inState);
				break;
				
			case SJME_INFLATE_STEP_LITERAL_SETUP:
				error = sjme_inflate_stepLiteralSetup(inState);
				break;
				
			case SJME_INFLATE_STEP_DYNAMIC_SETUP:
				error = sjme_inflate_stepDynamicSetup(inState);
				break;
				
			case SJME_INFLATE_STEP_FIXED_SETUP:
				error = sjme_inflate_stepFixedSetup(inState);
				break;
				
			case SJME_INFLATE_STEP_INFLATE_FROM_TREE:
				error = sjme_inflate_stepInflateFromTree(inState);
				break;
		}
		
		/* Did inflation fail? */
		if (sjme_error_is(error))
		{
			/* Not enough input data, need to fill more! */
			if (error == SJME_ERROR_TOO_SHORT)
				continue;
				
			/* Or we filled the output buffer as much as possible and */
			/* cannot fit anymore. */
			if (error == SJME_INFLATE_IO_BUFFER_SATURATED)
				break;
			
			/* Set as failed. */
			inState->failed = error;
			return sjme_error_default(error);
		}
	}
	
	/* The amount written is the amount drained. */
	*readCount = drainOff;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_inflate** outState,
	sjme_attrInNotNull sjme_stream_input source)
{
	sjme_errorCode error;
	sjme_inflate* result;
	sjme_circleBuffer* inputBuffer;
	sjme_circleBuffer* outputBuffer;
	sjme_circleBuffer* window;
	sjme_bitStream_input input;
	sjme_bitStream_output output;
	
	if (inPool == NULL || outState == NULL || source == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate resultant state. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool, sizeof(*result),
		(sjme_pointer*)&result)) ||
		result == NULL)
		goto fail_allocResult;
	
	/* Allocate the input buffer. */
	inputBuffer = NULL;
	if (sjme_error_is(error = sjme_circleBuffer_new(inPool,
		&inputBuffer,
		SJME_CIRCLE_BUFFER_QUEUE,
		SJME_INFLATE_IO_BUFFER_SIZE)) || inputBuffer == NULL)
		goto fail_allocInputBuffer;
	
	/* Allocate the output buffer. */
	outputBuffer = NULL;
	if (sjme_error_is(error = sjme_circleBuffer_new(inPool,
		&outputBuffer,
		SJME_CIRCLE_BUFFER_QUEUE,
		SJME_INFLATE_IO_BUFFER_SIZE)) || outputBuffer == NULL)
		goto fail_allocOutputBuffer;
	
	/* Allocate window. */
	window = NULL;
	if (sjme_error_is(error = sjme_circleBuffer_new(inPool,
		&window,
		SJME_CIRCLE_BUFFER_WINDOW,
		SJME_INFLATE_WINDOW_SIZE)) || window == NULL)
		goto fail_allocWindow;
	
	/* Open input bit stream. */
	input = NULL;
	if (sjme_error_is(error = sjme_bitStream_inputOpen(inPool,
		&input, sjme_inflate_bitRead,
		result, NULL)) || input == NULL)
		goto fail_openInput;
	
	/* Open output bit stream. */
	output = NULL;
	if (sjme_error_is(error = sjme_bitStream_outputOpen(inPool,
		&output, sjme_inflate_bitWrite,
		result, NULL)) || output == NULL)
		goto fail_openOutput;
	
	/* Store everything in the result now. */
	result->inPool = inPool;
	result->failed = SJME_ERROR_NONE;
	result->source = source;
	result->input = input;
	result->inputBuffer = inputBuffer;
	result->output = output;
	result->outputBuffer = outputBuffer;
	result->window = window;
	
	/* Since we hold onto the source, count it up. */
	if (sjme_error_is(error = sjme_alloc_weakRef(source, NULL)))
		goto fail_countSource;
	
	/* Along with the input... */
	if (sjme_error_is(error = sjme_alloc_weakRef(input, NULL)))
		goto fail_countSource;
	
	/* and output bit streams! */
	if (sjme_error_is(error = sjme_alloc_weakRef(output, NULL)))
		goto fail_countSource;
	
	/* Success! */
	*outState = result;
	return SJME_ERROR_NONE;

fail_countSource:
fail_openOutput:
	if (output != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(output));
fail_openInput:
	if (input != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(input));
fail_allocWindow:
	if (window != NULL)
		sjme_circleBuffer_destroy(window);
fail_allocOutputBuffer:
	if (outputBuffer != NULL)
		sjme_circleBuffer_destroy(outputBuffer);
fail_allocInputBuffer:
	if (inputBuffer != NULL)
		sjme_circleBuffer_destroy(inputBuffer);
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
		
	return sjme_error_default(error);
}
