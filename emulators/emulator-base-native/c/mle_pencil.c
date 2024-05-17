/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "sjme/debug.h"
#include "squirreljme.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/scritchui.h"

/* The class being implemented. */
#define FORWARD_CLASS "cc/squirreljme/jvm/mle/PencilShelf"
#define FORWARD_NATIVE_CLASS "cc/squirreljme/emulator/uiform/SwingPencilShelf"

/* Natives. */
#define FORWARD_DESC_hardwareCopyArea "(" \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareDrawChars "(" \
	DESC_PENCIL DESC_ARRAY(DESC_CHAR) DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareDrawHoriz "(" \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareDrawLine "(" \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareDrawPixel "(" \
	DESC_PENCIL DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareDrawRect "(" \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareDrawSubstring "(" \
	DESC_PENCIL DESC_STRING DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareDrawXRGB32Region "(" \
	DESC_PENCIL DESC_ARRAY(DESC_INT) DESC_INT DESC_INT DESC_BOOLEAN \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareFillRect "(" \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareFillTriangle "(" \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareGraphics "(" \
	DESC_INT DESC_INT DESC_INT DESC_OBJECT DESC_INT \
	DESC_ARRAY(DESC_INT) DESC_INT DESC_INT DESC_INT DESC_INT ")" DESC_PENCIL
#define FORWARD_DESC_hardwareHasAlpha "(" \
	DESC_PENCIL ")" DESC_BOOLEAN
#define FORWARD_DESC_hardwareSetAlphaColor "(" \
	DESC_PENCIL DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareSetBlendingMode "(" \
	DESC_PENCIL DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareSetClip "(" \
	DESC_PENCIL DESC_INT DESC_INT DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareSetDefaultFont "(" \
	DESC_PENCIL ")" DESC_VOID
#define FORWARD_DESC_hardwareSetFont "(" \
	DESC_PENCIL DESC_PENCILFONT ")" DESC_VOID
#define FORWARD_DESC_hardwareSetStrokeStyle "(" \
	DESC_PENCIL DESC_INT ")" DESC_VOID
#define FORWARD_DESC_hardwareTranslate "(" \
	DESC_PENCIL DESC_INT DESC_INT ")" DESC_VOID

/* Forwards */
#define FORWARD_DESC_capabilities "(I)I"
#define FORWARD_DESC_nativeImageLoadRGBA \
	"(I[BIILcc/squirreljme/jvm/mle/callbacks/NativeImageLoadCallback;)" \
	"Ljava/lang/Object;"
#define FORWARD_DESC_nativeImageLoadTypes "()I"

static sjme_scritchui_pencil recoverPencil(JNIEnv* env, jobject g)
{
	jclass classy;
	jfieldID pointerField;
	
	/* Does not map. */
	if (g == NULL)
		return NULL;

	/* Locate class. */
	classy = (*env)->FindClass(env,
		"cc/squirreljme/emulator/scritchui/dylib/DylibPencilObject");
	if (classy == NULL)
		sjme_die("Could not find DylibPencilObject.");
	
	/* Incorrect type. */
	if (!(*env)->IsInstanceOf(env, g, classy))
		sjme_die("Not a DylibPencilObject");
	
	/* Get the pointer data. */
	pointerField = (*env)->GetFieldID(env, classy, "objectP", "J");
	if (pointerField == NULL)
		sjme_die("No objectP in DylibPencilObject");
	
	/* Cast pencil data. */
	return (sjme_scritchui_pencil)
		((intptr_t)((*env)->GetLongField(env, g, pointerField)));
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareCopyArea)
	(JNIEnv* env, jclass classy, jobject g, jint sx, jint sy, jint w, jint h,
	jint dw, jint dh, jint anchor)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareDrawChars)
	(JNIEnv* env, jclass classy, jobject g, jcharArray s, jint o, jint l,
	jint x, jint y, jint anchor)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareDrawHoriz)
	(JNIEnv* env, jclass classy, jobject g, jint x, jint y, jint w)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Forward. */
	if (sjme_error_is(error = p->api->drawHoriz(p, x, y, w)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareDrawLine)
	(JNIEnv* env, jclass classy, jobject g, jint x1, jint y1, jint x2, jint y2)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Forward. */
	if (sjme_error_is(error = p->api->drawLine(p, x1, y1, x2, y2)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareDrawPixel)
	(JNIEnv* env, jclass classy, jobject g, jint x, jint y)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Forward. */
	if (sjme_error_is(error = p->api->drawPixel(p, x, y)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareDrawRect)
	(JNIEnv* env, jclass classy, jobject g, jint x, jint y, jint w, jint h)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareDrawSubstring)
	(JNIEnv* env, jclass classy, jobject g, jstring s, jint o, jint l,
	jint x, jint y, jint anchor)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareDrawXRGB32Region)
	(JNIEnv* env, jclass classy, jobject g, jintArray data, jint off,
	jint scanLen, jboolean alpha, jint xSrc, jint ySrc, jint wSrc, jint hSrc,
	jint trans, jint xDest, jint yDest, jint wDest, jint hDest,
	jint origImgWidth, jint origImgHeight)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareFillRect)
	(JNIEnv* env, jclass classy, jobject g, jint x, jint y, jint w, jint h)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Forward. */
	if (sjme_error_is(error = p->api->fillRect(p, x, y, w, h)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareFillTriangle)
	(JNIEnv* env, jclass classy, jobject g, jint x1, jint y1, jint x2, jint y2,
	jint x3, jint y3)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}

