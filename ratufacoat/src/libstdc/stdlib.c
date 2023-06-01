/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdlib.h>
#include <string.h>
#include "squirreljme/memoryimpl.h"

void* calloc(size_t numElems, size_t elemSize)
{
	void* result;
	size_t actualSize;

	/* Calculate actual size. */
	actualSize = numElems * elemSize;

	/* Try to allocate it. */
	result = malloc(actualSize);
	if (result == NULL)
		return NULL;

	/* Wipe initial memory. */
	memset(result, 0, actualSize);

	/* Use whatever result of it. */
	return result;
}

void exit(int status)
{
	sjme_stdcExit(status);
}

void free(void* ptr)
{
	sjme_stdcFree(ptr);
}

void* malloc(size_t size)
{
	return sjme_stdcMalloc(size);
}
