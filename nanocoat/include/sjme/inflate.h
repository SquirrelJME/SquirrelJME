/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Generic inflation interface.
 * 
 * @since 2024/08/25
 */

#ifndef SQUIRRELJME_INFLATE_H
#define SQUIRRELJME_INFLATE_H

#include "sjme/stdTypes.h"
#include "sjme/error.h"
#include "stream.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_INFLATE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

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
#define SJME_INFLATE_WINDOW_MASK 32768

/** Maximum huffman tree size. */
#define SJME_INFLATE_HUFF_STORAGE_SIZE 16383

/** The limit for code lengths. */
#define SJME_INFLATE_CODE_LEN_LIMIT 19

/** The maximum number of bits in the code length tree. */
#define SJME_INFLATE_CODE_LEN_MAX_BITS 15

/** The number of codes. */
#define SJME_INFLATE_NUM_CODES 288

/** The maximum number of literal lengths. */
#define SJME_INFLATE_NUM_LIT_LENS 287

/** The maximum number of distance lengths. */
#define SJME_INFLATE_NUM_DIST_LENS 33


/**
 * Zip bit reading mode.
 * 
 * @since 2024/08/18
 */
typedef enum sjme_inflate_order
{
	/** Least significant bit. */
	SJME_INFLATE_LSB,
	
	/** Most significant bit. */
	SJME_INFLATE_MSB,
} sjme_inflate_order;

/**
 * Used to either peek or pop from the bit stream.
 * 
 * @since 2024/08/20
 */
typedef enum sjme_inflate_peek
{
	/** Pop value. */
	SJME_INFLATE_POP,
	
	/** Peek value. */
	SJME_INFLATE_PEEK,
} sjme_inflate_peek;

/**
 * Which type of node is this in the tree?
 * 
 * @since 2024/08/22
 */
typedef enum sjme_inflate_huffNodeType
{
	/** Unknown. */
	SJME_INFLATE_UNKNOWN,
	
	/** Node. */
	SJME_INFLATE_NODE,
	
	/** Leaf. */
	SJME_INFLATE_LEAF,
} sjme_inflate_huffNodeType;

/**
 * The inflation step.
 * 
 * @since 2024/08/17
 */
typedef enum sjme_inflate_step
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
} sjme_inflate_step;

/**
 * Inflation buffer state.
 * 
 * @since 2024/08/17
 */
sjme_attrDeprecated typedef struct sjme_attrDeprecated sjme_inflate_buffer
{
	/** The amount of data that is ready for processing. */
	sjme_attrDeprecated sjme_jint ready;
	
	/** The current read head. */
	sjme_attrDeprecated sjme_jint readHead;
	
	/** The current write head. */
	sjme_attrDeprecated sjme_jint writeHead;
	
	/** The buffer storage. */
	sjme_attrDeprecated sjme_jubyte buffer[SJME_INFLATE_IO_BUFFER_SIZE];
	
	/** Was EOF hit in this buffer? */
	sjme_jboolean hitEof;
	
	/** The current bit buffer. */
	sjme_juint bitBuffer;
	
	/** The amount of bits in the buffer. */
	sjme_juint bitCount;
} sjme_inflate_buffer;

/**
 * The window for output inflated data.
 * 
 * @since 2024/08/18
 */
sjme_attrDeprecated typedef sjme_attrDeprecated struct sjme_inflate_window
{
	/** The number of bytes in the window. */
	sjme_attrDeprecated sjme_juint length;
	
	/** The end position of the window. */
	sjme_attrDeprecated sjme_juint end;
	
	/** The window buffer. */
	sjme_attrDeprecated sjme_jubyte window[SJME_INFLATE_WINDOW_SIZE];
} sjme_inflate_window;

/**
 * Initial code length huffman tree building values.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_inflate_huffInit
{
	/** Literal length. */
	sjme_juint litLen;
	
	/** Distance length. */
	sjme_juint distLen;
	
	/** The code length count. */
	sjme_juint codeLen;
	
	/** The raw code length bit values. */
	sjme_juint rawCodeLens[SJME_INFLATE_CODE_LEN_LIMIT];
} sjme_inflate_huffInit;

/**
 * Huffman tree building parameters, used as input.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_inflate_huffParam
{
	/** Code lengths to input. */
	sjme_juint* lengths;
	
	/** The number of code lengths. */
	sjme_juint count;
} sjme_inflate_huffParam;

