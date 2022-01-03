/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "builtin.h"
#include "debug.h"
#include "frontend/libretro/lrenv.h"
#include "frontend/libretro/lrjar.h"
#include "frontend/libretro/lrlocal.h"
#include "frontend/libretro/lrscreen.h"
#include "memory.h"

/** Load a game? */
SJME_GCC_USED bool retro_load_game(const struct retro_game_info* info)
{
	/* Ignore if null passed, because this means no content specified! */
	/* True must always be returned, otherwise false will force RetroArch */
	/* back into the menu for JAR selection. */
	if (info == NULL || info->data == NULL)
		return true;
	
	sjme_todo("Load game or JAR?");
#if 0
	struct retro_log_callback logging;
	
	/* Try to get the logger again because for some reason RetroArch */
	/* nukes our callback and then it never works again? */
	environ_cb(RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &logging);
	log_cb = (logging.log != NULL ? logging.log : fallback_log);
	
	/* With need_fullpath=false, info->data and info->size will be valid */
	/* and can be used to boot the JAR up. */
	log_cb(RETRO_LOG_INFO, "Implant JAR: path=%s data=%p size=%d\n",
		info->path, info->data, (int)info->size);
	
	/* Set in. */
	sjme_retroarch_optionjar.ptr = info->data;
	sjme_retroarch_optionjar.size = info->size;
	
	/* Always accept this. */
	return true;
#endif
}

/** Load game special? */
SJME_GCC_USED bool retro_load_game_special(unsigned type,
	const struct retro_game_info* info, size_t num)
{
	sjme_todo("Special load game?");
#if 0
	/*
	fprintf(stderr, "load game special? typ=%d path=%s data=%p size=%d n=%d\n",
		type, info->path, info->data, (int)info->size, (int)num);
	*/
	return false;
#endif
}

/** Unload a game? */
SJME_GCC_USED void retro_unload_game(void)
{
	sjme_todo("Unload game?");
#if 0
	/* Remove. */
	sjme_retroarch_optionjar.ptr = NULL;
	sjme_retroarch_optionjar.size = 0;
#endif
}

/**
 * Tries to load the external ROM.
 * 
 * @param config The output configuration.
 * @since 2022/01/02
 */
sjme_jboolean sjme_libRetro_selectRomExternal(sjme_engineConfig* config)
{
	sjme_jubyte* romData;
	const char* systemDir;
	char romPath[PATH_MAX];
	struct retro_vfs_file_handle* romFile;
	sjme_jint romSize, readAt, readCount;
	
	/* Without the VFS, can we really load the ROM? No. */
	if (g_libRetroCallbacks.vfs == NULL)
		return sjme_false;
	
	/* Try to locate the BIOS directory. */
	systemDir = NULL;
	if (!g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_GET_SYSTEM_DIRECTORY, &systemDir))
		return sjme_false;
	
	/* No system directory? */
	if (systemDir == NULL)
		return sjme_false;
	
	/* Build filename. */
	memset(romPath, 0, sizeof(romPath));
	snprintf(romPath, PATH_MAX - 1,
		"%s/squirreljme.sqc", systemDir);
		
	/* Open ROM for reading. */
	romFile = g_libRetroCallbacks.vfs->open(romPath,
		RETRO_VFS_FILE_ACCESS_READ, RETRO_VFS_FILE_ACCESS_HINT_NONE);
	if (romFile == NULL)
		return sjme_false;
	
	/* Get size of ROM. */
	romSize = (sjme_jint)g_libRetroCallbacks.vfs->size(romFile);
	if (romSize <= 0)
	{
		/* Close ROM. */
		g_libRetroCallbacks.vfs->close(romFile);
		
		/* Fail. */
		return sjme_false;
	}
	
	/* Allocate a big junk. */
	romData = sjme_malloc(romSize, NULL);
	if (romData == NULL)
	{
		/* Close ROM. */
		g_libRetroCallbacks.vfs->close(romFile);
		
		/* Fail. */
		return sjme_false;
	}
	
	/* Copy chunks of ROM. */
	for (readAt = 0; readAt < romSize;)
	{
		/* Read in chunk. */
		readCount = g_libRetroCallbacks.vfs->read(romFile,
			&romData[readAt], romSize - readAt);
		
		/* Error reading? */
		if (readCount < 0)
		{
			/* Free data. */
			sjme_free(romData, NULL);
			
			/* Close ROM. */
			g_libRetroCallbacks.vfs->close(romFile);
			
			/* Fail. */
			return sjme_false;
		}
		
		/* Read this. */
		readAt += readCount;
	}
	
	/* Close it. */
	g_libRetroCallbacks.vfs->close(romFile);
	
	/* Fill ROM details. */
	config->romPointer = romData;
	config->romSize = romSize;
	config->romIsAllocated = sjme_true;
	
	/* Notice. */
	sjme_libRetro_message(100, "Using external ROM: %s.",
		romPath);
	
	return sjme_true;
}

/**
 * Tries to load the internal ROM.
 * 
 * @param config The output configuration.
 * @since 2022/01/02
 */
sjme_jboolean sjme_libRetro_selectRomInternal(sjme_engineConfig* config)
{
	/* No built-in ROM at all? */
	if (sjme_builtInRomSize <= 0)
		return sjme_false;
	
	/* Notice. */
	sjme_libRetro_message(100, "Using internal ROM.");
	
	/* Otherwise, use its parameters. */
	config->romPointer = sjme_builtInRomData;
	config->romSize = sjme_builtInRomSize;

	/* Internal ROM is okay. */
	return sjme_true;
}

sjme_jboolean sjme_libRetro_selectRom(sjme_engineConfig* config)
{
	struct retro_variable getVar;
	const char* orderValue;
	
	/* Notice. */
	sjme_libRetro_message(10, "Determining ROM selection.");
	
	/* Get setting. */
	memset(&getVar, 0, sizeof(getVar));
	getVar.key = SJME_LIBRETRO_CONFIG_ROM_ORDER;
	orderValue = NULL;
	if (g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_GET_VARIABLE, &getVar))
		if (getVar.value != NULL)
			orderValue = getVar.value;
	
	/* Use a default value. */
	if (orderValue == NULL)
		orderValue = SJME_LIBRETRO_CONFIG_ROM_ORDER_EXT_INT;
	
	/* Notice. */
	sjme_libRetro_message(90, "Trying to load ROM.");
	
	/* External/Internal. */
	if (0 == strcmp(orderValue, SJME_LIBRETRO_CONFIG_ROM_ORDER_EXT_INT))
	{
		if (!sjme_libRetro_selectRomExternal(config))
			return sjme_libRetro_selectRomInternal(config);
		
		return sjme_true;
	}
	
	/* Internal/External. */
	else if (0 == strcmp(orderValue, SJME_LIBRETRO_CONFIG_ROM_ORDER_INT_EXT))
	{
		if (!sjme_libRetro_selectRomInternal(config))
			return sjme_libRetro_selectRomExternal(config);
		
		return sjme_true;
	}
	
	/* External only. */
	else if (0 == strcmp(orderValue, SJME_LIBRETRO_CONFIG_ROM_ORDER_EXT))
		return sjme_libRetro_selectRomExternal(config);
	
	/* Default case is internal only. */
	return sjme_libRetro_selectRomInternal(config);
}
