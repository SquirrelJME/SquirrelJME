/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include "frontend/ieee1275/boot.h"
#include "libstdc/squirreljme/memoryimpl.h"

void sjme_stdcExit(int status)
{
	/* Forward to exit call. */
	sjme_ieee1275Exit();
}

void sjme_stdcFree(void* ptr)
{
}

void* sjme_stdcMalloc(size_t size)
{
	return NULL;
}
