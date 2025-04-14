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

sjme_errorCode sjme_scritchui_win32_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus)
{
	sjme_errorCode error;
	HWND window;
	LONG value, change;
	
	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	window = inPanel->component.common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Get the current window style. */
	SetLastError(0);
	value = GetWindowLong(window, GWL_STYLE);
	if (value == 0)
		if (sjme_error_is(error = inState->implIntern->getLastError(
			inState, SJME_ERROR_NONE)))
			return error;
	
	/* Make the window accept tab stops, or not. */
	if (enableFocus)
	{
		value |= WS_TABSTOP;
		
		if (defaultFocus)
			value |= WS_GROUP;
	}
	else
		value &= ~(WS_TABSTOP | WS_GROUP);
	
	/* Set new value. */
	SetLastError(0);
	change = SetWindowLong(window, GWL_STYLE, value);
	if (change == 0)
		if (sjme_error_is(error = inState->implIntern->getLastError(
			inState, SJME_ERROR_NONE)))
			return error;
	
	/* Despite error being the value of zero, it still seems it can be */
	/* set to something else, despite being successful. */
	SetLastError(0);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	HWND window;
	WNDCLASSEX windowClass;
	ATOM classAtom;
	
	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Register window class for this window. */
	memset(&windowClass, 0, sizeof(windowClass));
	windowClass.cbSize = sizeof(windowClass);
	windowClass.style = CS_VREDRAW | CS_HREDRAW | CS_OWNDC;
	windowClass.hInstance = GetModuleHandle(NULL);
	windowClass.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	windowClass.lpszClassName = inPanel->component.strId;
	windowClass.lpfnWndProc = inState->implIntern->windowProcWin32;
	SetLastError(0);
	classAtom = RegisterClassEx(&windowClass);
	if (classAtom == 0)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Create window, child windows must always have a parent. */
	SetLastError(0);
	window = CreateWindowEx(WS_EX_TRANSPARENT | WS_EX_COMPOSITED,
		inPanel->component.strId,
		"SquirrelJME",
		WS_CHILD | WS_CLIPCHILDREN | WS_CLIPSIBLINGS,
		CW_USEDEFAULT, CW_USEDEFAULT,
		CW_USEDEFAULT, CW_USEDEFAULT,
		inState->common.handle[SJME_SUI_WIN32_H_VOID],
		NULL,
		GetModuleHandle(NULL),
		NULL);
	if (window == NULL)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED);
	
	/* Store handle. */
	inPanel->component.common.handle[SJME_SUI_WIN32_H_HWND] = window;
	inPanel->component.common.handle[SJME_SUI_WIN32_H_HWNDATOM] =
		(sjme_scritchui_handle)classAtom;
	
	/* Link back to this panel. */
	SetLastError(0);
	SetWindowLongPtr(window, GWLP_USERDATA,
		(LONG_PTR)inPanel);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
