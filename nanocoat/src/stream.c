/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/stream.h"
#include "sjme/debug.h"

sjme_errorCode sjme_stream_inputAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_inputClose(
	sjme_attrInNotNull sjme_stream_input stream)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull const void* buffer,
	sjme_attrInPositive sjme_jint length)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
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

	if (stream == NULL || readCount == NULL || dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	rawDest = (uintptr_t)dest;
	if (offset < 0 || length < 0 || (offset + length) < 0 ||
		(rawDest + length) < rawDest)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_inputReadSingle(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* result)
{
	sjme_jubyte single;
	sjme_jint readCount;
	sjme_errorCode error;

	/* Constantly try to read a single byte. */
	for (;;)
	{
		/* Attempt single byte read. */
		single = 999;
		readCount = 0;
		if (SJME_IS_ERROR(error = sjme_stream_inputReadIter(stream,
			&readCount, &single, 0, 1)))
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

sjme_errorCode sjme_stream_outputClose(
	sjme_attrInNotNull sjme_stream_output stream)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
