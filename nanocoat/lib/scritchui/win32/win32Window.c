/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

static sjme_errorCode sjme_scritchui_win32_windowCenter(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow)
{
	HWND window;
	RECT rect, desktop;
	sjme_jint w, h, dw, dh;

	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover window. */
	window = inWindow->component.common.handle[SJME_SUI_WIN32_H_HWND];

	/* Determine size of the window. */
	memset(&rect, 0, sizeof(rect));
	GetWindowRect(window, &rect);
	w = inWindow->component.bounds.d.width;
	if (w <= 0)
		w = rect.right - rect.left;
	h = inWindow->component.bounds.d.height;
	if (h <= 0)
		h = rect.bottom - rect.top;

	/* Get the size of the desktop. */
	memset(&desktop, 0, sizeof(desktop));
	SystemParametersInfo(SPI_GETWORKAREA, 0, &desktop, 0);
	dw = desktop.right - desktop.left;
	dh = desktop.bottom - desktop.top;

	/* Center on the screen and make it appear on top. */
	SetWindowPos(window,
		HWND_TOP,
		(dw / 4) + (w / 4), (dh / 8) + (h / 8),
		0, 0, SWP_NOSIZE);

	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	HWND window;
	WINDOWPLACEMENT placement;
	sjme_scritchui_dim* overhead;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	window = inWindow->component.common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* The overhead has been calculated via windowContentGetFrame(). */
	overhead = &inWindow->minOverhead;
	
	/* Setup new placement information. */
	memset(&placement, 0, sizeof(placement));
	placement.length = sizeof(placement);
	placement.flags = WPF_SETMINPOSITION;
	placement.ptMinPosition.x = width + overhead->width;
	placement.ptMinPosition.y = height + overhead->height;
	
	/* Set the window placement. */
	SetLastError(0);
	if (0 == SetWindowPlacement(window, &placement))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);

	/* Center the window before leaving */
	return sjme_scritchui_win32_windowCenter(inState, inWindow);
}

