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

sjme_attrUnused RETRO_API unsigned retro_api_version(void)
{
	return RETRO_API_VERSION;
}

sjme_attrUnused RETRO_API void retro_deinit(void)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API void retro_init(void)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API bool retro_load_game(
	const struct retro_game_info *game)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API bool retro_load_game_special(unsigned game_type,
	const struct retro_game_info *info, size_t num_info)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API void retro_reset(void)
{
	sjme_todo("Implement this?");
}

sjme_attrUnused RETRO_API void retro_unload_game(void)
{
	sjme_todo("Implement this?");
}

