/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "3rdparty/libretro/libretro.h"
#include "frontend/libretro/shared.h"

static sjme_jboolean sjme_libretro_danglingMessage(const char* message)
{
	struct retro_message retroMessage;
	struct retro_log_callback retroLogCallback;
	
	if (sjme_libretro_envCallback != NULL)
	{
		/* Setup details. */
		retroMessage.frames = 120;
		retroMessage.msg = message;
	
		/* Emit message. */
		sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_MESSAGE,
			&retroMessage);
		
		/* Is logging also available? */
		memset(&retroLogCallback, 0, sizeof(retroLogCallback));
		if (true == sjme_libretro_envCallback(
			RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &retroLogCallback) &&
			retroLogCallback.log != NULL)
			retroLogCallback.log(RETRO_LOG_INFO, "%s", message);
		
		/* We handled it here, so SquirrelJME does not have to print it. */
		return SJME_JNI_TRUE;
	}
	
	/* Not handled. */
	return SJME_JNI_FALSE;
}

sjme_attrUnused RETRO_API unsigned retro_api_version(void)
{
	return RETRO_API_VERSION;
}

sjme_attrUnused RETRO_API void retro_deinit(void)
{
}

sjme_attrUnused RETRO_API void retro_init(void)
{
	sjme_danglingMessage = sjme_libretro_danglingMessage;
}

sjme_attrUnused RETRO_API bool retro_load_game(
	const struct retro_game_info* game)
{
	/* If we requested no game then we do not really care do we? */
	if (game == NULL)
		return true;
		
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

