/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Bit streams, used to read data at the bit level rather than byte level.
 * 
 * @since 2024/08/25
 */

#ifndef SQUIRRELJME_BITSTREAM_H
#define SQUIRRELJME_BITSTREAM_H

#include "sjme/stdTypes.h"
#include "sjme/closeable.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_BITSTREAM_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Order for when reading or writing multiple bits.
 * 
 * @since 2024/08/28
 */
typedef enum sjme_bitStream_order
{
	/** Least significant bit. */
	SJME_BITSTREAM_LSB,
	
	/** Most significant bit. */
	SJME_BITSTREAM_MSB,
} sjme_bitStream_order;

/**
 * Bit stream state structure, generic.
 * 
 * @since 2024/08/26
 */
typedef struct sjme_bitStream_base
{
	/** Closeable data, as bit streams may be closed. */
	sjme_closeableBase closeable;
	
	/** The closeable to close, if forwarded, optional. */
	sjme_closeable forwardClose;
	
	/** Pointer to pass to the function as data. */
	sjme_pointer funcData;
	
	/** The number of bits read/written. */
	sjme_juint streamCount;
	
	/** The current bit queue. */
	sjme_juint bitQueue;
	
	/** The amount of bits in the buffer. */
	sjme_juint bitCount;
	
	/** Overflow bits. */
	sjme_juint overQueue;
	
	/** The overflow count. */
	sjme_juint overCount;
} sjme_bitStream_base;

/**
 * Base bitstream type.
 * 
 * @since 2024/08/31
 */
typedef sjme_bitStream_base* sjme_bitStream;

/** Cast to a bit stream base. */
#define SJME_AS_BITSTREAM(x) ((sjme_bitStream)(x))

/**
 * Input bit stream source.
 * 
 * @since 2024/08/26
 */
typedef struct sjme_bitStream_inputBase sjme_bitStream_inputBase;

/**
 * Input bit stream source.
 * 
 * @since 2024/08/26
 */
typedef struct sjme_bitStream_inputBase* sjme_bitStream_input;

/**
 * Output bit stream destination.
 * 
 * @since 2024/08/26
 */
typedef struct sjme_bitStream_outputBase sjme_bitStream_outputBase;

/**
 * Output bit stream destination.
 * 
 * @since 2024/08/26
 */
typedef struct sjme_bitStream_outputBase* sjme_bitStream_output;

/**
 * Reads a single byte from the input.
 * 
 * @param inStream The bit stream.
 * @param functionData The optional data passed to this function.
 * @param readCount The resultant read count, negative value means EOF.
 * @param outBuf The buffer of read bytes.
 * @param length The number of bytes to read.
 * @return On any resultant error, if any.
 * @since 2024/08/26
 */
typedef sjme_errorCode (*sjme_bitStream_inputReadByteFunc)(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrOutNotNull sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length);

/**
 * Writes a single byte to the output.
 * 
 * @param outStream The bit stream.
 * @param functionData The optional data passed to this function.
 * @param writeBuf The bytes to write.
 * @param length The number of bytes to write.
 * @return On any resultant error, if any.
 * @since 2024/08/26
 */
typedef sjme_errorCode (*sjme_bitStream_outputWriteByteFunc)(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrInNotNullBuf(length) sjme_buffer writeBuf,
	sjme_attrInPositiveNonZero sjme_jint length);

struct sjme_bitStream_inputBase
{
	/** Base bit stream data. */
	sjme_bitStream_base base;
	
	/** The read function. */
	sjme_bitStream_inputReadByteFunc readFunc;
	
	/** Was EOF hit? */
	sjme_jboolean eofHit;
};

struct sjme_bitStream_outputBase
{
	/** Base bit stream data. */
	sjme_bitStream_base base;
	
	/** The write function. */
	sjme_bitStream_outputWriteByteFunc writeFunc;
};

/**
 * Returns the number of bits that are ready.
 * 
 * @param ofStream The stream to get the ready count for. 
 * @param readyBits The number of ready bits.
 * @return Any resultant error, if any.
 * @since 2024/08/31 
 */
sjme_errorCode sjme_bitStream_bitsReady(
	sjme_attrInNotNull sjme_bitStream ofStream,
	sjme_attrOutNotNull sjme_jint* readyBits);

