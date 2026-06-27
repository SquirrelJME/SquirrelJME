/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "lib/scritchui/core/core.h"
#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

static LRESULT WINAPI sjme_scritchui_win32_windowProcForward(
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
	sjme_sm(.driverName, "win32"),
	sjme_sm(.apiInit, sjme_scritchui_win32_apiInit),
	sjme_sm(.choiceItemInsert, NULL),
	sjme_sm(.choiceItemRemove, NULL),
	sjme_sm(.choiceItemSetEnabled, NULL),
	sjme_sm(.choiceItemSetImage, NULL),
	sjme_sm(.choiceItemSetSelected, NULL),
	sjme_sm(.choiceItemSetString, NULL),
	sjme_sm(.componentFocusGrab, sjme_scritchui_win32_componentFocusGrab),
	sjme_sm(.componentFocusHas, sjme_scritchui_win32_componentFocusHas),
	sjme_sm(.componentPosition, sjme_scritchui_win32_componentPosition),
	sjme_sm(.componentRepaint, sjme_scritchui_win32_componentRepaint),
	sjme_sm(.componentRevalidate, sjme_scritchui_win32_componentRevalidate),
	sjme_sm(.componentSetActivateListener, NULL),
	sjme_sm(.componentSetInputListener, NULL),
	sjme_sm(.componentSetPaintListener,
		sjme_scritchui_win32_componentSetPaintListener),
	sjme_sm(.componentSetSizeListener, NULL),
	sjme_sm(.componentSetVisibleListener, NULL),
	sjme_sm(.componentSize, sjme_scritchui_win32_componentSize),
	sjme_sm(.containerAdd, sjme_scritchui_win32_containerAdd),
	sjme_sm(.containerRemove, sjme_scritchui_win32_containerRemove),
	sjme_sm(.containerSetBounds, sjme_scritchui_win32_containerSetBounds),
	sjme_sm(.fontScanSystem, NULL),
	sjme_sm(.hardwareGraphics, NULL),
	sjme_sm(.labelSetString, sjme_scritchui_win32_labelSetString),
	sjme_sm(.lafDpiProject, NULL),
	sjme_sm(.lafElementColor, sjme_scritchui_win32_lafElementColor),
	sjme_sm(.lafMetric, NULL),
	sjme_sm(.listNew, NULL),
	sjme_sm(.loopExecute, NULL),
	sjme_sm(.loopExecuteLater, sjme_scritchui_win32_loopExecuteLater),
	sjme_sm(.loopExecuteWait, NULL),
	sjme_sm(.loopIterate, sjme_scritchui_win32_loopIterate),
	sjme_sm(.menuBarNew, sjme_scritchui_win32_menuBarNew),
	sjme_sm(.menuInsert, sjme_scritchui_win32_menuInsert),
	sjme_sm(.menuItemNew, sjme_scritchui_win32_menuItemNew),
	sjme_sm(.menuNew, sjme_scritchui_win32_menuNew),
	sjme_sm(.menuRemove, sjme_scritchui_win32_menuRemove),
	sjme_sm(.panelEnableFocus, sjme_scritchui_win32_panelEnableFocus),
	sjme_sm(.panelNew, sjme_scritchui_win32_panelNew),
	sjme_sm(.screenGetBounds, sjme_scritchui_win32_screenGetBounds),
	sjme_sm(.screens, sjme_scritchui_win32_screens),
	sjme_sm(.scrollPanelNew, sjme_scritchui_win32_scrollPanelNew),
	sjme_sm(.viewGetView, sjme_scritchui_win32_viewGetView),
	sjme_sm(.viewSetArea, sjme_scritchui_win32_viewSetArea),
	sjme_sm(.viewSetView, sjme_scritchui_win32_viewSetView),
	sjme_sm(.viewSetViewListener, NULL),
	sjme_sm(.windowContentMinimumSize, 
		sjme_scritchui_win32_windowContentMinimumSize),
	sjme_sm(.windowGetFrame, sjme_scritchui_win32_windowGetFrame),
	sjme_sm(.windowNew, sjme_scritchui_win32_windowNew),
	sjme_sm(.windowSetCloseListener, NULL),
	sjme_sm(.windowSetMenuBar, sjme_scritchui_win32_windowSetMenuBar),
	sjme_sm(.windowSetVisible, sjme_scritchui_win32_windowSetVisible),
};

static const sjme_scritchui_implInternFunctions
	sjme_scritchui_win32InternFunctions =
{
	sjme_sm(.getLastError, sjme_scritchui_win32_intern_getLastError),
	sjme_sm(.recoverComponent, sjme_scritchui_win32_intern_recoverComponent),
	sjme_sm(.windowProc, sjme_scritchui_win32_intern_windowProc),
	sjme_sm(.windowProcWin32, (PROC)sjme_scritchui_win32_windowProcForward),
	sjme_sm(.dllProc, sjme_scritchui_win32_intern_dllProc),
	sjme_sm(.dpiWin10, sjme_scritchui_win32_intern_dpiWin10)
};