sjme_errorCode sjme_scritchui_win32_windowGetFrame(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrOutNullable sjme_scritchui_dim* contentSize,
	sjme_attrOutNullable sjme_scritchui_rect* frameBound,
	sjme_attrOutNullable sjme_scritchui_rect* contentBound)
{
	sjme_scritchui_uiWindow inWindow;
	sjme_scritchui_rect resultFrame, resultContent;
	HWND window;
	RECT winRect, clientRect;
	POINT clientOrig;
	sjme_jboolean notReady;
	sjme_jint menuH, dpi;
	sjme_scritchui_win32_intern_GSMFD smDpi;
	
	if (inState == NULL || inContainer == NULL ||
		(contentSize == NULL && frameBound == NULL && contentBound == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover the window. */
	inWindow = SJME_SUI_CAST_WINDOW(inContainer);
	if (inWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Recover window. */
	window = inWindow->component.common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Clear results. */
	memset(&resultFrame, 0, sizeof(resultFrame));
	memset(&resultContent, 0, sizeof(resultContent));
	
	/* Calculate the overhead of the window, we need to do this with knowing */
	/* the client rectangle and the window rectangle as GetSystemMetrics */
	/* returns old information for compatibility purposes, which breaks */
	/* on HiDPI or Vista Glass. */
	memset(&winRect, 0, sizeof(winRect));
	memset(&clientRect, 0, sizeof(clientRect));
	memset(&clientOrig, 0, sizeof(clientOrig));
	
	/* If either of these fail, the window exists but is not on the screen */
	/* so any attempts to place it will fail. */
	notReady = SJME_JNI_FALSE;
	if (0 == GetWindowRect(window, &winRect))
		notReady = SJME_JNI_TRUE;
	if (0 == GetClientRect(window, &clientRect))
		notReady = SJME_JNI_TRUE;
	if (0 == ClientToScreen(window, &clientOrig))
		notReady = SJME_JNI_TRUE;
	
	/* Do nothing yet if this is the case. */
	if (notReady)
		return SJME_ERROR_NONE;
	
	/* Calculate the frame bounds from the window rectangle. */
	resultFrame.s.x = winRect.left;
	resultFrame.s.y = winRect.top;
	resultFrame.d.width = abs(winRect.right - winRect.left);
	resultFrame.d.height = abs(winRect.bottom - winRect.top);
	
	/* The client rectangle always starts at (0, 0) so we have to convert */
	/* those to screen coordinates to get the actual position of the content */
	/* area on the screen. */
	resultContent.s.x = clientOrig.x;
	resultContent.s.y = clientOrig.y;
	resultContent.d.width = abs(clientRect.right - clientRect.left);
	resultContent.d.height = abs(clientRect.bottom - clientRect.top);

	/* The menu bar is not considered part of the client area unlike in */
	/* other windowing systems such as GTK. */
	
	/* Give the results. */
	if (frameBound != NULL)
		memmove(frameBound, &resultFrame, sizeof(resultFrame));
	if (contentBound != NULL)
		memmove(contentBound, &resultContent, sizeof(resultContent));
	if (contentSize != NULL)
		memmove(contentSize, &resultContent.d, sizeof(resultContent.d));
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_pointer ignored)
{
	HWND window;
	WNDCLASSEX windowClass;
	ATOM classAtom;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Register window class for this window. */
	memset(&windowClass, 0, sizeof(windowClass));
	windowClass.cbSize = sizeof(windowClass);
	windowClass.hInstance = GetModuleHandle(NULL);
	windowClass.hCursor = LoadCursor(NULL, IDC_ARROW);
	windowClass.lpszClassName = inWindow->component.strId;
	windowClass.lpfnWndProc = (WNDPROC)inState->implIntern->windowProcWin32;
	SetLastError(0);
	classAtom = RegisterClassEx(&windowClass);
	if (classAtom == 0)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Create new window. */
	SetLastError(0);
	window = CreateWindowEx(
		WS_EX_APPWINDOW | WS_EX_CONTROLPARENT |
			WS_EX_OVERLAPPEDWINDOW,
		inWindow->component.strId,
		"SquirrelJME",
		WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, CW_USEDEFAULT,
		CW_USEDEFAULT, CW_USEDEFAULT,
		NULL,
		NULL,
		GetModuleHandle(NULL),
		NULL);
	if (window == NULL)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED);
	
	/* Store handle. */
	inWindow->component.common.handle[SJME_SUI_WIN32_H_HWND] = window;
	inWindow->component.common.handle[SJME_SUI_WIN32_H_HWNDATOM] =
		(sjme_scritchui_handle)classAtom;
		
	/* Link back to this window. */
	SetLastError(0);
	SetWindowLongPtr(window, GWLP_USERDATA,
		(LONG_PTR)inWindow);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_windowSetFlags(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_jint setFlags,
	sjme_attrOutNullable sjme_jint* actualFlags)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_win32_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar)
{
	HWND window;
	HMENU menu;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window and menu. */
	window = inWindow->component.common.handle[SJME_SUI_WIN32_H_HWND];
	menu = (inMenuBar == NULL ? NULL :
		inMenuBar->menuKind.common.handle[SJME_SUI_WIN32_H_HMENU]);
	
	/* Set the new menu. */
	SetLastError(0);
	if (0 == SetMenu(window, menu))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_windowSetState(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_windowState setState,
	sjme_attrOutNullable sjme_scritchui_windowState* actualState)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_win32_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	HWND window;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	window = inWindow->component.common.handle[SJME_SUI_WIN32_H_HWND];

	/* Change visibility. */
	SetLastError(0);
	SetWindowPos(window, HWND_TOP,
		0, 0, 0, 0, SWP_NOSIZE | SWP_NOMOVE);
	ShowWindow(window, (isVisible ? SW_SHOW : SW_HIDE));
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
