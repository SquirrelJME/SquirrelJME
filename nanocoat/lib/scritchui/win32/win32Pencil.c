/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"

static sjme_errorCode sjme_scritchui_win32_pencilUpdatePen(
	sjme_scritchui_pencil g)
{
	sjme_scritchui inState;
	HWND hWnd;
	HDC hDc;
	LOGPEN penInfo;
	HPEN hPen, oldHPen;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window and drawing context. */
	inState = g->common.state;
	hWnd = g->frontEnd.wrapper;
	hDc = g->frontEnd.data;
	
	/* Setup pen details. */
	memset(&penInfo, 0, sizeof(penInfo));
	penInfo.lopnColor = RGB(g->state.color.r,
		g->state.color.g,
		g->state.color.b);
	penInfo.lopnWidth.x = 1;
	if (g->state.stroke == SJME_SCRITCHUI_PENCIL_STROKE_DOTTED)
		penInfo.lopnStyle = PS_DOT;
	else
		penInfo.lopnStyle = PS_SOLID;
	
	/* Create new pen. */
	hPen = CreatePenIndirect(&penInfo);
	
	/* Select new pen. */
	oldHPen = SelectObject(hDc, hPen);
	if (oldHPen != NULL)
		DeleteObject(oldHPen);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_win32_pencilDrawHorizSrc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_scritchui inState;
	HDC hDc;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window and drawing context. */
	inState = g->common.state;
	hDc = g->frontEnd.data;
	
	/* Draw line. */
	MoveToEx(hDc, x, y, NULL);
	LineTo(hDc, x + w, y);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_win32_pencilDrawLineSrc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	sjme_scritchui inState;
	HDC hDc;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window and drawing context. */
	inState = g->common.state;
	hDc = g->frontEnd.data;
	
	/* Draw line. */
	MoveToEx(hDc, x1, y1, NULL);
	LineTo(hDc, x2, y2);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_win32_pencilRawScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_scritchui inState;
	HWND hWnd;
	HDC hDc;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window and drawing context. */
	inState = g->common.state;
	hWnd = g->frontEnd.wrapper;
	hDc = g->frontEnd.data;

#if 0
	return sjme_error_notImplemented(0);
#endif
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_win32_pencilRawScanPutPure(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) sjme_cpointer srcRaw,
	sjme_attrInPositiveNonZero sjme_jint srcRawLen,
	sjme_attrInPositiveNonZero sjme_jint srcNumPixels)
{
	sjme_scritchui inState;
	HWND hWnd;
	HDC hDc;
	LPBITMAPINFO bitmap;
	DWORD* bmi;
	sjme_jint bmpLen;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window and drawing context. */
	inState = g->common.state;
	hWnd = g->frontEnd.wrapper;
	hDc = g->frontEnd.data;
	
	/* Allocate bitmap info. */
	bmpLen = sizeof(BITMAPINFO) + (sizeof(DWORD) * 4);
	bitmap = sjme_alloca(bmpLen);
	if (bitmap == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(bitmap, 0, bmpLen);
	
	/* Setup bitmap for the scan. */
	bitmap->bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
	bitmap->bmiHeader.biBitCount = 32;
	bitmap->bmiHeader.biWidth = srcNumPixels;
	bitmap->bmiHeader.biHeight = 1;
	bitmap->bmiHeader.biPlanes = 1;
	bitmap->bmiHeader.biSizeImage = srcRawLen;
	bitmap->bmiHeader.biCompression = BI_RGB | BI_BITFIELDS;
	
	/* Set RGB plane. */
	bmi = (DWORD*)(&bitmap->bmiColors[0]);
	bmi[0] = 0xFF0000;
	bmi[1] = 0x00FF00;
	bmi[2] = 0x0000FF;
	
	/* Perform the drawing. */
	SetLastError(0);
	if (GDI_ERROR == StretchDIBits(hDc,
		x, y,
		srcNumPixels, 1,
		0, 0,
		srcNumPixels, 1,
		srcRaw,
		bitmap,
		DIB_RGB_COLORS,
		SRCCOPY))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_win32_pencilSetAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	return sjme_scritchui_win32_pencilUpdatePen(g);
}

static sjme_errorCode sjme_scritchui_win32_pencilSetClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_scritchui inState;
	HWND hWnd;
	HDC hDc;
	HRGN region;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window and drawing context. */
	inState = g->common.state;
	hWnd = g->frontEnd.wrapper;
	hDc = g->frontEnd.data;
	
	/* Setup clipping region. */
	SetLastError(0);
	region = CreateRectRgn(x, y, x + w, y + h);
	if (region == NULL)
		goto fail_setClip;
	
	/* Set clipping rectangle. */
	SetLastError(0);
	if (ERROR == SelectClipRgn(hDc, region))
		goto fail_setClip;
	
	/* Make sure the region is deleted. */
	DeleteObject(region);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);

fail_setClip:
	/* Make sure the region is deleted. */
	if (region != NULL)
		DeleteObject(region);
	return inState->implIntern->getLastError(inState,
		SJME_ERROR_NATIVE_WIDGET_FAILURE);
}

static sjme_errorCode sjme_scritchui_win32_pencilSetStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style)
{
	return sjme_scritchui_win32_pencilUpdatePen(g);
}

const sjme_scritchui_pencilImplFunctions sjme_scritchui_win32_pencilFunctions =
{
	.copyArea = NULL,
	.drawHorizSrc = sjme_scritchui_win32_pencilDrawHorizSrc,
	.drawHorizSrcOver = NULL,
	.drawLineSrc = sjme_scritchui_win32_pencilDrawLineSrc,
	.drawLineSrcOver = NULL,
	.drawPixelSrc = NULL,
	.drawPixelSrcOver = NULL,
	.rawScanGet = sjme_scritchui_win32_pencilRawScanGet,
	.rawScanPutPure = sjme_scritchui_win32_pencilRawScanPutPure,
	.setAlphaColor = sjme_scritchui_win32_pencilSetAlphaColor,
	.setBlendingMode = NULL,
	.setClip = sjme_scritchui_win32_pencilSetClip,
	.setStrokeStyle = sjme_scritchui_win32_pencilSetStrokeStyle,
};
