/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * This file contains the memory management functions.
 *
 * @since 2019/05/31
 */

#include "ratufac.h"

#if defined(RATUFACOAT_ISLINUX)
	// If this is a 64-bit system then use mmap for 32-bit addresses
	#if __WORDSIZE == 64
		#define RATUFACOAT_USELINUXMMAP 1
	#endif
	
	// Header with memory map
	#include <sys/mman.h>
#endif

/** Allocates a pointer in the low 4GiB of memory for 32-bit pointer usage. */
void* ratufacoat_memalloc(size_t len)
{
#if defined(RATUFACOAT_USELINUXMMAP)
	// Use Linux mmap method to allocate in the low address space
	void* sp;
	size_t vlen;
	uintptr_t usp;
	uint32_t vsp;
	
	// Virtual length since the mapping length needs to be included
	vlen = len + 4;
	
	// MMap 32-bit pointer
	sp = mmap(NULL, vlen, PROT_READ | PROT_WRITE,
		MAP_PRIVATE | MAP_ANONYMOUS | MAP_32BIT, -1, 0);
	if (sp == MAP_FAILED)
		return NULL;
	
	// Check to ensure the pointer is in range
	usp = (uintptr_t)sp;
	if (usp > (uintptr_t)UINT32_MAX)
	{
		munmap(sp, vlen);
		return NULL;
	}
	
	// Encode vlen
	*((uint32_t*)sp) = vlen;
	
	// Return mapped pointer
	return (void*)(usp + 4);
#else
	// Use standard C function
	void* p;
	uintptr_t up;
	
	// Allocate memory
	p = calloc(1, len);
	if (p == NULL)
		return NULL;
	
	// Check to ensure the pointer is in range
	up = (uintptr_t)p;
	if (up > (uintptr_t)UINT32_MAX)
	{
		free(p);
		return NULL;
	}
	
	// Use this pointer
	return p;
#endif
}

/** Frees a pointer which was previously allocated with ratufacoat_memalloc. */
void ratufacoat_memfree(void* p)
{
#if defined(RATUFACOAT_USELINUXMMAP)
	void* xp;
#endif
	
	// Do nothing on NULL pointers
	if (p == NULL)
		return;
	
#if defined(RATUFACOAT_USELINUXMMAP)
	// Get the true base pointer
	xp = (void*)(((uintptr_t)p) - 4);
	
	// Unmap, be sure to use the base pointer!
	munmap(xp, *(((uint32_t*)xp)));
#else
	// Use standard C free
	free(p);
#endif
}

/** Reads a Java byte from memory. */
int8_t ratufacoat_memreadjbyte(void* p, int32_t o)
{
	uint8_t* rp;
	
	// Determine real pointer to use
	rp = (uint8_t*)(((uintptr_t)p) + o);
	
	// Read from memory
	return ((int8_t)(rp[0] & 0xFF));
}

/** Reads a Java int from memory. */
int32_t ratufacoat_memreadjint(void* p, int32_t o)
{
	uint8_t* rp;
	
	// Determine real pointer to use
	rp = (uint8_t*)(((uintptr_t)p) + o);
	
	// Read from memory
	return (int32_t)(((((uint32_t)rp[0]) & 0xFF) << 24) |
		((((uint32_t)rp[1]) & 0xFF) << 16) |
		((((uint32_t)rp[2]) & 0xFF) << 8) |
		((((uint32_t)rp[3]) & 0xFF)));
}

/** Reads a Java short from memory. */
int16_t ratufacoat_memreadjshort(void* p, int32_t o)
{
	uint8_t* rp;
	
	// Determine real pointer to use
	rp = (uint8_t*)(((uintptr_t)p) + o);
	
	// Read from memory
	return (int16_t)(((((uint32_t)rp[0]) & 0xFF) << 8) |
		(((uint32_t)rp[1]) & 0xFF));
}
