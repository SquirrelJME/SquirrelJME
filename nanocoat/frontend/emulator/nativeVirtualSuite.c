/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "frontend/emulator/jniHelper.h"
#include "frontend/emulator/common.h"
#include "sjme/rom.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/romInternal.h"

/**
 * Cache storage for virtual suites.
 *
 * @since 2023/12/15
 */
typedef struct sjme_jni_virtualSuite_cache
{
	/** Todo. */
	sjme_jint todo;
} sjme_jni_virtualSuite_cache;

/**
 * Caches for libraries.
 *
 * @since 2023/12/29
 */
typedef struct sjme_jni_virtualLibrary_cache
{
	/** Todo. */
	sjme_jint todo;
} sjme_jni_virtualLibrary_cache;

static sjme_errorCode sjme_jni_virtualSuite_initCache(
	sjme_attrInNotNull sjme_rom_suite inSuite)
{
	JNIEnv* env;

	if (inSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get env back. */
	env = (JNIEnv*)inSuite->functions->frontEnd.data;
	
	/* Setup wrapper to reference, note that there is only ever a single */
	/* Object since we dynamically allocate the functions accordingly. */
	inSuite->cache.common.frontEnd.data = env;
	inSuite->cache.common.frontEnd.wrapper =
		inSuite->functions->frontEnd.wrapper;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_virtualSuite_libraryId(
	sjme_attrInNotNull const sjme_rom_suiteFunctions* functions,
	sjme_attrInNotNull sjme_rom_suite targetSuite,
	sjme_attrInNotNull sjme_rom_library targetLibrary,
	sjme_attrOutNotNull sjme_jint* outId)
{
	sjme_todo("Implement this?");
	return 0;
}

static sjme_errorCode sjme_jni_virtualSuite_list(
	sjme_attrInNotNull sjme_rom_suite targetSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibraries)
{
	JNIEnv* env;
	jobject virtualSuite;
	jmethodID javaListMethod;
	jclass classy;
	sjme_list_sjme_rom_library* result;

	if (targetSuite == NULL || outLibraries == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get instance that we need to call into. */
	env = targetSuite->cache.common.frontEnd.data;
	virtualSuite = targetSuite->cache.common.frontEnd.wrapper;
	classy = (*env)->GetObjectClass(env, virtualSuite);

	/* Execute method accordingly. */
	javaListMethod = (*env)->GetMethodID(env, classy, "__list", "()J");
	result = SJME_JLONG_TO_POINTER(sjme_list_sjme_rom_library*,
		(*env)->CallLongMethod(env, virtualSuite, javaListMethod));

	/* Return result of it. */
	*outLibraries = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_jni_virtualSuite_loadLibrary()
{
	sjme_todo("Implement this?");
	return 0;
}

jlong SJME_JNI_METHOD(SJME_CLASS_VIRTUAL_SUITE, _1_1init)
	(JNIEnv* env, jclass classy, jlong structPtr, jobject this)
{
	sjme_rom_suiteFunctions* functions;

	if (structPtr == 0 || this == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Get the pool pointer. */
	functions = SJME_JLONG_TO_POINTER(sjme_rom_suiteFunctions*, structPtr);

	/* Set function handlers. */
	functions->uncommonTypeSize = sizeof(sjme_jni_virtualSuite_cache);
	functions->initCache = sjme_jni_virtualSuite_initCache;
	functions->libraryId = sjme_jni_virtualSuite_libraryId;
	functions->list = sjme_jni_virtualSuite_list;
	functions->loadLibrary = sjme_jni_virtualSuite_loadLibrary;

	/* Set environment to call back into Java code. */
	functions->frontEnd.data = env;
	functions->frontEnd.wrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, this));

	/* Use the given result. */
	return SJME_POINTER_TO_JLONG(functions);
}