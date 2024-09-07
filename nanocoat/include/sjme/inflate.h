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
#include "sjme/stream.h"
#include "sjme/bitStream.h"
#include "sjme/circleBuffer.h"
#include "traverse.h"

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
#define SJME_INFLATE_IO_BUFFER_MASK (SJME_INFLATE_IO_BUFFER_SIZE - 1)

/** When the output buffer is considered saturated. */
#define SJME_INFLATE_IO_BUFFER_SATURATED (SJME_INFLATE_IO_BUFFER_SIZE - 512)

/** The maximum window size. */
#define SJME_INFLATE_WINDOW_MAX_SIZE 32768

/** The window size. */
#define SJME_INFLATE_WINDOW_SIZE SJME_INFLATE_WINDOW_MAX_SIZE

/** The window mask. */
#define SJME_INFLATE_WINDOW_MASK (SJME_INFLATE_WINDOW_SIZE - 1)

/** The limit for code lengths. */
#define SJME_INFLATE_NUM_CODE_LEN 19

/** The maximum number of bits in the code length tree. */
#define SJME_INFLATE_CODE_LEN_MAX_BITS 15

/** The number of codes. */
#define SJME_INFLATE_NUM_CODES 288

/** The maximum number of literal lengths. */
#define SJME_INFLATE_NUM_LIT_LENS 287

/** The maximum number of distance lengths. */
#define SJME_INFLATE_NUM_DIST_LENS 33

/**
 * The inflation step.
 * 
 * @since 2024/08/17
 */
typedef enum sjme_inflate_step
{
	/** Parse BTYPE and determine how to continue. */
	SJME_INFLATE_STEP_CHECK_BTYPE,
	
	/** Literal uncompressed data setup. */
	SJME_INFLATE_STEP_LITERAL_SETUP,
	
	/** Literal uncompressed data. */
	SJME_INFLATE_STEP_LITERAL_DATA,
	
	/** Setup for dynamic huffman data. */
	SJME_INFLATE_STEP_DYNAMIC_SETUP,
	
	/** Setup for fixed huffman data. */
	SJME_INFLATE_STEP_FIXED_SETUP,
	
	/** Inflate from a given huffman tree. */
	SJME_INFLATE_STEP_INFLATE_FROM_TREE,
	
	/** Finished, nothing is left. */
	SJME_INFLATE_STEP_FINISHED,
} sjme_inflate_step;

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
	sjme_juint rawCodeLens[SJME_INFLATE_NUM_CODE_LEN];
} sjme_inflate_huffInit;

/**
 * Inflation state.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_inflate sjme_inflate;

/**
 * Reads a code from the input.
 * 
 * @param state The state to read from.
 * @param outCode The resultant code which was read.
 * @return On any error, if any.
 * @since 2024/08/25
 */
typedef sjme_errorCode (*sjme_inflate_readCodeFunc)(
	sjme_attrInNotNull sjme_inflate* inState,
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
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_juint* outDist);

/**
 * Inflation state.
 * 
 * @since 2024/08/17
 */
struct sjme_inflate
{
	/** The pool this allocates within. */
	sjme_alloc_pool* inPool;
	
	/** The inflation data source. */
	sjme_stream_input source;
	
	/** The input bit stream. */
	sjme_bitStream_input input;
	
	/** The input data buffer. */
	sjme_circleBuffer* inputBuffer;
	
	/** The input reached EOF. */
	sjme_jboolean inputEof;
	
	/** The output bit stream. */
	sjme_bitStream_output output;
	
	/** The output data buffer. */
	sjme_circleBuffer* outputBuffer;
	
	/** The output data window. */
	sjme_circleBuffer* window;
	
	/** Code length tree. */
	sjme_traverse_sjme_jint treeCodeLen;
	
	/** Literal tree. */
	sjme_traverse_sjme_jint treeLit;
	
	/* Distance tree. */
	sjme_traverse_sjme_jint treeDist;
	
	/** The current step in inflation. */
	sjme_inflate_step step;
	
	/** Has decompression failed? */
	sjme_errorCode failed;
	
	/** Was the final block hit? */
	sjme_jboolean finalHit;
	
	/** Sub-state. */
	union
	{
		/** The amount of literal data left. */
		sjme_jint literalLeft;
		
		/** Huffman based decompression. */
		struct
		{
			/** Read code. */
			sjme_inflate_readCodeFunc readCode;
			
			/** Read distance value. */
			sjme_inflate_readDistFunc readDist;
		} huffman;
	} sub;
};

/**
 * Destroys the given inflation state.
 * 
 * @param inState The state to destroy.
 * @return Any resultant error, if any.
 * @since 2024/08/30 
 */
sjme_errorCode sjme_inflate_destroy(
	sjme_attrInNotNull sjme_inflate* inState);

/**
 * Inflates data from the given inflation state and returns it.
 * 
 * @param inState The inflation state.
 * @param readCount The number of bytes inflated, will be @c -1 if the end
 * of the compressed stream is reached.
 * @param outBuf The resultant buffer to receive the inflated data.
 * @param length The maximum number of bytes to decompress.
 * @return Any resultant error, if any.
 * @since 2024/08/30
 */
sjme_errorCode sjme_inflate_inflate(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length);

/**
 * Initializes a new blank inflation state for the inflation of deflate
 * compressed data.
 * 
 * @param inPool The pool to allocate within.
 * @param outState The resultant state.
 * @param source Source compressed input data.
 * @return Any resultant error, if any.
 * @since 2024/08/30
 */
sjme_errorCode sjme_inflate_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_inflate** outState,
	sjme_attrInNotNull sjme_stream_input source);

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
