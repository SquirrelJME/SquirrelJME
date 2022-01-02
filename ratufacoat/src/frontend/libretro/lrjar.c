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

/** Load a game? */
SJME_GCC_USED bool retro_load_game(const struct retro_game_info* info)
{
	sjme_todo("Load game or JAR?");
#if 0
	struct retro_log_callback logging;
	
	/* Ignore if null passed, because this means no content specified! */
	/* True must always be returned, otherwise false will force RetroArch */
	/* back into the menu for JAR selection. */
	if (info == NULL)
		return true;
	
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
