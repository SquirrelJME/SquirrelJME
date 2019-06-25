/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Native memory functions.
 *
 * @since 2019/06/25
 */

#include "sjmerc.h"

/** Allocates the given number of bytes. */
void* sjme_malloc(sjme_jint size)
{
	void* rv;
	
	/* These will never allocate. */
	if (size <= 0)
		return NULL;
	
	/* Round size and include extra 4-bytes for size storage. */
	size = ((size + SJME_JINT_C(3)) & (~SJME_JINT_C(3))) + SJME_JINT_C(4);
	
	/* Exceeds maximum permitted allocation size? */
	if (sizeof(sjme_jint) > sizeof(size_t) && size > (sjme_jint)SIZE_MAX)
		return NULL;
	
	/* Use standard C function otherwise. */
	rv = calloc(1, size);
	
	/* Address out of range? Only when virtual pointers are not used! */
#if !defined(SJME_VIRTUAL_MEM)
	if (rv != NULL && ((uintptr_t)rv) > (uintptr_t)UINT32_C(0xFFFFFFFF))
	{
		free(rv);
		return NULL;
	}
#endif

	/* Did not allocate? */
	if (rv == NULL)
		return NULL;
	
	/* Store the size into this memory block for later free. */
	*((sjme_jint*)rv) = size;
	
	/* Return the adjusted pointer. */
	return SJME_POINTER_OFFSET_LONG(rv, 4);
}

/** Frees the given pointer. */
void sjme_free(void* p)
{
	void* basep;
	
	/* Ignore null pointers. */
	if (p == NULL)
		return;
	
	/* Base pointer which is size shifted. */
	basep = SJME_POINTER_OFFSET_LONG(p, -4);
	
	/* Use Standard C free. */
	free(basep);
}