/**
 * Huffman tree node.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_align64 sjme_inflate_huffNode
	sjme_inflate_huffNode;

struct sjme_align64 sjme_inflate_huffNode
{
	/** Node data. */
	union
	{
		/** Data if a leaf. */
		struct
		{
			/** Zero branch. */
			sjme_inflate_huffNode* zero;
			
			/** One branch. */
			sjme_inflate_huffNode* one;
		} node;
		
		/** Data if a node. */
		struct
		{
			/** The code stored here. */
			sjme_juint code;
		} leaf;
	} data;
	
	/** Which type of node is this? */
	sjme_inflate_huffNodeType type;
};

/**
 * Huffman tree parameters.
 * 
 * @since 2024/08/20
 */
typedef struct sjme_inflate_huffTree
{
	/** The root node. */
	sjme_inflate_huffNode* root;
} sjme_inflate_huffTree;

/**
 * Storage for huffman tree nodes.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_inflate_huffTreeStorage
{
	/** Storage for the huffman tree nodes. */
	sjme_align64 sjme_jubyte storage[SJME_INFLATE_HUFF_STORAGE_SIZE];
	
	/** Next free node. */
	sjme_inflate_huffNode* next;
	
	/** Final end of tree. */
	sjme_inflate_huffNode* finalEnd;
} sjme_inflate_huffTreeStorage;

/**
 * Inflation state.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_inflate_state sjme_inflate_state;

/**
 * Reads a code from the input.
 * 
 * @param state The state to read from.
 * @param outCode The resultant code which was read.
 * @return On any error, if any.
 * @since 2024/08/25
 */
typedef sjme_errorCode (*sjme_inflate_readCodeFunc)(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outCode);

/**
 * Reads a distance code from the input.
 * 
 * @param state The state to read from.
 * @param outDist The resultant distance code which was read.
 * @return On any error, if any.
 * @since 2024/08/25
 */
typedef sjme_errorCode (*sjme_inflate_readDistFunc)(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outDist);

/**
 * Inflation state.
 * 
 * @since 2024/08/17
 */
struct sjme_inflate_state
{
	/** The current step in inflation. */
	sjme_inflate_step step;
	
	/** Was the final block hit? */
	sjme_jboolean finalHit;
	
	/** Is the input data corrupted? */
	sjme_jboolean invalidInput;
	
	/** The output window. */
	sjme_inflate_window window;
	
	/** The amount of literal bytes left to read. */
	sjme_jint literalLeft;
	
	/** Initialization data for the initial huffman tree. */
	sjme_inflate_huffInit huffInit;
	
	/** The function for reading codes. */
	sjme_inflate_readCodeFunc readCode;
	
	/** The function for reading distances. */
	sjme_inflate_readDistFunc readDist;
	
	/** Huffman tree node storage. */
	sjme_inflate_huffTreeStorage huffStorage;
	
	/** Code length tree. */
	sjme_inflate_huffTree codeLenTree;
	
	/** Distance tree. */
	sjme_inflate_huffTree distanceTree;
	
	/** Literal tree, not literally. */
	sjme_inflate_huffTree literalTree;
	
	/** The input buffer. */
	sjme_inflate_buffer input;
	
	/** The output buffer. */
	sjme_inflate_buffer output;
};

/**
 * Inflate stream initialization.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_inflate_init
{
	/** The compressed data stream. */
	sjme_stream_input handle;
	
	/** Decompression state. */
	sjme_inflate_state* handleTwo;
} sjme_inflate_init;

sjme_errorCode sjme_inflate_bitIn(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrInValue sjme_inflate_order order,
	sjme_attrInValue sjme_inflate_peek popPeek,
	sjme_attrInRange(1, 32) sjme_juint bitCount,
	sjme_attrOutNotNull sjme_juint* readValue);

sjme_errorCode sjme_inflate_bitInCodeLen(
	sjme_attrInNotNull sjme_inflate_buffer* inBuffer,
	sjme_attrInNotNull sjme_inflate_huffTree* codeLenTree,
	sjme_attrInOutNotNull sjme_juint* index,
	sjme_attrOutNotNull sjme_juint* outLengths,
	sjme_attrInPositive sjme_juint count);

