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
#include "alloc.h"

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
typedef struct sjme_stream_inputBase* sjme_stream_input;

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
typedef struct sjme_stream_outputBase* sjme_stream_output;

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

	/** Read from stream. */
	sjme_stream_inputReadFunc read;
} sjme_stream_inputFunctions;

struct sjme_stream_inputBase
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
	SJME_UNCOMMON_MEMBER(sjme_stream_inputBase, uncommon, \
		uncommonType, (base))

/**
 * Determines the size of the input stream structure.
 *
 * @param uncommonSize The uncommon size.
 * @return The input stream structure size.
 * @since 2024/01/01
 */
#define SJME_SIZEOF_INPUT_STREAM_N(uncommonSize) \
    SJME_SIZEOF_UNCOMMON_N(sjme_stream_inputBase, uncommon, uncommonSize)

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
 * Closes the specified output stream.
 *
 * @param stream The output stream to close.
 * @param optResult Optional output result.
 * @return On any resultant error, if any.
 * @since 2024/01/09
 */
typedef sjme_errorCode (*sjme_stream_outputCloseFunc)(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrOutNullable sjme_pointer* optResult);

/**
 * Writes to the given output stream.
 *
 * @param stream The stream to write to.
 * @param buf The bytes to write.
 * @param length The number of bytes to write.
 * @return On any resultant error, if any.
 * @since 2024/01/09
 */
typedef sjme_errorCode (*sjme_stream_outputWriteFunc)(
	sjme_attrInNotNull sjme_stream_output stream,
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

	/** Writes to the given output stream. */
	sjme_stream_outputWriteFunc write;
} sjme_stream_outputFunctions;

struct sjme_stream_outputBase
{
	/** Functions for output. */
	const sjme_stream_outputFunctions* functions;

	/** Front end holders. */
	sjme_frontEnd frontEnd;

	/** The current number of written bytes. */
	sjme_jint totalWritten;

	/** Uncommon stream specific data. */
	sjme_jlong uncommon[sjme_flexibleArrayCount];
};

/**
 * Gets the state information from the given output stream.
 *
 * @param uncommonType The uncommon type.
 * @param base The base pointer.
 * @since 2024/01/09
 */
#define SJME_OUTPUT_UNCOMMON(uncommonType, base) \
	SJME_UNCOMMON_MEMBER(sjme_stream_outputBase, uncommon, \
		uncommonType, (base))

/**
 * Determines the size of the output stream structure.
 *
 * @param uncommonSize The uncommon size.
 * @return The output stream structure size.
 * @since 2024/01/09
 */
#define SJME_SIZEOF_OUTPUT_STREAM_N(uncommonSize) \
    SJME_SIZEOF_UNCOMMON_N(sjme_stream_outputBase, uncommon, uncommonSize)

/**
 * Determines the size of the output stream structure.
 *
 * @param uncommonType The uncommon type.
 * @return The output stream structure size.
 * @since 2024/01/09
 */
#define SJME_SIZEOF_OUTPUT_STREAM(uncommonType) \
	SJME_SIZEOF_OUTPUT_STREAM_N(sizeof(uncommonType))

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
 * Closes an input stream.
 *
 * @param stream The stream to close.
 * @return Any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_inputClose(
	sjme_attrInNotNull sjme_stream_input stream);

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
 * Closes an output stream.
 *
 * @param stream The stream to close.
 * @param optResult Optional resultant output value, if any.
 * @return Any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_stream_outputClose(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrOutNullable sjme_pointer* optResult);

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

	/** The optional output result. */
	sjme_pointer* optResult;
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
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
typedef sjme_errorCode (*sjme_stream_outputByteArrayFinishFunc)(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_resultByteArray* result);

/**
 * Opens a dynamically resizing output byte array.
 *
 * @param inPool The pool to allocate within.
 * @param outStream The resultant output stream.
 * @param initialLimit The initial buffer limit.
 * @param finish The function to call when the stream is closed.
 * @param whatever Can be used to pass whatever is needed for the finish
 * processor.
 * @return On any error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_stream_outputOpenByteArray(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInPositive sjme_jint initialLimit,
	sjme_attrInNotNull sjme_stream_outputByteArrayFinishFunc finish,
	sjme_attrInNullable sjme_pointer whatever);

/**
 * Initializes a simplified data output stream.
 * 
 * @param inStream The input stream.
 * @param outData The data output to initialize.
 * @return Any resultant error, if any.
 * @since 2024/06/20
 */
sjme_errorCode sjme_stream_outputOpenDataStatic(
	sjme_attrInNotNull sjme_stream_output inStream,
	sjme_attrInOutNotNull sjme_stream_outputData outData);

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
