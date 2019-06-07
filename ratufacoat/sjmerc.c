/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat Source.
 *
 * @since 2019/06/02
 */

#include "sjmerc.h"

/**
 * Allocates the given number of bytes.
 *
 * @param size The number of bytes to allocate.
 * @since 2019/06/07
 */
void* sjme_malloc(sjme_jint size)
{
	void* rv;
	
	/* These will never allocate. */
	if (size <= 0)
		return NULL;
	
	/** Round size and include extra 4-bytes for size storage. */
	size = ((size + SJME_JINT_C(3)) & (~SJME_JINT_C(3))) + SJME_JINT_C(4);
	
#if defined(SJME_IS_LINUX) && SJME_BITS > 32
	/* Use memory map on Linux so it is forced to a 32-bit pointer. */
	rv = mmap(NULL, size, PROT_READ | PROT_WRITE,
		MAP_PRIVATE | MAP_ANONYMOUS | MAP_32BIT, -1, 0);
	if (rv == MAP_FAILED)
		return NULL;
#else
	/* Use standard C function otherwise. */
	rv = calloc(1, len);
#endif

	/* Did not allocate? */
	if (rv == NULL)
		return NULL;
	
	/* Store the size into this memory block for later free. */
	*((sjme_jint*)rv) = size;
	
	/* Return the adjusted pointer. */
	return SJME_JINT_TO_POINTER(SJME_POINTER_TO_JINT(rv) + SJME_JINT_C(4));
}

/** Executes code running within the JVM. */
int sjme_jvmexec(sjme_jvm* jvm)
{
	if (jvm == NULL)
		return 0;
	
	return 0;
}

/** Creates a new instance of the JVM. */
sjme_jvm* sjme_jvmnew(sjme_jvmoptions* options, sjme_nativefuncs* nativefuncs)
{
	sjme_jvmoptions nulloptions;
	
	/* We need native functions. */
	if (nativefuncs == NULL)
		return NULL;
	
	/* If there were no options specified, just use a null set. */
	if (options == NULL)
	{
		memset(&nulloptions, 0, sizeof(nulloptions));
		options = &nulloptions;
	}
}
