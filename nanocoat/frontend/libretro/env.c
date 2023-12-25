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

/** Variables to use within the core. */
static const struct retro_variable sjme_libretro_coreVariables[] =
{
#if defined(SJME_CONFIG_DEBUG) && defined(SJME_CONFIG_UNIT_TEST)
	/* Run unit tests? */
	{
		SJME_LIBRETRO_CONFIG_UNIT_TESTS,
		"Run Unit Tests; false|true"
	},
#endif

	/* End. */
	{NULL, NULL}
};

static sjme_jboolean sjme_libretro_exitHandler(int exitCode)
{
	/* If there is no environment callback, then do nothing here. */
	if (sjme_libretro_envCallback == NULL)
		return SJME_JNI_FALSE;

	/* Tell the front end to stop the core. */
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SHUTDOWN, NULL);
	return SJME_JNI_TRUE;
}

static sjme_jboolean sjme_libretro_abortHandler(void)
{
	/* Forward to the exit handler. */
	return sjme_libretro_exitHandler(1);
}

sjme_attrUnused RETRO_API void retro_cheat_reset(void)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
}

sjme_attrUnused RETRO_API void retro_cheat_set(unsigned index, bool enabled,
	const char *code)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
}

sjme_attrUnused RETRO_API unsigned retro_get_region(void)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
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

	/* Set abort and exit handler. */
	sjme_debug_abortHandler = sjme_libretro_abortHandler;
	sjme_debug_exitHandler = sjme_libretro_exitHandler;

	/* Control input. */
	retro_set_controller_port_device(0, 0);
	
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

	/* Core options. */
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_VARIABLES,
		(void*)&sjme_libretro_coreVariables);
}
