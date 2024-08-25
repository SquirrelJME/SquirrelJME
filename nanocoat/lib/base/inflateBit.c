/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/inflate.h"

static sjme_errorCode sjme_inflate_bitNeed(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrInPositiveNonZero sjme_jint bitCount)
{
	sjme_jint readyBits;
	
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitCount <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* How many full bytes are ready? */
	readyBits = buffer->ready * 8;
	
	/* Then add whatever is in the current bit count. */
	readyBits += buffer->bitCount;
	
	/* Too little, or has enough? */
	if (readyBits < bitCount)
		return SJME_ERROR_TOO_SHORT;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_bitIn(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrInValue sjme_inflate_order order,
	sjme_attrInValue sjme_inflate_peek popPeek,
	sjme_attrInRange(1, 32) sjme_juint bitCount,
	sjme_attrOutNotNull sjme_juint* readValue)
{
	sjme_errorCode error;
	sjme_juint result, val;
#if defined(SJME_CONFIG_DEBUG)
	sjme_cchar binary[40];
	sjme_cchar binary2[40];
#endif
	
	if (buffer == NULL || readValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitCount <= 0 || bitCount > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (order != SJME_INFLATE_LSB && order != SJME_INFLATE_MSB)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (popPeek != SJME_INFLATE_POP && popPeek != SJME_INFLATE_PEEK)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Can we actually read this much in? */
	/* If not we illegal state because bitNeed should have been called */
	/* before this to check that this is actually valid! */
	if (sjme_error_is(sjme_inflate_bitNeed(buffer, bitCount)))
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Is there room to read into the tiny bit buffer? */
	while (buffer->bitCount <= 24 && buffer->ready > 0)
	{
		/* Read the next byte from the buffer. */
		val = buffer->buffer[buffer->readHead] & 0xFF;
		
		/* Move counters as we consumed a byte. */
		buffer->ready--;
		buffer->readHead = (buffer->readHead + 1) &
			SJME_INFLATE_IO_BUFFER_MASK;
		
		/* Shift in the read bytes to the higher positions, so this way */
		/* they are always added onto the top-most value. */
		/* Then layer the bits at the highest position. */
		buffer->bitBuffer |= (val << buffer->bitCount); 
		buffer->bitCount += 8;

#if defined(SJME_CONFIG_DEBUG)
		memset(binary, 0, sizeof(binary));
		memset(binary2, 0, sizeof(binary2));
		sjme_util_intToBinary(binary, sizeof(binary) - 1,
			buffer->bitBuffer, buffer->bitCount);
		sjme_util_intToBinary(binary2, sizeof(binary2) - 1,
			val, 8);
		sjme_message("bitBuffer %s (%d) << %s %d %02x",
			binary, buffer->bitCount, binary2, val, val);
#endif
	}
	
	/* Mask in the value, which is always at the lower bits */
	result = buffer->bitBuffer & ((1 << bitCount) - 1);
	
	/* Shift down the mini window for the next read, if not peeking. */
	if (popPeek == SJME_INFLATE_POP)
	{
		buffer->bitBuffer >>= bitCount;
		buffer->bitCount -= bitCount;
	}
	
	/* If in MSB, the bits need to be reversed. */
	if (order == SJME_INFLATE_MSB)
		result = sjme_util_reverseBitsU(result) >> (32 - bitCount);
	
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	if (popPeek == SJME_INFLATE_POP)
	{
		memset(binary, 0, sizeof(binary));
		memset(binary2, 0, sizeof(binary2));
		sjme_util_intToBinary(binary, sizeof(binary) - 1,
			buffer->bitBuffer, buffer->bitCount);
		sjme_util_intToBinary(binary2, sizeof(binary2) - 1,
			result, bitCount);
		sjme_message("bitBuffer %s (%d) >> %s %d 0x%02x (%d bits)",
			binary, buffer->bitCount,
			binary2, result, result, bitCount);
	}
#endif
	
	/* Success! */
	*readValue = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_bitInTree(
	sjme_attrInNotNull sjme_inflate_buffer* inBuffer,
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

static sjme_errorCode sjme_inflate_bitInCodeLen(
	sjme_attrInNotNull sjme_inflate_buffer* inBuffer,
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

static sjme_errorCode sjme_inflate_bitOut(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrInValue sjme_inflate_order order,
	sjme_attrOutNotNull sjme_inflate_window* window,
	sjme_attrInRange(1, 32) sjme_juint bitCount,
	sjme_attrOutNotNull sjme_juint writeValue)
{
	sjme_juint mask;
	sjme_jubyte single;
	
	if (buffer == NULL || window == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitCount <= 0 || bitCount > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (order != SJME_INFLATE_LSB && order != SJME_INFLATE_MSB)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Buffer overflowing? */
	if (buffer->bitCount + bitCount > 32)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Calculate the data mask, make sure the value is valid. */
	mask = (1 << bitCount) - 1;
	writeValue &= mask;
	
	/* If writing MSB, reverse bits. */
	if (order == SJME_INFLATE_MSB)
		writeValue = (sjme_util_reverseBitsU(writeValue) >>
			(32 - bitCount)) & mask;
	
	/* Debug. */	
	sjme_message("Shift in %d %x (bc=%d)",
		writeValue, writeValue, buffer->bitCount);
	
	/* Place into the bit buffer. */
	buffer->bitBuffer |= (writeValue << buffer->bitCount);
	buffer->bitCount += bitCount;
	
	/* Drain the buffer out. */
	while (buffer->bitCount >= 8)
	{
		/* Read in byte value to store. */
		single = (buffer->bitBuffer & 0xFF);
		
		sjme_message("Out: %d %x (we=%d)",
			single, single, window->end);
		
		/* Shift down. */
		buffer->bitBuffer >>= 8;
		buffer->bitCount -= 8;
		
		/* Store into output buffer and shift head. */
		buffer->buffer[buffer->writeHead] = single;
		buffer->writeHead = (buffer->writeHead + 1) &
			SJME_INFLATE_IO_BUFFER_MASK;
		buffer->ready += 1;
		
		/* Store into the window as well! Shifting the end there also */
		window->window[window->end] = single;
		window->end = ((window->end + 1) & SJME_INFLATE_WINDOW_MASK);
		if (window->length < SJME_INFLATE_WINDOW_SIZE)
			window->length++;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
