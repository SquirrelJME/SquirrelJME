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

sjme_errorCode sjme_scritchui_win32_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScrollPanel inScrollPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	HWND window;
	WNDCLASSEX windowClass;
	ATOM classAtom;
	
	if (inState == NULL || inScrollPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Register window class for this window. */
	memset(&windowClass, 0, sizeof(windowClass));
	windowClass.cbSize = sizeof(windowClass);
	windowClass.style = CS_VREDRAW | CS_HREDRAW | CS_OWNDC;
	windowClass.hInstance = GetModuleHandle(NULL);
	windowClass.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	windowClass.lpszClassName = inScrollPanel->component.strId;
	windowClass.lpfnWndProc = inState->implIntern->windowProcWin32;
	SetLastError(0);
	classAtom = RegisterClassEx(&windowClass);
	if (classAtom == 0)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Create window, child windows must always have a parent. */
	SetLastError(0);
	window = CreateWindowEx(WS_EX_TRANSPARENT | WS_EX_RIGHTSCROLLBAR |
		WS_EX_COMPOSITED,
		inScrollPanel->component.strId,
		"SquirrelJME",
		WS_CHILD | WS_CLIPCHILDREN | WS_CLIPSIBLINGS |
			WS_HSCROLL | WS_VSCROLL,
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
	inScrollPanel->component.common.handle[SJME_SUI_WIN32_H_HWND] = window;
	inScrollPanel->component.common.handle[SJME_SUI_WIN32_H_HWNDATOM] =
		(sjme_scritchui_handle)classAtom;
	
	/* Link back to this panel. */
	SetLastError(0);
	SetWindowLongPtr(window, GWLP_USERDATA,
		(LONG_PTR)inScrollPanel);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
