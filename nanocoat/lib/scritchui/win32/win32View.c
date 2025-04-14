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

sjme_errorCode sjme_scritchui_win32_viewGetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect)
{
	HWND window;
	SCROLLINFO hInfo, vInfo;
	RECT rect;
	
	if (inComponent == NULL || inComponent == NULL || outViewRect == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover window. */
	window = inComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Obtain horizontal scroll info. */
	memset(&hInfo, 0, sizeof(hInfo));
	hInfo.cbSize = sizeof(hInfo);
	hInfo.fMask = SIF_PAGE | SIF_POS | SIF_RANGE | SIF_TRACKPOS;
	SetLastError(0);
	if (0 == GetScrollInfo(window, SB_HORZ, &hInfo))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
			
	/* Obtain vertical scroll info. */
	memset(&vInfo, 0, sizeof(vInfo));
	vInfo.cbSize = sizeof(vInfo);
	vInfo.fMask = SIF_PAGE | SIF_POS | SIF_RANGE | SIF_TRACKPOS;
	SetLastError(0);
	if (0 == GetScrollInfo(window, SB_VERT, &vInfo))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
			
	/* Get the rectangle of the viewport, to determine the dimension. */
	memset(&rect, 0, sizeof(rect));
	SetLastError(0);
	if (0 == GetClientRect(window, &rect))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Return scroll details. */
	outViewRect->s.x = hInfo.nPos;
	outViewRect->s.y = vInfo.nPos;
	outViewRect->d.width = rect.right - rect.left;
	outViewRect->d.height = rect.bottom - rect.top;
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewPage)
{
	sjme_errorCode error;
	HWND window;
	SCROLLINFO info;
	
	if (inState == NULL || inComponent == NULL || inViewArea == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover window. */
	window = inComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Setup base scroll info. */
	memset(&info, 0, sizeof(info));
	info.cbSize = sizeof(info);
	info.fMask = SIF_RANGE | SIF_PAGE;
	
	/* Horizontal scroll. */
	info.nMax = inViewArea->width;
	info.nPage = inViewPage->width;
	SetLastError(0);
	SetScrollInfo(window, SB_HORZ, &info, TRUE);
	if (sjme_error_is(error = inState->implIntern->getLastError(inState,
		SJME_ERROR_NONE)))
		return sjme_error_default(error);
	
	/* Vertical scroll. */
	info.nMax = inViewArea->height;
	info.nPage = inViewPage->height;
	SetLastError(0);
	SetScrollInfo(window, SB_VERT, &info, TRUE);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_viewSetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_point* inViewPos)
{
	sjme_errorCode error;
	HWND window;
	SCROLLINFO info;
	
	if (inState == NULL || inComponent == NULL || inViewPos == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover window. */
	window = inComponent->common.handle[SJME_SUI_WIN32_H_HWND];

#if 0
	/* Debug. */
	sjme_message("setView(%p, %p, (%d, %d))",
		inState, inComponent, inViewPos->x, inViewPos->y);
#endif
	
	/* Setup base scroll info. */
	memset(&info, 0, sizeof(info));
	info.cbSize = sizeof(info);
	info.fMask = SIF_POS;
	
	/* Horizontal scroll. */
	info.nPos = inViewPos->x;
	SetLastError(0);
	SetScrollInfo(window, SB_HORZ, &info, TRUE);
	if (sjme_error_is(error = inState->implIntern->getLastError(inState,
		SJME_ERROR_NONE)))
		return sjme_error_default(error);
	
	/* Vertical scroll. */
	info.nPos = inViewPos->y;
	SetLastError(0);
	SetScrollInfo(window, SB_VERT, &info, TRUE);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
