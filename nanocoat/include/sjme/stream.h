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
 * @file stream.h
 * @since 2023/12/30
 */

#ifndef SQUIRRELJME_STREAM_H
#define SQUIRRELJME_STREAM_H

#include "sjme/stdTypes.h"
#include "sjme/error.h"
#include "sjme/alloc.h"
#include "sjme/closeable.h"
#include "sjme/seekable.h"

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
typedef struct sjme_stream_inputBase sjme_stream_inputBase;

/**
 * Represents a data stream that can be read from.
 *
 * @since 2023/12/30
 */
typedef sjme_stream_inputBase* sjme_stream_input;

/**
 * The core output stream which is written to with data.
 *
 * @since 2024/01/09
 */
typedef struct sjme_stream_outputBase sjme_stream_outputBase;

/**
 * Represents a data stream that can be written to.
 *
 * @since 2023/12/30
 */
typedef sjme_stream_outputBase* sjme_stream_output;

/**
 * Implementation state within streams.
 * 
 * @since 2024/08/11
 */
typedef struct sjme_stream_implState
{
	/** The pool this is in. */
	sjme_alloc_pool* inPool;
	
	/** Internal handle. */
	sjme_pointer handle;
	
	/** Second internal handle. */
	sjme_pointer handleTwo;
	
	/** Internal buffer. */
	sjme_pointer buffer;
	
	/** Internal offset. */
	sjme_jint offset;
	
	/** Internal limit. */
	sjme_jint limit;
	
	/** Internal index. */
	sjme_jint index;
	
	/** Internal length. */
	sjme_jint length;
	
	/** Forward close? */
	sjme_jboolean forwardClose;
} sjme_stream_implState;

/**
 * Determines the number of bytes which are quickly available before blocking
 * takes effect.
 *
 * @param stream The stream to read from.
 * @param inImplState The implementation state.
 * @param outAvail The number of bytes which are available.
 * @return On any resultant error.
 * @since 2024/01/01
 */
typedef sjme_errorCode (*sjme_stream_inputAvailableFunc)(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail);

/**
 * Closes the given input stream and frees up any resources.
 *
 * @param stream The stream to close.
 * @param inImplState The implementation state.
 * @return On any resultant error.
 * @since 2024/01/01
 */
typedef sjme_errorCode (*sjme_stream_inputCloseFunc)(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState);

/**
 * Initializes the new input stream.
 * 
 * @param stream The current stream.
 * @param inImplState The implementation state.
 * @param data Any passed in data through initialize.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_stream_inputInitFunc)(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data);

/**
 * Reads from the given input stream and writes to the destination buffer.
 *
 * @param stream The stream to read from.
 * @param inImplState The implementation state.
 * @param readCount The number of bytes which were read, if end of stream is
 * reached this will be @c -1 .
 * @param dest The destination buffer.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
typedef sjme_errorCode (*sjme_stream_inputReadFunc)(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
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
	
	/** Stream initialization. */
	sjme_stream_inputInitFunc init;

	/** Read from stream. */
	sjme_stream_inputReadFunc read;
} sjme_stream_inputFunctions;

struct sjme_stream_inputBase
{
	/** Closeable. */
	sjme_closeableBase closable;
	
	/** Implementation state. */
	sjme_stream_implState implState;
	
	/** Front end holders. */
	sjme_frontEnd frontEnd;
	
	/** Functions for input. */
	const sjme_stream_inputFunctions* functions;

	/** The current number of read bytes. */
	sjme_jint totalRead;
};

/**
 * Closes the specified output stream.
 *
 * @param stream The output stream to close.
 * @param inImplState The implementation state.
 * @return On any resultant error, if any.
 * @since 2024/01/09
 */
typedef sjme_errorCode (*sjme_stream_outputCloseFunc)(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState);

/**
 * Initializes the new output stream.
 * 
 * @param stream The current stream.
 * @param inImplState The implementation state.
 * @param data Any passed in data through initialize.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_stream_outputInitFunc)(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data);

/**
 * Writes to the given output stream.
 *
 * @param stream The stream to write to.
 * @param inImplState The implementation state.
 * @param buf The bytes to write.
 * @param length The number of bytes to write.
 * @return On any resultant error, if any.
 * @since 2024/01/09
 */
typedef sjme_errorCode (*sjme_stream_outputWriteFunc)(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_cpointer buf,
	sjme_attrInPositiveNonZero sjme_jint length);

/**
 * Functions for writing to the output.
 *
 * @since 2024/01/09
 */
typedef struct sjme_stream_outputFunctions
{
	/** Closes the specified stream. */
	sjme_stream_outputCloseFunc close;
	
	/** Stream initialization. */
	sjme_stream_outputInitFunc init;

	/** Writes to the given output stream. */
	sjme_stream_outputWriteFunc write;
} sjme_stream_outputFunctions;

