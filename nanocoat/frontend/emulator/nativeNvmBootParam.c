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
#include "sjme/nvm/boot.h"
#include "sjme/alloc.h"

void SJME_JNI_METHOD(SJME_CLASS_NVM_BOOT_PARAM, _1_1setMainArgs)
	(JNIEnv* env, jclass classy, jlong thisPtr, jlong listPtr)
{
	sjme_nvm_bootParam* param;

	if (thisPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Get parameter data. */
	param = SJME_JLONG_TO_POINTER(sjme_nvm_bootParam*, thisPtr);

	/* Set main arguments. */
	param->mainArgs = SJME_JLONG_TO_POINTER(const sjme_list_sjme_lpcstr*,
		listPtr);
}

void SJME_JNI_METHOD(SJME_CLASS_NVM_BOOT_PARAM, _1_1setMainClass)
	(JNIEnv* env, jclass classy, jlong thisPtr, jlong stringPtr)
{
	sjme_nvm_bootParam* param;

	if (thisPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Get parameter data. */
	param = SJME_JLONG_TO_POINTER(sjme_nvm_bootParam*, thisPtr);

	/* Set main class. */
	param->mainClass = SJME_JLONG_TO_POINTER(sjme_lpcstr, stringPtr);
}

void SJME_JNI_METHOD(SJME_CLASS_NVM_BOOT_PARAM, _1_1setMainClassPathIds)
	(JNIEnv* env, jclass classy, jlong thisPtr, jlong idsPtr)
{
	sjme_nvm_bootParam* param;

	if (thisPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Get parameter data. */
	param = SJME_JLONG_TO_POINTER(sjme_nvm_bootParam*, thisPtr);

	/* Set suite information. */
	param->mainClassPathById =
		SJME_JLONG_TO_POINTER(const sjme_list_sjme_jint*, idsPtr);
}

void SJME_JNI_METHOD(SJME_CLASS_NVM_BOOT_PARAM, _1_1setSuite)
	(JNIEnv* env, jclass classy, jlong thisPtr, jlong funcsPtr)
{
	sjme_nvm_bootParam* param;

	if (thisPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Get parameter data. */
	param = SJME_JLONG_TO_POINTER(sjme_nvm_bootParam*, thisPtr);

	/* Set suite information. */
	param->bootSuite = SJME_JLONG_TO_POINTER(sjme_nvm_rom_suite, funcsPtr);
}

void SJME_JNI_METHOD(SJME_CLASS_NVM_BOOT_PARAM, _1_1setSysProps)
	(JNIEnv* env, jclass classy, jlong thisPtr, jlong listPtr)
{
	sjme_nvm_bootParam* param;

	if (thisPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Get parameter data. */
	param = SJME_JLONG_TO_POINTER(sjme_nvm_bootParam*, thisPtr);

	/* Set system property chain. */
	param->sysProps = SJME_JLONG_TO_POINTER(sjme_list_sjme_lpcstr*, listPtr);
}

