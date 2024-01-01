/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Stream support.
 * 
 * @since 2023/12/30
 */

#ifndef SQUIRRELJME_STREAM_H
#define SQUIRRELJME_STREAM_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STREAM_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents a data stream that can be read from.
 *
 * @since 2023/12/30
 */
typedef struct sjme_stream_inputCore sjme_stream_inputCore;

/**
 * Represents a data stream that can be read from.
 *
 * @since 2023/12/30
 */
typedef struct sjme_stream_inputCore* sjme_stream_input;

/**
 * Represents a data stream that can be written to.
 *
 * @since 2023/12/30
 */
typedef struct sjme_stream_outputCore* sjme_stream_output;

/**
 * Determines the number of bytes which are quickly available before blocking
 * takes effect.
 *
 * @param stream The stream to read from.
 * @param outAvail The number of bytes which are available.
 * @return On any resultant error.
 * @since 2024/01/01
 */
typedef sjme_errorCode (*sjme_stream_inputAvailableFunc)(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail);

/**
 * Closes the given input stream and frees up any resources.
 *
 * @param stream The stream to close.
 * @return On any resultant error.
 * @since 2024/01/01
 */
typedef sjme_errorCode (*sjme_stream_inputCloseFunc)(
	sjme_attrInNotNull sjme_stream_input stream);

/**
 * Reads from the given input stream and writes to the destination buffer.
 *
 * @param stream The stream to read from.
 * @param readCount The number of bytes which were read, if end of stream is
 * reached this will be @c -1 .
 * @param dest The destination buffer.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
typedef sjme_errorCode (*sjme_stream_inputReadFunc)(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint length);

/**
 * Functions for input streams.
 *
 * @since 2024/01/01
 */
typedef struct sjme_stream_inputFunctions
{
	/** Number of bytes available. */
	sjme_stream_inputAvailableFunc available;

	/** Close stream. */
	sjme_stream_inputCloseFunc close;

	/** Read from stream. */
	sjme_stream_inputReadFunc read;
} sjme_stream_inputFunctions;

struct sjme_stream_inputCore
{
	/** Functions for input. */
	const sjme_stream_inputFunctions* functions;

	/** Front end holders. */
	sjme_frontEnd frontEnd;

	/** The current number of read bytes. */
	sjme_jint totalRead;

	/** Uncommon stream specific data. */
	sjme_jlong uncommon[sjme_flexibleArrayCount];
};

/**
 * Gets the state information from the given input stream.
 *
 * @param uncommonType The uncommon type.
 * @param base The base pointer.
 * @since 2024/01/01
 */
#define SJME_INPUT_UNCOMMON(uncommonType, base) \
	SJME_UNCOMMON_MEMBER(sjme_stream_inputCore, uncommon, \
		uncommonType, (base))

/**
 * Determines the size of the input stream structure.
 *
 * @param uncommonSize The uncommon size.
 * @return The input stream structure size.
 * @since 2024/01/01
 */
#define SJME_SIZEOF_INPUT_STREAM_N(uncommonSize) \
    SJME_SIZEOF_UNCOMMON_N(sjme_stream_inputCore, uncommon, uncommonSize)

/**
 * Determines the size of the input stream structure.
 *
 * @param uncommonType The uncommon type.
 * @return The input stream structure size.
 * @since 2024/01/01
 */
#define SJME_SIZEOF_INPUT_STREAM(uncommonType) \
	SJME_SIZEOF_INPUT_STREAM_N(sizeof(uncommonType))

/**
 * Determines the number of bytes which are quickly available before blocking
 * takes effect.
 *
 * @param stream The stream to read from.
 * @param outAvail The number of bytes which are available.
 * @return On any resultant error or {@code null}.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail);

/**
 * Closes an input stream.
 *
 * @param stream The stream to close.
 * @return Any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputClose(
	sjme_attrInNotNull sjme_stream_input stream);

/**
 * Creates a stream which reads from the given block of memory.
 *
 * Memory based streams are never blocking.
 *
 * @param inPool The pool to allocate within.
 * @param outStream The resultant stream.
 * @param buffer The buffer to access.
 * @param length The length of the buffer.
 * @return On any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull const void* buffer,
	sjme_attrInPositive sjme_jint length);

/**
 * Reads from the given input stream and writes to the destination buffer.
 *
 * @param stream The stream to read from.
 * @param readCount The number of bytes which were read, if end of stream is
 * reached this will be @c -1 .
 * @param dest The destination buffer.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint length);

/**
 * Reads from the given input stream and writes to the destination buffer.
 *
 * @param stream The stream to read from.
 * @param readCount The number of bytes which were read, if end of stream is
 * reached this will be @c -1 .
 * @param dest The destination buffer.
 * @param offset The offset into the destination buffer.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputReadIter(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInPositive sjme_jint length);

/**
 * Reads a single byte from the input stream.
 *
 * @param stream The stream to read from.
 * @param result The resultant byte, @c -1 indicates end of stream while
 * every other value is always positive.
 * @return Any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputReadSingle(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* result);

/**
 * Closes an output stream.
 *
 * @param stream The stream to close.
 * @return Any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_outputClose(
	sjme_attrInNotNull sjme_stream_output stream);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STREAM_H
}
		#undef SJME_CXX_SQUIRRELJME_STREAM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STREAM_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STREAM_H */
