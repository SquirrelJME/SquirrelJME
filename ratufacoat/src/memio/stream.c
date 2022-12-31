/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "memory.h"
#include "memio/stream.h"

static sjme_jboolean sjme_memStreamCollect(sjme_counter* counter,
	sjme_error* error)
{
	sjme_dataStream* stream;
	sjme_counter* linkedCounter;
	sjme_jboolean isOkay;
	
	if (counter == NULL || counter->dataPointer == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Get the stream. */
	stream = counter->dataPointer;
	
	/* Count down the data chunk, if one is linked. */
	linkedCounter = stream->linkedCounter;
	isOkay = sjme_true;
	if (linkedCounter != NULL)
		isOkay = sjme_counterDown(linkedCounter, NULL, error);
	
	/* Perform final cleanup. */
	return sjme_free(stream, error) && isOkay;
}

static sjme_jboolean sjme_memStreamRead(sjme_dataStream* stream,
	void* dest, sjme_jint len, sjme_jint* readLen, sjme_error* error)
{
	sjme_jint startRead, readLeft, copyLimit;
	sjme_memChunk* memChunk;
	void* realPointer;
	
	if (stream == NULL || dest == NULL || readLen == NULL ||
		stream->streamSource == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	if (len < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALID_ARGUMENT, len);
		
		return sjme_false;
	}
	
	/* We need this chunk. */
	memChunk = stream->streamSource;
	
	/* Determine how much data we can read. */
	startRead = sjme_atomicIntGet(&stream->readBytes);
	readLeft = memChunk->size - startRead;
	copyLimit = sjme_min(readLeft, len);
	
	/* EOF reached? */
	if (readLeft <= 0)
	{
		/* Make sure this remains consistent. */
		if (readLeft < 0)
		{
			sjme_setError(error, SJME_ERROR_INVALID_STREAM_STATE, readLeft);
			
			return sjme_false;
		}
		
		*readLen = -1;
		return sjme_true;
	}
	
	realPointer = NULL;
	if (!sjme_chunkCheckBound(memChunk, startRead,
			copyLimit, error) ||
		!sjme_chunkRealPointer(memChunk, startRead,
			&realPointer, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_STREAM_STATE, 0);
		
		return sjme_false;
	}
	
	/* Copy as many bytes as we can over. */
	memmove(dest, realPointer, copyLimit);
	
	/* Move pointers over. */
	sjme_atomicIntGetThenAdd(&stream->readBytes, copyLimit);
	
	/* Successful read! */
	*readLen = copyLimit;
	return sjme_true;
}

sjme_jboolean sjme_streamFromChunkCounted(sjme_dataStream** outStream,
	sjme_countableMemChunk* chunk, sjme_jint off, sjme_jint len,
	sjme_jboolean countUpChunk, sjme_error* error)
{
	sjme_dataStream* result;
	
	if (outStream == NULL || chunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* This needs to be allocated. */
	result = sjme_malloc(sizeof(*result), error);
	if (result == NULL)
		return sjme_false;
	
	/* Initialize our stream. */
	result->streamSource = &chunk->chunk;
	result->readFunction = sjme_memStreamRead;
	result->linkedCounter = &chunk->count;
	if (!sjme_counterInit(&result->count,
			sjme_memStreamCollect,
			result, countUpChunk, error) &&
		(countUpChunk && !sjme_counterUp(&chunk->count, error)))
	{
		sjme_free(result, error);
		
		return sjme_false;
	}
	
	/* Use this. */
	*outStream = result;
	return sjme_true;
}

sjme_jboolean sjme_streamRead(sjme_dataStream* stream,
	void* dest, sjme_jint len, sjme_jint* readLen, sjme_error* error)
{
	if (stream == NULL || dest == NULL || readLen == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	if (len < 0)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, 0);
		
		return sjme_false;
	}
	
	if (stream->readFunction == NULL)
	{
		sjme_setError(error, SJME_ERROR_INVALID_STREAM_STATE, 0);
		
		return sjme_false;
	}
	
	/* Forward to the general read. */
	return stream->readFunction(stream, dest, len, readLen, error);
}
