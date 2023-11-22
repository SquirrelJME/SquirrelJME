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

sjme_attrUnused RETRO_API void retro_cheat_reset(void)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API void retro_cheat_set(unsigned index, bool enabled,
	const char *code)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API unsigned retro_get_region(void)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API void retro_get_system_info(
	struct retro_system_info *info)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API void retro_set_environment(
	retro_environment_t environment)
{
	sjme_todo("Implement this?");
}
