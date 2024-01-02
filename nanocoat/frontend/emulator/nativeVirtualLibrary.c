/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>
#include <string.h>

#include "frontend/emulator/common.h"
#include "frontend/emulator/jniHelper.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/rom.h"

sjme_errorCode sjme_jni_virtualLibrary_initCache(
	sjme_attrInNotNull sjme_rom_library inLibrary)
{
	JNIEnv* env;
	jobject self;
	jclass classy;
	jmethodID idFunc;
	jmethodID nameFunc;

	if (inLibrary == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get env and object back. */
	env = inLibrary->cache.common.frontEnd.data;
	self = inLibrary->cache.common.frontEnd.wrapper;
	classy = (*env)->GetObjectClass(env, self);

	/* Find the ID and name functions. */
	idFunc = (*env)->GetMethodID(env, classy, "__id", "()I");
	nameFunc = (*env)->GetMethodID(env, classy, "__name", "()J");

	/* Debug. */
	sjme_message("env: %p, self: %p, selfClass: %p, idFunc: %p, "
		"nameFunc: %p\n",
		env, self, classy, idFunc, nameFunc);

	/* Pre-seed ID. */
	inLibrary->id = (*env)->CallIntMethod(env, self, idFunc, classy);
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_JNI_EXCEPTION;

	/* Pre-seed name. */
	inLibrary->name = SJME_JLONG_TO_POINTER(sjme_lpcstr,
		(*env)->CallObjectMethod(env, self, nameFunc));
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_JNI_EXCEPTION;

	/* Nothing needs to be done here... */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_virtualLibrary_rawData(
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint length)
{
	JNIEnv* env;
	jobject self;
	jclass classy;
	jmethodID rawDataFunc;
	jint result;
	jobject nioBuf;
	sjme_errorCode error;

	if (inLibrary == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (srcPos < 0 || length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Initial setup. */
	nioBuf = NULL;
	error = SJME_ERROR_UNKNOWN;

	/* Get env and object back. */
	env = inLibrary->cache.common.frontEnd.data;
	self = inLibrary->cache.common.frontEnd.wrapper;
	classy = (*env)->GetObjectClass(env, self);

	/* It is much faster to use NIO for this! */
	nioBuf = (*env)->NewDirectByteBuffer(env, dest, length);
	if (sjme_jni_checkVMException(env))
	{
		error = SJME_ERROR_JNI_EXCEPTION;
		goto fail_cleanupNioBuf;
	}

	/* Find the raw data function. */
	rawDataFunc = (*env)->GetMethodID(env, classy,
		"__rawData", "(ILjava/nio/ByteBuffer;)V");
	if (sjme_jni_checkVMException(env))
	{
		error = SJME_ERROR_JNI_EXCEPTION;
		goto fail_cleanupNioBuf;
	}

	/* Call function accordingly. */
	(*env)->CallVoidMethod(env, self, rawDataFunc, srcPos, nioBuf);
	if (sjme_jni_checkVMException(env))
	{
		error = SJME_ERROR_JNI_EXCEPTION;
		goto fail_cleanupNioBuf;
	}

	/* Success! */
	error = SJME_ERROR_NONE;

	/* Cleanup NIO, just in case. */
fail_cleanupNioBuf:
	if (nioBuf != NULL)
	{
		(*env)->DeleteLocalRef(env, nioBuf);
		if (sjme_jni_checkVMException(env))
			return SJME_ERROR_JNI_EXCEPTION;
	}

	/* Return whatever error state. */
	return error;
}

sjme_errorCode sjme_jni_virtualLibrary_rawSize(
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	JNIEnv* env;
	jobject self;
	jclass classy;
	jmethodID rawSizeFunc;
	jint result;

	if (inLibrary == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get env and object back. */
	env = inLibrary->cache.common.frontEnd.data;
	self = inLibrary->cache.common.frontEnd.wrapper;
	classy = (*env)->GetObjectClass(env, self);

	/* Find the raw size function. */
	rawSizeFunc = (*env)->GetMethodID(env, classy, "__rawSize", "()I");
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_JNI_EXCEPTION;

	/* Ask for the raw size of the given library. */
	result = (*env)->CallIntMethod(env, self, rawSizeFunc);
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_JNI_EXCEPTION;

	/* Store result and success! */
	*outSize = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_jni_virtualLibrary_resourceStream(
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrInNotNull sjme_lpcstr resourceName,
	sjme_attrOutNotNull sjme_stream_input* outStream)
{
	if (inLibrary == NULL || resourceName == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this.");
	return SJME_ERROR_NONE;
}

/** Functions for JNI accessed libraries. */
static const sjme_rom_libraryFunctions sjme_jni_virtualLibrary_functions =
{
	.uncommonTypeSize = sizeof(sjme_jni_virtualLibrary_cache),
	.initCache = sjme_jni_virtualLibrary_initCache,
	.path = NULL,
	.rawData = sjme_jni_virtualLibrary_rawData,
	.rawSize = sjme_jni_virtualLibrary_rawSize,
	.resourceStream = sjme_jni_virtualLibrary_resourceStream,
};

jlong SJME_JNI_METHOD(SJME_CLASS_VIRTUAL_LIBRARY, _1_1init)
	(JNIEnv* env, jclass classy, jobject self, jlong suitePtr)
{
	sjme_rom_suite suite;
	sjme_alloc_pool* pool;
	sjme_rom_library result;
	sjme_errorCode error;
	sjme_frontEnd frontEnd;

	/* Get the original owning suite among other details. */
	suite = SJME_JLONG_TO_POINTER(sjme_rom_suite, suitePtr);
	pool = suite->cache.common.allocPool;

	/* Seed front end data. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.data = env;
	frontEnd.wrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, self));

	/* Setup resultant library. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_rom_libraryNew(pool,
		&result, &sjme_jni_virtualLibrary_functions,
		&frontEnd)) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success! */
	return SJME_POINTER_TO_JLONG(result);
}
