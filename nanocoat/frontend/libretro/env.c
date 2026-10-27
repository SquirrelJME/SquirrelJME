/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <libretro.h>

#include "sjme/nvm/nvm.h"
#include "sjme/debug.h"
#include "frontend/libretro/shared.h"
#include "sjme/util.h"

/** Variables to use within the core. */
static const struct retro_variable sjme_libretro_coreVariables[] =
{
#if defined(SJME_CONFIG_DEBUG) && defined(SJME_CONFIG_UNIT_TEST)
	/* Run unit tests? */
	{
		SJME_LIBRETRO_CORE_OPTION_UNIT_TESTS_NAME,
		"Run Unit Tests; false|true",
	},
#endif
	
	{
		SJME_LIBRETRO_CORE_OPTION_DUMP_VARS_NAME,
		"Dump Variables (Hello Demo); false|true",
	},
	
	{
		SJME_LIBRETRO_CORE_OPTION_TIC_RATE_NAME,
		"Clock; 491520|122880|245760|983040|1966080|3932160"
	},

	/* End. */
	{NULL, NULL}
};

/** Whether variables are to be displayed or not. */
static const struct retro_core_option_display
	sjme_libretro_coreVariablesDisplay[] =
{
#if defined(SJME_CONFIG_DEBUG) && defined(SJME_CONFIG_UNIT_TEST)
	/* Run unit tests? */
	{
		SJME_LIBRETRO_CORE_OPTION_UNIT_TESTS_NAME,
		SJME_JNI_FALSE,
	},
#endif
	
	{
		SJME_LIBRETRO_CORE_OPTION_DUMP_VARS_NAME,
		SJME_JNI_FALSE,
	},
	
	{
		SJME_LIBRETRO_CORE_OPTION_TIC_RATE_NAME,
		SJME_JNI_TRUE,
	},
	
	/* End. */
	{NULL, false}
};

sjme_lpcstr sjme_libretro_optNames[SJME_LIBRETRO_NUM_CORE_OPTIONS] =
{
	"sjme_undefined",
	SJME_LIBRETRO_CORE_OPTION_UNIT_TESTS_NAME,
	SJME_LIBRETRO_CORE_OPTION_DUMP_VARS_NAME,
	SJME_LIBRETRO_CORE_OPTION_TIC_RATE_NAME,
};

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
	
	/* These are all the Java related extensions. */
	info->valid_extensions = "jar|sqc|jam|jad|kjx|adf|sec|sto|sp|"
		"sp0|sp1|sp2|sp3|sp4|sp5|sp6|sp7|sp8|sp9";
	
	/* Everything is handled by the VFS wrapper, so we do not need */
	/* native OS support here. */
	info->need_fullpath = false;

	/* RetroArch is not permitted to unzip entries for us. */
	info->block_extract = true;
}

sjme_attrUnused RETRO_API void retro_set_environment(
	retro_environment_t environment)
{
	bool supportsNoGame;
	enum retro_pixel_format pixelFormat;
	
	/* Store the callback pointer. */
	sjme_libretro_globals.envCallback = environment;

	/* Control input. */
	retro_set_controller_port_device(0, 0);
	
	/* Playing with no software is supported. */
	supportsNoGame = true;
	sjme_libretro_globals.envCallback(
		RETRO_ENVIRONMENT_SET_SUPPORT_NO_GAME,
		(void*)&supportsNoGame);
	
	/* Declare extension interface for special functions. */
	sjme_libretro_globals.envCallback(
		RETRO_ENVIRONMENT_SET_PROC_ADDRESS_CALLBACK,
		(void*)sjme_libretro_extInterface());
	
	/* Pixel format. */
	pixelFormat = RETRO_PIXEL_FORMAT_XRGB8888;
	sjme_libretro_globals.envCallback(
		RETRO_ENVIRONMENT_SET_PIXEL_FORMAT,
		(void*)&pixelFormat);

	/* Core options. */
	sjme_libretro_globals.envCallback(
		RETRO_ENVIRONMENT_SET_VARIABLES,
		(void*)&sjme_libretro_coreVariables);
	sjme_libretro_globals.envCallback(
		RETRO_ENVIRONMENT_SET_CORE_OPTIONS_DISPLAY,
		(void*)&sjme_libretro_coreVariablesDisplay);

	/* Lookup VFS interface. */
	sjme_libretro_globals.vfs.required_interface_version = 1;
	if (!sjme_libretro_globals.envCallback(
		RETRO_ENVIRONMENT_GET_VFS_INTERFACE,
		(void*)&sjme_libretro_globals.vfs))
		sjme_libretro_globals.vfs.iface = NULL;
}

void sjme_libretro_refreshCoreOptions(void)
{
	sjme_jint i;
	sjme_libretro_coreOptionValue* option;
	struct retro_variable retroVar;
	
	/* Go through and get each option. */
	for (i = 0; i < SJME_LIBRETRO_NUM_CORE_OPTIONS; i++)
	{
		/* Reset option to nothing. */
		option = &sjme_libretro_globals.options[i];
		memset(option, 0, sizeof(*option));
		
		/* We want this specific variable. */
		memset(&retroVar, 0, sizeof(retroVar));
		retroVar.key = sjme_libretro_optNames[i];
		
		/* Read in the value, we only care if not-null. */
		if (sjme_libretro_globals.envCallback(
			RETRO_ENVIRONMENT_GET_VARIABLE,
			(void*)&retroVar))
			if (retroVar.value != NULL)
			{
				option[i].string = retroVar.value;
				option[i].number = sjme_lpcstoimax(
					retroVar.value, NULL, 10);
			}
	}
}
