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
#define mleGroupId PencilFontShelf
#define mleShelfClass "cc/squirreljme/jvm/mle/PencilFontShelf"
#define mleProxyTarget "cc/squirreljme/emulator/uiform/SwingPencilShelf"
#include "squirreljmeMle.h"
/* //////////// */

#include "lib/scritchui/scritchui.h"
#include "sjme/debug.h"
#include "squirreljme.h"

#define RECOVER_FONT() \
	do { font = sjme_jni_recoverFont(env, fontInstance); \
	if (font == NULL) \
	{ \
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS); \
		return 0; \
	} } while(0)

#define MLE_DESC_equals DESC_METHOD(DESC_BOOLEAN,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) DESC_PENCILFONT DESC_ARRAY(DESC_INT))
MLE_FUNC_PROTO(jboolean, equals, jobject a, jintArray aParams,
	jobject b, jintArray bParams)
{
	sjme_scritchui_pencilFont fontA;
	sjme_scritchui_pencilFont fontB;

	sjme_todo("Impl?");
	sjme_jni_throwVMException(env, SJME_ERROR_NOT_IMPLEMENTED);
#if 0
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
#endif
}

#define MLE_DESC_metricCharDirection DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT DESC_INT )
MLE_FUNC_PROTO(jint, metricCharDirection, jobject fontInstance, jint c)
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

#define MLE_DESC_metricCharValid DESC_METHOD(DESC_BOOLEAN,  \
	DESC_PENCILFONT DESC_INT )
MLE_FUNC_PROTO(jboolean, metricCharValid, jobject fontInstance, jint c)
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

#define MLE_DESC_metricFontFace DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT )
MLE_FUNC_PROTO(jint, metricFontFace, jobject fontInstance)
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

#define MLE_DESC_metricFontName DESC_METHOD(DESC_STRING,  \
	DESC_PENCILFONT )
MLE_FUNC_PROTO(jstring, metricFontName, jobject fontInstance)
{
	sjme_scritchui_pencilFont font;
	sjme_errorCode error;
	sjme_lpcstr name;

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

#define MLE_DESC_metricFontStyle DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT )
MLE_FUNC_PROTO(jint, metricFontStyle, jobject fontInstance)
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

#define MLE_DESC_metricPixelAscent DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) DESC_BOOLEAN )
MLE_FUNC_PROTO(jint, metricPixelAscent, jobject jFont, jintArray jFontParamI,
	jboolean max)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_scritchui_pencilFontParam fontParam;
	sjme_jint result;

	/* Recover font. */
	font = sjme_jni_recoverFont(env, jFont);
	if (font == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Map parameters. */
	memset(&fontParam, 0, sizeof(fontParam));
	if (jFontParamI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env,
			font->common.state, &fontParam, jFontParamI)))
		{
			sjme_jni_throwVMException(env, error);
			return 0;
		}

	/* Determine ascent. */
	result = 0;
	if (sjme_error_is(error = font->api->metricPixelAscent(font,
		(jFontParamI != NULL ? &fontParam : NULL), max,
		&result)))
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success! */
	return result;
}

#define MLE_DESC_metricPixelBaseline DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) )
MLE_FUNC_PROTO(jint, metricPixelBaseline, jobject jFont, jintArray jFontParamI)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_scritchui_pencilFontParam fontParam;
	sjme_jint result;

	/* Recover font. */
	font = sjme_jni_recoverFont(env, jFont);
	if (font == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Map parameters. */
	memset(&fontParam, 0, sizeof(fontParam));
	if (jFontParamI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env,
			font->common.state, &fontParam, jFontParamI)))
		{
			sjme_jni_throwVMException(env, error);
			return 0;
		}

	/* Determine ascent. */
	result = 0;
	if (sjme_error_is(error = font->api->metricPixelBaseline(font,
		(jFontParamI != NULL ? &fontParam : NULL),
		&result)))
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success! */
	return result;
}

#define MLE_DESC_metricPixelDescent DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) DESC_BOOLEAN )
MLE_FUNC_PROTO(jint, metricPixelDescent, jobject jFont, jintArray jFontParamI,
	jboolean max)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_scritchui_pencilFontParam fontParam;
	sjme_jint result;

	/* Recover font. */
	font = sjme_jni_recoverFont(env, jFont);
	if (font == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Map parameters. */
	memset(&fontParam, 0, sizeof(fontParam));
	if (jFontParamI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env,
			font->common.state, &fontParam, jFontParamI)))
		{
			sjme_jni_throwVMException(env, error);
			return 0;
		}

	/* Determine descent. */
	result = 0;
	if (sjme_error_is(error = font->api->metricPixelDescent(font,
		(jFontParamI != NULL ? &fontParam : NULL), max,
		&result)))
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success! */
	return result;
}

