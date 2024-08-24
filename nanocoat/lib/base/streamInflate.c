/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdio.h>

#include "sjme/stream.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/util.h"

/** The size of the input/output buffer. */
#define SJME_INFLATE_IO_BUFFER_SIZE 2048

/** The mask for the input/output buffer position. */
#define SJME_INFLATE_IO_BUFFER_MASK 2047

/** When the output buffer is considered saturated. */
#define SJME_INFLATE_IO_BUFFER_SATURATED 1700

/** The maximum window size. */
#define SJME_INFLATE_WINDOW_MAX_SIZE 32768

/** The window size. */
#define SJME_INFLATE_WINDOW_SIZE 16384

/** The window mask. */
#define SJME_INFLATE_WINDOW_MASK 16383

/** Maximum huffman tree size, ~ceil^2((288 * 24 * 3) * 0.50 for lower mem). */
#define SJME_INFLATE_HUFF_STORAGE_SIZE 10240

/** The limit for code lengths. */
#define SJME_INFLATE_CODE_LEN_LIMIT 19

/** The maximum number of bits in the code length tree. */
#define SJME_INFLATE_CODE_LEN_MAX_BITS 15

/** The number of codes. */
#define SJME_INFLATE_NUM_CODES 288

/** Code length shuffled bit order, for "optimal" bit placement. */
static const sjme_jubyte sjme_stream_inflateShuffleBits[
	SJME_INFLATE_CODE_LEN_LIMIT] =
{
	16, 17, 18, 0, 8, 7, 9,
	6, 10, 5, 11, 4, 12, 3,
	13, 2, 14, 1, 15
};

/**
 * Zip bit reading mode.
 * 
 * @since 2024/08/18
 */
typedef enum sjme_stream_inflateOrder
{
	/** Least significant bit. */
	SJME_INFLATE_LSB,
	
	/** Most significant bit. */
	SJME_INFLATE_MSB,
} sjme_stream_inflateOrder;

/**
 * Used to either peek or pop from the bit stream.
 * 
 * @since 2024/08/20
 */
typedef enum sjme_stream_inflatePeek
{
	/** Pop value. */
	SJME_INFLATE_POP,
	
	/** Peek value. */
	SJME_INFLATE_PEEK,
} sjme_stream_inflatePeek;

/**
 * Which type of node is this in the tree?
 * 
 * @since 2024/08/22
 */
typedef enum sjme_stream_inflateHuffNodeType
{
	/** Unknown. */
	SJME_INFLATE_UNKNOWN,
	
	/** Node. */
	SJME_INFLATE_NODE,
	
	/** Leaf. */
	SJME_INFLATE_LEAF,
} sjme_stream_inflateHuffNodeType;

/**
 * The inflation step.
 * 
 * @since 2024/08/17
 */
typedef enum sjme_stream_inflateStep
{
	/** Parse BTYPE and determine how to continue. */
	SJME_INFLATE_STEP_CHECK_BTYPE,
	
	/** Literal uncompressed data header. */
	SJME_INFLATE_STEP_LITERAL_HEADER,
	
	/** Literal uncompressed data. */
	SJME_INFLATE_STEP_LITERAL_DATA,
	
	/** Load in dynamic huffman table. */
	SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD,
	
	/** Load in dynamic huffman table: Code length tree. */
	SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_CODE_LEN,
	
	/** Load in dynamic huffman table: Literal. */
	SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_LITERAL,
	
	/** Load in dynamic huffman table: Distance tree. */
	SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_DISTANCE,
	
	/** Fixed static huffman table. */
	SJME_INFLATE_STEP_FIXED_TABLE_INFLATE,
	
	/** Inflate from a given huffman tree. */
	SJME_INFLATE_STEP_INFLATE_FROM_TREE,
	
	/** Finished, nothing is left. */
	SJME_INFLATE_STEP_FINISHED,
} sjme_stream_inflateStep;

/**
 * Inflation buffer state.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_stream_inflateBuffer
{
	/** The amount of data that is ready for processing. */
	sjme_jint ready;
	
	/** The current read head. */
	sjme_jint readHead;
	
	/** The current write head. */
	sjme_jint writeHead;
	
	/** The buffer storage. */
	sjme_jubyte buffer[SJME_INFLATE_IO_BUFFER_SIZE];
	
	/** Was EOF hit in this buffer? */
	sjme_jboolean hitEof;
	
	/** The current bit buffer. */
	sjme_juint bitBuffer;
	
	/** The amount of bits in the buffer. */
	sjme_juint bitCount;
} sjme_stream_inflateBuffer;

/**
 * The window for output inflated data.
 * 
 * @since 2024/08/18
 */
typedef struct sjme_stream_inflateWindow
{
	/** The number of bytes in the window. */
	sjme_juint length;
	
	/** The end position of the window. */
	sjme_juint end;
	
	/** The window buffer. */
	sjme_jubyte window[SJME_INFLATE_WINDOW_SIZE];
} sjme_stream_inflateWindow;

