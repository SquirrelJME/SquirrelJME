/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include <string.h>

#include "lib/scritchui/scritchuiExtern.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiPencilFontSqf.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"
#include "squirreljme.h"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/PencilFontShelf"

#define FORWARD_DESC_equals "(" \
	DESC_PENCILFONT DESC_PENCILFONT ")" DESC_BOOLEAN
#define FORWARD_DESC_metricCharDirection "(" \
	DESC_PENCILFONT DESC_INT ")" DESC_INT
#define FORWARD_DESC_metricCharValid "(" \
	DESC_PENCILFONT DESC_INT ")" DESC_BOOLEAN
#define FORWARD_DESC_metricFontFace "(" \
	DESC_PENCILFONT ")" DESC_INT
#define FORWARD_DESC_metricFontName "(" \
	DESC_PENCILFONT ")" DESC_STRING
#define FORWARD_DESC_metricPixelSize "(" \
	DESC_PENCILFONT ")" DESC_INT
#define FORWARD_DESC_metricFontStyle "(" \
	DESC_PENCILFONT ")" DESC_INT
#define FORWARD_DESC_metricPixelAscent "(" \
	DESC_PENCILFONT DESC_BOOLEAN ")" DESC_INT
#define FORWARD_DESC_metricPixelBaseline "(" \
	DESC_PENCILFONT ")" DESC_INT
#define FORWARD_DESC_metricPixelDescent "(" \
	DESC_PENCILFONT DESC_BOOLEAN ")" DESC_INT
#define FORWARD_DESC_metricPixelLeading "(" \
	DESC_PENCILFONT ")" DESC_INT
#define FORWARD_DESC_pixelCharHeight "(" \
	DESC_PENCILFONT DESC_INT ")" DESC_INT
#define FORWARD_DESC_pixelCharWidth "(" \
	DESC_PENCILFONT DESC_INT ")" DESC_INT
#define FORWARD_DESC_renderBitmap "(" \
	DESC_PENCILFONT DESC_INT DESC_ARRAY(DESC_BYTE) DESC_INT \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC_renderChar "(" \
	DESC_PENCILFONT DESC_INT DESC_PENCIL DESC_INT DESC_INT \
	DESC_ARRAY(DESC_INT) ")" DESC_VOID

#define RECOVER_FONT() \
	do { font = sjme_jni_recoverFont(env, fontInstance); \
	if (font == NULL) \
	{ \
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS); \
		return 0; \
	} } while(0)


JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(PencilFontShelf, equals)
	(JNIEnv* env, jclass classy, jobject a, jobject b)
{
	sjme_scritchui_pencilFont fontA;
	sjme_scritchui_pencilFont fontB;
	
	/* Recover fonts. */
	fontA = sjme_jni_recoverFont(env, a);
	fontB = sjme_jni_recoverFont(env, b);
	
	/* Call which one? */
	if (fontA != NULL)
		return fontA->api->equals(fontA, fontB);
	else if (fontB != NULL)
		return fontB->api->equals(fontA, fontB);
		
	/* Both would be NULL at this point. */
	return JNI_TRUE;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricCharDirection)
	(JNIEnv* env, jclass classy, jobject fontInstance, jint c)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jint result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricCharDirection,
		(font, c, &result));
	return result;
}

JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricCharValid)
	(JNIEnv* env, jclass classy, jobject fontInstance, jint c)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jboolean result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricCharValid,
		(font, c, &result));
	return result;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricFontFace)
	(JNIEnv* env, jclass classy, jobject fontInstance)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_scritchui_pencilFontFace result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricFontFace,
		(font, &result));
	return result;
}

JNIEXPORT jstring JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricFontName)
	(JNIEnv* env, jclass classy, jobject fontInstance)
{
	sjme_scritchui_pencilFont font;
	sjme_errorCode error;
	sjme_lpcstr name;
	
	if (fontInstance == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return NULL;
	}
	
	/* Recover font. */
	font = sjme_jni_recoverFont(env, fontInstance);
	if (font == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return NULL;
	}
	
	/* Not implemented? */
	if (font->api->metricFontName == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NOT_IMPLEMENTED);
		return NULL;
	}
	
	/* Get name. */
	name = NULL;
	if (sjme_error_is(error = font->api->metricFontName(font,
		&name)) || name == NULL)
	{
		sjme_jni_throwMLECallError(env, error);
		return NULL;
	}
	
	/* Wrap in string. */
	return (*env)->NewStringUTF(env, name);
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricFontStyle)
	(JNIEnv* env, jclass classy, jobject fontInstance)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_scritchui_pencilFontStyle result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricFontStyle,
		(font, &result));
	return result;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelAscent)
	(JNIEnv* env, jclass classy, jobject fontInstance, jboolean max)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jint result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricPixelAscent,
		(font, max, &result));
	return result;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelBaseline)
	(JNIEnv* env, jclass classy, jobject fontInstance)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jint result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricPixelBaseline,
		(font, &result));
	return result;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelDescent)
	(JNIEnv* env, jclass classy, jobject fontInstance, jboolean max)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jint result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricPixelDescent,
		(font, max, &result));
	return result;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelLeading)
	(JNIEnv* env, jclass classy, jobject fontInstance)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jint result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricPixelLeading,
		(font, &result));
	return result;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelSize)
	(JNIEnv* env, jclass classy, jobject fontInstance)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jint result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->metricPixelSize,
		(font, &result));
	return result;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, pixelCharWidth)
	(JNIEnv* env, jclass classy, jobject fontInstance, jint c)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jint result;
	
	/* Forward. */
	RECOVER_FONT();
	CHECK_AND_FORWARD(0, font->api->pixelCharWidth,
		(font, c, &result));
	return result;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilFontShelf, renderBitmap)
	(JNIEnv* env, jclass classy, jobject fontInstance, jint c, jbyteArray buf,
	jint bufOff, jint scanLen, jint sx, jint sy, jint sw, jint sh)
{
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilFontShelf, renderChar)
	(JNIEnv* env, jclass classy, jobject fontInstance, jint c, jobject pencil,
	jint x, jint y, jintArray nextXY)
{
	sjme_todo("Impl?");
}

static const JNINativeMethod mlePencilFontMethods[] =
{
	FORWARD_list(PencilFontShelf, equals),
	FORWARD_list(PencilFontShelf, metricCharDirection),
	FORWARD_list(PencilFontShelf, metricCharValid),
	FORWARD_list(PencilFontShelf, metricFontFace),
	FORWARD_list(PencilFontShelf, metricFontName),
	FORWARD_list(PencilFontShelf, metricPixelSize),
	FORWARD_list(PencilFontShelf, metricFontStyle),
	FORWARD_list(PencilFontShelf, metricPixelAscent),
	FORWARD_list(PencilFontShelf, metricPixelBaseline),
	FORWARD_list(PencilFontShelf, metricPixelDescent),
	FORWARD_list(PencilFontShelf, metricPixelLeading),
	FORWARD_list(PencilFontShelf, pixelCharWidth),
	FORWARD_list(PencilFontShelf, renderBitmap),
	FORWARD_list(PencilFontShelf, renderChar),
};

FORWARD_init(mlePencilFontInit, mlePencilFontMethods)
