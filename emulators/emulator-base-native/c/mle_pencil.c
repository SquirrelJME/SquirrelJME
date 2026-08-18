/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include <string.h>

/* //// MLE /// */
#define mleGroupId PencilShelf
#define mleShelfClass "cc/squirreljme/jvm/mle/PencilShelf"
#define mleProxyTarget "cc/squirreljme/emulator/uiform/SwingPencilShelf"
#include "squirreljmeMle.h"
/* //////////// */

#include "sjme/debug.h"
#include "squirreljme.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/scritchui.h"

#define MLE_DESC_hardwareCloseGraphics DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL)
MLE_FUNC_PROTO(void, hardwareCloseGraphics, jobject g)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->close(p)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareCopyArea DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareCopyArea, jobject g, jint sx, jint sy, jint w, jint h,
	jint dw, jint dh, jint anchor)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->copyArea(p, sx, sy, w, h, dw, dh,
		anchor)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawArc DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawArc, jobject g, jint x, jint y, jint w, jint h,
	jint startAngle, jint arcAngle)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawArc(p, x, y, w, h, startAngle,
			arcAngle)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawChar DESC_METHOD(DESC_VOID, \
	DESC_PENCIL DESC_CHAR DESC_INT DESC_INT DESC_INT)
MLE_FUNC_PROTO(void, hardwareDrawChar, jobject g, jchar c,
	jint x, jint y, jint anchor)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawChar(p,
		c, x, y, anchor, NULL)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawChars DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_ARRAY(DESC_CHAR) DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawChars, jobject g, jcharArray s, jint o, jint l,
	jint x, jint y, jint anchor)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	jchar* chars;
	jboolean isCopy;
	sjme_jint len;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL || s == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Double check length. */
	len = (*env)->GetArrayLength(env, s);
	if (o < 0 || l < 0 || (o + l) > len || (o + l) < 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_INDEX_OUT_OF_BOUNDS);
		return;
	}

	/* Get character array. */
	chars = NULL;
	isCopy = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_jni_arrayGetElements(env, s,
		(sjme_pointer*)&chars, &isCopy, NULL)) || chars == NULL)
	{
		sjme_jni_throwMLECallError(env, error);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawChars(p,
		&chars[o], 0, l, x, y, anchor)))
		sjme_jni_throwMLECallError(env, error);

	/* Cleanup. */
	sjme_jni_arrayReleaseElements(env, s, chars);
}

#define MLE_DESC_hardwareDrawHoriz DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawHoriz, jobject g, jint x, jint y, jint w)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawHoriz(p, x, y, w)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawLine DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawLine, jobject g, jint x1, jint y1, jint x2, jint y2)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawLine(p, x1, y1, x2, y2)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawPixel DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawPixel, jobject g, jint x, jint y)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawPixel(p, x, y)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawPolyline DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_ARRAY(DESC_INT) DESC_INT DESC_ARRAY(DESC_INT) DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawPolyline, jobject g, jintArray xPoints, jint xOffset,
		jintArray yPoints, jint yOffset, jint nPoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	jboolean isCopyX, isCopyY;
	jint* xElements;
	jint* yElements;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	isCopyX = JNI_FALSE;
	xElements = (*env)->GetIntArrayElements(env, xPoints, &isCopyX);
	if (xElements == NULL)
	{
		sjme_jni_throwMLECallError(env,
			SJME_ERROR_NATIVE_ARRAY_ACCESS_FAILED);
		return;
	}

	isCopyY = JNI_FALSE;
	yElements = (*env)->GetIntArrayElements(env, yPoints, &isCopyY);
	if (yElements == NULL)
	{
		sjme_jni_throwMLECallError(env,
			SJME_ERROR_NATIVE_ARRAY_ACCESS_FAILED);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawPolyline(p,
		(sjme_jint*)xElements, xOffset,
		(sjme_jint*)yElements, yOffset, nPoints)))
		sjme_jni_throwMLECallError(env, error);

	/* Cleanup. */
	(*env)->ReleaseIntArrayElements(env, xPoints, xElements, 0);
	(*env)->ReleaseIntArrayElements(env, yPoints, yElements, 0);
}

