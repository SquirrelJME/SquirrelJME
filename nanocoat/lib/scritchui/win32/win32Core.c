/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

static const sjme_scritchui_implFunctions sjme_scritchui_win32Functions =
{
	.apiInit = sjme_scritchui_win32_apiInit,
	.choiceItemInsert = NULL,
	.choiceItemRemove = NULL,
	.choiceItemSetEnabled = NULL,
	.choiceItemSetImage = NULL,
	.choiceItemSetSelected = NULL,
	.choiceItemSetString = NULL,
	.componentFocusGrab = NULL,
	.componentFocusHas = NULL,
	.componentRepaint = NULL,
	.componentRevalidate = NULL,
	.componentSetActivateListener = NULL,
	.componentSetInputListener = NULL,
	.componentSetPaintListener = NULL,
	.componentSetSizeListener = NULL,
	.componentSetVisibleListener = NULL,
	.componentSize = NULL,
	.containerAdd = NULL,
	.containerRemove = NULL,
	.containerSetBounds = NULL,
	.hardwareGraphics = NULL,
	.labelSetString = NULL,
	.lafElementColor = NULL,
	.listNew = NULL,
	.loopExecute = NULL,
	.loopExecuteLater = sjme_scritchui_win32_loopExecuteLater,
	.loopExecuteWait = NULL,
	.loopIterate = sjme_scritchui_win32_loopIterate,
	.menuBarNew = NULL,
	.menuInsert = NULL,
	.menuItemNew = NULL,
	.menuNew = NULL,
	.menuRemove = NULL,
	.panelEnableFocus = NULL,
	.panelNew = NULL,
	.screens = NULL,
	.scrollPanelNew = NULL,
	.viewGetView = NULL,
	.viewSetArea = NULL,
	.viewSetView = NULL,
	.viewSetViewListener = NULL,
	.windowContentMinimumSize = NULL,
	.windowNew = NULL,
	.windowSetCloseListener = NULL,
	.windowSetMenuBar = NULL,
	.windowSetVisible = NULL,
};

static const sjme_scritchui_implInternFunctions
	sjme_scritchui_win32InternFunctions =
{
	.getLastError = sjme_scritchui_win32_intern_getLastError,
};

static sjme_thread_result sjme_scritchui_win32_loopMain(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_scritchui state;
	MSG message;
	sjme_jboolean terminated;
	
	/* Restore state. */
	state = (sjme_scritchui)anything;
	if (state == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* By calling this, we are forcing the event queue to be created. */
	memset(&message, 0, sizeof(message));
	PeekMessage(&message, NULL,
		WM_USER, WM_USER, PM_NOREMOVE);
	
	/* Before we go into the main loop, signal it is ready. */
	sjme_atomic_sjme_jint_set(&state->loopThreadReady, 1);
	
	/* Message loop. */
	terminated = SJME_JNI_FALSE;
	do
	{
		/* Keep running single executions. */
		error = state->impl->loopIterate(state, SJME_JNI_TRUE,
			&terminated);
		
		/* Did this error? */
		if (sjme_error_is(error))
			sjme_message("Loop iterate failure: %d", error);
	} while (!terminated);
	
	/* Success?? */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

/**
 * Returns the Win32 ScritchUI interface.
 * 
 * @param inPool The allocation pool used.
 * @param loopExecute The loop execution to run after init.
 * @param initFrontEnd Optional initial frontend data.
 * @param outState The newly created state.
 * @return The library interface.
 * @since 2024/07/16 
 */
sjme_errorCode SJME_DYLIB_EXPORT SJME_SCRITCHUI_DYLIB_SYMBOL(win32)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInOutNotNull sjme_scritchui* outState)
{
	sjme_errorCode error;
	sjme_scritchui state;

	if (outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInit(inPool,
		&state,
		&sjme_scritchui_win32Functions, loopExecute,
		initFrontEnd)) || state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_apiInit(
	sjme_attrInNotNull sjme_scritchui inState)
{
	sjme_errorCode error;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Internal functions to use specifically for Win32. */
	inState->implIntern = &sjme_scritchui_win32InternFunctions;
	
	/* This is a standard desktop. */
	inState->wmType = SJME_SCRITCHUI_WM_TYPE_STANDARD_DESKTOP;
	
	/* Start main Win32 thread. */
	if (sjme_error_is(error = sjme_thread_new(
		&inState->loopThread,
		sjme_scritchui_win32_loopMain, inState)) ||
		inState->loopThread == SJME_THREAD_NULL)
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
