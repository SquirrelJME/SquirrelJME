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

sjme_attrUnused RETRO_API void retro_run(void)
{
	uint32_t buf[240*320];
	int i;
	
	static jint trigger;
	if (!(trigger++))
		sjme_message("Implement this?");
	
	for (i = 0; i < 240; i++)
		buf[i] = rand();
	sjme_libretro_videoRefreshCallback(
		buf, 240, 320, 240);
}
