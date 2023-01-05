/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "memio/memchunk.h"
#include "memops.h"

sjme_jboolean sjme_chunkCheckBound(const sjme_memChunk* chunk, sjme_jint off,
	sjme_jint len, sjme_error* error)
{
	/* Check. */
	if (chunk == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Check the bounds. */
	if (off < 0 || len < 0 || (off + len) < 0 || (off + len) > chunk->size)
		return sjme_setErrorF(error, SJME_ERROR_OUT_OF_BOUNDS, 0);
		
	return sjme_true;
}

sjme_jboolean sjme_chunkReadBigInt(const sjme_memChunk* chunk, sjme_jint off,
	sjme_jint* value, sjme_error* error)
{
	/* Check. */
	if (chunk == NULL || value == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	if (!sjme_chunkCheckBound(chunk, off, 4, error))
		return sjme_false;
	
	*value = sjme_memReadBigInt(chunk->data, off);
	return sjme_true;
}

sjme_jboolean sjme_chunkReadBigShort(const sjme_memChunk* chunk, sjme_jint off,
	sjme_jshort* value, sjme_error* error)
{
	/* Check. */
	if (chunk == NULL || value == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	if (!sjme_chunkCheckBound(chunk, off, 2, error))
		return sjme_false;
	
	*value = sjme_memReadBigShort(chunk->data, off);
	return sjme_true;
}

sjme_jboolean sjme_chunkRealPointer(const sjme_memChunk* chunk, sjme_jint off,
	void** outPointer, sjme_error* error)
{
	if (chunk == NULL || outPointer == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Out of bounds? */
	if (off < 0 || off > chunk->size)
		return sjme_setErrorF(error, SJME_ERROR_OUT_OF_BOUNDS, off);
	
	/* Use the resultant pointer. */
	*outPointer = (void*)(((uintptr_t)chunk->data) + off);
	return sjme_true;
}

sjme_jboolean sjme_chunkSubChunk(const sjme_memChunk* chunk,
	sjme_memChunk* outSubChunk, sjme_jint off, sjme_jint size,
	sjme_error* error)
{
	void* splicePoint;
	
	if (chunk == NULL || outSubChunk == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Out of bounds? */
	if (off < 0 || size < 0 || off + size > chunk->size)
		return sjme_setErrorF(error, SJME_ERROR_OUT_OF_BOUNDS, off);
	
	/* Determine the splice point. */
	splicePoint = NULL;
	if (!sjme_chunkRealPointer(chunk, off, &splicePoint, error))
		return sjme_false;
	 
	/* Subsection this chunk. */
	outSubChunk->data = splicePoint;
	outSubChunk->size = size;
	
	return sjme_true;
}