#define MLE_DESC_metricPixelLeading DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) )
MLE_FUNC_PROTO(jint, metricPixelLeading,
	jobject jFont, jintArray jFontParamI)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_scritchui_pencilFontParam fontParam;
	sjme_jint result;

	/* Recover font. */
	font = sjme_jni_recoverFont(env, jFont);
	if (font == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Map parameters. */
	memset(&fontParam, 0, sizeof(fontParam));
	if (jFontParamI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env,
			font->common.state, &fontParam, jFontParamI)))
		{
			sjme_jni_throwVMException(env, error);
			return 0;
		}

	/* Determine size. */
	result = 0;
	if (sjme_error_is(error = font->api->metricPixelLeading(font,
		(jFontParamI != NULL ? &fontParam : NULL),
		&result)))
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success! */
	return result;
}

#define MLE_DESC_metricPixelSize DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) DESC_INT )
MLE_FUNC_PROTO(jint, metricPixelSize,
	jobject jFont, jintArray jFontParamI, jint codepoint)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_scritchui_pencilFontParam fontParam;
	sjme_jint result;

	/* Recover font. */
	font = sjme_jni_recoverFont(env, jFont);
	if (font == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Map parameters. */
	memset(&fontParam, 0, sizeof(fontParam));
	if (jFontParamI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env,
			font->common.state, &fontParam, jFontParamI)))
		{
			sjme_jni_throwVMException(env, error);
			return 0;
		}

	/* Determine size. */
	result = 0;
	if (sjme_error_is(error = font->api->metricPixelSize(font,
		(jFontParamI != NULL ? &fontParam : NULL), codepoint,
		&result)))
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success! */
	return result;
}

#define MLE_DESC_pixelCharWidth DESC_METHOD(DESC_INT,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) DESC_INT )
MLE_FUNC_PROTO(jint, pixelCharWidth, jobject jFont, jintArray jFontParamI,
	jint c)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_scritchui_pencilFontParam fontParam;
	sjme_jint result;

	/* Recover font. */
	font = sjme_jni_recoverFont(env, jFont);
	if (font == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Map parameters. */
	memset(&fontParam, 0, sizeof(fontParam));
	if (jFontParamI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env,
			font->common.state, &fontParam, jFontParamI)))
		{
			sjme_jni_throwVMException(env, error);
			return 0;
		}

	/* Forward. */
	CHECK_AND_FORWARD(0, font->api->pixelCharWidth,
		(font, &fontParam, c, &result));
	return result;
}

#define MLE_DESC_renderBitmap DESC_METHOD(DESC_VOID,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) DESC_INT DESC_ARRAY(DESC_BYTE) \
	DESC_INT \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT )
MLE_FUNC_PROTO(void, renderBitmap, jobject fontInstance, jintArray fontParam,
		jint c, jbyteArray buf,
	jint bufOff, jint scanLen, jint sx, jint sy, jint sw, jint sh)
{
	sjme_todo("Impl?");
}

#define MLE_DESC_renderChar DESC_METHOD(DESC_VOID,  \
	DESC_PENCILFONT DESC_ARRAY(DESC_INT) DESC_INT DESC_PENCIL DESC_INT \
	DESC_INT \
	DESC_ARRAY(DESC_INT) )
MLE_FUNC_PROTO(void, renderChar, jobject fontInstance, jint c, jobject pencil,
	jint x, jint y, jintArray nextXY)
{
	sjme_todo("Impl?");
}

MLE_LIST_BEGIN()
	MLE_LIST_ITEM(equals),
	MLE_LIST_ITEM(metricCharDirection),
	MLE_LIST_ITEM(metricCharValid),
	MLE_LIST_ITEM(metricFontFace),
	MLE_LIST_ITEM(metricFontName),
	MLE_LIST_ITEM(metricPixelSize),
	MLE_LIST_ITEM(metricFontStyle),
	MLE_LIST_ITEM(metricPixelAscent),
	MLE_LIST_ITEM(metricPixelBaseline),
	MLE_LIST_ITEM(metricPixelDescent),
	MLE_LIST_ITEM(metricPixelLeading),
	MLE_LIST_ITEM(pixelCharWidth),
	MLE_LIST_ITEM(renderBitmap),
	MLE_LIST_ITEM(renderChar),
MLE_LIST_END()
