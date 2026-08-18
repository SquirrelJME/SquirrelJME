/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include <string.h>

#include "squirreljme.h"
#include "lib/scritchaudio/scritchaudio.h"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/AudioStreamShelf"
#define FORWARD_CLASS_NAME AudioStream
#define FORWARD_NATIVE_CLASS "cc/squirreljme/emulator/EmulatedAudioStreamShelf"

#define FORWARD_DESC_attach \
	DESC_METHOD(DESC_AUDIOCONN, DESC_AUDIOSTREAM DESC_AUDIORENDERER \
		DESC_INT DESC_INT DESC_INT)
#define FORWARD_DESC_decoder \
	DESC_METHOD(DESC_AUDIOPLAYER, DESC_STRING DESC_STRING DESC_INT \
	DESC_INT DESC_INT DESC_ARRAY(DESC_BYTE) DESC_INT DESC_INT)
#define FORWARD_DESC_decoderSupports \
	DESC_METHOD(DESC_BOOLEAN, DESC_STRING)
#define FORWARD_DESC_destroy \
	DESC_METHOD(DESC_VOID, DESC_AUDIOSTREAM)
#define FORWARD_DESC_disconnect \
	DESC_METHOD(DESC_VOID, DESC_AUDIOCONN)
#define FORWARD_DESC_midiPort \
	DESC_METHOD(DESC_MIDIPORT, DESC_STRING DESC_INT DESC_INT DESC_INT)
#define FORWARD_DESC_midiRenderer \
	DESC_METHOD(DESC_AUDIORENDERER, DESC_MIDIPORT)
#define FORWARD_DESC_stream \
	DESC_METHOD(DESC_AUDIOSTREAM, DESC_INT DESC_INT DESC_INT)

#define FORWARD_DESC___attach \
	DESC_METHOD(DESC_LONG, DESC_LONG DESC_LONG DESC_AUDIORENDERER \
		DESC_INT DESC_INT DESC_INT)
#define FORWARD_DESC___disconnect \
	DESC_METHOD(DESC_VOID, DESC_LONG DESC_LONG)
#define FORWARD_DESC___dylibLoad \
	DESC_METHOD(DESC_LONG, DESC_STRING DESC_STRING)
#define FORWARD_DESC___stream \
	DESC_METHOD(DESC_LONG, DESC_LONG DESC_INT DESC_INT DESC_INT)

FORWARD_IMPL(AudioStream, attach,
	jobject, Object,
	FORWARD_IMPL_args(jobject stream, jobject renderer,
		jint format, jint rate, jint channels),
	FORWARD_IMPL_pass(stream, renderer, format, rate, channels))
FORWARD_IMPL(AudioStream, decoder,
	jobject, Object,
	FORWARD_IMPL_args(jstring urlFile, jstring mime, jint format,
		jint rate, jint channels, jarray buf, jint off, jint len),
	FORWARD_IMPL_pass(urlFile, mime, format, rate, channels, buf, off, len))
FORWARD_IMPL(AudioStream, decoderSupports,
	jboolean, Boolean,
	FORWARD_IMPL_args(jstring mime),
	FORWARD_IMPL_pass(mime))
FORWARD_IMPL_VOID(AudioStream, disconnect,
	FORWARD_IMPL_args(jobject conn),
	FORWARD_IMPL_pass(conn))
FORWARD_IMPL(AudioStream, midiPort,
	jobject, Object,
	FORWARD_IMPL_args(jstring mime, jint format, jint rate, jint channels),
	FORWARD_IMPL_pass(mime, format, rate, channels))
FORWARD_IMPL(AudioStream, midiRenderer,
	jobject, Object,
	FORWARD_IMPL_args(jobject port),
	FORWARD_IMPL_pass(port))
FORWARD_IMPL(AudioStream, stream,
	jobject, Object,
	FORWARD_IMPL_args(jint format, jint rate, jint channels),
	FORWARD_IMPL_pass(format, rate, channels))