#define MLE_DESC_hardwareDrawRect DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawRect, jobject g, jint x, jint y, jint w, jint h)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawRect(p, x, y, w, h)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawRegion DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_OBJECT DESC_INT DESC_INT DESC_BOOLEAN \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawRegion, jobject g, jint pf, jobject data,
	jint off, jint scanLen, jboolean alpha,
	jint xSrc, jint ySrc, jint wSrc, jint hSrc, jint trans, jint xDest,
	jint yDest, jint anchor, jint wDest, jint hDest,
	jint origImgWidth, jint origImgHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	jboolean isCopy;
	sjme_pointer dataElem;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL || data == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Need this. */
	isCopy = JNI_FALSE;
	dataElem = NULL;

	if (sjme_error_is(error = sjme_jni_arrayGetElements(env, data, &dataElem,
		&isCopy, NULL)) || dataElem == NULL)
	{
		sjme_jni_throwMLECallError(env,
			SJME_ERROR_NATIVE_ARRAY_ACCESS_FAILED);
		return;
	}

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	if (xSrc != 0 || ySrc != 0)
		sjme_message("s(%d, %d)", xSrc, ySrc);
#endif

	/* Forward. */
	if (sjme_error_is(error = p->api->drawRegion(p, pf,
		(sjme_cpointer) dataElem, off,
		(*env)->GetArrayLength(env, data), scanLen,
		alpha, xSrc, ySrc, wSrc, hSrc, trans,
		xDest, yDest, anchor, wDest, hDest,
		origImgWidth, origImgHeight)))
		sjme_jni_throwMLECallError(env, error);

	/* Cleanup. */
	sjme_jni_arrayReleaseElements(env, data, dataElem);
}

#define MLE_DESC_hardwareDrawRoundRect DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawRoundRect, jobject g, jint x, jint y, jint w, jint h,
	jint arcWidth, jint arcHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawRoundRect(p, x, y, w, h, arcWidth,
			arcHeight)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawTriangle DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawTriangle, jobject g, jint x1, jint y1, jint x2, jint y2,
	jint x3, jint y3)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->drawTriangle(p, x1, y1, x2, y2, x3, y3)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawSubstring DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_STRING DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawSubstring,
	jobject jG, jstring s, jint o, jint l, jint x, jint y, jint anchor)
{
	sjme_scritchui_pencil g;
	sjme_errorCode error;
	sjme_charSeqStatic seq;

	/* Recover. */
	g = sjme_jni_recoverPencil(env, jG);
	if (g == NULL || s == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Should be valid. */
	if (g->api == NULL || g->api->drawSubstring == NULL)
	{
		sjme_jni_throwMLECallError(env,
			sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE));
		return;
	}

	/* Get wrapping string sequence. */
	memset(&seq, 0, sizeof(seq));
	if (sjme_error_is(error = sjme_jni_charSeq(env, &seq, s)))
		goto fail_makeSeq;

	/* Forward. */
	if (sjme_error_is(error = g->api->drawSubstring(g,
		&seq, o, l, x, y, anchor)))
		goto fail_drawOp;

	/* Cleanup. */
	if (sjme_error_is(error = sjme_charSeq_free(&seq)))
		goto fail_cleanup;

	return;

fail_drawOp:
	sjme_charSeq_free(&seq);
fail_cleanup:
fail_makeSeq:
	sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareDrawXRGB32Region DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_ARRAY(DESC_INT) DESC_INT DESC_INT DESC_BOOLEAN \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareDrawXRGB32Region, jobject g, jintArray data, jint off,
	jint scanLen, jboolean alpha, jint xSrc, jint ySrc, jint wSrc, jint hSrc,
	jint trans, jint xDest, jint yDest, jint anchor, jint wDest, jint hDest,
	jint origImgWidth, jint origImgHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	jboolean isCopy;
	jint* elem;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL || data == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Need this. */
	isCopy = JNI_FALSE;
	elem = (*env)->GetIntArrayElements(env, data, &isCopy);
	if (elem == NULL)
	{
		sjme_jni_throwMLECallError(env,
			SJME_ERROR_NATIVE_ARRAY_ACCESS_FAILED);
		return;
	}

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	if (xSrc != 0 || ySrc != 0)
		sjme_message("s(%d, %d)", xSrc, ySrc);
#endif

	/* Forward. */
	if (sjme_error_is(error = p->api->drawXRGB32Region(p,
		(sjme_jint*)elem, off, (*env)->GetArrayLength(env, data), scanLen,
		alpha, xSrc, ySrc, wSrc, hSrc, trans,
		xDest, yDest, anchor, wDest, hDest,
		origImgWidth, origImgHeight)))
		sjme_jni_throwMLECallError(env, error);

	/* Cleanup. */
	(*env)->ReleaseIntArrayElements(env, data, elem, 0);
}

