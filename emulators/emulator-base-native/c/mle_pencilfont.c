/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiPencilFontSqf.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"
#include "squirreljme.h"
#include "lib/scritchui/scritchuiExtern.h"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/PencilFontShelf"

#define FORWARD_DESC_builtin "(" \
	")" DESC_ARRAY(DESC_PENCILFONT)
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
#define FORWARD_DESC_metricFontPixelSize "(" \
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

JNIEXPORT jobjectArray JNICALL FORWARD_FUNC_NAME(PencilFontShelf, builtin)
	(JNIEnv* env, jclass classy)
{
	static struct sjme_scritchui_pencilFontBase onlyBase;
	sjme_errorCode error;
	JavaVM* vm;
	jobject instance;
	jclass instanceClass;
	jmethodID instanceNew;
	
	/* Find wrapper class. */
	instanceClass = (*env)->FindClass(env, DESC_DYLIB_PENCILFONT);
	if (instanceClass == NULL)
	{
		sjme_die("No DylibPencilFontObject?");
		sjme_jni_throwMLECallError(env, SJME_ERROR_UNKNOWN);
		return NULL;
	}

	/* Need to initialize? */
	if (onlyBase.frontEnd.wrapper == NULL)
	{
		/* Initialize font. */
		if (sjme_error_is(error = sjme_scritchui_newPencilFontSqfStatic(
			&onlyBase,
			&sqf_font_sanserif_12)))
		{
			sjme_jni_throwMLECallError(env, error);
			return NULL;
		}
		
		/* Find constructor. */
		instanceNew = (*env)->GetMethodID(env, instanceClass,
			"<init>", "(J)V");
		if (instanceNew == NULL)
		{
			sjme_die("Could not find new for pencil font wrapper.");
			sjme_jni_throwMLECallError(env, SJME_ERROR_UNKNOWN);
			return NULL;
		}
		
		/* Setup instance. */
		instance = (*env)->NewObject(env, instanceClass, instanceNew,
			(jlong)&onlyBase);
		if (instance == NULL)
		{
			sjme_jni_throwMLECallError(env, SJME_ERROR_JNI_EXCEPTION);
			return NULL;
		}
	
		/* Store object information. */
		(*env)->GetJavaVM(env, &vm);
		onlyBase.frontEnd.data = vm;
		onlyBase.frontEnd.wrapper = (*env)->NewGlobalRef(env, instance);
	}
	
	/* We just need the wrapped instance. */
	else
		instance = onlyBase.frontEnd.wrapper;
	
	/* Wrap array. */
	return (*env)->NewObjectArray(env, 1, instanceClass, instance);
}

JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(PencilFontShelf, equals)
	(JNIEnv* env, jclass classy, jobject a, jobject b)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricCharDirection)
	(JNIEnv* env, jclass classy, jobject font, jint c)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricCharValid)
	(JNIEnv* env, jclass classy, jobject font, jint c)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricFontFace)
	(JNIEnv* env, jclass classy, jobject font)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jstring JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricFontName)
	(JNIEnv* env, jclass classy, jobject font)
{
	sjme_todo("Impl?");
	return NULL;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricFontPixelSize)
	(JNIEnv* env, jclass classy, jobject font)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricFontStyle)
	(JNIEnv* env, jclass classy, jobject font)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelAscent)
	(JNIEnv* env, jclass classy, jobject font, jboolean max)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelBaseline)
	(JNIEnv* env, jclass classy, jobject font)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelDescent)
	(JNIEnv* env, jclass classy, jobject font, jboolean max)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, metricPixelLeading)
	(JNIEnv* env, jclass classy, jobject font)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, pixelCharHeight)
	(JNIEnv* env, jclass classy, jobject font, jint c)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(PencilFontShelf, pixelCharWidth)
	(JNIEnv* env, jclass classy, jobject font, jint c)
{
	sjme_todo("Impl?");
	return 0;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilFontShelf, renderBitmap)
	(JNIEnv* env, jclass classy, jobject font, jint c, jbyteArray buf,
	jint bufOff, jint scanLen, jint sx, jint sy, jint sw, jint sh)
{
	sjme_todo("Impl?");
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(PencilFontShelf, renderChar)
	(JNIEnv* env, jclass classy, jobject font, jint c, jobject pencil,
	jint x, jint y, jintArray nextXY)
{
	sjme_todo("Impl?");
}

static const JNINativeMethod mlePencilFontMethods[] =
{
	FORWARD_list(PencilFontShelf, builtin),
	FORWARD_list(PencilFontShelf, equals),
	FORWARD_list(PencilFontShelf, metricCharDirection),
	FORWARD_list(PencilFontShelf, metricCharValid),
	FORWARD_list(PencilFontShelf, metricFontFace),
	FORWARD_list(PencilFontShelf, metricFontName),
	FORWARD_list(PencilFontShelf, metricFontPixelSize),
	FORWARD_list(PencilFontShelf, metricFontStyle),
	FORWARD_list(PencilFontShelf, metricPixelAscent),
	FORWARD_list(PencilFontShelf, metricPixelBaseline),
	FORWARD_list(PencilFontShelf, metricPixelDescent),
	FORWARD_list(PencilFontShelf, metricPixelLeading),
	FORWARD_list(PencilFontShelf, pixelCharHeight),
	FORWARD_list(PencilFontShelf, pixelCharWidth),
	FORWARD_list(PencilFontShelf, renderBitmap),
	FORWARD_list(PencilFontShelf, renderChar),
};

FORWARD_init(mlePencilFontInit, mlePencilFontMethods)