static sjme_jboolean sjme_scritchui_win32_dpiWin10(
	sjme_attrInNotNull sjme_scritchui inState)
{
	sjme_scritchui_win32_intern_SPDAC proc;
	
	if (inState == NULL || inState->implIntern->dllProc == NULL)
		return SJME_JNI_FALSE;
	
	/* Find procedure. */
	if (sjme_error_is(inState->implIntern->dllProc(inState,
		SJME_SCRITCHUI_WIN32_USER32_DLL,
		"SetProcessDpiAwarenessContext",
		(PROC*)&proc)))
	{
		sjme_message("ScritchUI: Win10 DPI not found!");
		return SJME_JNI_FALSE;
	}
	
	/* Try every single handle! */
	if (TRUE != proc(DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2))
		if (TRUE != proc(DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE))
			if (TRUE != proc(DPI_AWARENESS_CONTEXT_SYSTEM_AWARE))
				return SJME_JNI_FALSE;
	
	/* One of the checks passed! */
	return SJME_JNI_TRUE;
}

static sjme_jboolean sjme_scritchui_win32_dpiWin8(
	sjme_attrInNotNull sjme_scritchui inState)
{
	sjme_scritchui_win32_intern_SPDA proc;
	
	if (inState == NULL || inState->implIntern->dllProc == NULL)
		return SJME_JNI_FALSE;
	
	/* Find procedure. */
	if (sjme_error_is(inState->implIntern->dllProc(inState,
		SJME_SCRITCHUI_WIN32_SHCORE32_DLL,
		"SetProcessDpiAwarenessContext",
		(PROC*)&proc)))
	{
		sjme_message("ScritchUI: Win8 DPI not found!");
		return SJME_JNI_FALSE;
	}
	
	/* Try every single type! */
	if (!proc(2))
		if (!proc(1))
			return SJME_JNI_FALSE;
	
	/* One of the checks passed! */
	return SJME_JNI_TRUE;
}

static sjme_jboolean sjme_scritchui_win32_dpiWinVista(
	sjme_attrInNotNull sjme_scritchui inState)
{
	sjme_scritchui_win32_intern_SPDPIA proc;
	
	if (inState == NULL || inState->implIntern->dllProc == NULL)
		return SJME_JNI_FALSE;
	
	/* Find procedure. */
	if (sjme_error_is(inState->implIntern->dllProc(inState,
		SJME_SCRITCHUI_WIN32_USER32_DLL,
		"SetProcessDPIAware",
		(PROC*)&proc)))
	{
		sjme_message("ScritchUI: WinVista DPI not found!");
		return SJME_JNI_FALSE;
	}
	
	/* For Vista, there is only one possible function. */
	if (!proc())
		return SJME_JNI_FALSE;
	return SJME_JNI_TRUE;
}

static sjme_thread_result sjme_attrThreadCall sjme_scritchui_win32_loopMain(
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
	
	/* ScritchUI on Win32 is DPI aware! Try setting it... */
	if (!sjme_scritchui_win32_dpiWin10(state))
		if (!sjme_scritchui_win32_dpiWin8(state))
			if (!sjme_scritchui_win32_dpiWinVista(state))
				sjme_message("ScritchUI: Not DPI Aware");
	
	/* Windows specific bugs. */
	state->bugs.noContentSizeWhenVisible = SJME_JNI_TRUE;
	
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
#if defined(WS_EX_NOACTIVATE)
			WS_EX_NOACTIVATE |
#endif
			WS_EX_NOPARENTNOTIFY,
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
	sjme_atomic_s(sjme_jint, &state->loopThreadReady, 1);
	
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

sjme_errorCode SJME_SCRITCHUI_DYLIB_SYMBOL_DECLARE(win32)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable const sjme_scritchui_externalFunctions* externals,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui state;

	if (outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInit(inPool,
		&state,
		&sjme_scritchui_win32Functions, loopExecute, externals,
		initFrontEnd)) || state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}

SJME_SCRITCHUI_DYLIB_API_EXPORT_SET(win32)

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

	/* The number pad uses the calculator layout. */
	inState->platformFlags |= SJME_SCRITCHUI_LAF_PLATFORM_NUMPAD_CALC_LAYOUT;
	
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
		&inState->loopThreadId,
		sjme_scritchui_win32_loopMain, inState)) ||
		inState->loopThread == SJME_THREAD_NULL)
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