#define MLE_DESC_hardwareFillArc DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareFillArc, jobject g, jint x, jint y, jint w, jint h,
	jint startAngle, jint arcAngle)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->fillArc(p, x, y, w, h, startAngle,
			arcAngle)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareFillPolygon DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_ARRAY(DESC_INT) DESC_INT DESC_ARRAY(DESC_INT) DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareFillPolygon, jobject g, jintArray xPoints, jint xOffset,
		jintArray yPoints, jint yOffset, jint nPoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	jboolean isCopyX, isCopyY;
	jint* xElements;
	jint* yElements;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	isCopyX = JNI_FALSE;
	xElements = (*env)->GetIntArrayElements(env, xPoints, &isCopyX);
	if (xElements == NULL)
	{
		sjme_jni_throwMLECallError(env,
			SJME_ERROR_NATIVE_ARRAY_ACCESS_FAILED);
		return;
	}

	isCopyY = JNI_FALSE;
	yElements = (*env)->GetIntArrayElements(env, yPoints, &isCopyY);
	if (yElements == NULL)
	{
		sjme_jni_throwMLECallError(env,
			SJME_ERROR_NATIVE_ARRAY_ACCESS_FAILED);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->fillPolygon(p,
		(sjme_jint*)xElements, xOffset,
		(sjme_jint*)yElements, yOffset, nPoints)))
		sjme_jni_throwMLECallError(env, error);

	/* Cleanup. */
	(*env)->ReleaseIntArrayElements(env, xPoints, xElements, 0);
	(*env)->ReleaseIntArrayElements(env, yPoints, yElements, 0);
}

#define MLE_DESC_hardwareFillRect DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareFillRect, jobject g, jint x, jint y, jint w, jint h)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->fillRect(p, x, y, w, h)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareFillRoundRect DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareFillRoundRect, jobject g, jint x, jint y, jint w, jint h,
	jint arcWidth, jint arcHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->fillRoundRect(p, x, y, w, h, arcWidth,
			arcHeight)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareFillTriangle DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT )
MLE_FUNC_PROTO(void, hardwareFillTriangle, jobject g, jint x1, jint y1, jint x2, jint y2,
	jint x3, jint y3)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->fillTriangle(p,
		x1, y1, x2, y2, x3, y3)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareGetPixelFormat DESC_METHOD(DESC_INT,  \
	DESC_PENCIL )
MLE_FUNC_PROTO(jint, hardwareGetPixelFormat, jobject g)
{
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return JNI_FALSE;
	}

	/* Simply return the pencil's pixel format */
	return p->pixelFormat;
}

#define MLE_DESC_hardwareGetRegion DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_OBJECT DESC_INT DESC_INT DESC_BOOLEAN \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareGetRegion, jobject g, jint pf, jobject data,
	jint off, jint scanLen, jboolean alpha, jint xSrc,
	jint ySrc, jint wSrc, jint hSrc, jint anchor)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	jboolean isCopy;
	sjme_pointer dataElem;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL || data == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Need this. */
	isCopy = JNI_FALSE;
	dataElem = NULL;
	if (sjme_error_is(error = sjme_jni_arrayGetElements(env, data, &dataElem,
		&isCopy, NULL)) || dataElem == NULL)
	{
		sjme_jni_throwMLECallError(env,
			SJME_ERROR_NATIVE_ARRAY_ACCESS_FAILED);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->getRegion(p, pf,
		(sjme_cpointer) dataElem, off,
		(*env)->GetArrayLength(env, data), scanLen, alpha, xSrc,
		ySrc, wSrc, hSrc, anchor)))
		sjme_jni_throwMLECallError(env, error);

	/* Cleanup. */
	sjme_jni_arrayReleaseElements(env, data, dataElem);
}

#define MLE_DESC_hardwareHasAlpha DESC_METHOD(DESC_BOOLEAN,  \
	DESC_PENCIL )
MLE_FUNC_PROTO(jboolean, hardwareHasAlpha, jobject g)
{
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return JNI_FALSE;
	}

	/* Is there an alpha channel? */
	return (p->hasAlpha ? JNI_TRUE : JNI_FALSE);
}

#define MLE_DESC_hardwareSetAlphaColor DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT )
MLE_FUNC_PROTO(void, hardwareSetAlphaColor, jobject g, jint argb)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->setAlphaColor(p, argb)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareSetBlendingMode DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT )
MLE_FUNC_PROTO(void, hardwareSetBlendingMode, jobject g, jint mode)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->setBlendingMode(p, mode)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareSetClip DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareSetClip, jobject g, jint x, jint y, jint w, jint h)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward the clip. */
	if (sjme_error_is(error = p->api->setClip(p, x, y, w, h)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareSetDefaultFont DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL )
MLE_FUNC_PROTO(void, hardwareSetDefaultFont, jobject g)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->setDefaultFont(p)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareSetFont DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_PENCILFONT DESC_ARRAY(DESC_INT))
MLE_FUNC_PROTO(void, hardwareSetFont, jobject jG, jobject jF,
	jintArray jFPI)
{
	sjme_errorCode error;
	sjme_scritchui_pencil g;
	sjme_scritchui_pencilFont fp;
	sjme_scritchui_pencilFontParam fontParams;

	/* Recover. */
	g = sjme_jni_recoverPencil(env, jG);
	fp = sjme_jni_recoverFont(env, jF);
	if (g == NULL || fp == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Should be valid. */
	if (g->api == NULL || g->api->setFont == NULL)
	{
		sjme_jni_throwMLECallError(env,
			sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE));
		return;
	}

	/* Map params. */
	memset(&fontParams, 0, sizeof(fontParams));
	if (jFPI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env,
			g->common.state, &fontParams, jFPI)))
		{
			sjme_jni_throwMLECallError(env, error);
			return;
		}

	/* Forward. */
	if (sjme_error_is(error = g->api->setFont(g, fp,
		(jFPI != NULL ? &fontParams : NULL))))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareSetStrokeStyle DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT )
