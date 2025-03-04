/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <libretro.h>

#include "sjme/config.h"

#if defined(SJME_CONFIG_DEBUG)
	#include <signal.h>
#endif

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/pure/pure.h"
#include "sjme/nvm/nvm.h"
#include "sjme/debug.h"
#include "frontend/libretro/shared.h"
#include "sjme/dylib.h"

/** The default pool size for ScritchUI on RetroArch. */
#define SJME_LIBRETRO_SCRITCHUI_POOL_SIZE INT32_C(25165824)

static sjme_jboolean sjme_libretro_debugMessageHandler(sjme_lpcstr fullMessage,
	sjme_lpcstr partMessage)
{
	struct retro_message retroMessage;
	struct retro_log_callback retroLogCallback;
	
	if (sjme_libretro_envCallback != NULL)
	{
		/* Setup details. */
		retroMessage.frames = 240;
		retroMessage.msg = partMessage;
	
		/* Emit message. */
		sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_MESSAGE,
			&retroMessage);
		
		/* Is logging also available? */
		memset(&retroLogCallback, 0, sizeof(retroLogCallback));
		if (true == sjme_libretro_envCallback(
			RETRO_ENVIRONMENT_GET_LOG_INTERFACE,
				&retroLogCallback) &&
			retroLogCallback.log != NULL)
			retroLogCallback.log(RETRO_LOG_INFO, "%s\n",
			fullMessage);
		
		/* We handled it here, so SquirrelJME does not have to print it. */
		return SJME_JNI_TRUE;
	}
	
	/* Not handled. */
	return SJME_JNI_FALSE;
}

static sjme_jboolean sjme_libretro_exitHandler(int exitCode)
{
	/* If there is no environment callback, then do nothing here. */
	if (sjme_libretro_envCallback == NULL)
		return SJME_JNI_FALSE;

	/* Tell the front end to stop the core. */
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SHUTDOWN, NULL);
	return SJME_JNI_TRUE;
}

#if defined(SJME_CONFIG_DEBUG)
static void sjme_libretro_signalHandler(int)
{
	/* Does nothing... */
}
#endif

static sjme_jboolean sjme_libretro_abortHandler(void)
{
	/* Forward to the exit handler. */
	return sjme_libretro_exitHandler(1);
}

static sjme_debug_handlerFunctions sjme_libretro_debugHandlers =
{
	.abort = sjme_libretro_abortHandler,
	.exit = sjme_libretro_exitHandler,
	.message = sjme_libretro_debugMessageHandler,
};

sjme_attrUnused RETRO_API unsigned retro_api_version(void)
{
	return RETRO_API_VERSION;
}

sjme_attrUnused RETRO_API void retro_deinit(void)
{
}

sjme_attrUnused RETRO_API void retro_init(void)
{
	sjme_errorCode error;
	sjme_scritchui scritchUi;
	sjme_alloc_pool scritchPool;
	
	/* Setup handlers for debug calls. */
	sjme_debug_handlers = &sjme_libretro_debugHandlers;

	/* Allocate ScritchUI memory. */
	scritchPool = NULL;
	sjme_message("Allocating ScritchUI memory...");
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&scritchPool,
		SJME_LIBRETRO_SCRITCHUI_POOL_SIZE)) || scritchPool == NULL)
		goto fail_initMem;

	/* Initialize ScritchUI. */
	scritchUi = NULL;
	sjme_message("Initializing ScritchUI...");
	if (sjme_error_is(error = SJME_SCRITCHUI_DYLIB_SYMBOL(pure)(
		scritchPool, &scritchUi, NULL, NULL, NULL)) || scritchUi == NULL)
		goto fail_initUi;
	
fail_initUi:
fail_initMem:
	if (scritchPool != NULL)
		sjme_alloc_poolDestroy(scritchPool);
	sjme_error_fatal(error);
}

sjme_attrUnused RETRO_API bool retro_load_game(
	const struct retro_game_info* game)
{
	static sjme_jint trigger;
	
	/* If we requested no game then we do not really care, do we? */
	if (game == NULL)
		return true;
		
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
		
	return false;
}

sjme_attrUnused RETRO_API bool retro_load_game_special(unsigned game_type,
	const struct retro_game_info *info, size_t num_info)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
	return false;
}

sjme_attrUnused RETRO_API void retro_reset(void)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
}

sjme_attrUnused RETRO_API void retro_unload_game(void)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
}

