/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "3rdparty/libretro/libretro.h"
#include "frontend/libretro/shared.h"

static retro_proc_address_t RETRO_CALLCONV sjme_libretro_extLookup(
	sjme_lpcstr sym)
{
	return NULL;
}

const struct retro_get_proc_address_interface sjme_libretro_extInterfaceDef =
{
	.get_proc_address = sjme_libretro_extLookup,
};

const struct retro_get_proc_address_interface* sjme_libretro_extInterface(void)
{
	return &sjme_libretro_extInterfaceDef;
}
