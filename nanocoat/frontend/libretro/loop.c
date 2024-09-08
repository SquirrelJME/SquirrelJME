/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/nvm.h"
#include "sjme/debug.h"
#include "sjme/nvm/modelessStars.h"
#include "3rdparty/libretro/libretro.h"
#include "frontend/libretro/shared.h"

#if defined(SJME_CONFIG_DEBUG) && defined(SJME_CONFIG_UNIT_TEST)
	#include "sjme/test/externTest.h"
	#include "sjme/dylib.h"
#endif

#if defined(SJME_CONFIG_DEBUG) && defined(SJME_CONFIG_UNIT_TEST)

/** Did we already check that we are running unit tests? */
static sjme_jboolean sjme_libretro_checkRunUnitTests;

/** Did we read the environment for unit tests? */
static sjme_jboolean sjme_libretro_checkedEnvRunUnitTests;

/** Are we running unit tests? */
static sjme_jboolean sjme_libretro_doRunUnitTests;

/** The next test to run. */
static sjme_lpcstr sjme_libretro_nextTest;

/**
 * Check if unit tests should run.
 *
 * @since 2023/12/21
 */
static void sjme_libretro_checkUnitTests(void)
{
	struct retro_variable var;

	/* Do we need to check if we are running unit tests? */
	if (!sjme_libretro_checkedEnvRunUnitTests)
	{
		/* Do not check again. */
		sjme_libretro_checkedEnvRunUnitTests = SJME_JNI_TRUE;

		/* Ask the front end if we should run unit tests. */
		memset(&var, 0, sizeof(var));
		var.key = SJME_LIBRETRO_CONFIG_UNIT_TESTS;
		sjme_libretro_envCallback(RETRO_ENVIRONMENT_GET_VARIABLE,
			&var);

		/* Are we running unit tests? */
		if (var.value != NULL && 0 == strcmp(var.value, "true"))
		{
			/* Do run the tests now. */
			sjme_libretro_doRunUnitTests = SJME_JNI_TRUE;

			/* Implicit start at first test. */
			sjme_libretro_nextTest = "";
		}
	}
}

sjme_jboolean sjme_libretro_unitTestAbortHandler(void)
{
	return SJME_JNI_TRUE;
}

static sjme_debug_handlerFunctions sjme_libretro_unitTestDebugHandlers =
{
	.abort = sjme_libretro_unitTestAbortHandler,
	.exit = NULL,
	.message = NULL,
};

/**
 * Runs unit tests.
 *
 * @return If they are running.
 * @since 2023/12/21
 */
static sjme_jboolean sjme_libretro_runUnitTests(void)
{
	sjme_lpstr argv[2];

	/* Not running unit tests, so ignore. */
	if (!sjme_libretro_doRunUnitTests)
		return SJME_JNI_FALSE;

	/* Ran out of tests to run? */
	if (sjme_libretro_nextTest == NULL)
		return SJME_JNI_TRUE;

	/* Clear the abort and exit handlers so the tests do not just end. */
	sjme_debug_handlers = &sjme_libretro_unitTestDebugHandlers;

	/* Run unit tests. */
	argv[0] = "libretro";
	argv[1] = sjme_libretro_nextTest;
	sjme_test_main(2, argv, &sjme_libretro_nextTest);

	/* We ran something. */
	return SJME_JNI_TRUE;
}

#endif

sjme_attrUnused RETRO_API void retro_run(void)
{
	static sjme_modelessStarState modelessStarState;
	static sjme_jint tick;
	uint32_t buf[240*320];
	int i;
	
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);

	/* Do a basic animation. */
	sjme_modelessStars(&modelessStarState, buf,
		240, 320, 240, tick++);
	sjme_libretro_videoRefreshCallback(
		buf, 240, 320, 240 * 4);

#if defined(SJME_CONFIG_DEBUG) && defined(SJME_CONFIG_UNIT_TEST)
	/* Running unit tests? */
	if (!sjme_libretro_checkRunUnitTests)
	{
		/* Do not emit again. */
		sjme_libretro_checkRunUnitTests = SJME_JNI_TRUE;

		/* Run the check. */
		sjme_libretro_checkUnitTests();
	}

	/* Run unit tests in the loop. */
	if (sjme_libretro_doRunUnitTests)
	{
		sjme_libretro_runUnitTests();
		return;
	}
#endif
}
