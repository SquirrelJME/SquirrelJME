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

#include "sjme/nvm/boot.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/pure/pure.h"
#include "sjme/nvm/nvm.h"
#include "sjme/debug.h"
#include "frontend/libretro/shared.h"
#include "sjme/dylib.h"
#include "sjme/native.h"

/** A single MiB. */
#define SJME_LIBRETRO_ONE_MIB INT32_C(1048576)

/** The default pool size on RetroArch. */
#define SJME_LIBRETRO_DEFAULT_POOL_SIZE (SJME_LIBRETRO_ONE_MIB * 64)

sjme_libretro_globalStruct sjme_libretro_globals =
{
	sjme_sm(.envCallback, NULL),
	sjme_sm(.videoRefreshCallback, NULL),
	sjme_sm(.vfs, {0}),
	sjme_sm(.allocPool, NULL),
	sjme_sm(.inState, NULL),
	sjme_sm(.modelessStars, {0})
};

static sjme_jboolean sjme_libretro_debugMessageHandler(sjme_lpcstr fullMessage,
	sjme_lpcstr partMessage)
{
	struct retro_message retroMessage;
	struct retro_log_callback retroLogCallback;
	
	if (sjme_libretro_globals.envCallback != NULL)
	{
		/* Setup details. */
		retroMessage.frames = 240;
		retroMessage.msg = partMessage;
	
		/* Emit message. */
		sjme_libretro_globals.envCallback(RETRO_ENVIRONMENT_SET_MESSAGE,
			&retroMessage);
		
		/* Is logging also available? */
		memset(&retroLogCallback, 0, sizeof(retroLogCallback));
		if (true == sjme_libretro_globals.envCallback(
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
	if (sjme_libretro_globals.envCallback == NULL)
		return SJME_JNI_FALSE;

	/* Tell the front end to stop the core. */
#if 0
	sjme_libretro_globals.envCallback(RETRO_ENVIRONMENT_SHUTDOWN, NULL);
#endif
	return SJME_JNI_TRUE;
}

static sjme_jboolean sjme_libretro_abortHandler(sjme_errorCode error)
{
	/* Set modeless stars error. */
	sjme_atomic_sjme_jint_set(&sjme_libretro_globals.modelessStars.errorCode,
		error);
	
	/* Forward to the exit handler. */
	return sjme_libretro_exitHandler(1);
}

static sjme_debug_handlerFunctions sjme_libretro_debugHandlers =
{
	sjme_sm(.abort, sjme_libretro_abortHandler),
	sjme_sm(.exit, sjme_libretro_exitHandler),
	sjme_sm(.message, sjme_libretro_debugMessageHandler),
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
	sjme_alloc_pool allocPool;
	sjme_jint tryAlloc;
	
	/* Setup handlers for debug calls. */
	sjme_debug_handlers = &sjme_libretro_debugHandlers;
	
	/* Reset error. */
	error = SJME_ERROR_UNKNOWN;

	/* Allocate memory. */
	allocPool = NULL;
	sjme_message("Allocating memory...");
	for (tryAlloc = SJME_LIBRETRO_DEFAULT_POOL_SIZE;
		tryAlloc >= SJME_LIBRETRO_ONE_MIB;
		tryAlloc -= SJME_LIBRETRO_ONE_MIB)
	{
		/* Attempt allocation. */
		allocPool = NULL;
		if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&allocPool,
			SJME_LIBRETRO_DEFAULT_POOL_SIZE)) || allocPool == NULL)
			continue;

		/* Was successful, so do not try again. */
		break;
	}

	/* Failed to allocate? */
	if (allocPool == NULL)
		goto fail_initMem;

	/* Set global memory. */
	sjme_libretro_globals.allocPool = allocPool;

	/* Success! */
	return;

fail_initMem:
	if (allocPool != NULL)
		sjme_alloc_poolDestroy(allocPool);
	sjme_error_fatal(error);
}

sjme_attrUnused RETRO_API bool retro_load_game(
	const struct retro_game_info* game)
{
	/* Share everything with platform-specific loading. */
	if (game == NULL)
		return retro_load_game_special(0, NULL, 0);
	return retro_load_game_special(0, game, 1);
}