/**
 * Initial code length huffman tree building values.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_stream_inflateHuffInit
{
	/** Literal length. */
	sjme_juint litLen;
	
	/** Distance length. */
	sjme_juint distLen;
	
	/** The code length count. */
	sjme_juint codeLen;
	
	/** The raw code length bit values. */
	sjme_juint rawCodeLens[SJME_INFLATE_CODE_LEN_LIMIT];
} sjme_stream_inflateHuffInit;

/**
 * Huffman tree building parameters, used as input.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_stream_inflateHuffParam
{
	/** Code lengths to input. */
	sjme_juint* lengths;
	
	/** The number of code lengths. */
	sjme_juint count;
} sjme_stream_inflateHuffParam;

/**
 * Huffman tree node.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_align64 sjme_stream_inflateHuffNode
	sjme_stream_inflateHuffNode;

struct sjme_align64 sjme_stream_inflateHuffNode
{
	/** Node data. */
	union
	{
		/** Data if a leaf. */
		struct
		{
			/** Zero branch. */
			sjme_stream_inflateHuffNode* zero;
			
			/** One branch. */
			sjme_stream_inflateHuffNode* one;
		} node;
		
		/** Data if a node. */
		struct
		{
			/** The code stored here. */
			sjme_juint code;
		} leaf;
	} data;
	
	/** Which type of node is this? */
	sjme_stream_inflateHuffNodeType type;
};

/**
 * Huffman tree parameters.
 * 
 * @since 2024/08/20
 */
typedef struct sjme_stream_inflateHuffTree
{
	/** The root node. */
	sjme_stream_inflateHuffNode* root;
} sjme_stream_inflateHuffTree;

/**
 * Storage for huffman tree nodes.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_stream_inflateHuffTreeStorage
{
	/** Storage for the huffman tree nodes. */
	sjme_align64 sjme_jubyte storage[SJME_INFLATE_HUFF_STORAGE_SIZE];
	
	/** Next free node. */
	sjme_stream_inflateHuffNode* next;
	
	/** Final end of tree. */
	sjme_stream_inflateHuffNode* finalEnd;
} sjme_stream_inflateHuffTreeStorage;

/**
 * Inflation state.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_stream_inflateState sjme_stream_inflateState;

typedef sjme_errorCode (*sjme_stream_inflateReadCodeFunc)(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrOutNotNull sjme_juint* outCode);

typedef sjme_errorCode (*sjme_stream_inflateReadDistFunc)(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrOutNotNull sjme_juint* outDist);

/**
 * Inflation state.
 * 
 * @since 2024/08/17
 */
struct sjme_stream_inflateState
{
	/** The current step in inflation. */
	sjme_stream_inflateStep step;
	
	/** Was the final block hit? */
	sjme_jboolean finalHit;
	
	/** Is the input data corrupted? */
	sjme_jboolean invalidInput;
	
	/** The output window. */
	sjme_stream_inflateWindow window;
	
	/** The amount of literal bytes left to read. */
	sjme_jint literalLeft;
	
	/** Initialization data for the initial huffman tree. */
	sjme_stream_inflateHuffInit huffInit;
	
	/** The function for reading codes. */
	sjme_stream_inflateReadCodeFunc readCode;
	
	/** The function for reading distances. */
	sjme_stream_inflateReadDistFunc readDist;
	
	/** Huffman tree node storage. */
	sjme_stream_inflateHuffTreeStorage huffStorage;
	
	/** Code length tree. */
	sjme_stream_inflateHuffTree codeLenTree;
	
	/** Distance tree. */
	sjme_stream_inflateHuffTree distanceTree;
	
	/** Literal tree, not literally. */
	sjme_stream_inflateHuffTree literalTree;
	
	/** The input buffer. */
	sjme_stream_inflateBuffer input;
	
	/** The output buffer. */
	sjme_stream_inflateBuffer output;
};

/**
 * Inflate stream initialization.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_stream_inflateInit
{
	/** The compressed data stream. */
	sjme_stream_input handle;
	
	/** Decompression state. */
	sjme_stream_inflateState* handleTwo;
} sjme_stream_inflateInit;