/**
 * Skips the given number of bits until the input bit stream is aligned with
 * the given alignment, that is the modulo of it is zero.
 * 
 * @param inStream The input bit stream.
 * @param alignBit The bit to align to.
 * @param outSkipped The number of bits that were skipped, optional.
 * @return On any resultant error, if any.
 * @since 2024/08/31
 */
sjme_errorCode sjme_bitStream_inputAlign(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInRange(2, 32) sjme_jint alignBit,
	sjme_attrOutNullable sjme_jint* outSkipped);

/**
 * Opens an input bit stream.
 * 
 * @param inPool The pool to allocate within.
 * @param resultStream The resultant stream.
 * @param readFunc The read function to use.
 * @param readFuncData The optional data to pass to the function.
 * @param forwardClose The closeable to close, if closed.
 * @return On any resultant error, if any.
 * @since 2024/08/26
 */
sjme_errorCode sjme_bitStream_inputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_bitStream_inputReadByteFunc readFunc,
	sjme_attrInNullable sjme_pointer readFuncData,
	sjme_attrInNullable sjme_closeable forwardClose);

/**
 * Opens a bit stream from a memory location.
 * 
 * @param inPool The pool to allocate within. 
 * @param resultStream The resultant stream.
 * @param base The base memory address.
 * @param length The length of the memory block.
 * @return Any resultant error, if any.
 * @since 2024/08/27
 */
sjme_errorCode sjme_bitStream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_cpointer base,
	sjme_attrInPositive sjme_jint length);

/**
 * Opens a bit stream which reads from the given @c sjme_stream_input .
 * 
 * @param inPool The pool to allocate within.
 * @param resultStream The resultant stream.
 * @param inputStream The stream to read byte data from.
 * @param forwardClose Should close be forwarded to the given stream?
 * @return On any resultant error, if any.
 * @since 2024/08/26
 */
sjme_errorCode sjme_bitStream_inputOpenStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_stream_input inputStream,
	sjme_attrInValue sjme_jboolean forwardClose);

/**
 * Reads bits from the input source. 
 * 
 * @param inStream The stream to read bits from.
 * @param bitOrder The order of the bit read.
 * @param outValue The resultant value.
 * @param bitCount The number of bits to read.
 * @return Any resultant error, if any.
 * @since 2024/08/27
 */
sjme_errorCode sjme_bitStream_inputRead(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInValue sjme_bitStream_order bitOrder,
	sjme_attrOutNotNull sjme_juint* outValue,
	sjme_attrInPositiveNonZero sjme_jint bitCount);

/**
 * Opens an output bit stream.
 * 
 * @param inPool The pool to allocate within.
 * @param resultStream The resultant stream.
 * @param writeFunc The write function to use.
 * @param writeFuncData The optional data to pass to the function.
 * @param forwardClose The closeable to close, if closed.
 * @return On any resultant error, if any.
 * @since 2024/08/26
 */
sjme_errorCode sjme_bitStream_outputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_bitStream_outputWriteByteFunc writeFunc,
	sjme_attrInNullable sjme_pointer writeFuncData,
	sjme_attrInNullable sjme_closeable forwardClose);

/**
 * Opens an output bit stream which writes to the given output stream.
 * 
 * @param inPool The pool to allocate within.
 * @param resultStream The resultant output bit stream.
 * @param outputStream The stream to write.
 * @param forwardClose If this is closed, should @c outputStream be closed?
 * @return Any resultant error, if any.
 * @since 2024/08/28
 */
sjme_errorCode sjme_bitStream_outputOpenStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_stream_output outputStream,
	sjme_attrInValue sjme_jboolean forwardClose);

/**
 * Writes bits to the output destination. 
 * 
 * @param outStream The stream to write bits to.
 * @param bitOrder The order of the bit write.
 * @param inValue The value to write.
 * @param bitCount The number of bits to write.
 * @return Any resultant error, if any.
 * @since 2024/08/27
 */
sjme_errorCode sjme_bitStream_outputWrite(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInValue sjme_bitStream_order bitOrder,
	sjme_attrInValue sjme_juint outValue,
	sjme_attrInPositiveNonZero sjme_jint bitCount);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_BITSTREAM_H
}
		#undef SJME_CXX_SQUIRRELJME_BITSTREAM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_BITSTREAM_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_BITSTREAM_H */
