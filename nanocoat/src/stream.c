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

/**
 * Contains the state for reading directly from memory.
 *
 * @since 2024/01/01
 */
typedef struct sjme_stream_inputCacheMemory
{
	/** The base address. */
	const void* base;

	/** The number of bytes in the memory stream. */
	sjme_jint length;
} sjme_stream_inputCacheMemory;

/**
 * Gets the state information from the given input stream.
 *
 * @param base The base pointer.
 * @since 2024/01/01
 */
#define SJME_INPUT_MEMORY_UNCOMMON(base) \
	SJME_INPUT_UNCOMMON(sjme_stream_inputCacheMemory, (base))

static sjme_errorCode sjme_stream_inputMemoryAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
	sjme_stream_inputCacheMemory* cache;

	if (stream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get cache. */
	cache = SJME_INPUT_MEMORY_UNCOMMON(stream);

	/* Calculating this is trivial. */
	*outAvail = cache->length - stream->totalRead;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputMemoryClose(
	sjme_attrInNotNull sjme_stream_input stream)
{
	if (stream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing needs to happen here. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputMemoryRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint length)
{
	sjme_stream_inputCacheMemory* cache;
	sjme_jint limit;

	if (stream == NULL || readCount == NULL || dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Get cache. */
	cache = SJME_INPUT_MEMORY_UNCOMMON(stream);

	/* End of stream? */
	if (stream->totalRead >= cache->length)
	{
		*readCount = -1;
		return SJME_ERROR_NONE;
	}

	/* Determine how many bytes we can actually read. */
	limit = cache->length - stream->totalRead;
	if (length < limit)
		limit = length;

	/* Do a direct memory copy. */
	memmove(dest,
		(void*)(((uintptr_t)cache->base) + stream->totalRead), limit);

	/* Indicate read count and consider success! */
	*readCount = limit;
	return SJME_ERROR_NONE;
}

/** Input memory functions. */
static const sjme_stream_inputFunctions sjme_stream_inputMemoryFunctions =
{
	.available = sjme_stream_inputMemoryAvailable,
	.close = sjme_stream_inputMemoryClose,
	.read = sjme_stream_inputMemoryRead,
};

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
	if (SJME_IS_ERROR(error = stream->functions->available(stream,
		&result)) || result < 0)
		return SJME_DEFAULT_ERROR(error);

	/* Return result. */
	*outAvail = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_inputClose(
	sjme_attrInNotNull sjme_stream_input stream)
{
	sjme_errorCode error;

	if (stream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Function needs to exist. */
	if (stream->functions == NULL || stream->functions->close == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Close the stream. */
	if (SJME_IS_ERROR(error = stream->functions->close(stream)))
		return SJME_DEFAULT_ERROR(error);

	/* Cleanup after it. */
	if (SJME_IS_ERROR(error = sjme_alloc_free(stream)))
		return SJME_DEFAULT_ERROR(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull const void* buffer,
	sjme_attrInPositive sjme_jint length)
{
	sjme_stream_input result;
	sjme_stream_inputCacheMemory* cache;
	sjme_errorCode error;

	if (inPool == NULL || outStream == NULL || buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0 || (((uintptr_t)buffer) + length) < ((uintptr_t)buffer))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Allocate result. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc(inPool,
		SJME_SIZEOF_INPUT_STREAM(sjme_stream_inputCacheMemory),
		&result)) || result == NULL)
		return SJME_DEFAULT_ERROR(error);

	/* Set base information. */
	result->functions = &sjme_stream_inputMemoryFunctions;

	/* Get the cache. */
	cache = SJME_INPUT_MEMORY_UNCOMMON(result);

	/* Set initial state information. */
	cache->base = buffer;
	cache->length = length;

	/* Return result. */
	*outStream = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_inputRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint length)
{
	/* This is just a simplified wrapper. */
	return sjme_stream_inputReadIter(stream, readCount, dest, 0, length);
}

sjme_errorCode sjme_stream_inputReadIter(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t rawDest;
	sjme_jint count, newTotal;
	sjme_errorCode error;
	void* trueDest;

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
	trueDest = (void*)(rawDest + offset);

	/* Read in the data. */
	count = -2;
	if ((SJME_IS_ERROR(error = stream->functions->read(stream,
		&count, trueDest, length))) || count < -1)
		return SJME_DEFAULT_ERROR(error);

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
		single = 999;
		readCount = -2;
		if (SJME_IS_ERROR(error = sjme_stream_inputReadIter(stream,
			&readCount, &single, 0, 1)) || readCount < -1)
			return SJME_DEFAULT_ERROR(error);

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
	if (SJME_IS_ERROR(error = sjme_stream_inputRead(stream,
		&readCount, &temp, reqCount)) || readCount != reqCount)
		return SJME_DEFAULT_ERROR_OR(error,
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

sjme_errorCode sjme_stream_outputClose(
	sjme_attrInNotNull sjme_stream_output stream)
{
	if (stream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_outputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInNotNull void* base,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t realBase;

	if (inPool == NULL || outStream == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	realBase = (uintptr_t)base;
	if (length < 0 || (realBase + length) < realBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_outputWrite(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrOutNotNullBuf(length) void* src,
	sjme_attrInPositive sjme_jint length)
{
	/* Forwards to iter variant. */
	return sjme_stream_outputWriteIter(outStream, src, 0, length);
}

sjme_errorCode sjme_stream_outputWriteIter(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrOutNotNullBuf(length) void* src,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t realSrc;

	if (outStream == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	realSrc = (uintptr_t)src;
	if (offset < 0 || length < 0 || (offset + length) < 0 ||
		(realSrc + offset) < realSrc || (realSrc + length) < realSrc ||
		(realSrc + offset + length) < realSrc)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
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
	if (outStream == NULL || value == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (typeId < 0 || typeId >= SJME_NUM_BASIC_TYPE_IDS ||
		typeId == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE ||
		typeId == SJME_BASIC_TYPE_ID_OBJECT)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
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
			value.z = !!va_arg(va, sjme_jboolean);
			break;

		case SJME_BASIC_TYPE_ID_BYTE:
			value.b = va_arg(va, sjme_jbyte);
			break;

		case SJME_BASIC_TYPE_ID_SHORT:
			value.s = va_arg(va, sjme_jshort);
			break;

		case SJME_BASIC_TYPE_ID_CHARACTER:
			value.c = va_arg(va, sjme_jchar);
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