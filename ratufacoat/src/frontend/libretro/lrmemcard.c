/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libretro/lrlocal.h"
#include "debug.h"

SJME_EXTERN_C SJME_GCC_USED void* retro_get_memory_data(unsigned id)
{
	sjme_message("Get memory data?");
	return NULL;
}

SJME_EXTERN_C SJME_GCC_USED size_t retro_get_memory_size(unsigned id)
{
	sjme_message("TODO: Get memory size?");
	return 0;
}