JNIEXPORT jobject JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareGraphics)
	(JNIEnv* env, jclass classy, jint pf, jint bw, jint bh, jobject buf,
	jint offset, jintArray pal, jint sx, jint sy, jint sw, jint sh)
{
	sjme_todo("Impl?");
	return NULL;
}

JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareHasAlpha)
	(JNIEnv* env, jclass classy, jobject g)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return JNI_FALSE;
	}
	
	/* Is there an alpha channel? */
	return (p->hasAlpha ? JNI_TRUE : JNI_FALSE);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareSetAlphaColor)
	(JNIEnv* env, jclass classy, jobject g, jint argb)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Forward. */
	if (sjme_error_is(error = p->api->setAlphaColor(p, argb)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareSetBlendingMode)
	(JNIEnv* env, jclass classy, jobject g, jint mode)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Forward. */
	if (sjme_error_is(error = p->api->setBlendingMode(p, mode)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareSetClip)
	(JNIEnv* env, jclass classy, jobject g, jint x, jint y, jint w, jint h)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareSetDefaultFont)
	(JNIEnv* env, jclass classy, jobject g)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Forward. */
	if (sjme_error_is(error = p->api->setDefaultFont(p)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareSetFont)
	(JNIEnv* env, jclass classy, jobject g, jobject font)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareSetStrokeStyle)
	(JNIEnv* env, jclass classy, jobject g, jint style)
{
	sjme_errorCode error;
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Forward. */
	if (sjme_error_is(error = p->api->setStrokeStyle(p, style)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilShelf, hardwareTranslate)
	(JNIEnv* env, jclass classy, jobject g, jint x, jint y)
{
	sjme_scritchui_pencil p;
	
	/* Recover. */
	p = recoverPencil(env, g);
	if (g == NULL || p == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	sjme_todo("Impl?");
}
	
FORWARD_IMPL(PencilShelf, capabilities, jint, Integer, \
	FORWARD_IMPL_args(jint pixelFormat), \
	FORWARD_IMPL_pass(pixelFormat))
FORWARD_IMPL(PencilShelf, nativeImageLoadRGBA, jobject, Object, \
	FORWARD_IMPL_args(jint type, jbyteArray buf, jint off, jint len, \
		jobject callback), \
	FORWARD_IMPL_pass(type, buf, off, len, callback))
FORWARD_IMPL(PencilShelf, nativeImageLoadTypes, jint, Integer, \
	FORWARD_IMPL_none(), FORWARD_IMPL_none())

static const JNINativeMethod mlePencilMethods[] =
{
	FORWARD_list(PencilShelf, capabilities),
	FORWARD_list(PencilShelf, hardwareCopyArea),
	FORWARD_list(PencilShelf, hardwareDrawChars),
	FORWARD_list(PencilShelf, hardwareDrawHoriz),
	FORWARD_list(PencilShelf, hardwareDrawLine),
	FORWARD_list(PencilShelf, hardwareDrawPixel),
	FORWARD_list(PencilShelf, hardwareDrawRect),
	FORWARD_list(PencilShelf, hardwareDrawSubstring),
	FORWARD_list(PencilShelf, hardwareDrawXRGB32Region),
	FORWARD_list(PencilShelf, hardwareFillRect),
	FORWARD_list(PencilShelf, hardwareFillTriangle),
	FORWARD_list(PencilShelf, hardwareGraphics),
	FORWARD_list(PencilShelf, hardwareHasAlpha),
	FORWARD_list(PencilShelf, hardwareSetAlphaColor),
	FORWARD_list(PencilShelf, hardwareSetBlendingMode),
	FORWARD_list(PencilShelf, hardwareSetClip),
	FORWARD_list(PencilShelf, hardwareSetDefaultFont),
	FORWARD_list(PencilShelf, hardwareSetFont),
	FORWARD_list(PencilShelf, hardwareSetStrokeStyle),
	FORWARD_list(PencilShelf, hardwareTranslate),
	FORWARD_list(PencilShelf, nativeImageLoadRGBA),
	FORWARD_list(PencilShelf, nativeImageLoadTypes),
};

FORWARD_init(mlePencilInit, mlePencilMethods)

