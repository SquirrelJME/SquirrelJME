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

	/* Find the ID and name functions. */
	classy = (*env)->GetObjectClass(env, self);
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

/** Functions for JNI accessed libraries. */
static const sjme_rom_libraryFunctions sjme_jni_virtualLibrary_functions =
{
	.uncommonTypeSize = sizeof(sjme_jni_virtualLibrary_cache),
	.initCache = sjme_jni_virtualLibrary_initCache,
	.path = NULL,
	.resourceDirect = NULL,
	.resourceStream = NULL,
	.size = NULL,
	.rawData = NULL,
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
	if (SJME_IS_ERROR(error = sjme_rom_newLibrary(pool,
		&result, &sjme_jni_virtualLibrary_functions,
		&frontEnd)) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success! */
	return SJME_POINTER_TO_JLONG(result);
}
