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
