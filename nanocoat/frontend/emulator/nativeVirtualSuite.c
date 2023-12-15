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
#include "sjme/rom.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

static sjme_errorCode sjme_jni_virtualSuite_initCache(
	sjme_attrInNotNull const sjme_rom_suiteFunctions* functions,
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInOutNotNull sjme_rom_suite* targetSuite)
{
	sjme_todo("Implement this?");
	return 0;
}

static sjme_errorCode sjme_jni_virtualSuite_list()
{
	sjme_todo("Implement this?");
	return 0;
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
	functions->initCache = sjme_jni_virtualSuite_initCache;
	functions->list = sjme_jni_virtualSuite_list;
	functions->loadLibrary = sjme_jni_virtualSuite_loadLibrary;

	/* Setup wrapper to reference. */
	functions->frontEnd.data = env;
	functions->frontEnd.wrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, this));

	/* Use the given result. */
	return SJME_POINTER_TO_JLONG(functions);
}