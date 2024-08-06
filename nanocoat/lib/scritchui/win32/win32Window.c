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
	
	/* Calculate the overhead of the window. */
	overhead = &inWindow->minOverhead;
	overhead->width = GetSystemMetrics(SM_CXSIZEFRAME);
	overhead->height = GetSystemMetrics(SM_CYSIZEFRAME) +
		GetSystemMetrics(SM_CYCAPTION);
	
	/* Add menu bar height? */
	if (inWindow->menuBar != NULL)
		overhead->height += GetSystemMetrics(SM_CYMENU);
	
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
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
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
	windowClass.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	windowClass.lpszClassName = inWindow->strId;
	windowClass.lpfnWndProc = sjme_scritchui_win32_windowProcForward;
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
		(LPCSTR)classAtom,
		"SquirrelJME",
		WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, CW_USEDEFAULT,
		CW_USEDEFAULT, CW_USEDEFAULT,
		HWND_DESKTOP,
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
	ShowWindow(window, (isVisible ? SW_SHOW : SW_HIDE));
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
