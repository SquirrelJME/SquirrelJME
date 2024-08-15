/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdlib.h>

#if defined(_WIN32)
	#define SJME_DYLIB_EXPORT __declspec(dllexport)
#endif

#define _JNI_IMPLEMENTATION_
#include "../../include/3rdparty/jni/jni.h"

SJME_DYLIB_EXPORT JNIEXPORT jint JNICALL JNI_GetDefaultJavaVMInitArgs(void* args)
{
	abort();
	return -1;
}

SJME_DYLIB_EXPORT JNIEXPORT jint JNICALL JNI_CreateJavaVM(JavaVM** pvm, void** penv,
	void* args)
{
	abort();
	return -1;
}

SJME_DYLIB_EXPORT JNIEXPORT jint JNICALL JNI_GetCreatedJavaVMs(JavaVM** pvm,
	jsize a, jsize* b)
{
	abort();
	return -1;
}
