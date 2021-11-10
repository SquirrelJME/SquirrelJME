/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "memchunk.h"
#include "memops.h"

sjme_jboolean sjme_chunkCheckBound(const sjme_memChunk* chunk, sjme_jint off,
	sjme_jint len, sjme_error* error)
{
	/* Check. */
	if (chunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Check the bounds. */
	if (off < 0 || len < 0 || (off + len) < 0 || (off + len) > chunk->size)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, 0);
		return sjme_false;
	}
		
	return sjme_true;
}

sjme_jboolean sjme_chunkReadBigInt(const sjme_memChunk* chunk, sjme_jint off,
	sjme_jint* value, sjme_error* error)
{
	/* Check. */
	if (chunk == NULL || value == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
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
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	if (!sjme_chunkCheckBound(chunk, off, 2, error))
		return sjme_false;
	
	*value = sjme_memReadBigShort(chunk->data, off);
	return sjme_true;
}

sjme_jboolean sjme_chunkSubChunk(const sjme_memChunk* chunk,
	sjme_memChunk* outSubChunk, sjme_jint off, sjme_jint size,
	sjme_error* error)
{
	if (chunk == NULL || outSubChunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Out of bounds? */
	if (off < 0 || size < 0 || off + size > chunk->size)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, off);
		
		return sjme_false;
	}
	
	/* Subsection this chunk. */
	outSubChunk->data = (void*)(((uintptr_t)chunk->data) + off);
	outSubChunk->size = size;
	
	return sjme_true;
}
