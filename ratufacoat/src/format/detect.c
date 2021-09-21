/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/detect.h"
#include "debug.h"
#include "memops.h"

sjme_jboolean sjme_detectMagicNumber(const void* data, sjme_jint size,
	sjme_jint wantMagic, sjme_error* error)
{
	sjme_jint val;
	
	/* Check parameters first. */
	if (data == NULL || size < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, 0);
		return sjme_false;
	}
	
	/* Too small to read from? */
	if (size < 4)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, size);
		return sjme_false;
	}
	
	/* Is this the magic for this format? */
	val = sjme_memReadBigInt(data, 0);
	if (val == wantMagic)
		return sjme_true;
	
	return sjme_false;
}

sjme_jboolean sjme_detectFormat(const void* data, sjme_jint size,
	const void** outDriver, const void (**choices), sjme_jint offsetOfDetect,
	sjme_error* error)
{
	int i;
	const void* possible;
	sjme_formatDetectFunction detectFunction;
	
	/* Check arguments. */
	if (data == NULL || size <= 0 || outDriver == NULL || choices == NULL ||
		offsetOfDetect < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, 0);
		return sjme_false;
	}
	
	/* Scan through all of the drivers to find the one that matches. */
	for (i = 0;; i++)
	{
		/* Still looking? */
		possible = choices[i];
		if (possible == NULL)
			break;
		
		/* Load detection function. */
		detectFunction = sjme_unoffsetof(sjme_formatDetectFunction,
			offsetOfDetect, possible);
		if (detectFunction == NULL)
			continue;
		
		/* Is this the matching driver? */
		if (detectFunction(data, size, error))
		{
			*outDriver = possible; 
			return sjme_true;
		}
	}
	
	/* Not found. */
	sjme_setError(error, SJME_ERROR_DRIVER_NOT_FOUND, 0);
	return sjme_false;
}