sjme_attrUnused RETRO_API bool retro_load_game_special(unsigned game_type,
	const struct retro_game_info* info, size_t num_info)
{
#define MAX_ARGC 32
	sjme_errorCode error;
	sjme_jint i, argC, libStrSize;
	sjme_lpcstr* argV;
	sjme_nvm_bootParam bootParam;
	sjme_lpcstr systemDir;
	const struct retro_game_info* jarInfo;

	/* Default to out of memory. */
	error = SJME_ERROR_OUT_OF_MEMORY;
	
	/* Allocate arguments to pass in. */
	argC = 0;
	argV = sjme_alloca(sizeof(*argV) * (MAX_ARGC + 1));
	if (argV == NULL)
		goto fail_allocArgV;
	memset(argV, 0, sizeof(*argV) * (MAX_ARGC + 1));

	/* -Xlibraries? */
	if (num_info > 1)
	{
		/* Determine size of everything. */
		libStrSize = strlen("-Xlibraries:") + 1;
		for (i = 1; i < num_info; i++)
		{
			/* Which info are we operating on? */
			jarInfo = &info[i];
			if (jarInfo->path == NULL)
				continue;

			/* Add in. */
			libStrSize = strlen(jarInfo->path) + 2;
		}

		/* Allocate. */
		argV[argC] = sjme_alloca(sizeof(*argV[argC]) * libStrSize);
		if (argV[argC] == NULL)
			goto fail_allocArgV;
		memset((void*)argV[argC], 0, sizeof(*argV[argC]) * libStrSize);

		/* Concat everything. */
		strncpy((char*)argV[argC], "-Xlibraries:", libStrSize);
		for (i = 1; i < num_info; i++)
		{
			/* Which info are we operating on? */
			jarInfo = &info[i];
			if (jarInfo->path == NULL)
				continue;

			/* Pass it through. */
			strncat((char*)argV[argC], jarInfo->path, libStrSize);
			if ((i + 1) < num_info)
				strncat((char*)argV[argC],
					SJME_CONFIG_PATH_SEPARATOR, libStrSize);
		}

		/* Consume it now. */
		argC++;
	}

	/* -jar? */
	if (num_info >= 1)
	{
		/* Which Jar are we operating on? */
		jarInfo = &info[0];

		/* Pass it through. */
		argV[argC++] = "-jar";
		argV[argC++] = jarInfo->path;
	}
	
	/* Setup base boot parameters. */
	memset(&bootParam, 0, sizeof(bootParam));
	bootParam.nal = &sjme_libretro_nal;

	/* Parse main arguments. */
	if (sjme_error_is(error = sjme_nvm_parseCommandLine(
		sjme_libretro_globals.allocPool,
		bootParam.nal, &bootParam, argC, argV)))
		goto fail_parseCommandLine;
	
	/* If there was no boot suite specified, load a default one. */
	if (bootParam.bootSuite == NULL)
	{
		/* Obtain the system directory. */
		error = SJME_ERROR_NATIVE_ERROR;
		systemDir = NULL;
		if (!sjme_libretro_globals.envCallback(
			RETRO_ENVIRONMENT_GET_SYSTEM_DIRECTORY, &systemDir) ||
			systemDir == NULL)
			goto fail_getSystemDir;
		
		/* Look it up. */
		if (sjme_error_is(error = sjme_nvm_defaultBootSuiteInDirectory(
			sjme_libretro_globals.allocPool,
			bootParam.nal,
			systemDir,
			&bootParam.bootSuite)) ||
			bootParam.bootSuite == NULL)
			goto fail_bootSuiteBackup;
	}

	/* If no classpath was specified, load the launcher instead. */
	if (bootParam.mainClassPathById == NULL &&
		bootParam.mainClassPathByName == NULL)
	{
		/* Try to find default launcher. */
		if (sjme_error_is(error = sjme_nvm_rom_suiteDefaultLaunch(
			sjme_libretro_globals.allocPool,
			bootParam.bootSuite,
			(sjme_lpstr*)&bootParam.mainClass,
			(sjme_list_sjme_lpstr**)&bootParam.mainArgs,
			(sjme_list_sjme_jint**)&bootParam.mainClassPathById,
			(sjme_list_sjme_lpstr**)&bootParam.mainClassPathByName)))
			goto fail_defaultLaunch;
		
		/* Still failed? */
		if (bootParam.mainClassPathById == NULL &&
			bootParam.mainClassPathByName == NULL)
		{
			error = SJME_ERROR_NO_CLASS;
			goto fail_defaultLaunch;
		}
	}
	
	/* Boot the virtual machine. */
	sjme_libretro_globals.inState = NULL;
	if (sjme_error_is(error = sjme_nvm_boot(
		sjme_libretro_globals.allocPool,
		&bootParam, &sjme_libretro_globals.inState, NULL)))
		goto fail_boot;
	
	/* Success! */
	return true;
#undef MAX_ARGC

fail_defaultLaunch:
fail_boot:
fail_bootSuiteBackup:
fail_parseCommandLine:
fail_getSystemDir:
fail_allocArgV:
	return sjme_error_fatal(error) & 0;
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