struct sjme_stream_outputBase
{
	/** Closeable. */
	sjme_closeableBase closable;
	
	/** Implementation state. */
	sjme_stream_implState implState;
	
	/** Front end holders. */
	sjme_frontEnd frontEnd;
	
	/** Functions for output. */
	const sjme_stream_outputFunctions* functions;
	
	/** The current number of written bytes. */
	sjme_jint totalWritten;
};

/**
 * Determines the number of bytes which are quickly available before blocking
 * takes effect.
 *
 * @param stream The stream to read from.
 * @param outAvail The number of bytes which are available.
 * @return On any resultant error or @c NULL .
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail);

/**
 * Opens an input stream.
 * 
 * @param inPool The pool to allocate within. 
 * @param outStream The resultant stream.
 * @param inFunctions Stream implementation functions.
 * @param data Any data to pass to the initialization routine.
 * @param copyFrontEnd Any front end data to copy.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
sjme_errorCode sjme_stream_inputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull const sjme_stream_inputFunctions* inFunctions,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd);

/**
 * Opens a decompressing input stream from the given compressed input stream.
 * 
 * @param inPool The pool to allocate within.
 * @param outStream The resultant stream.
 * @param inCompressed The stream to decompress.
 * @param forwardClose If the input stream is closed, should the compressed
 * input stream also be closed?
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
sjme_errorCode sjme_stream_inputOpenDeflate(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_stream_input inCompressed,
	sjme_attrInValue sjme_jboolean forwardClose);
	
/**
 * Creates a stream which reads from the given block of memory.
 *
 * Memory based streams are never blocking.
 *
 * @param inPool The pool to allocate within.
 * @param outStream The resultant stream.
 * @param base The buffer to access.
 * @param length The length of the buffer.
 * @return On any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_cpointer base,
	sjme_attrInPositive sjme_jint length);

/**
 * Provides an input stream to read data from a seekable, note that
 * unlike @c sjme_seekable_regionLockAsInputStream there is no locking
 * involved and as such there may be a performance penalty or otherwise.
 *
 * @param seekable The seekable to access.
 * @param outStream The resultant stream.
 * @param base The base address within the seekable.
 * @param length The number of bytes to stream.
 * @param forwardClose If the input stream is closed, should the seekable
 * also be closed?
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_stream_inputOpenSeekable(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length,
	sjme_attrInValue sjme_jboolean forwardClose);

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
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
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
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
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
 * Reads a Java value from the given stream.
 *
 * @param stream The stream to read from.
 * @param typeId The type to read.
 * @param outValue The resultant value.
 * @return On any error, if any.
 * @since 2024/01/05
 */
sjme_errorCode sjme_stream_inputReadValueJ(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS)
		sjme_basicTypeId typeId,
	sjme_attrOutNotNull sjme_jvalue* outValue);

/**
 * Contains the result of the written byte array.
 *
 * @since 2024/01/09
 */
typedef struct sjme_stream_resultByteArray
{
	/** The buffer where the data is. */
	sjme_jbyte* array;

	/** The length of the array. */
	sjme_jint length;

	/**
	 * If this is set to @c SJME_JNI_TRUE , then the close handler for
	 * the byte array will free the memory contained in the buffer. To prevent
	 * it from being freed, this should return @c SJME_JNI_FALSE in which case
	 * the @c optResult value will be written to with the final buffer.
	 */
	sjme_jboolean free;

	/** The input whatever value. */
	sjme_pointer whatever;
} sjme_stream_resultByteArray;

typedef struct sjme_stream_outputData sjme_stream_outputData;

/**
 * Name of data output function type.
 * 
 * @param type The type.
 * @param noun The proper noun of the type.
 * @since 2024/06/20
 */
#define SJME_DATA_OUTPUT_NAME_FUNC(type, noun) \
	SJME_TOKEN_PASTE3_PP(sjme_stream_outputDataWrite, noun, Func)

/**
 * Simplified data output stream.
 * 
 * @param type The type.
 * @param noun The proper noun of the type.
 * @since 2024/06/20
 */
#define SJME_DATA_OUTPUT_PROTOTYPE(type, noun) \
	typedef sjme_errorCode (*SJME_DATA_OUTPUT_NAME_FUNC(type, noun)) \
		(sjme_attrInNotNull sjme_stream_outputData* out, \
		sjme_attrInValue type value)

/**
 * Quick output data type.
 * 
 * @param type The type.
 * @param noun The proper noun of the type.
 * @since 2024/06/20
 */
#define SJME_DATA_OUTPUT_TYPE(type, noun) \
	SJME_DATA_OUTPUT_NAME_FUNC(type, noun) SJME_TOKEN_PASTE_PP(w, noun)