MLE_FUNC_PROTO(void, hardwareSetStrokeStyle, jobject g, jint style)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = p->api->setStrokeStyle(p, style)))
		sjme_jni_throwMLECallError(env, error);
}

#define MLE_DESC_hardwareTranslate DESC_METHOD(DESC_VOID,  \
	DESC_PENCIL DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, hardwareTranslate, jobject g, jint x, jint y)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	if (sjme_error_is(error = p->api->translate(p, x, y)))
		sjme_jni_throwMLECallError(env, error);\
}

#define MLE_DESC_hardwareTranslateXY DESC_METHOD(DESC_INT,  \
	DESC_PENCIL DESC_BOOLEAN )
MLE_FUNC_PROTO(jint, hardwareTranslateXY, jobject g, jboolean y)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;

	/* Recover. */
	p = sjme_jni_recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	if (y)
		return p->state.translate.y;
	return p->state.translate.x;
}

#define MLE_DESC_nativeImageLoadRGBA DESC_METHOD(DESC_OBJECT, \
	DESC_INT DESC_ARRAY(DESC_BYTE) DESC_INT DESC_INT \
	DESC_CLASS("cc/squirreljme/jvm/mle/callbacks/NativeImageLoadCallback"))
MLE_FUNC_PROXY_STATIC(jobject, nativeImageLoadRGBA)

#define MLE_DESC_nativeImageLoadTypes DESC_METHOD(DESC_INT, )
MLE_FUNC_PROXY_STATIC(jobject, nativeImageLoadTypes)

MLE_LIST_BEGIN()
	MLE_LIST_ITEM(hardwareCloseGraphics),
	MLE_LIST_ITEM(hardwareCopyArea),
	MLE_LIST_ITEM(hardwareDrawArc),
	MLE_LIST_ITEM(hardwareDrawChar),
	MLE_LIST_ITEM(hardwareDrawChars),
	MLE_LIST_ITEM(hardwareDrawHoriz),
	MLE_LIST_ITEM(hardwareDrawLine),
	MLE_LIST_ITEM(hardwareDrawPixel),
	MLE_LIST_ITEM(hardwareDrawPolyline),
	MLE_LIST_ITEM(hardwareDrawRect),
	MLE_LIST_ITEM(hardwareDrawRegion),
	MLE_LIST_ITEM(hardwareDrawRoundRect),
	MLE_LIST_ITEM(hardwareDrawTriangle),
	MLE_LIST_ITEM(hardwareDrawSubstring),
	MLE_LIST_ITEM(hardwareDrawXRGB32Region),
	MLE_LIST_ITEM(hardwareFillArc),
	MLE_LIST_ITEM(hardwareFillPolygon),
	MLE_LIST_ITEM(hardwareFillRect),
	MLE_LIST_ITEM(hardwareFillRoundRect),
	MLE_LIST_ITEM(hardwareFillTriangle),
	MLE_LIST_ITEM(hardwareGetPixelFormat),
	MLE_LIST_ITEM(hardwareGetRegion),
	MLE_LIST_ITEM(hardwareHasAlpha),
	MLE_LIST_ITEM(hardwareSetAlphaColor),
	MLE_LIST_ITEM(hardwareSetBlendingMode),
	MLE_LIST_ITEM(hardwareSetClip),
	MLE_LIST_ITEM(hardwareSetDefaultFont),
	MLE_LIST_ITEM(hardwareSetFont),
	MLE_LIST_ITEM(hardwareSetStrokeStyle),
	MLE_LIST_ITEM(hardwareTranslate),
	MLE_LIST_ITEM(hardwareTranslateXY),
	MLE_LIST_ITEM(nativeImageLoadRGBA),
	MLE_LIST_ITEM(nativeImageLoadTypes),
MLE_LIST_END()

