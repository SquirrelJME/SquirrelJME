/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "lib/scritchui/scritchui.h"

/** The ScritchUI libraries to try. */
static const sjme_lpcstr sjme_tryLibs[] =
{
	"cocoa",
	"win32",
	"gtk2",
	NULL,
};

static sjme_errorCode sjme_findScritchUI(sjme_scritchui_dylibApiFunc* outFunc)
{
	sjme_errorCode error;
	sjme_cchar libName[1024];
	sjme_cchar dylibPath[1024];
	sjme_cchar funcName[1024];
	sjme_jint tryLib;
	sjme_dylib uiLib;
	sjme_scritchui_dylibApiFunc apiFunc;

	if (outFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Load the first ScritchUI library we find. */
	uiLib = NULL;
	apiFunc = NULL;
	for (tryLib = 0; sjme_tryLibs[tryLib] != NULL; tryLib++)
	{
		/* Build base library name. */
		memset(libName, 0, sizeof(libName));
		snprintf(libName, 1023, "%s%s",
			SJME_SCRITCHUI_DYLIB_NAME_BASE, sjme_tryLibs[tryLib]);

		/* Get what the native library would be called. */
		memset(dylibPath, 0, sizeof(dylibPath));
		sjme_dylib_name(libName, dylibPath, 1023);

		/* Try loading it. */
		uiLib = NULL;
		if (sjme_error_is(error = sjme_dylib_open(dylibPath,
			&uiLib)) || uiLib == NULL)
		{
			sjme_message("Could not find library %s: %d",
				dylibPath, error);
			continue;
		}

		/* It loaded, so try looking up our API function. */
		memset(funcName, 0, sizeof(funcName));
		snprintf(funcName, 1023, "%s%s",
			"sjme_scritchui_dylibApi", sjme_tryLibs[tryLib]);

		/* Lookup API function. */
		apiFunc = NULL;
		if (sjme_error_is(error = sjme_dylib_lookup(uiLib,
			funcName,
			(sjme_pointer*)&apiFunc)) || apiFunc == NULL)
		{
			sjme_message("Could not find %s in %s: %d",
				funcName, dylibPath, error);
			continue;
		}

		/* We found it! */
		*outFunc = apiFunc;
		return SJME_ERROR_NONE;
	}

	/* Not found! */
	return SJME_ERROR_HEADLESS_DISPLAY;
}

static sjme_errorCode sjme_mainWindowClose(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow)
{
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Terminate. */
	exit(0);
	return SJME_ERROR_NONE;
}

/**
 * Main entry point.
 *
 * @param argc Argument count.
 * @param argv Arguments.
 * @return Exit status.
 * @since 2024/08/15
 */
int main(int argc, char** argv)
{
	sjme_errorCode error;
	sjme_scritchui_dylibApiFunc apiFunc;
	sjme_scritchui uiState;
	sjme_alloc_pool* inPool;
	sjme_scritchui_uiWindow mainWindow;

	/* Set up a basic pool. */
	inPool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&inPool,
		16 * 1048576)) || inPool == NULL)
	{
		sjme_message("Could not allocate memory pool: %d",
			error);
		return EXIT_FAILURE;
	}

	/* Try to locate ScritchUI... */
	apiFunc = NULL;
	if (sjme_error_is(error = sjme_findScritchUI(&apiFunc)) ||
		apiFunc == NULL)
	{
		sjme_message("Did not find a ScritchUI library: %d",
			error);
		return EXIT_FAILURE;
	}

	/* Initialize ScritchUI. */
	uiState = NULL;
	if (sjme_error_is(error = apiFunc(inPool,
		NULL, NULL, &uiState)) ||
		uiState == NULL)
	{
		sjme_message("Could not initialize ScritchUI: %d",
			error);
		return EXIT_FAILURE;
	}

	/* Setup window. */
	mainWindow = NULL;
	if (sjme_error_is(error = uiState->api->windowNew(uiState,
		&mainWindow)) || mainWindow == NULL)
	{
		sjme_message("Could not create main window: %d",
			error);
		return EXIT_FAILURE;
	}

	/* Set close listener. */
	if (sjme_error_is(error = uiState->api->windowSetCloseListener(
		uiState, mainWindow,
		sjme_mainWindowClose, NULL)))
	{
		sjme_message("Could not show main window: %d",
			error);
		return EXIT_FAILURE;
	}

	/* Show it. */
	if (sjme_error_is(error = uiState->api->windowSetVisible(uiState,
		mainWindow, SJME_JNI_TRUE)))
	{
		sjme_message("Could not show main window: %d",
			error);
		return EXIT_FAILURE;
	}

	/* Burn the CPU. */
	for (;;)
		sjme_thread_yield();

	/* Success! */
	return EXIT_SUCCESS;
}