SJME_DATA_OUTPUT_PROTOTYPE(sjme_jbyte, Byte);
SJME_DATA_OUTPUT_PROTOTYPE(sjme_jubyte, UByte);
SJME_DATA_OUTPUT_PROTOTYPE(sjme_jshort, Short);
SJME_DATA_OUTPUT_PROTOTYPE(sjme_jchar, Character);
SJME_DATA_OUTPUT_PROTOTYPE(sjme_jint, Integer);
SJME_DATA_OUTPUT_PROTOTYPE(sjme_juint, UInteger);

/**
 * Functions for simplified data output.
 * 
 * @since 2024/06/20
 */
typedef struct sjme_stream_outputDataFunctions
{
	/** Writes a @c sjme_jbyte . */
	SJME_DATA_OUTPUT_TYPE(sjme_jbyte, Byte);
	
	/** Writes a @c sjme_jubyte . */
	SJME_DATA_OUTPUT_TYPE(sjme_jubyte, UByte);
	
	/** Writes a @c sjme_jshort . */
	SJME_DATA_OUTPUT_TYPE(sjme_jshort, Short);
	
	/** Writes a @c sjme_jchar . */
	SJME_DATA_OUTPUT_TYPE(sjme_jchar, Character);
	
	/** Writes a @c sjme_jint . */
	SJME_DATA_OUTPUT_TYPE(sjme_jint, Integer);
	
	/** Writes a @c sjme_juint . */
	SJME_DATA_OUTPUT_TYPE(sjme_juint, UInteger);
} sjme_stream_outputDataFunctions;

#undef SJME_DATA_OUTPUT_NAME_FUNC
#undef SJME_DATA_OUTPUT_PROTOTYPE
#undef SJME_DATA_OUTPUT_TYPE

struct sjme_stream_outputData
{
	/** The stream to write to. */
	sjme_stream_output stream;
	
	/** Functions for data output. */
	const sjme_stream_outputDataFunctions df;
};

/**
 * This function is called back when the output byte array stream has been
 * closed, which provides the byte array for access.
 *
 * @param stream The stream that is finished and is about to be closed.
 * @param result The result of the array operation.
 * @param data Any data for the finish.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
typedef sjme_errorCode (*sjme_stream_outputByteArrayFinishFunc)(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_resultByteArray* result,
	sjme_attrInNullable sjme_pointer data);

/**
 * Opens an output stream.
 * 
 * @param inPool The pool to allocate within. 
 * @param outStream The resultant stream.
 * @param inFunctions Stream implementation functions.
 * @param data Any data to pass to the initialization routine.
 * @param copyFrontEnd Any front end data to copy.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
sjme_errorCode sjme_stream_outputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInNotNull const sjme_stream_outputFunctions* inFunctions,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd);

/**
 * Opens a dynamically resizing output byte array.
 *
 * @param inPool The pool to allocate within.
 * @param outStream The resultant output stream.
 * @param initialLimit The initial buffer limit.
 * @param finish The function to call when the stream is closed.
 * @param finishData Can be used to pass whatever is needed for the finish
 * processor.
 * @return On any error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_stream_outputOpenByteArray(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInPositive sjme_jint initialLimit,
	sjme_attrInNotNull sjme_stream_outputByteArrayFinishFunc finish,
	sjme_attrInNullable sjme_pointer finishData);

/**
 * Opens an output stream which writes to the given block of memory, note that
 * when it reaches the end of the block it will fail to write following it.
 *
 * @param inPool The pool to allocate within.
 * @param outStream The resultant output stream.
 * @param base The base memory address to write to.
 * @param length The length of the memory region.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_stream_outputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInNotNull sjme_pointer base,
	sjme_attrInPositive sjme_jint length);

/**
 * Writes to the given output stream.
 *
 * @param outStream The stream to write to.
 * @param src The source bytes.
 * @param length The number of bytes to write.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_stream_outputWrite(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrOutNotNullBuf(length) sjme_pointer src,
	sjme_attrInPositive sjme_jint length);

/**
 * Writes to the given output stream.
 *
 * @param stream The stream to write to.
 * @param src The source bytes.
 * @param offset The offset into the buffer.
 * @param length The number of bytes to write.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_stream_outputWriteIter(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrOutNotNullBuf(length) sjme_pointer src,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInPositive sjme_jint length);

/**
 * Writes a single byte to the output stream.
 *
 * @param outStream The stream to write to.
 * @param value The value to write, only the lower eight bits are kept.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_stream_outputWriteSingle(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrInRange(0, 256) sjme_jint value);

/**
 * Writes a Java value to the output stream.
 *
 * @param outStream The stream to write to.
 * @param typeId The type to write.
 * @param value The value to write.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_stream_outputWriteValueJP(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS) sjme_basicTypeId typeId,
	sjme_attrInNotNull const sjme_jvalue* value);

/**
 * Writes a Java value to the output stream.
 *
 * @param outStream The stream to write to.
 * @param typeId The type to write.
 * @param ... The value to write.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_stream_outputWriteValueJ(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS) sjme_basicTypeId typeId,
	...);

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
