/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdint.h>
#include <string.h>

#if defined(SJME_SYSTEM_IEEE1275)
/* ------------------------------------------------------------------------- */

void* memset(void* dest, int value, size_t size)
{
	size_t i;

	for (i = 0; i < size; i++)
		((uint8_t*)dest)[i] = value;

	return dest;
}

/* ------------------------------------------------------------------------- */
#endif