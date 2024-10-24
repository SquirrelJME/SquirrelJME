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

sjme_errorCode sjme_scritchui_win32_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	HWND window;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	window = inComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Grab the focus for our window. */
	SetLastError(0);
	SetFocus(window);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus)
{
	HWND window;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	window = inComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* We have the focus if our window is returned here. */
	SetLastError(0);
	*outHasFocus = (window == GetFocus());
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_componentPosition(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outX,
	sjme_attrOutNullable sjme_jint* outY)
{
	HWND window;
	RECT rect;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	window = inComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Get window rect. */
	memset(&rect, 0, sizeof(rect));
	SetLastError(0);
	if (0 == GetWindowRect(window, &rect))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Just copy left/top coordinates. */
	if (outX != NULL)
		*outX = rect.left;
	if (outY != NULL)
		*outY = rect.top;
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

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
		return SJME_ERROR_NULL_ARGUMENTS;
	
	memset(&rect, 0, sizeof(rect));
	noRect = (width == INT32_MAX || height == INT32_MAX);
	rect.left = x;
	rect.right = x + width;
	rect.top = y;
	rect.bottom = y + height;
	
	/* Invalidate the window rectangle. */
	SetLastError(0);
	if (0 == InvalidateRect(
		inComponent->common.handle[SJME_SUI_WIN32_H_HWND],
		(noRect ? NULL : &rect), FALSE))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Nothing needs to be done. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint))
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Nothing needs to be done as we handle WM_PAINT elsewhere. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight)
{
	HWND window;
	RECT rect;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	window = inComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Get window rect. */
	memset(&rect, 0, sizeof(rect));
	SetLastError(0);
	if (0 == GetWindowRect(window, &rect))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Calculate dimension from the corner coordinates. */
	if (outWidth != NULL)
		*outWidth = rect.right - rect.left;
	if (outHeight != NULL)
		*outHeight = rect.bottom - rect.top;
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
