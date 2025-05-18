/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include <string.h>

#include "squirreljme.h"
#include "lib/scritchaudio/scritchaudio.h"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/AudioStreamShelf"
#define FORWARD_NATIVE_CLASS "cc/squirreljme/emulator/EmulatedAudioStreamShelf"

#define FORWARD_DESC_attach \
	DESC_METHOD(DESC_AUDIOCONN, DESC_AUDIOSTREAM DESC_AUDIORENDERER)
#define FORWARD_DESC_create \
	DESC_METHOD(DESC_AUDIOSTREAM, DESC_STRING DESC_INT DESC_INT DESC_INT)
#define FORWARD_DESC_decoder \
	DESC_METHOD(DESC_AUDIOPLAYER, DESC_STRING DESC_STRING DESC_INT \
	DESC_INT DESC_INT DESC_ARRAY(DESC_BYTE) DESC_INT DESC_INT)
#define FORWARD_DESC_decoderSupports \
	DESC_METHOD(DESC_BOOLEAN, DESC_STRING)
#define FORWARD_DESC_destroy \
	DESC_METHOD(DESC_VOID, DESC_AUDIOSTREAM)
#define FORWARD_DESC_midiPort \
	DESC_METHOD(DESC_MIDIPORT, DESC_STRING DESC_INT DESC_INT DESC_INT)
#define FORWARD_DESC_midiRenderer \
	DESC_METHOD(DESC_AUDIORENDERER, DESC_MIDIPORT)
#define FORWARD_DESC_unregister \
	DESC_METHOD(DESC_VOID, DESC_AUDIOSTREAM DESC_AUDIORENDERER)

#define FORWARD_DESC___attach \
	DESC_METHOD(DESC_LONG, DESC_LONG DESC_LONG DESC_AUDIORENDERER)
#define FORWARD_DESC___create \
	DESC_METHOD(DESC_LONG, DESC_LONG DESC_STRING DESC_INT DESC_INT DESC_INT)
#define FORWARD_DESC___dylibLoad \
	DESC_METHOD(DESC_LONG, DESC_STRING DESC_STRING)

FORWARD_IMPL(AudioStream, attach,
	jobject, Object,
	FORWARD_IMPL_args(jobject stream, jobject renderer),
	FORWARD_IMPL_pass(stream, renderer))
FORWARD_IMPL(AudioStream, create,
	jobject, Object,
	FORWARD_IMPL_args(jstring name, jint format, jint rate, jint channels),
	FORWARD_IMPL_pass(name, format, rate, channels))
FORWARD_IMPL(AudioStream, decoder,
	jobject, Object,
	FORWARD_IMPL_args(jstring urlFile, jstring mime, jint format,
		jint rate, jint channels, jarray buf, jint off, jint len),
	FORWARD_IMPL_pass(urlFile, mime, format, rate, channels, buf, off, len))
FORWARD_IMPL(AudioStream, decoderSupports,
	jboolean, Boolean,
	FORWARD_IMPL_args(jstring mime),
	FORWARD_IMPL_pass(mime))
FORWARD_IMPL_VOID(AudioStream, destroy,
	FORWARD_IMPL_args(jobject stream),
	FORWARD_IMPL_pass(stream))
FORWARD_IMPL(AudioStream, midiPort,
	jobject, Object,
	FORWARD_IMPL_args(jstring mime, jint format, jint rate, jint channels),
	FORWARD_IMPL_pass(mime, format, rate, channels))
FORWARD_IMPL(AudioStream, midiRenderer,
	jobject, Object,
	FORWARD_IMPL_args(jobject port),
	FORWARD_IMPL_pass(port))