sjme_errorCode sjme_inflate_bitInTree(
	sjme_attrInNotNull sjme_inflate_buffer* inBuffer,
	sjme_attrInNotNull sjme_inflate_huffTree* fromTree,
	sjme_attrOutNotNull sjme_juint* outValue);

sjme_errorCode sjme_inflate_bitNeed(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrInPositiveNonZero sjme_jint bitCount);

sjme_errorCode sjme_inflate_bitOut(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrInValue sjme_inflate_order order,
	sjme_attrOutNotNull sjme_inflate_window* window,
	sjme_attrInRange(1, 32) sjme_juint bitCount,
	sjme_attrOutNotNull sjme_juint writeValue);

sjme_attrDeprecated sjme_errorCode sjme_inflate_bufferArea(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrOutNotNull sjme_jint* outRemainder,
	sjme_attrOutNotNull sjme_pointer* outBufOpPos,
	sjme_attrOutNotNull sjme_jint* outBufOpLen);

sjme_attrDeprecated sjme_errorCode sjme_inflate_bufferConsume(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrInPositiveNonZero sjme_jint count);

sjme_attrDeprecated sjme_errorCode sjme_inflate_bufferGive(
	sjme_attrInNotNull sjme_inflate_buffer* buffer,
	sjme_attrInPositiveNonZero sjme_jint count);

sjme_errorCode sjme_inflate_decode(
	sjme_attrInNotNull sjme_inflate_state* state);

sjme_errorCode sjme_inflate_decodeBType(
	sjme_attrInNotNull sjme_inflate_state* state);

sjme_errorCode sjme_inflate_decodeDynLoad(
	sjme_attrInNotNull sjme_inflate_state* state);

sjme_errorCode sjme_inflate_decodeDynLoadCodeLen(
	sjme_attrInNotNull sjme_inflate_state* state);

sjme_errorCode sjme_inflate_decodeDynLoadLitDist(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInPositive sjme_juint count,
	sjme_attrInNotNull sjme_inflate_huffTree* outTree,
	sjme_attrInPositiveNonZero sjme_juint maxCount);

sjme_errorCode sjme_inflate_decodeLiteralData(
	sjme_attrInNotNull sjme_inflate_state* state);

sjme_errorCode sjme_inflate_decodeLiteralHeader(
	sjme_attrInNotNull sjme_inflate_state* state);

sjme_errorCode sjme_inflate_buildTree(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_huffParam* param,
	sjme_attrInNotNull sjme_inflate_huffTree* outTree,
	sjme_attrInNotNull sjme_inflate_huffTreeStorage* inStorage);

sjme_errorCode sjme_inflate_buildTreeInsert(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_huffTree* outTree,
	sjme_attrInNotNull sjme_inflate_huffTreeStorage* inStorage,
	sjme_attrInPositive sjme_juint code,
	sjme_attrInValue sjme_juint sym,
	sjme_attrInPositiveNonZero sjme_juint symMask);

sjme_errorCode sjme_inflate_buildTreeInsertNext(
	sjme_attrInNotNull sjme_inflate_huffTree* outTree,
	sjme_attrInNotNull sjme_inflate_huffTreeStorage* inStorage,
	sjme_attrOutNotNull sjme_inflate_huffNode** outNode);

sjme_errorCode sjme_inflate_processCodes(
	sjme_attrInNotNull sjme_inflate_state* state);

sjme_errorCode sjme_inflate_processDistance(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_buffer* inBuffer,
	sjme_attrInRange(257, 285) sjme_juint origCode,
	sjme_attrOutNotNull sjme_juint* outDist);

sjme_errorCode sjme_inflate_processLength(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_buffer* inBuffer,
	sjme_attrInRange(257, 285) sjme_juint code,
	sjme_attrOutNotNull sjme_juint* outLength);

sjme_errorCode sjme_inflate_processWindow(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_buffer* outBuffer,
	sjme_attrInNotNull sjme_inflate_window* window,
	sjme_attrInPositive sjme_juint windowDist,
	sjme_attrInPositive sjme_juint windowLen);

sjme_errorCode sjme_inflate_readCodeDynamic(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outCode);

sjme_errorCode sjme_inflate_readDistDynamic(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outDist);

sjme_errorCode sjme_inflate_readCodeFixed(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outCode);

sjme_errorCode sjme_inflate_readDistFixed(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outDist);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_INFLATE_H
}
		#undef SJME_CXX_SQUIRRELJME_INFLATE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_INFLATE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_INFLATE_H */
