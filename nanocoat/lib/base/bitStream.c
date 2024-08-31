/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/bitStream.h"
#include "sjme/util.h"

static sjme_errorCode sjme_bitStream_inputOutputClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_bitStream_base* stream;
	
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover stream. */
	stream = (sjme_bitStream_base*)closeable;
	
	/* If there is a forward close, then close it. */
	if (stream->forwardClose != NULL)
	{
		/* Forward close. */
		if (sjme_error_is(error = sjme_closeable_close(
			stream->forwardClose)))
			return sjme_error_default(error);
		
		/* Clear so it is not closed again. */
		stream->forwardClose = NULL;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_bitStream_inputReadStream(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrOutNotNull sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_stream_input source;
	
	if (inStream == NULL || functionData == NULL || readCount == NULL ||
		outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Read in value. */
	source = functionData;
	if (sjme_error_is(error = sjme_stream_inputRead(source,
		readCount, outBuf, length)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_bitStream_outputWriteStream(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrInNotNullBuf(length) sjme_buffer writeBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_stream_output dest;
	
	if (outStream == NULL || functionData == NULL || writeBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Forward write. */
	dest = functionData;
	if (sjme_error_is(error = sjme_stream_outputWrite(dest,
		writeBuf, length)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_bitStream_overToQueue(
	sjme_attrInNotNull sjme_bitStream_base* base)
{
	sjme_jint limit;
	sjme_juint mask, result;
	
	if (base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	while (base->overCount > 0 && base->bitCount < 32)
	{
		/* How many bits can we actually take? */
		limit = 32 - base->bitCount;
		if (limit > base->overCount)
			limit = base->overCount;
		
		/* Take in from the overflow. */
		mask = sjme_util_intOverShiftU(1, limit) - 1;
		result = base->overQueue & mask;
		base->bitQueue |= (result << base->bitCount);
		base->bitCount += limit;
		
		/* Reduce the overflow. */
		base->overQueue = sjme_util_intOverShiftU(
			base->overQueue, -limit);
		base->overCount -= limit;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_bitStream_inputAlign(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInRange(2, 32) sjme_jint alignBit,
	sjme_attrOutNullable sjme_jint* outSkipped)
{
	sjme_errorCode error;
	sjme_jint at, skip;
	sjme_juint discard;
	
	if (inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (alignBit < 2 || alignBit > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Which alignment are we at? Do we even need to align? */
	at = inStream->base.streamCount % alignBit;
	if (at == 0)
	{
		if (outSkipped != NULL)
			*outSkipped = 0;
		return SJME_ERROR_NONE;
	}
	
	/* Skip bits until we reach the alignment amount. */
	skip = alignBit - at;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inStream, SJME_BITSTREAM_LSB,
		&discard, skip)))
		return sjme_error_default(error);
	
	/* Success! */
	if (outSkipped != NULL)
		*outSkipped = skip;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_bitStream_inputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_bitStream_inputReadByteFunc readFunc,
	sjme_attrInNullable sjme_pointer readFuncData,
	sjme_attrInNullable sjme_closeable forwardClose)
{
	sjme_errorCode error;
	sjme_bitStream_input result;
	
	if (inPool == NULL || resultStream == NULL || readFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), sjme_closeable_autoEnqueue,
		NULL, (sjme_pointer*)&result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Setup result. */
	result->base.closeable.closeHandler = sjme_bitStream_inputOutputClose;
	result->base.forwardClose = forwardClose;
	result->base.funcData = readFuncData;
	result->readFunc = readFunc;
	
	/* Success! */
	*resultStream = result;
	return SJME_ERROR_NONE;

fail_alloc:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_bitStream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_cpointer base,
	sjme_attrInPositive sjme_jint length)
{
	sjme_errorCode error;
	sjme_stream_input memory;
	sjme_bitStream_input result;
	
	if (inPool == NULL || resultStream == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Open memory stream first. */
	memory = NULL;
	if (sjme_error_is(error = sjme_stream_inputOpenMemory(inPool,
		&memory, base, length)) || memory == NULL)
		goto fail_openMemory;
	
	/* Then try the resultant bit stream. */
	result = NULL;
	if (sjme_error_is(error = sjme_bitStream_inputOpenStream(inPool,
		&result,
		memory, SJME_JNI_TRUE)) || result == NULL)
		goto fail_openStream;
	
	/* Success! */
	*resultStream = result;
	return SJME_ERROR_NONE;
	
fail_openStream:
fail_openMemory:
	if (memory != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(memory));
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_bitStream_inputOpenStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_stream_input inputStream,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	if (inPool == NULL || resultStream == NULL || inputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to general open. */
	return sjme_bitStream_inputOpen(inPool,
		resultStream, sjme_bitStream_inputReadStream,
		inputStream,
		(forwardClose ? SJME_AS_CLOSEABLE(inputStream) : NULL));
}

sjme_errorCode sjme_bitStream_inputRead(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInValue sjme_bitStream_order bitOrder,
	sjme_attrOutNotNull sjme_juint* outValue,
	sjme_attrInPositiveNonZero sjme_jint bitCount)
{
#define CHUNK_SIZE 4
	sjme_errorCode error;
	sjme_jint shiftIn;
	sjme_juint result, mask;
	sjme_jint limit, i;
	sjme_jubyte chunk[CHUNK_SIZE];
	
	if (inStream == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitOrder != SJME_BITSTREAM_LSB && bitOrder != SJME_BITSTREAM_MSB)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (bitCount <= 0 || bitCount > 32)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Fill in overflow and normal bit queue. */
	do
	{
		/* Fill into the overflow */
		while (!inStream->eofHit && inStream->base.overCount <= 24)
		{
			/* How many full bytes can we read in? */
			limit = (32 - inStream->base.overCount) / 8;
			if (limit > CHUNK_SIZE)
				limit = CHUNK_SIZE;
			
			/* Read in multiple bytes at once. */
			memset(chunk, 0, sizeof(chunk));
			
			/* Read in next byte from the input. */
			shiftIn = INT32_MAX;
			if (sjme_error_is(error = inStream->readFunc(inStream,
				inStream->base.funcData,
				&shiftIn, chunk, limit)) ||
				shiftIn == INT32_MAX)
				return sjme_error_default(error);
			
			/* EOF? */
			if (shiftIn < 0)
			{
				inStream->eofHit = SJME_JNI_TRUE;
				break;
			}
			
			/* Place on top of the current set of bits. */
			for (i = 0; i < shiftIn; i++)
			{
				inStream->base.overQueue |= ((((sjme_juint)chunk[i]) & 0xFF) <<
					inStream->base.overCount);
				inStream->base.overCount += 8;
			}
		}
		
		/* Can we take from the overflow? */
		if (sjme_error_is(error = sjme_bitStream_overToQueue(&inStream->base)))
			return sjme_error_default(error);
	} while (!inStream->eofHit && inStream->base.bitCount < 32);
	
	/* Not enough bits in the input? */
	if (inStream->base.bitCount < bitCount)
	{
		/* If EOF was hit, then read what remains. */
		if (inStream->eofHit && inStream->base.bitCount > 0)
		{
			/* Getting what remains helps if we are hitting EOF, the upper */
			/* bits will just be assumed to be zero. */
			result = inStream->base.bitQueue;
			
			/* Clear. */
			inStream->base.bitQueue = 0;
			inStream->base.bitCount = 0;
			
			/* Success! */
			*outValue = result;
			return SJME_ERROR_NONE;
		}
		
		/* Otherwise, fail. */
		else if (inStream->eofHit)
			return SJME_ERROR_END_OF_FILE;
		
		/* Fail with not enough data. */
		return SJME_ERROR_TOO_SHORT;
	}
	
	/* Get masked bits from the window for the bits we want. */
	mask = sjme_util_intOverShiftU(1, bitCount) - 1;
	result = inStream->base.bitQueue & mask;
	
	/* Reverse? */
	if (bitOrder == SJME_BITSTREAM_MSB)
		result = sjme_util_intOverShiftU(sjme_util_intReverseU(result),
			-32 + bitCount);
	
	/* Remove bits from the queue. */
	inStream->base.bitQueue = sjme_util_intOverShiftU(
		inStream->base.bitQueue, -bitCount);
	inStream->base.bitCount -= bitCount;
	
	/* Count up read. */
	inStream->base.streamCount += bitCount;
	
	/* Success! */
	*outValue = result;
	return SJME_ERROR_NONE;
#undef CHUNK_SIZE
}

sjme_errorCode sjme_bitStream_outputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_bitStream_outputWriteByteFunc writeFunc,
	sjme_attrInNullable sjme_pointer writeFuncData,
	sjme_attrInNullable sjme_closeable forwardClose)
{
	sjme_errorCode error;
	sjme_bitStream_output result;
	
	if (inPool == NULL || resultStream == NULL || writeFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), sjme_closeable_autoEnqueue,
		NULL, (sjme_pointer*)&result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Setup result. */
	result->base.closeable.closeHandler = sjme_bitStream_inputOutputClose;
	result->base.forwardClose = forwardClose;
	result->base.funcData = writeFuncData;
	result->writeFunc = writeFunc;
	
	/* Success! */
	*resultStream = result;
	return SJME_ERROR_NONE;

fail_alloc:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_bitStream_outputOpenStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_stream_output outputStream,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	if (inPool == NULL || resultStream == NULL || outputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to general open. */
	return sjme_bitStream_outputOpen(inPool,
		resultStream, sjme_bitStream_outputWriteStream,
		outputStream,
		(forwardClose ? SJME_AS_CLOSEABLE(outputStream) : NULL));
}

sjme_errorCode sjme_bitStream_outputWrite(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInValue sjme_bitStream_order bitOrder,
	sjme_attrInValue sjme_juint outValue,
	sjme_attrInPositiveNonZero sjme_jint bitCount)
{
#define CHUNK_SIZE 4
	sjme_errorCode error;
	sjme_juint mask, length;
	sjme_jubyte chunk[CHUNK_SIZE];
	
	if (outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitOrder != SJME_BITSTREAM_LSB && bitOrder != SJME_BITSTREAM_MSB)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (bitCount <= 0 || bitCount > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Reverse? */
	if (bitOrder == SJME_BITSTREAM_MSB)
		outValue = sjme_util_intOverShiftU(
			sjme_util_intReverseU(outValue), -32 + bitCount);
	
	/* Shift in on top of the overflow queue. */
	mask = sjme_util_intOverShiftU(1, bitCount) - 1;
	outStream->base.overQueue |= ((outValue & mask) <<
		outStream->base.overCount);
	outStream->base.overCount += bitCount;
	
	/* Count up write. */
	outStream->base.streamCount += bitCount;
		
	/* Can we take from the overflow? */
	if (sjme_error_is(error = sjme_bitStream_overToQueue(&outStream->base)))
		return sjme_error_default(error);
	
	/* Can we write more bytes to the output? */
	while (outStream->base.bitCount >= 8)
	{
		/* Copy to the small chunk buffer. */
		for (length = 0; outStream->base.bitCount >= 8; length++)
		{
			/* Get byte to write. */
			chunk[length] = outStream->base.bitQueue & 0xFF;
		
			/* Shift queue down. */
			outStream->base.bitQueue >>= 8;
			outStream->base.bitCount -= 8;
		}
		
		/* Write to target. */
		if (sjme_error_is(error = outStream->writeFunc(outStream,
			outStream->base.funcData, chunk, length)))
			return sjme_error_default(error);
		
		/* Can we take from the overflow? */
		if (sjme_error_is(error = sjme_bitStream_overToQueue(
			&outStream->base)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
#undef CHUNK_SIZE
}