FORWARD_IMPL_VOID(AudioStream, unregister,
	FORWARD_IMPL_args(jobject stream, jobject renderer),
	FORWARD_IMPL_pass(stream, renderer))

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(Emulated, __attach)(
	JNIEnv* env, jclass classy, jlong statePtr, jlong streamPtr,
	jobject javaRenderer)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_scritchaudio_stream inStream;

	/* Recover state. */
	inState = (sjme_scritchaudio)statePtr;
	inStream = (sjme_scritchaudio_stream)streamPtr;
	if (inState == NULL || inStream == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(Emulated, __create)(
	JNIEnv* env, jclass classy, jlong statePtr, jstring name, jint format,
	jint rate, jint channels)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_scritchaudio_stream result;
	const char* nameChars;
	jboolean nameCopy;

	/* Recover state. */
	inState = (sjme_scritchaudio)statePtr;
	if (inState == NULL || name == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Extract characters. */
	nameChars = (*env)->GetStringUTFChars(env, name, &nameCopy);

	/* Create native stream. */
	result = NULL;
	if (sjme_error_is(error = inState->api->streamCreate(inState, &result,
		nameChars, format, rate, channels)) || result == NULL)
	{
		/* Free characters. */
		(*env)->ReleaseStringUTFChars(env, name, nameChars);

		sjme_jni_throwMLECallError(env, error);
		return 0;
	}

	/* Free characters. */
	(*env)->ReleaseStringUTFChars(env, name, nameChars);

	/* Success! */
	return (sjme_intPointer)result;
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(Emulated, __dylibLoad)(
	JNIEnv* env, jclass classy, jstring path, jstring name)
{
#define BUF_SIZE 128
	char buf[BUF_SIZE];
	jboolean pathCopy, nameCopy;
	const char* nameChars;
	const char* pathChars;
	sjme_dylib dylib;
	sjme_errorCode error;
	sjme_debug_handlerFunctions** dylibDebugHandlers;
	sjme_scritchaudio_dylibApiFunc apiInit;
	sjme_scritchaudio result;
	sjme_alloc_pool pool;

	if (env == NULL || classy == NULL || path == NULL)
	{
		sjme_jni_throwNullPointerException(env);
		return 0;
	}

	/* Globally reference self so this remains always loaded. */
	(*env)->NewGlobalRef(env, classy);

	/* Get the path characters. */
	pathCopy = SJME_JNI_FALSE;
	pathChars = (*env)->GetStringUTFChars(env, path, &pathCopy);
	if (pathChars == NULL)
		goto fail_badPath;

	/* Get the name characters. */
	nameCopy = SJME_JNI_FALSE;
	nameChars = (*env)->GetStringUTFChars(env, name, &nameCopy);
	if (nameChars == NULL)
		goto fail_badName;

	/* Load in the dynamic library. */
	dylib = NULL;
	if (sjme_error_is(error = sjme_dylib_open(pathChars, &dylib)) ||
		dylib == NULL)
		goto fail_dylibOpen;

	/* Copy debug handlers since it may be in a different symbol domain. */
	dylibDebugHandlers = NULL;
	if (!sjme_error_is(sjme_dylib_lookup(dylib,
		"sjme_debug_handlers",
		(void**)&dylibDebugHandlers)))
		*dylibDebugHandlers = &sjme_jni_debugHandlers;

	/* Resolve name to load in. */
	memset(buf, 0, sizeof(buf));
	snprintf(buf, BUF_SIZE - 2, "%s%s",
		SJME_TOKEN_STRING_PP(SJME_SCRITCHAUDIO_DYLIB_SYMBOL_PREFIX),
		nameChars);
	buf[BUF_SIZE - 1] = 0;

	/* Lookup API initializer. */
	apiInit = NULL;
	if (sjme_error_is(error = sjme_dylib_lookup(dylib,
		buf, (sjme_pointer*)&apiInit)) || apiInit == NULL)
		goto fail_lookupInit;

	/* Allocate memory pool. */
	pool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
		16 * 1048576)) || pool == NULL)
		goto fail_allocPool;

	/* Initialize the API. */
	result = NULL;
	if (sjme_error_is(error = apiInit(pool, &result, NULL)) ||
		result == NULL)
		goto fail_init;

	/* Return the state pointer. */
	return (sjme_intPointer)result;

fail_init:
fail_allocPool:
	if (pool != NULL)
		free((void*)pool);

fail_lookupInit:
fail_dylibOpen:
fail_badName:
	if (nameChars != NULL)
		(*env)->ReleaseStringUTFChars(env, name, nameChars);

fail_badPath:
	if (pathChars != NULL)
		(*env)->ReleaseStringUTFChars(env, path, pathChars);

	sjme_jni_throwMLECallError(env, error);
	return 0;
#undef BUF_SIZE
}

static const JNINativeMethod mleAudioStreamMethods[] =
{
	FORWARD_list(AudioStream, attach),
	FORWARD_list(AudioStream, create),
	FORWARD_list(AudioStream, decoder),
	FORWARD_list(AudioStream, decoderSupports),
	FORWARD_list(AudioStream, destroy),
	FORWARD_list(AudioStream, midiPort),
	FORWARD_list(AudioStream, midiRenderer),
	FORWARD_list(AudioStream, unregister),
};

static const JNINativeMethod mleEmulAudioStreamMethods[] =
{
	FORWARD_list(Emulated, __attach),
	FORWARD_list(Emulated, __create),
	FORWARD_list(Emulated, __dylibLoad),
};

jint JNICALL mleAudioStreamInit(JNIEnv* env, jclass classy)
{
	jint result;

	/* Helpers in the forwarded class. */
	if (0 != (result = (*env)->RegisterNatives(env,
		(*env)->FindClass(env, FORWARD_NATIVE_CLASS),
		mleEmulAudioStreamMethods,
		sizeof(mleEmulAudioStreamMethods) /
			sizeof(JNINativeMethod))))
		return result;

	/* Then the native forwards. */
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, FORWARD_CLASS),
		mleAudioStreamMethods, sizeof(mleAudioStreamMethods) /
			sizeof(JNINativeMethod));
}