static sjme_errorCode sjme_stream_inflateBufferArea(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrOutNotNull sjme_jint* outRemainder,
	sjme_attrOutNotNull sjme_pointer* outBufOpPos,
	sjme_attrOutNotNull sjme_jint* outBufOpLen)
{
	sjme_jint remainder, chunkSize;
	sjme_jboolean leftSide;
	
	if (buffer == NULL || outBufOpPos == NULL || outBufOpLen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can we even read/write more data? */
	remainder = SJME_INFLATE_IO_BUFFER_SIZE - buffer->ready;
	if (remainder <= 0)
		return SJME_ERROR_TOO_SHORT;
	
	/* If nothing is ready, reset heads. */
	if (buffer->ready == 0)
	{
		buffer->readHead = 0;
		buffer->writeHead = 0;
	}
	
	/* Is the write head on the left, or the right? */
	leftSide = (buffer->writeHead <= buffer->readHead) && buffer->ready != 0;
	if (leftSide)
		chunkSize = buffer->readHead - buffer->writeHead;
	else
		chunkSize = SJME_INFLATE_IO_BUFFER_SIZE - buffer->writeHead;
	
	/* Limit to the remainder amount. */
	if (remainder < chunkSize)
		chunkSize = remainder;
	
	/* Give what was calculated. */
	*outRemainder = remainder;
	*outBufOpPos = &buffer->buffer[buffer->writeHead];
	*outBufOpLen = chunkSize;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBufferGive(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInPositiveNonZero sjme_jint count)
{
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (count < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Move count up and adjust write head. */
	buffer->ready += count;
	buffer->writeHead = (buffer->writeHead + count) &
		SJME_INFLATE_IO_BUFFER_MASK;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBufferConsume(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInPositiveNonZero sjme_jint count)
{
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (count < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Move count down and adjust read head. */
	buffer->ready -= count;
	buffer->readHead = (buffer->readHead + count) &
		SJME_INFLATE_IO_BUFFER_MASK;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBitNeed(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInRange(1, 32) sjme_jint bitCount)
{
	sjme_jint readyBits;
	
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitCount <= 0 || bitCount > 32)
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

static sjme_errorCode sjme_stream_inflateBitIn(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInValue sjme_stream_inflateOrder order,
	sjme_attrInValue sjme_stream_inflatePeek popPeek,
	sjme_attrInRange(1, 32) sjme_juint bitCount,
	sjme_attrOutNotNull sjme_juint* readValue)
{
	sjme_errorCode error;
	sjme_juint result, val;
	
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
	if (sjme_error_is(sjme_stream_inflateBitNeed(buffer, bitCount)))
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Is there room to read into the tiny bit buffer? */
	while (buffer->bitCount <= 24 && buffer->ready > 0)
	{
		/* Read the next byte from the buffer. */
		val = buffer->buffer[buffer->readHead] & 0xFF;
		
		/* Debug. */
		sjme_message("Inflate Raw In: %d 0x%02x", val, val);
		
		/* Move counters as we consumed a byte. */
		buffer->ready--;
		buffer->readHead = (buffer->readHead + 1) &
			SJME_INFLATE_IO_BUFFER_MASK;
		
		/* Shift in the read bytes to the higher positions, so this way */
		/* they are always added onto the top-most value. */
		/* First make sure the bits above are clear. */
		buffer->bitBuffer &= ((1 << buffer->bitCount) - 1);
		
		/* Then layer the bits at the highest position. */
		buffer->bitBuffer |= (val << buffer->bitCount); 
		buffer->bitCount += 8;
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
		sjme_message("Read %d 0x%02x (%d bits)",
			result, result, bitCount);
#endif
	
	/* Success! */
	*readValue = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBitInTree(
	sjme_attrInNotNull sjme_stream_inflateBuffer* inBuffer,
	sjme_attrInNotNull sjme_stream_inflateHuffTree* fromTree,
	sjme_attrOutNotNull sjme_juint* outValue)
{
	sjme_stream_inflateHuffNode* atNode;
	sjme_errorCode error;
	sjme_juint v;
	
	if (inBuffer == NULL || fromTree == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Start at the root node. */
	atNode = fromTree->root;
	if (atNode == NULL)
		return SJME_ERROR_INFLATE_HUFF_TREE_INCOMPLETE;
	
	/* Read in bits and go in the given direction. */
	while (atNode != NULL && atNode->type == SJME_INFLATE_NODE)
	{
		sjme_message("atNode %p -> %p, %p", atNode,
			atNode->data.node.zero, atNode->data.node.one);
		
		/* Read in single bit. */
		v = INT32_MAX;
		if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			1, &v)) || v == INT32_MAX)
			return sjme_error_default(error);
		
		/* Traverse in the given direction. */
		atNode = (v != 0 ? atNode->data.node.one : atNode->data.node.zero);
	}
	
	/* If we stopped, then this is not even a complete/valid tree. */
	sjme_message("atNode %p", atNode);
	if (atNode == NULL || atNode->type != SJME_INFLATE_LEAF)
		return SJME_ERROR_INFLATE_HUFF_TREE_INCOMPLETE;
	
	/* Give the value here. */
	*outValue = atNode->data.leaf.code;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBitInCodeLen(
	sjme_attrInNotNull sjme_stream_inflateBuffer* inBuffer,
	sjme_attrInNotNull sjme_stream_inflateHuffTree* codeLenTree,
	sjme_attrInOutNotNull sjme_juint* index,
	sjme_attrOutNotNull sjme_juint* outLengths,
	sjme_attrInPositive sjme_juint count)
{
	sjme_errorCode error;
	sjme_juint code;
	
	if (inBuffer == NULL || codeLenTree == NULL || index == NULL ||
		outLengths == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (count < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Read in the next code. */
	code = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inflateBitInTree(inBuffer,
		codeLenTree, &code)) || code == INT32_MAX)
		return sjme_error_default(error);
	
	/* Literal value? */
	if (code >= 0 && code < 16)
	{
		/* Make sure we do not write out of the length buffer. */
		if ((*index) >= count)
			return SJME_ERROR_INFLATE_INVALID_CODE;
		
		/* Store in value. */
		outLengths[(*index)++] = code;
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Repeating sequence. */
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inflateBitOut(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInValue sjme_stream_inflateOrder order,
	sjme_attrOutNotNull sjme_stream_inflateWindow* window,
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

static sjme_errorCode sjme_stream_inflateBuildTreeInsertNext(
	sjme_attrInNotNull sjme_stream_inflateHuffTree* outTree,
	sjme_attrInNotNull sjme_stream_inflateHuffTreeStorage* inStorage,
	sjme_attrOutNotNull sjme_stream_inflateHuffNode** outNode)
{
	if (outTree == NULL || inStorage == NULL || outNode == NULL)
		return SJME_ERROR_NONE;
	
	/* Does the storage need initialization? */
	if (inStorage->next == NULL || inStorage->finalEnd == NULL)
	{
		inStorage->next = (sjme_stream_inflateHuffNode*)
			&inStorage->storage[0];
		inStorage->finalEnd = (sjme_stream_inflateHuffNode*)
			&inStorage->storage[SJME_INFLATE_HUFF_STORAGE_SIZE /
				sizeof(sjme_stream_inflateHuffNode)];
	}
	
	/* Is the tree now full? */
	if ((sjme_intPointer)inStorage->next >=
		(sjme_intPointer)inStorage->finalEnd)
		return SJME_ERROR_INFLATE_HUFF_TREE_FULL;
	
	/* Move pointer up. */
	*outNode = (inStorage->next++);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBuildTreeInsert(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrInNotNull sjme_stream_inflateHuffTree* outTree,
	sjme_attrInNotNull sjme_stream_inflateHuffTreeStorage* inStorage,
	sjme_attrInPositive sjme_juint value,
	sjme_attrInValue sjme_juint sym,
	sjme_attrInPositiveNonZero sjme_juint symMask)
{
	sjme_errorCode error;
	sjme_juint maskBitCount, sh;
	sjme_stream_inflateHuffNode* atNode;
	sjme_stream_inflateHuffNode** dirNode;
	sjme_jboolean one;
	
	if (state == NULL || outTree == NULL)
		return SJME_ERROR_NONE;
	
	/* Make sure the correct inputs are used for adding to a tree. */
	maskBitCount = sjme_util_bitCountU(symMask);
	if ((sym & (~symMask)) != 0 ||
		maskBitCount == 0 ||
		maskBitCount != (32 - sjme_util_numLeadingZeroesU(symMask)) ||
		(symMask & 1) == 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Debug. */
	sjme_message("treeInsert(%d, %d, %x)",
		value, sym, symMask);
	
	/* If there is no root node, create it. */
	if (outTree->root == NULL)
	{
		/* Grab next node. */
		if (sjme_error_is(error = sjme_stream_inflateBuildTreeInsertNext(
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
			if (sjme_error_is(error = sjme_stream_inflateBuildTreeInsertNext(
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
	atNode->data.leaf.code = value;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBuildTree(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrInNotNull sjme_stream_inflateHuffParam* param,
	sjme_attrInNotNull sjme_stream_inflateHuffTree* outTree,
	sjme_attrInNotNull sjme_stream_inflateHuffTreeStorage* inStorage)
{
	sjme_errorCode error;
	sjme_jint i, code, len;
	sjme_juint blCount[SJME_INFLATE_CODE_LEN_MAX_BITS + 1];
	sjme_juint nextCode[SJME_INFLATE_CODE_LEN_MAX_BITS + 1];
	
	if (state == NULL || param == NULL || outTree == NULL || inStorage == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initialize working space. */
	memset(blCount, 0, sizeof(blCount));
	memset(nextCode, 0, sizeof(nextCode));
	
	/* Wipe the target tree. */
	memset(outTree, 0, sizeof(*outTree));
	
	/* Determine the bit-length for the input counts. */
	for (i = 0; i < param->count; i++)
		blCount[param->lengths[i] & SJME_INFLATE_CODE_LEN_MAX_BITS] += 1;
	blCount[0] = 0;
	
	/* Find the numerical value of the smallest code for each code length. */
	for (i = 1, code = 0; i <= SJME_INFLATE_CODE_LEN_MAX_BITS; i++)
	{
		code = (code + blCount[i - 1]) << 1;
		nextCode[i] = code;
	}
	
	/* Assign values to codes and build the huffman tree. */
	for (i = 0; i < param->count; i++)
	{
		sjme_message("param[%d] = %d", i, param->lengths[i]);
		
		len = param->lengths[i];
		if (len != 0)
			if (sjme_error_is(error = sjme_stream_inflateBuildTreeInsert(
				state, outTree, inStorage, i,
				(nextCode[len])++,
				(1 << len) - 1)))
				return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateReadCodeDynamic(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrOutNotNull sjme_juint* outCode)
{
	if (state == NULL || outCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inflateReadDistDynamic(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	if (state == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inflateReadCodeFixed(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrOutNotNull sjme_juint* outCode)
{
	sjme_errorCode error;
	sjme_stream_inflateBuffer* inBuffer;
	sjme_juint hiSeven, bitsNeeded, litBase, litSub, raw;
	
	if (state == NULL || outCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We at least need 7 bits for the minimum code length. */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_stream_inflateBitNeed(inBuffer,
		7)))
		return sjme_error_default(error);
	
	/* Read in upper 7 bits first, as a peek. */
	hiSeven = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
		SJME_INFLATE_MSB, SJME_INFLATE_PEEK, 7,
		&hiSeven)) || hiSeven == INT32_MAX)
		return sjme_error_default(error);
	
	/* Determine the actual number of bits we need. */
	/* 0b0000000 - 0b0010111 */
	if (hiSeven >= 0 && hiSeven <= 23)
	{
		bitsNeeded = 7;
		litBase = 256;
		litSub = 0;
	}
		
	/* 0b0011000[0] - 0b1011111[1] */
	else if (hiSeven >= 24 && hiSeven <= 95)
	{
		bitsNeeded = 8;
		litBase = 0;
		litSub = 48;
	}
	
	/* 0b1100000[0] - 0b1100011[1] */
	else if (hiSeven >= 96 && hiSeven <= 99)
	{
		bitsNeeded = 8;
		litBase = 280;
		litSub = 192;
	}
	
	/* 0b1100100[00] - 0b1111111[11] */
	else
	{
		bitsNeeded = 9;
		litBase = 144;
		litSub = 400;
	}
	
	/* Now that we know what we need, make sure we have it. */
	if (sjme_error_is(error = sjme_stream_inflateBitNeed(inBuffer,
		bitsNeeded)))
		return sjme_error_default(error);
	
	/* Pop everything off now, so we can recover the code. */
	raw = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inflateBitIn(inBuffer,
		SJME_INFLATE_MSB, SJME_INFLATE_POP,
		bitsNeeded,
		&raw)) || raw == INT32_MAX)
		return sjme_error_default(error);
	
	/* Recover the code. */
	*outCode = litBase + (raw - litSub);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateReadDistFixed(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	if (state == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just a basic bit read. */
	return sjme_stream_inflateBitIn(&state->input,
		SJME_INFLATE_MSB, SJME_INFLATE_POP,
		5, outDist);
}

static sjme_errorCode sjme_stream_inflateProcessDistance(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrInNotNull sjme_stream_inflateBuffer* inBuffer,
	sjme_attrInRange(257, 285) sjme_juint origCode,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	sjme_errorCode error;
	sjme_juint base, result, i, readIn;
	
	if (state == NULL || inBuffer == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in distance code. */
	base = INT32_MAX;
	if (sjme_error_is(error = state->readDist(
		state, &base)) || base == INT32_MAX)
		return sjme_error_default(error);
	
	/* Must be too high of a code! */
	if (base > 29)
		return SJME_ERROR_INFLATE_INVALID_CODE;
	
	/* Calculate the required distance to use */
	result = 1;
	for (i = 0; i < base; i++)
	{
		/* Similar to length but in groups of two. */
		if (i >= 2)
			result += 1;
		else
			result += (1 << ((((i / 2)) - 1)));
	}
	
	/* Also any extra bits needed as part of the distance. */
	if (base >= 4)
	{
		/* Similarly the same as length, just smaller parts. */
		i = ((base / 2)) - 1;
		
		/* Read in given bits. */
		readIn = INT32_MAX;
		if (sjme_error_is(error = sjme_stream_inflateBitIn(
			inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			i, &readIn)) ||
			readIn == INT32_MAX)
			return sjme_error_default(error);
		
		/* Add in extra value. */
		result += readIn;
	}
	
	/* Give the result. */
	*outDist = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateProcessLength(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrInNotNull sjme_stream_inflateBuffer* inBuffer,
	sjme_attrInRange(257, 285) sjme_juint code,
	sjme_attrOutNotNull sjme_juint* outLength)
{
	sjme_errorCode error;
	sjme_juint base, result, i, readIn;
	
	if (state == NULL || inBuffer == NULL || outLength == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (code < 257 || code > 285)
		return SJME_ERROR_INFLATE_INVALID_CODE;
	
	/* Maximum distance possible? */
	if (code == 285)
	{
		*outLength = 258;
		return SJME_ERROR_NONE;
	}
	
	/* Get the base distance code. */
	base = code - 257;
	
	/* Calculate the required length to use */
	result = 3;
	for (i = 0; i < base; i++)
	{
		/* Determine how many groups of 4 the code is long. Since zero */
		/* appears as items then subtract 1 to make it longer. However */
		/* after the first 8 it goes up in a standard pattern. */
		if (i < 8)
			result += 1;
		else
			result += (1 << ((((i / 4)) - 1)));
	}
	
	/* Also any extra bits needed as part of the length. */
	if (base >= 8)
	{
		/* Calculate needed amount. Same as the length, it goes up in */
		/* a specific pattern as well except without single increments. */
		i = ((base / 4)) - 1;
		
		/* Read in given bits. */
		readIn = INT32_MAX;
		if (sjme_error_is(error = sjme_stream_inflateBitIn(
			inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			i, &readIn)) ||
			readIn == INT32_MAX)
			return sjme_error_default(error);
		
		/* Add in extra value. */
		result += readIn;
	}
	
	/* Give the result. */
	*outLength = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateProcessWindow(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrInNotNull sjme_stream_inflateBuffer* outBuffer,
	sjme_attrInNotNull sjme_stream_inflateWindow* window,
	sjme_attrInPositive sjme_juint windowDist,
	sjme_attrInPositive sjme_juint windowLen)
{
	sjme_errorCode error;
	sjme_juint maxLen, i, w, readBase;
	sjme_jubyte* chunk;
	
	if (state == NULL || outBuffer == NULL || window == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The length chunk can never exceed the distance, however it does */
	/* wrap around accordingly. */
	maxLen = (windowLen > windowDist ? windowDist : windowLen);
	
	sjme_message("Dist %d > %d", windowDist, window->length);
	
	/* Cannot read more than what there is. */
	if (windowDist > window->length)
		return SJME_ERROR_INFLATE_DISTANCE_OUT_OF_RANGE;
	
	/* Setup buffer for the sliding window chunk. */
	chunk = sjme_alloca(sizeof(*chunk) * maxLen);
	if (chunk == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(chunk, 0, sizeof(*chunk) * maxLen);
	
	/* Can read in one full slice? */
	readBase = (window->end - windowDist) & SJME_INFLATE_WINDOW_MASK;
	if (readBase < window->end)
		memmove(chunk, &window->window[readBase], maxLen);
	
	/* Need to copy in two slices. */
	else
	{
		i = SJME_INFLATE_WINDOW_SIZE - readBase;
		memmove(&chunk[0], &window->window[readBase], i);
		memmove(&chunk[i], &window->window[0], maxLen - i);
	}
	
	/* Debug. */
	sjme_message("Dist chunk: %d", maxLen);
	sjme_message_hexDump(chunk, maxLen);
	
	/* Write output. */
	for (i = 0, w = 0; i < windowLen; i++)
	{
		/* Write value to the output. */
		if (sjme_error_is(error = sjme_stream_inflateBitOut(
			outBuffer, SJME_INFLATE_LSB, window,
			8, chunk[w] & 0xFF)))
			return sjme_error_default(error);
		
		/* Move window up, handle wrap around. */
		if ((++w) >= maxLen)
			w = 0;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateProcessCodes(
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_errorCode error;
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateBuffer* outBuffer;
	sjme_stream_inflateWindow* window;
	sjme_juint code, windowReadLen, windowReadDist;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* This must be set!! */
	if (state->readCode == NULL ||
		state->readDist == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Read as much as possible until we hit saturation. */
	inBuffer = &state->input;
	outBuffer = &state->output;
	window = &state->window;
	while (outBuffer->ready < SJME_INFLATE_IO_BUFFER_SATURATED)
	{
		/* Read in code. */
		code = INT32_MAX;
		if (sjme_error_is(error = state->readCode(state, &code)) ||
			code == INT32_MAX)
			return sjme_error_default(error);
		
		/* Stop decoding! */
		if (code == 256)
		{
			/* Reset back to initial step. */
			state->step = SJME_INFLATE_STEP_CHECK_BTYPE;
			state->readCode = NULL;
			
			/* Success! */
			return SJME_ERROR_NONE;
		}
		
		/* Literal byte value. */
		else if (code >= 0 && code <= 255)
		{
			if (sjme_error_is(error = sjme_stream_inflateBitOut(
				outBuffer,
				SJME_INFLATE_LSB, window,
				8, code)))
				return sjme_error_default(error);
		}
		
		/* Window. */
		else if (code >= 257 && code <= 285)
		{
			/* Read in window length. */
			windowReadLen = INT32_MAX;
			if (sjme_error_is(error = sjme_stream_inflateProcessLength(
				state, inBuffer, code, &windowReadLen)) ||
				windowReadLen == INT32_MAX)
				return sjme_error_default(error);
			
			/* Read in distance. */
			windowReadDist = INT32_MAX;
			if (sjme_error_is(error = sjme_stream_inflateProcessDistance(
				state, inBuffer, code, &windowReadDist)) ||
				windowReadDist == INT32_MAX)
				return sjme_error_default(error);
			
			/* Copy from the input window. */
			if (sjme_error_is(error = sjme_stream_inflateProcessWindow(
				state, outBuffer, window,
				windowReadDist, windowReadLen)))
				return sjme_error_default(error);
		}
		
		/* Invalid. */
		else
			return SJME_ERROR_INFLATE_INVALID_CODE;
			
		/* Debug. */
		sjme_message("Code: %d 0x%x", code, code);
		sjme_message_hexDump(&window->window[0],
			window->length);
	}
	
	/* If we over-saturated, just stop and give all the data. */
	if (outBuffer->ready >= SJME_INFLATE_IO_BUFFER_SATURATED)
		return SJME_ERROR_BUFFER_SATURATED;
	return SJME_ERROR_NONE;
}

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
	
	/* We need 3 bits for each length. */
	inBuffer = &state->input;
	init = &state->huffInit;
	if (sjme_error_is(error = sjme_stream_inflateBitNeed(inBuffer,
		init->codeLen * 3)))
		return sjme_error_default(error);
	
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
	sjme_attrInNotNull sjme_stream_inflateHuffTree* outTree)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateHuffTree* codeLenTree;
	sjme_errorCode error;
	sjme_juint index, v;
	sjme_juint* lengths;
	sjme_stream_inflateHuffParam param;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* This cannot be empty. */
	if (count <= 0)
		return SJME_ERROR_INFLATE_INVALID_TREE_LENGTH;
	
	/* Allocate stack memory for the lengths we need to process. */
	lengths = sjme_alloca(sizeof(*lengths) * count);
	if (lengths == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(lengths, 0, sizeof(*lengths) * count);
	
	/* Read in all code values. */
	inBuffer = &state->input;
	codeLenTree = &state->codeLenTree;
	for (index = 0; index < count;)
		if (sjme_error_is(error = sjme_stream_inflateBitInCodeLen(
			inBuffer, codeLenTree, &index, lengths, count)))
		{
			if (error == SJME_ERROR_TOO_SHORT)
				return SJME_ERROR_ILLEGAL_STATE;
			return sjme_error_default(error);
		}
	
	/* Build tree from this. */
	memset(&param, 0, sizeof(param));
	param.lengths = lengths;
	param.count = count;
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
				&state->literalTree)))
				return sjme_error_default(error);
			
			/* Read in distance codes next. */
			state->step = SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_DISTANCE;
			return SJME_ERROR_NONE;
		
			/* Load in dynamic huffman table: Distance tree. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD_DISTANCE:
			if (sjme_error_is(error = sjme_stream_inflateDecodeDynLoadLitDist(
				state,
				state->huffInit.distLen,
				&state->distanceTree)))
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

static sjme_errorCode sjme_stream_inputInflateClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	sjme_errorCode error;
	sjme_stream_input uncompressed;
	
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do nothing if already closed. */
	uncompressed = inImplState->handle;
	if (uncompressed == NULL)
		return SJME_ERROR_NONE;
	
	/* Close it. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(uncompressed))))
		return sjme_error_default(error);
	
	/* Clear it since we cannot use it anymore. */
	inImplState->handle = NULL;
	
	/* Free the decompression state. */
	if (inImplState->handleTwo != NULL)
	{
		/* Free it. */
		if (sjme_error_is(error = sjme_alloc_free(
			inImplState->handleTwo)))
			return sjme_error_default(error);
		
		/* Clear. */
		inImplState->handleTwo = NULL;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputInflateInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_inflateInit* init;
	
	init = data;
	if (stream == NULL || inImplState == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set data. */
	inImplState->handle = init->handle;
	inImplState->handleTwo = init->handleTwo;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputInflateFlushIn(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_errorCode error;
	sjme_stream_inflateBuffer* inBuffer;
	sjme_pointer bufOpPos;
	sjme_jint bufOpLen;
	sjme_jint remainder, sourceRead;
	
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We just use this buffer for input. */
	inBuffer = &state->input;
	
	/* Determine the read/write positions and how much we can chunk at */
	/* the same time. */
	remainder = -1;
	bufOpPos = NULL;
	bufOpLen = -1;
	if (sjme_error_is(error = sjme_stream_inflateBufferArea(
		inBuffer,
		&remainder,
		&bufOpPos, &bufOpLen)) ||
		remainder < 0 || bufOpPos == NULL || bufOpLen < 0)
	{
		/* No room for anything, just skip. */
		if (error == SJME_ERROR_TOO_SHORT)
			return SJME_ERROR_BUFFER_FULL;
		
		return sjme_error_default(error);
	}
	
	/* Read in data. */
	sourceRead = -2;
	if (sjme_error_is(error = sjme_stream_inputRead(source,
		&sourceRead,
		bufOpPos,
		bufOpLen)) || sourceRead < -1)
		return sjme_error_default(error);
	
	/* If EOF was hit, indicate as such. */
	if (sourceRead == -1)
		inBuffer->hitEof = SJME_JNI_TRUE;
	
	/* Otherwise, move the write head up and up the ready count. */
	else if (sourceRead > 0)
	{
		/* Count source data. */
		if (sjme_error_is(error = sjme_stream_inflateBufferGive(
			inBuffer,
			sourceRead)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputInflateFlushOut(
	sjme_attrInNotNull sjme_stream_inflateState* state,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	if (state == NULL || readCount == NULL || dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputInflateRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	sjme_errorCode error;
	sjme_stream_input source;
	sjme_stream_inflateState* state;
	sjme_jint remainder, lastRemainder;
	
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Fail if closed. */
	source = inImplState->handle;
	state = inImplState->handleTwo;
	if (source == NULL || state == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Is there data to be written to the output? */
	if (state->output.ready > 0)
		return sjme_stream_inputInflateFlushOut(state,
			readCount, dest, length);
	
	/* If there is nothing ready to output and the output hit EOF, then */
	/* there will never be data ready. */
	if (state->output.ready <= 0 && state->output.hitEof)
	{
		*readCount = -1;
		return SJME_ERROR_NONE;
	}
	
	/* The zip data is corrupted or invalid. */
	if (state->invalidInput)
		return SJME_ERROR_IO_EXCEPTION;
	
	/* Try to decompress as much data as possible into the output buffer. */
	lastRemainder = -1;
	while (!state->output.hitEof)
	{
		/* How much room is left in the output? */
		/* We can go over the saturation limit, however we do not want */
		/* to fill past it, so we do not hit the end of the buffer. */
		remainder = SJME_INFLATE_IO_BUFFER_SATURATED - state->output.ready;
		
		/* If this did not change, we probably need more input or the */
		/* output buffer does not have enough space. */
		if (remainder == lastRemainder)
			break;
		
		/* Fill the input buffer as much as possible before we decompress */
		/* as it is more efficient to operate in larger chunks. */
		/* Naturally we stop when there is no input anyway. */
		while (!state->input.hitEof)
		{
			/* Read in. */
			if (sjme_error_is(error = sjme_stream_inputInflateFlushIn(
				source, state)))
			{
				/* If the input buffer is full, it is not an error! */
				if (error == SJME_ERROR_BUFFER_FULL)
					break;
				
				return sjme_error_default(error);
			}
		}
		
		/* Used to determine if we should run the loop again, and if we */
		/* get stuck in a zero-read/write loop. */
		lastRemainder = remainder;
		
		/* Perform inflation. */
		if (sjme_error_is(error = sjme_stream_inflateDecode(
			state)))
		{
			/* Do not fail if there is not enough input data, just stop */
			/* trying to decompress. */
			if (error == SJME_ERROR_TOO_SHORT ||
				error == SJME_ERROR_BUFFER_SATURATED)
				break;
			return sjme_error_default(error);
		}
	}
	
	/* Try flushing to the output again? */
	if (state->output.ready > 0)
		return sjme_stream_inputInflateFlushOut(
			state, readCount, dest, length);
	
	/* If all ends hit EOF, then we are in the EOF state. */
	if (state->input.hitEof && state->output.hitEof)
		*readCount = -1;
	else
		*readCount = 0;
	return SJME_ERROR_NONE;
}

/** Input deflate functions. */
static const sjme_stream_inputFunctions sjme_stream_inputInflateFunctions =
{
	.close = sjme_stream_inputInflateClose,
	.init = sjme_stream_inputInflateInit,
	.read = sjme_stream_inputInflateRead,
};

sjme_errorCode sjme_stream_inputOpenInflate(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_stream_input inCompressed)
{
	sjme_errorCode error;
	sjme_stream_input result;
	sjme_stream_inflateInit init;
	
	if (inPool == NULL || outStream == NULL || inCompressed == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set initialization data. */
	memset(&init, 0, sizeof(init));
	init.handle = inCompressed;
	
	/* Setup decompression state data. */
	if (sjme_error_is(error = sjme_alloc(inPool,
		sizeof(sjme_stream_inflateState),
		(sjme_pointer*)&init.handleTwo)) ||
		init.handleTwo == NULL)
		goto fail_allocState;
	
	/* Setup sub-stream. */
	result = NULL;
	if (sjme_error_is(error = sjme_stream_inputOpen(inPool,
		&result, &sjme_stream_inputInflateFunctions,
		&init, NULL)) || result == NULL)
		goto fail_open;
	
	/* Valid, so count up the compressed seekable. */
	if (sjme_error_is(error = sjme_alloc_weakRef(inCompressed, NULL)))
		goto fail_countStream;
	
	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_countStream:
fail_open:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	
fail_allocState:
	if (init.handleTwo != NULL)
		sjme_alloc_free(init.handleTwo);
	
	return sjme_error_default(error);
}
