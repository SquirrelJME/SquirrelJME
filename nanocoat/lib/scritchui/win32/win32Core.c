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

static LRESULT sjme_scritchui_win32_windowProcForward(
	HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	sjme_scritchui inState;
	sjme_scritchui_uiWindow inWindow;
	LRESULT result;
	
	/* Ignore if no window was specified. */
	if (hWnd == NULL)
		return DefWindowProc(hWnd, message, wParam, lParam);
	
	/* Link back to this window. */
	SetLastError(0);
	inWindow = (sjme_scritchui_uiWindow)GetWindowLongPtr(hWnd,
		GWLP_USERDATA);
	if (inWindow == NULL)
		return DefWindowProc(hWnd, message, wParam, lParam);
	
	/* Recover state. */
	inState = inWindow->component.common.state;
	
	/* Handle message. */
	result = 0;
	inState->implIntern->windowProc(inState,
		hWnd, message, wParam, lParam, &result);
	return result;
}

static const sjme_scritchui_implFunctions sjme_scritchui_win32Functions =
{
	.apiInit = sjme_scritchui_win32_apiInit,
	.choiceItemInsert = NULL,
	.choiceItemRemove = NULL,
	.choiceItemSetEnabled = NULL,
	.choiceItemSetImage = NULL,
	.choiceItemSetSelected = NULL,
	.choiceItemSetString = NULL,
	.componentFocusGrab = sjme_scritchui_win32_componentFocusGrab,
	.componentFocusHas = sjme_scritchui_win32_componentFocusHas,
	.componentPosition = sjme_scritchui_win32_componentPosition,
	.componentRepaint = sjme_scritchui_win32_componentRepaint,
	.componentRevalidate = sjme_scritchui_win32_componentRevalidate,
	.componentSetActivateListener = NULL,
	.componentSetInputListener = NULL,
	.componentSetPaintListener =
		sjme_scritchui_win32_componentSetPaintListener,
	.componentSetSizeListener = NULL,
	.componentSetVisibleListener = NULL,
	.componentSize = sjme_scritchui_win32_componentSize,
	.containerAdd = sjme_scritchui_win32_containerAdd,
	.containerRemove = sjme_scritchui_win32_containerRemove,
	.containerSetBounds = sjme_scritchui_win32_containerSetBounds,
	.hardwareGraphics = NULL,
	.labelSetString = sjme_scritchui_win32_labelSetString,
	.lafElementColor = sjme_scritchui_win32_lafElementColor,
	.listNew = NULL,
	.loopExecute = NULL,
	.loopExecuteLater = sjme_scritchui_win32_loopExecuteLater,
	.loopExecuteWait = NULL,
	.loopIterate = sjme_scritchui_win32_loopIterate,
	.menuBarNew = sjme_scritchui_win32_menuBarNew,
	.menuInsert = sjme_scritchui_win32_menuInsert,
	.menuItemNew = sjme_scritchui_win32_menuItemNew,
	.menuNew = sjme_scritchui_win32_menuNew,
	.menuRemove = sjme_scritchui_win32_menuRemove,
	.panelEnableFocus = sjme_scritchui_win32_panelEnableFocus,
	.panelNew = sjme_scritchui_win32_panelNew,
	.screens = sjme_scritchui_win32_screens,
	.scrollPanelNew = NULL,
	.viewGetView = NULL,
	.viewSetArea = NULL,
	.viewSetView = NULL,
	.viewSetViewListener = NULL,
	.windowContentMinimumSize = sjme_scritchui_win32_windowContentMinimumSize,
	.windowNew = sjme_scritchui_win32_windowNew,
	.windowSetCloseListener = NULL,
	.windowSetMenuBar = sjme_scritchui_win32_windowSetMenuBar,
	.windowSetVisible = sjme_scritchui_win32_windowSetVisible,
};

static const sjme_scritchui_implInternFunctions
	sjme_scritchui_win32InternFunctions =
{
	.getLastError = sjme_scritchui_win32_intern_getLastError,
	.recoverComponent = sjme_scritchui_win32_intern_recoverComponent,
	.windowProc = sjme_scritchui_win32_intern_windowProc,
	.windowProcWin32 = sjme_scritchui_win32_windowProcForward,
};

static sjme_thread_result sjme_scritchui_win32_loopMain(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_scritchui state;
	MSG message;
	sjme_jboolean terminated;
	HWND voidWindow;
	
	/* Restore state. */
	state = (sjme_scritchui)anything;
	if (state == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* By calling this, we are forcing the event queue to be created. */
	memset(&message, 0, sizeof(message));
	PeekMessage(&message, NULL,
		WM_USER, WM_USER, PM_NOREMOVE);
	
	/* Past Windows 98, we can use a specific message window. */
	if (!state->common.intVals[SJME_SUI_WIN32_V_WIN9X])
		voidWindow = HWND_MESSAGE;
	
	/* Because all child windows need a parent, we need somewhere to store */
	/* them before reparenting. */
	else
	{
		voidWindow = CreateWindowEx(
			WS_EX_NOACTIVATE | WS_EX_NOPARENTNOTIFY,
			"Static",
			"SquirrelJME Void",
			WS_DISABLED | WS_OVERLAPPED,
			0, 0, 1, 1,
			NULL,
			NULL,
			GetModuleHandle(NULL),
			NULL);
		if (voidWindow == NULL)
			return SJME_THREAD_RESULT(state->implIntern->getLastError(
				state, SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED));
	}
	
	/* Store the handle for later. */
	state->common.handle[SJME_SUI_WIN32_H_VOID] = voidWindow;
	
	/* Debug. */
	sjme_message("Void Window: %p", voidWindow);
	
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
	OSVERSIONINFOEX winVer;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Internal functions to use specifically for Win32. */
	inState->implIntern = &sjme_scritchui_win32InternFunctions;
	
	/* This is a standard desktop. */
	inState->wmType = SJME_SCRITCHUI_WM_TYPE_STANDARD_DESKTOP;
	
	/* Are we on Windows 9x? */
	memset(&winVer, 0, sizeof(winVer));
	GetVersionEx((LPOSVERSIONINFOA)&winVer);
	if ((winVer.dwMajorVersion == 4 && (winVer.dwMinorVersion == 0 ||
		winVer.dwMinorVersion == 10 || winVer.dwMinorVersion == 90)) ||
		winVer.dwPlatformId == VER_PLATFORM_WIN32_WINDOWS)
		inState->common.intVals[SJME_SUI_WIN32_V_WIN9X] = SJME_JNI_TRUE;
	else
		inState->common.intVals[SJME_SUI_WIN32_V_WIN9X] = SJME_JNI_FALSE;
	
	/* Start main Win32 thread. */
	if (sjme_error_is(error = sjme_thread_new(
		&inState->loopThread,
		sjme_scritchui_win32_loopMain, inState)) ||
		inState->loopThread == SJME_THREAD_NULL)
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
