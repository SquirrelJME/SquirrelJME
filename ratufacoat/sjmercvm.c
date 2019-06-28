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
sjme_vmemmap* sjme_vmmmap(sjme_vmem* vmem, sjme_jint at, void* ptr,
	sjme_jint size, sjme_error* error)
{
	sjme_vmemmap* rv;
	sjme_vmemmap** newmaps;
	sjme_jint i;
	
	(void)at;
	
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

/** Resolves the given true memory address. */
void* sjme_vmmresolve(sjme_vmem* vmem, sjme_vmemptr ptr, sjme_jint off,
	sjme_error* error)
{
	sjme_jint i, n;
	sjme_vmemmap* map;
	sjme_vmemptr optr;
	
	/* Invalid argument. */
	if (vmem == NULL)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return NULL;
	}
	
	/* Get true pointer position. */
	optr = ptr + off;
	
	/* Search for the mapping. */
	for (i = 0, n = vmem->count; i < n; i++)
	{
		/* Get mapping. */
		map = vmem->maps[i];
		
		/* Is in range? */
		if (optr >= map->fakeptr && optr < map->fakeptr + map->size)
			return (void*)(map->realptr + (optr - map->fakeptr));
	}
	
	/* Address is not valid. */
	sjme_seterror(error, SJME_ERROR_BADADDRESS, optr);
	return NULL;
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
	void* realptr;
	sjme_error xerror;
	
	/* Invalid argument? */
	if (vmem == NULL)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return 0;
	}
	
	/* Need error. */
	if (error == NULL)
	{
		memset(&xerror, 0, sizeof(xerror));
		error = &xerror;
	}
	
	/* Resolve address. */
	realptr = sjme_vmmresolve(vmem, ptr, off, error);
	if (realptr == NULL)
	{
		if (error->code == SJME_ERROR_NONE)
			sjme_seterror(error, SJME_ERROR_ADDRRESFAIL, ptr + off);
		
		return 0;
	}
	
	/* Depends on the type. */
	switch (type)
	{
		case SJME_VMMTYPE_BYTE:
			return *((sjme_jbyte*)realptr);

#if defined(SJME_BIG_ENDIAN)
		case SJME_VMMTYPE_SHORT:
		case SJME_VMMTYPE_JAVASHORT:
			return *((sjme_jshort*)realptr);
#else
		case SJME_VMMTYPE_SHORT:
			return *((sjme_jshort*)realptr);
			
		case SJME_VMMTYPE_JAVASHORT:
			type = *((sjme_jshort*)realptr);
			type = (type & SJME_JINT_C(0xFFFF0000)) |
				(((type << SJME_JINT_C(8)) & SJME_JINT_C(0xFF00)) |
				((type >> SJME_JINT_C(8)) & SJME_JINT_C(0x00FF)));
			return type;
#endif

#if defined(SJME_BIG_ENDIAN)
		case SJME_VMMTYPE_INTEGER:
		case SJME_VMMTYPE_JAVAINTEGER:
			return *((sjme_jint*)realptr);
#else
		case SJME_VMMTYPE_INTEGER:
			return *((sjme_jint*)realptr);
		
		case SJME_VMMTYPE_JAVAINTEGER:
			type = *((sjme_jint*)realptr);
			type = (((type >> SJME_JINT_C(24)) & SJME_JINT_C(0x000000FF)) |
				((type >> SJME_JINT_C(8)) & SJME_JINT_C(0x0000FF00)) |
				((type << SJME_JINT_C(8)) & SJME_JINT_C(0x00FF0000)) |
				((type << SJME_JINT_C(24)) & SJME_JINT_C(0xFF000000)));
			return type;
#endif
			
		default:
			sjme_seterror(error, SJME_ERROR_INVALIDMEMTYPE, type);
			return 0;
	}
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
	void* realptr;
	sjme_error xerror;
	
	/* Invalid argument? */
	if (vmem == NULL)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return;
	}
	
	/* Need error. */
	if (error == NULL)
	{
		memset(&xerror, 0, sizeof(xerror));
		error = &xerror;
	}
	
	/* Resolve address. */
	realptr = sjme_vmmresolve(vmem, ptr, off, error);
	if (realptr == NULL)
	{
		if (error->code == SJME_ERROR_NONE)
			sjme_seterror(error, SJME_ERROR_ADDRRESFAIL, ptr + off);
		
		return;
	}
	
	/* Depends on the type. */
	switch (type)
	{
		case SJME_VMMTYPE_BYTE:
			*((sjme_jbyte*)realptr) = (sjme_jbyte)val;
			return;
		
		case SJME_VMMTYPE_JAVASHORT:
#if defined(SJME_LITTLE_ENDIAN)
			val = (val & SJME_JINT_C(0xFFFF0000)) |
				(((val << SJME_JINT_C(8)) & SJME_JINT_C(0xFF00)) |
				((val >> SJME_JINT_C(8)) & SJME_JINT_C(0x00FF)));
#endif
		case SJME_VMMTYPE_SHORT:
			*((sjme_jshort*)realptr) = (sjme_jshort)val;
			return;
		
		case SJME_VMMTYPE_JAVAINTEGER:
#if defined(SJME_LITTLE_ENDIAN)
			val = (((val >> SJME_JINT_C(24)) & SJME_JINT_C(0x000000FF)) |
				((val >> SJME_JINT_C(8)) & SJME_JINT_C(0x0000FF00)) |
				((val << SJME_JINT_C(8)) & SJME_JINT_C(0x00FF0000)) |
				((val << SJME_JINT_C(24)) & SJME_JINT_C(0xFF000000)));
#endif
		case SJME_VMMTYPE_INTEGER:
			*((sjme_jint*)realptr) = (sjme_jint)val;
			return;
			
		default:
			sjme_seterror(error, SJME_ERROR_INVALIDMEMTYPE, type);
			return;
	}
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
