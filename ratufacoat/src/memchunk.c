/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "memchunk.h"
#include "memops.h"

sjme_jboolean sjme_chunkCheckBound(sjme_memChunk* chunk, sjme_jint off,
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

sjme_jboolean sjme_chunkReadBigShort(sjme_memChunk* chunk, sjme_jint off,
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
