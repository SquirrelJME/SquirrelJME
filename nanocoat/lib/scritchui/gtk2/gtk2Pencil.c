/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gdk/gdk.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"

static sjme_errorCode sjme_scritchui_gtk2_pencilCopyArea(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilDrawChars(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilDrawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilDrawRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilDrawSubstring(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_lpcstr s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilDrawXRGB32Region(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull int* data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anch,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilFillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilFillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetBlendingMode(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint mode)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetDefaultFont(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetFont(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_lpcstr name,
	sjme_attrInValue sjme_jint style,
	sjme_attrInPositiveNonZero sjme_jint pixelSize)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint style)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilTranslate(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

const sjme_scritchui_pencilFunctions sjme_scritchui_gtk2_pencilFunctions =
{
};
