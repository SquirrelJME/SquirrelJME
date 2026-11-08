/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchui.h"

#include "sjme/dylibExtra.h"

static sjme_errorCode sjme_demo_loop(sjme_alloc_pool allocPool,
	sjme_scritchui inState)
{
	sjme_errorCode error;
	sjme_scritchui_uiWindow window;
	sjme_scritchui_uiPanel panel;
	
	if (allocPool == NULL || inState == NULL)
		return SJME_ERROR_NONE;

	/* Create window. */
	window = NULL;
	if (sjme_error_is(error = inState->api->windowNew(inState, &window)) ||
		window == NULL)
		return sjme_error_default(error);
	
	/* Create a panel to paint on. */
	panel = NULL;
	if (sjme_error_is(error = inState->api->panelNew(inState,
		&panel)) || panel == NULL)
		return sjme_error_default(error);
	
	/* Place panel into the window. */
	if (sjme_error_is(error = inState->api->containerAdd(inState,
		SJME_SUI_CAST_CONTAINER(window), 
		SJME_SUI_CAST_COMPONENT(panel))))
		return sjme_error_default(error);
	
	/* Set minimum size. */
	if (sjme_error_is(error = inState->api->windowContentMinimumSize(
		inState, window, 320, 200)))
		return sjme_error_default(error);
	
	/* Set component bounds. */
	if (sjme_error_is(error = inState->api->containerSetBounds(
		inState, SJME_SUI_CAST_CONTAINER(window),
		SJME_SUI_CAST_COMPONENT(panel),
		0, 0, 320, 200)))
		return sjme_error_default(error);
	
	/* Show the window. */
	if (sjme_error_is(error = inState->api->windowSetVisible(inState,
		window, SJME_JNI_TRUE)))
		return sjme_error_default(error);
	
	/* Await termination. */
	if (sjme_error_is(error = inState->api->loopIterate(inState,
		SJME_JNI_TRUE, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

/**
 * Main entry point for the ScritchUI demo.
 * 
 * @param argc Argument count.
 * @param argv Arguments passed.
 * @return Zero on success, otherwise any other value is failure.
 * @since 2025/12/07
 */
int main(int argc, char** argv)
{
	sjme_errorCode error;
	sjme_dylib lib;
	sjme_scritchui_dylibApiFunc apiInit;
	sjme_lpcstr actualSubComponent;
	sjme_cchar buf[SJME_BUF_SIZE_SYMBOL];
	sjme_alloc_pool allocPool;
	sjme_scritchui uiState;
	
	/* Set up an allocation pool. */
	allocPool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(
		&allocPool, SJME_JVM_INIT_MEMORY)))
		goto fail_allocPool;

	/* Load the ScritchUI library. */
	lib = NULL;
	actualSubComponent = NULL;
	if (sjme_error_is(error = sjme_dylib_openExtra(NULL,
		SJME_DYLIB_EXTRA_FAMILY_SCRITCHUI, NULL, &lib,
		&actualSubComponent)) ||
		lib == NULL)
		goto fail_openDyLib;
	
	/* What is the API function entrypoint called? */
	memset(&buf, 0, sizeof(buf));
	snprintf(buf, SJME_BUF_SIZE_SYMBOL - 1,
	SJME_TOKEN_STRING_PP(SJME_SCRITCHUI_DYLIB_SYMBOL_PREFIX) "%s",
		(actualSubComponent != NULL ? actualSubComponent : ""));

	/* Grab the API initializer from the library. */
	apiInit = NULL;
	if (sjme_error_is(error = sjme_dylib_lookup(lib, buf,
		(sjme_pointer*)&apiInit)) || apiInit == NULL)
		goto fail_lookupInit;

	/* Initialize ScritchUI from the pointer. */
	uiState = NULL;
	if (sjme_error_is(error = apiInit(allocPool, &uiState, 
		NULL, NULL, NULL)))
		goto fail_initUi;
	
	/* Enter basic loop. */
	if (sjme_error_is(error = sjme_demo_loop(allocPool, uiState)))
		goto fail_loopy;
	
	/* Success! */
	return 0;

fail_loopy:
fail_initUi:
fail_lookupInit:
	if (lib != NULL)
		sjme_dylib_close(lib);
	
fail_openDyLib:
fail_allocPool:
	if (allocPool != NULL)
		sjme_alloc_poolDestroy(allocPool);
	sjme_emitB("Error: %d", error);
	return EXIT_FAILURE;
}
