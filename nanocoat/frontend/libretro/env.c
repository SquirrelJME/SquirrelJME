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
	sjme_attrInNotNull struct retro_system_info* info)
{
	/* Base information. */
	info->library_name = "SquirrelJME";
	info->library_version = SQUIRRELJME_VERSION;
	
	/* These are all of the Java related extensions. */
	info->valid_extensions = "zip|jam|jar|jad|adf|sec|sto|kjx";
	
	/* We need the full path for executables. */
	info->need_fullpath = true;

	/* RetroArch is not permitted to unzip entries for us. */
	info->block_extract = true;
}

sjme_attrUnused RETRO_API void retro_set_environment(
	retro_environment_t environment)
{
	bool supportsNoGame;
	enum retro_pixel_format pixelFormat;
	
	/* Store the callback pointer. */
	sjme_libretro_envCallback = environment;
	
	/* Playing with no software is supported. */
	supportsNoGame = true;
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_SUPPORT_NO_GAME,
		(void*)&supportsNoGame);
	
	/* Declare extension interface for special functions. */
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_PROC_ADDRESS_CALLBACK,
		(void*)sjme_libretro_extInterface());
	
	/* Pixel format. */
	pixelFormat = RETRO_PIXEL_FORMAT_XRGB8888;
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_PIXEL_FORMAT,
		(void*)&pixelFormat);
}
