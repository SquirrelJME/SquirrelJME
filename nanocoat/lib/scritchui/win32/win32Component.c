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

sjme_errorCode sjme_scritchui_win32_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	RECT rect;
	sjme_jboolean noRect;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NONE;
	
	memset(&rect, 0, sizeof(rect));
	noRect = (width == INT32_MAX || height == INT32_MAX);
	rect.left = x;
	rect.right = x + width;
	rect.top = y;
	rect.bottom = y + height;
	
	/* Invalidate the window rectangle. */
	if (0 == InvalidateRect(
		inComponent->common.handle[SJME_SUI_WIN32_H_HWND],
		(noRect ? NULL : &rect), FALSE))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint))
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NONE;
	
	/* Nothing needs to be done as we handle WM_PAINT elsewhere. */
	return SJME_ERROR_NONE;
}
