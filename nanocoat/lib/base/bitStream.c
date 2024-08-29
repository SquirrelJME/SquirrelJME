/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/bitStream.h"
#include "sjme/util.h"

static sjme_errorCode sjme_bitStream_inputClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_bitStream_input stream;
	
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover stream. */
	stream = (sjme_bitStream_input)closeable;
	
	/* If there is a forward close, then close it. */
	if (stream->base.forwardClose != NULL)
	{
		/* Forward close. */
		if (sjme_error_is(error = sjme_closeable_close(
			stream->base.forwardClose)))
			return sjme_error_default(error);
		
		/* Clear so it is not closed again. */
		stream->base.forwardClose = NULL;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_bitStream_inputReadStream(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrOutNotNull sjme_jint* readByte)
{
	sjme_errorCode error;
	sjme_stream_input source;
	sjme_jint count;
	sjme_jubyte value;
	
	if (inStream == NULL || functionData == NULL || readByte == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in single byte. */
	do
	{
		source = functionData;
		count = INT32_MAX;
		value = 0;
		if (sjme_error_is(error = sjme_stream_inputRead(source,
			&count, &value, 1)) ||
			count == INT32_MAX)
			return sjme_error_default(error);
	} while (count == 0);
	
	/* EOF? */
	if (count < 0)
	{
		*readByte = -1;
		return SJME_ERROR_NONE;
	}
	
	/* Otherwise mask in. */
	*readByte = value & 0xFF;
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
	result->base.closeable.closeHandler = sjme_bitStream_inputClose;
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
	sjme_errorCode error;
	sjme_jint shiftIn;
	sjme_juint result, mask;
	sjme_jint limit;
	
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
			/* Read in next byte from the input. */
			shiftIn = INT32_MAX;
			if (sjme_error_is(error = inStream->readFunc(inStream,
				inStream->base.funcData, &shiftIn)) ||
				shiftIn == INT32_MAX)
				return sjme_error_default(error);
			
			/* EOF? */
			if (shiftIn < 0)
			{
				inStream->eofHit = SJME_JNI_TRUE;
				break;
			}
			
			/* Place on top of the current set of bits. */
			else
			{
				inStream->base.overQueue |= ((shiftIn & 0xFF) <<
					inStream->base.overCount);
				inStream->base.overCount += 8;
			}
		}

#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Queue [%d:%d] A (%08x:%08x)",
			inStream->base.overCount, inStream->base.bitCount,
			inStream->base.overQueue, inStream->base.bitQueue);
#endif
		
		/* Can we take from the overflow? */
		while (inStream->base.overCount > 0 && inStream->base.bitCount < 32)
		{
			/* How many bits can we actually take? */
			limit = 32 - inStream->base.bitCount;
			if (limit > inStream->base.overCount)
				limit = inStream->base.overCount;
			
			/* Take in from the overflow. */
			mask = sjme_util_intOverShiftU(1, limit) - 1;
			result = inStream->base.overQueue & mask;
			inStream->base.bitQueue |= (result << inStream->base.bitCount);
			inStream->base.bitCount += limit;
			
			/* Reduce the overflow. */
			inStream->base.overQueue = sjme_util_intOverShiftU(
				inStream->base.overQueue, -limit);
			inStream->base.overCount -= limit;
		}

#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Queue [%d:%d] B (%08x:%08x)",
			inStream->base.overCount, inStream->base.bitCount,
			inStream->base.overQueue, inStream->base.bitQueue);
#endif
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
	
	/* Success! */
	*outValue = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_bitStream_outputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_bitStream_outputWriteByteFunc writeFunc,
	sjme_attrInNullable sjme_pointer writeFuncData,
	sjme_attrInNullable sjme_closeable forwardClose)
{
	if (inPool == NULL || resultStream == NULL || writeFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_bitStream_outputOpenStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_stream_output outputStream,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	if (inPool == NULL || resultStream == NULL || outputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_bitStream_outputWrite(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInValue sjme_bitStream_order bitOrder,
	sjme_attrInValue sjme_juint outValue,
	sjme_attrInPositiveNonZero sjme_jint bitCount)
{
	if (outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitOrder != SJME_BITSTREAM_LSB && bitOrder != SJME_BITSTREAM_MSB)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
