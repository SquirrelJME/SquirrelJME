/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
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

#include <sys/mman.h>

#include "ratufac.h"

/** Allocates a pointer in the encoded address space. */
ratufacoat_pointer_t ratufacoat_memalloc(size_t len)
{
#if defined(RATUFACOAT_32BIT)
	// No need to do special pointer stuff
	return (uintptr_t)calloc(1, len);

#elif defined(__linux__)
	// Use MMap in Linux to map to this space
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
		return 0;
	
	// Check to ensure the pointer is in range
	usp = (uintptr_t)sp;
	vsp = (uint32_t)usp;
	if (usp != vsp)
	{
		munmap(sp, vlen);
		return 0;
	}
	
	// Encode vlen
	*((uint32_t*)sp) = vlen;
	
	// Return mapped pointer
	return vsp + 4;
#else
	ratufacoat_todo();
	return 0;
#endif
}

/** Frees a pointer in the encoded address space. */
void ratufacoat_memfree(ratufacoat_pointer_t vp)
{
#if defined(RATUFACOAT_32BIT)
	// Do nothing for NULL pointers
	if (vp == 0)
		return;
	
	free((void*)((uintptr_t)p));
#elif defined(__linux__)
	void* xp;
	size_t len;
	
	// Do nothing for NULL pointers
	if (vp == 0)
		return;
	
	// Get the true base pointer
	xp = (void*)((uintptr_t)(vp - 4));
	
	// Read mapping length
	len = *((uint32_t*)(((uintptr_t)vp) - 4));
	
	// Unmap
	munmap(xp, len);
#else
	ratufacoat_todo();
	return;
#endif
}

/** Returns the real memory pointer for the given encoded pointer. */
void* ratufacoat_memrealptr(ratufacoat_pointer_t vp)
{
	// Null pointers get mapped to NULL
	if (vp == 0)
		return NULL;
	
#if defined(RATUFACOAT_32BIT) || defined(__linux__)
	return (void*)((uintptr_t)vp);
#else
	ratufacoat_todo();
	return NULL;
#endif
}
