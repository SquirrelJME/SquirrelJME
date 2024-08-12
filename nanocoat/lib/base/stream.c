/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/stream.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/util.h"

static sjme_errorCode sjme_stream_inputClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_stream_input stream;

	/* Recover stream. */
	stream = (sjme_stream_input)closeable;
	if (stream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Function needs to exist. */
	if (stream->functions == NULL || stream->functions->close == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Close the stream. */
	if (sjme_error_is(error = stream->functions->close(stream,
		&stream->implState)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_outputClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_stream_output stream;

	/* Recover stream. */
	stream = (sjme_stream_output)closeable;
	if (stream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Function needs to exist. */
	if (stream->functions == NULL || stream->functions->close == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Close the stream. */
	if (sjme_error_is(error = stream->functions->close(stream,
		&stream->implState)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_inputAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
	sjme_jint result;
	sjme_errorCode error;

	if (stream == NULL || outAvail == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Function needs to exist. */
	if (stream->functions == NULL || stream->functions->available == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Request the number of available bytes. */
	result = -1;
	if (sjme_error_is(error = stream->functions->available(stream,
		&stream->implState, &result)) || result < 0)
		return sjme_error_default(error);

	/* Return result. */
	*outAvail = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_inputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull const sjme_stream_inputFunctions* inFunctions,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_stream_input result;
	
	if (inPool == NULL || outStream == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* These are required. */
	if (inFunctions->read == NULL || inFunctions->init == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), sjme_closeable_autoEnqueue, NULL,
		&result, NULL)))
		return sjme_error_default(error);
	
	/* Setup details. */
	result->implState.inPool = inPool;
	result->functions = inFunctions;
	result->closable.closeHandler = sjme_stream_inputClose;
	
	/* Copy front end? */
	if (copyFrontEnd != NULL)
		memmove(&result->frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
		
	/* Call sub-init. */
	if (sjme_error_is(error = result->functions->init(result,
		&result->implState, data)))
	{
		/* Free before failure. */
		sjme_alloc_free(result);
		
		return sjme_error_default(error);
	}
	
	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_inputRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	/* This is just a simplified wrapper. */
	return sjme_stream_inputReadIter(stream, readCount,
		dest, 0, length);
}

sjme_errorCode sjme_stream_inputReadIter(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t rawDest;
	sjme_jint count, newTotal, totalRead;
	sjme_errorCode error;
	sjme_pointer trueDest;

	if (stream == NULL || readCount == NULL || dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	rawDest = (uintptr_t)dest;
	if (offset < 0 || length < 0 || (offset + length) < 0 ||
		(rawDest + length) < rawDest)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Function needs to exist. */
	if (stream->functions == NULL || stream->functions->read == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Calculate the true target address. */
	trueDest = (sjme_pointer)(rawDest + offset);

	/* Overflowing write? */
	totalRead = stream->totalRead;
	if (totalRead < 0 || (totalRead + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Read in the data. */
	count = -2;
	if ((sjme_error_is(error = stream->functions->read(stream,
		&stream->implState,
		&count, trueDest, length))) || count < -1)
		return sjme_error_default(error);

	/* If not EOS, move counters up. */
	if (count >= 0)
	{
		/* Never underflow, just keep it at max int. */
		newTotal = stream->totalRead + count;
		if (newTotal > stream->totalRead)
			stream->totalRead = newTotal;
	}

	/* Return result. */
	*readCount = count;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_inputReadSingle(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* result)
{
	sjme_jubyte single;
	sjme_jint readCount;
	sjme_errorCode error;

	if (stream == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Constantly try to read a single byte. */
	for (;;)
	{
		/* Attempt single byte read. */
		single = 0xFE;
		readCount = -2;
		if (sjme_error_is(error = sjme_stream_inputReadIter(stream,
			&readCount, &single, 0, 1)) || readCount < -1)
			return sjme_error_default(error);

		/* Did not read anything? */
		if (readCount == 0)
			continue;

		/* EOF? */
		if (readCount < 0)
		{
			*result = -1;
			return SJME_ERROR_NONE;
		}

		/* Return resultant character. */
		*result = (single & 0xFF);
		return SJME_ERROR_NONE;
	}
}

sjme_errorCode sjme_stream_inputReadValueJ(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS)
		sjme_basicTypeId typeId,
	sjme_attrOutNotNull sjme_jvalue* outValue)
{
	sjme_jint reqCount, readCount;
	sjme_jvalue temp;
	sjme_errorCode error;

	if (stream == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (typeId < 0 || typeId >= SJME_NUM_BASIC_TYPE_IDS ||
		typeId == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE ||
		typeId == SJME_JAVA_TYPE_ID_OBJECT)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* How many bytes do we need to read? */
	switch (typeId)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
		case SJME_BASIC_TYPE_ID_BYTE:
			reqCount = 1;
			break;

		case SJME_BASIC_TYPE_ID_SHORT:
		case SJME_BASIC_TYPE_ID_CHARACTER:
			reqCount = 2;
			break;

		case SJME_BASIC_TYPE_ID_INTEGER:
		case SJME_BASIC_TYPE_ID_FLOAT:
			reqCount = 4;
			break;

		case SJME_BASIC_TYPE_ID_LONG:
		case SJME_BASIC_TYPE_ID_DOUBLE:
			reqCount = 8;
			break;

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Read into temporary, so we do not alter memory just yet. */
	memset(&temp, 0, sizeof(temp));
	readCount = -2;
	if (sjme_error_is(error = sjme_stream_inputRead(stream,
		&readCount, &temp, reqCount)) || readCount != reqCount)
		return sjme_error_defaultOr(error,
			SJME_ERROR_UNEXPECTED_EOF);

	/* Normalize boolean? */
	if (typeId == SJME_BASIC_TYPE_ID_BOOLEAN)
		temp.z = (temp.b == 0 ? SJME_JNI_FALSE : SJME_JNI_TRUE);

#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
	/* Perform byte swap on the data. */
	if (reqCount > 1)
	{
		if (reqCount == 2)
			temp.s = sjme_swap_short(temp.s);
		else if (reqCount == 4)
			temp.i = sjme_swap_int(temp.i);
		else
			temp.j = sjme_swap_long(temp.j);
	}
#endif

	/* Success! */
	memmove(outValue, &temp, sizeof(temp));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_outputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInNotNull const sjme_stream_outputFunctions* inFunctions,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_stream_output result;
	
	if (inPool == NULL || outStream == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* These are required. */
	if (inFunctions->write == NULL || inFunctions->init == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), sjme_closeable_autoEnqueue, NULL,
		&result, NULL)))
		return sjme_error_default(error);
	
	/* Setup details. */
	result->implState.inPool = inPool;
	result->functions = inFunctions;
	result->closable.closeHandler = sjme_stream_outputClose;
	
	/* Copy front end? */
	if (copyFrontEnd != NULL)
		memmove(&result->frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
		
	/* Call sub-init. */
	if (sjme_error_is(error = result->functions->init(result,
		&result->implState, data)))
	{
		/* Free before failure. */
		sjme_alloc_free(result);
		
		return sjme_error_default(error);
	}
	
	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_outputWrite(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrOutNotNullBuf(length) sjme_pointer src,
	sjme_attrInPositive sjme_jint length)
{
	/* Forwards to iter variant. */
	return sjme_stream_outputWriteIter(outStream, src,
		0, length);
}

sjme_errorCode sjme_stream_outputWriteIter(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrOutNotNullBuf(length) sjme_pointer src,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t realSrc;
	sjme_errorCode error;
	sjme_jint written;

	if (stream == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	realSrc = (uintptr_t)src;
	if (offset < 0 || length < 0 || (offset + length) < 0 ||
		(realSrc + offset) < realSrc || (realSrc + length) < realSrc ||
		(realSrc + offset + length) < realSrc)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Overflowing write? */
	written = stream->totalWritten;
	if (written < 0 || (written + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* No write function exists? */
	if (stream->functions->write == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Forward call. */
	if (sjme_error_is(error = stream->functions->write(stream,
		&stream->implState,
		(sjme_pointer)(realSrc + offset), length)))
		return sjme_error_default(error);

	/* Increase write count. */
	stream->totalWritten += length;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_outputWriteSingle(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrInRange(0, 256) sjme_jint value)
{
	sjme_jubyte really;

	/* Map down value. */
	really = (sjme_jubyte)(value & 0xFF);

	/* Forward call. */
	return sjme_stream_outputWriteIter(outStream,
		&really, 0, 1);
}

sjme_errorCode sjme_stream_outputWriteValueJP(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS) sjme_basicTypeId typeId,
	sjme_attrInNotNull const sjme_jvalue* value)
{
	sjme_jint reqCount;
	union
	{
		sjme_jvalue value;
		sjme_jubyte raw[8];
	} temp;

	if (outStream == NULL || value == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (typeId < 0 || typeId >= SJME_NUM_BASIC_TYPE_IDS ||
		typeId == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE ||
		typeId == SJME_BASIC_TYPE_ID_OBJECT)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* How many bytes do we need to write? */
	switch (typeId)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
		case SJME_BASIC_TYPE_ID_BYTE:
			reqCount = 1;
			break;

		case SJME_BASIC_TYPE_ID_SHORT:
		case SJME_BASIC_TYPE_ID_CHARACTER:
			reqCount = 2;
			break;

		case SJME_BASIC_TYPE_ID_INTEGER:
		case SJME_BASIC_TYPE_ID_FLOAT:
			reqCount = 4;
			break;

		case SJME_BASIC_TYPE_ID_LONG:
		case SJME_BASIC_TYPE_ID_DOUBLE:
			reqCount = 8;
			break;

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Copy value directly. */
	memmove(&temp, value, sizeof(*value));

#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
	/* Perform byte swap on the data. */
	if (reqCount > 1)
	{
		if (reqCount == 2)
			temp.value.s = sjme_swap_short(temp.value.s);
		else if (reqCount == 4)
			temp.value.i = sjme_swap_int(temp.value.i);
		else
			temp.value.j = sjme_swap_long(temp.value.j);
	}
#endif

	/* Forward write. */
	return sjme_stream_outputWriteIter(outStream,
		&temp, 0, reqCount);
}

sjme_errorCode sjme_stream_outputWriteValueJ(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS) sjme_basicTypeId typeId,
	...)
{
	va_list va;
	sjme_jvalue value;

	if (typeId < 0 || typeId >= SJME_NUM_BASIC_TYPE_IDS ||
		typeId == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE ||
		typeId == SJME_BASIC_TYPE_ID_OBJECT)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Clear before reading in. */
	memset(&value, 0, sizeof(value));

	/* Read in the argument accordingly. */
	va_start(va, typeId);
	switch (typeId)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
			value.z = va_arg(va, sjme_jboolean) != SJME_JNI_FALSE;
			break;

		case SJME_BASIC_TYPE_ID_BYTE:
			value.b = va_arg(va, sjme_jbyte_promoted);
			break;

		case SJME_BASIC_TYPE_ID_SHORT:
			value.s = va_arg(va, sjme_jshort_promoted);
			break;

		case SJME_BASIC_TYPE_ID_CHARACTER:
			value.c = va_arg(va, sjme_jchar_promoted);
			break;

		case SJME_BASIC_TYPE_ID_INTEGER:
			value.i = va_arg(va, sjme_jint);
			break;

		case SJME_BASIC_TYPE_ID_LONG:
			value.j = va_arg(va, sjme_jlong);
			break;

		case SJME_BASIC_TYPE_ID_FLOAT:
			value.f = va_arg(va, sjme_jfloat);
			break;

		case SJME_BASIC_TYPE_ID_DOUBLE:
			value.d = va_arg(va, sjme_jdouble);
			break;

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Stop. */
	va_end(va);

	/* Forward call. */
	return sjme_stream_outputWriteValueJP(outStream, typeId,
		&value);
}
