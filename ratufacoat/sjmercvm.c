/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Virtual memory functions.
 *
 * @since 2019/06/25
 */

#include "sjmerc.h"

/** Virtual memory information. */
struct sjme_vmem
{
	/** The number of mappings. */
	sjme_jint count;
};

/** Creates a new virtual memory manager. */
sjme_vmem* sjme_vmmnew(sjme_error* error)
{
	sjme_vmem* rv;
	
	/* Try to allocate space. */
	rv = sjme_malloc(sizeof(*rv));
	if (rv == NULL)
	{
		sjme_seterror(error, SJME_ERROR_NOMEMORY, sizeof(*rv));
		
		return NULL;
	}
	
	return rv;
}

/** Virtually maps the given region of memory. */
sjme_vmemmap* sjme_vmmmap(sjme_vmem* vmem, void* ptr, sjme_jint size,
	sjme_error* error)
{
	/* Invalid argument. */
	if (vmem == NULL || ptr == NULL || size <= 0)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return NULL;
	}
	
	abort();
}

/** Convert size to Java type. */
sjme_jint sjme_vmmsizetojavatype(sjme_jint size, sjme_error* error)
{
	abort();
}

/** Convert size to type. */
sjme_jint sjme_vmmsizetotype(sjme_jint size, sjme_error* error)
{
	abort();
}

/** Reads from virtual memory. */
sjme_jint sjme_vmmread(sjme_vmem* vmem, sjme_jint type, sjme_vmemptr ptr,
	sjme_jint off, sjme_error* error)
{
	abort();
}

/** Reads from virtual memory. */
sjme_jint sjme_vmmreadp(sjme_vmem* vmem, sjme_jint type, sjme_vmemptr* ptr,
	sjme_error* error)
{
	abort();
}

/** Write to virtual memory. */
void sjme_vmmwrite(sjme_vmem* vmem, sjme_jint type, sjme_vmemptr ptr,
	sjme_jint off, sjme_jint val, sjme_error* error)
{
	abort();
}

/** Write to virtual memory. */
void sjme_vmmwritep(sjme_vmem* vmem, sjme_jint type, sjme_vmemptr* ptr,
	sjme_jint val, sjme_error* error)
{
	abort();
}

/** Atomically increments and integer and then gets its value. */
sjme_jint sjme_vmmatomicintaddandget(sjme_vmem* vmem,
	sjme_vmemptr ptr, sjme_jint off, sjme_jint add, sjme_error* error)
{
	abort();
}
