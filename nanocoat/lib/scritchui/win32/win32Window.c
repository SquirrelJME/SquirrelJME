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

sjme_errorCode sjme_scritchui_win32_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	HWND window;
	WINDOWPLACEMENT placement;
	RECT winRect;
	POINT clientPoint;
	sjme_scritchui_dim* overhead;
	sjme_jboolean notReady;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	window = inWindow->component.common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Calculate the overhead of the window, we need to do this with knowing */
	/* the client rectangle and the window rectangle as GetSystemMetrics */
	/* returns old information for compatibility purposes, which breaks */
	/* on HiDPI or Vista Glass. */
	overhead = &inWindow->minOverhead;
	memset(&winRect, 0, sizeof(winRect));
	memset(&clientPoint, 0, sizeof(clientPoint));
	
	/* If either of these fail, the window exists but is not on the screen */
	/* so any attempts to place it will fail. */
	notReady = SJME_JNI_FALSE;
	if (0 == GetWindowRect(window, &winRect))
		notReady = SJME_JNI_TRUE;
	if (0 == ClientToScreen(window, &clientPoint))
		notReady = SJME_JNI_TRUE;
	
	/* Do nothing yet if this is the case. */
	if (notReady)
		return SJME_ERROR_NONE;
	
	/* The X coordinates are effectively a lie. */
	overhead->width = (clientPoint.x - winRect.left) +
		(GetSystemMetrics(SM_CXSIZEFRAME) * 2) +
		(GetSystemMetrics(SM_CXEDGE) * 2);
	
	/* This is correct. */
	overhead->height = (clientPoint.y - winRect.top) +
		GetSystemMetrics(SM_CYSIZEFRAME) +
		GetSystemMetrics(SM_CYEDGE);
	
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
	windowClass.lpszClassName = inWindow->component.strId;
	windowClass.lpfnWndProc = inState->implIntern->windowProcWin32;
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