static sjme_thread_result sjme_attrThreadCall sjme_jni_bindAudioThread(
	sjme_thread_parameter anything)
{
	sjme_scritchaudio_stream inStream;
	JavaVM* vm;
	JNIEnv* env;
	JNIEnv* checkEnv;
	JavaVMAttachArgs attachArgs;
	jint error;

	/* Debug. */
	sjme_message("Binding ScritchAudio thread to JNI...");

	/* Restore state. */
	inStream = (sjme_scritchaudio_stream)anything;
	if (inStream == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);

	/* Restore VM. */
	vm = (JavaVM*)inStream->connection.inState->frontEnd.data;

	/* If this thread is already attached, only attach once. */
	checkEnv = NULL;
	error = (*vm)->GetEnv(vm, (void**)&checkEnv, JNI_VERSION_1_1);
	if (error == JNI_OK)
	{
		/* Debug. */
		sjme_message("Already bound!");
		return SJME_THREAD_RESULT(SJME_ERROR_NONE);
	}

	/* Debug. */
	sjme_message("Recovered ScritchAudio env...");

	/* Setup arguments. */
	memset(&attachArgs, 0, sizeof(attachArgs));
	attachArgs.version = JNI_VERSION_1_1;
	attachArgs.name = "ScritchAudioLoop";

	/* Attach audio loop to the JVM. */
	env = NULL;
	error = (*vm)->AttachCurrentThreadAsDaemon(vm, (void**)&env, &attachArgs);
	if (env == NULL)
		sjme_die("Could not attach thread: %d??", error);

	/* Debug. */
	sjme_message("Bound ScritchAudio to JNI!");

	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

static sjme_errorCode sjme_jni_renderAudio(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_source inSource,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo,
	sjme_attrInNotNull sjme_scritchaudio_buffer* buf)
{
	sjme_errorCode error;
	jobject sourceBracket;
	jobject byteBuffer;
	JNIEnv* env;

	if (inState == NULL || inSource == NULL || renderInfo == NULL ||
		buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover the Java environment. */
	env = NULL;
	if (sjme_error_is(sjme_jni_recoverEnvFrontEnd(&env,
		SJME_AS_FE_BINDABLEP(&inSource->frontEnd))) || env == NULL)
		return SJME_ERROR_NO_JAVA_ENVIRONMENT;

	/* Obtain the source bracket. */
	sourceBracket = inSource->frontEnd.wrapper;
	if (sourceBracket == NULL)
		return SJME_ERROR_RESOURCE_NOT_FOUND;

	/* Setup byte buffer to render into. */
	byteBuffer = (*env)->NewDirectByteBuffer(env, buf, renderInfo->bufSize);

	/* Forward call to the renderer. */
	forwardCallStaticVoid(env, FORWARD_NATIVE_CLASS,
		"__render",
		"("DESC_AUDIORENDERER"Ljava/nio/ByteBuffer;JIIIIIII)V",
		sourceBracket, byteBuffer, renderInfo->clock, renderInfo->samples,
		renderInfo->totalSamples, renderInfo->bytesPerSample,
		renderInfo->bufSize, inSource->format,
		inSource->rate, inSource->channels);

	/* Destroy the byte buffer reference. */
	(*env)->DeleteLocalRef(env, byteBuffer);

	/* Was there an error? */
	if (sjme_jni_checkVMException(env))
	{
		(*env)->ExceptionClear(env);
		return SJME_ERROR_NATIVE_ERROR;
	}

	return SJME_ERROR_NONE;
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(Emulated, __attach)(
	JNIEnv* env, jclass classy, jlong statePtr, jlong streamPtr,
	jobject javaRenderer, jint format, jint rate, jint channels)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_scritchaudio_stream inStream;
	sjme_scritchaudio_source result;
	sjme_frontEndBindable frontEnd;

	/* Recover state. */
	inState = (sjme_scritchaudio)statePtr;
	inStream = (sjme_scritchaudio_stream)streamPtr;
	if (inState == NULL || inStream == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Initialize front end. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	if (sjme_error_is(error = sjme_jni_fillFrontEnd(env, &frontEnd,
		javaRenderer)))
	{
		sjme_jni_throwMLECallError(env, error);
		return 0;
	}

	/* Setup source. */
	result = NULL;
	if (sjme_error_is(error = inState->api->sourceAttach(inState, inStream,
		&result, sjme_jni_renderAudio, format, rate, channels,
		&frontEnd)) || result == NULL)
	{
		sjme_jni_throwMLECallError(env, error);
		return 0;
	}

	/* Return the resultant source. */
	return (sjme_intPointer)result;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(Emulated, __disconnect)(
	JNIEnv* env, jclass classy, jlong statePtr, jlong connPtr)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_scritchaudio_connection inConn;

	/* Recover state. */
	inState = (sjme_scritchaudio)statePtr;
	inConn = (sjme_scritchaudio_connection)connPtr;
	if (inState == NULL || inConn == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward call. */
	if (sjme_error_is(error = inState->api->disconnect(inState, inConn)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(Emulated, __stream)(
	JNIEnv* env, jclass classy, jlong statePtr, jint format, jint rate,
	jint channels)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_scritchaudio_stream stream;

	/* Recover state. */
	inState = (sjme_scritchaudio)statePtr;
	if (inState == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Create a new stream. */
	stream = NULL;
	if (sjme_error_is(error = inState->api->streamCreate(inState,
		&stream, NULL, format, rate, channels)) || stream == NULL)
	{
		sjme_jni_throwMLECallError(env, sjme_error_default(error));
		return 0;
	}

	/* Return the stream pointer. */
	return (sjme_intPointer)stream;
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
	sjme_frontEndBindable frontEnd;

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

	/* Setup frontend data. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	(*env)->GetJavaVM(env, (void*)&frontEnd.base.data);

	/* Initialize the API. */
	result = NULL;
	if (sjme_error_is(error = apiInit(pool, &result,
		sjme_jni_bindAudioThread, &frontEnd)) ||
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
	FORWARD_list(AudioStream, decoder),
	FORWARD_list(AudioStream, decoderSupports),
	FORWARD_list(AudioStream, disconnect),
	FORWARD_list(AudioStream, midiPort),
	FORWARD_list(AudioStream, midiRenderer),
	FORWARD_list(AudioStream, stream),
};

static const JNINativeMethod mleEmulAudioStreamMethods[] =
{
	FORWARD_list(Emulated, __attach),
	FORWARD_list(Emulated, __disconnect),
	FORWARD_list(Emulated, __dylibLoad),
	FORWARD_list(Emulated, __stream),
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
