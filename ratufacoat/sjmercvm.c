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

/** Base of virtual memory. */
#define SJME_VIRTUAL_MEM_BASE SJME_JINT_C(1048576)

/** Rounding of virtual memory. */
#define SJME_VIRTUAL_MEM_MASK SJME_JINT_C(1023)

/** Virtual memory information. */
struct sjme_vmem
{
	/** The number of mappings. */
	sjme_jint count;
	
	/** The next address for allocations. */
	sjme_jint nextaddr;
	
	/** Mappings. */
	sjme_vmemmap** maps;
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
	
	/* Initialize. */
	rv->nextaddr = SJME_VIRTUAL_MEM_BASE;
	
	return rv;
}

/** Virtually maps the given region of memory. */
sjme_vmemmap* sjme_vmmmap(sjme_vmem* vmem, void* ptr, sjme_jint size,
	sjme_error* error)
{
	sjme_vmemmap* rv;
	sjme_vmemmap** newmaps;
	sjme_jint i;
	
	/* Invalid argument. */
	if (vmem == NULL || ptr == NULL || size <= 0)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return NULL;
	}
	
	/* Allocate return value. */
	rv = sjme_malloc(sizeof(*rv));
	newmaps = sjme_malloc(sizeof(*newmaps) * (vmem->count + 1));
	if (rv == NULL)
	{
		sjme_seterror(error, SJME_ERROR_NOMEMORY, sizeof(*rv));
		
		sjme_free(rv);
		sjme_free(newmaps);
		
		return NULL;
	}
	
	/* Copy and set new mappings. */
	for (i = 0; i < vmem->count; i++)
		newmaps[i] = vmem->maps[i];
	newmaps[vmem->count] = rv;
	
	/* Setup mapping. */
	rv->realptr = (uintptr_t)ptr;
	rv->fakeptr = vmem->nextaddr;
	rv->size = size;
	
	/* Store the mappings (remember to free the old ones!). */
	vmem->nextaddr = (vmem->nextaddr + size + SJME_VIRTUAL_MEM_MASK) &
		(~SJME_VIRTUAL_MEM_MASK);
	vmem->count = vmem->count + 1;
	sjme_free(vmem->maps);
	vmem->maps = newmaps;
	
	return rv;
}

/** Convert size to Java type. */
sjme_jint sjme_vmmsizetojavatype(sjme_jint size, sjme_error* error)
{
	/* Convert. */
	switch (size)
	{
		case 1:
			return SJME_VMMTYPE_BYTE;
			
		case 2:
			return SJME_VMMTYPE_JAVASHORT;
			
		case 4:
			return SJME_VMMTYPE_JAVAINTEGER;
	}
	
	/* Not valid. */
	sjme_seterror(error, SJME_ERROR_INVALIDSIZE, size);
	return 0;
}

/** Convert size to type. */
sjme_jint sjme_vmmsizetotype(sjme_jint size, sjme_error* error)
{
	/* Convert. */
	switch (size)
	{
		case 1:
			return SJME_VMMTYPE_BYTE;
			
		case 2:
			return SJME_VMMTYPE_SHORT;
			
		case 4:
			return SJME_VMMTYPE_INTEGER;
	}
	
	/* Not valid. */
	sjme_seterror(error, SJME_ERROR_INVALIDSIZE, size);
	return 0;
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
	sjme_jint rv;
	
	/* Read value. */
	rv = sjme_vmmread(vmem, type, *ptr, 0, error);
	
	/* Increment pointer. */
	switch (type)
	{
		case SJME_VMMTYPE_BYTE:
			*ptr = *ptr + 1;
			break;
			
		case SJME_VMMTYPE_SHORT:
		case SJME_VMMTYPE_JAVASHORT:
			*ptr = *ptr + 2;
			break;
			
		case SJME_VMMTYPE_INTEGER:
		case SJME_VMMTYPE_JAVAINTEGER:
			*ptr = *ptr + 4;
			break;
	}
	
	/* Return value. */
	return rv;
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
	sjme_jint rv;
	
	/* Write value. */
	sjme_vmmwrite(vmem, type, *ptr, 0, val, error);
	
	/* Increment pointer. */
	switch (type)
	{
		case SJME_VMMTYPE_BYTE:
			*ptr = *ptr + 1;
			break;
			
		case SJME_VMMTYPE_SHORT:
		case SJME_VMMTYPE_JAVASHORT:
			*ptr = *ptr + 2;
			break;
			
		case SJME_VMMTYPE_INTEGER:
		case SJME_VMMTYPE_JAVAINTEGER:
			*ptr = *ptr + 4;
			break;
	}
}

/** Atomically increments and integer and then gets its value. */
sjme_jint sjme_vmmatomicintaddandget(sjme_vmem* vmem,
	sjme_vmemptr ptr, sjme_jint off, sjme_jint add, sjme_error* error)
{
	sjme_jint rv;
	
	/* Read current value. */
	rv = sjme_vmmread(vmem, SJME_VMMTYPE_INTEGER, ptr, off, error);
	
	/* Add value. */
	rv += add;
	
	/* Store new value. */
	sjme_vmmwrite(vmem, SJME_VMMTYPE_INTEGER, ptr, off, rv, error);
	
	/* Return new value. */
	return rv;
}
