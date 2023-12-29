/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "frontend/emulator/common.h"
#include "frontend/emulator/jniHelper.h"
#include "sjme/debug.h"
#include "sjme/rom.h"

jlong SJME_JNI_METHOD(SJME_CLASS_VIRTUAL_LIBRARY, _1_1init)
	(JNIEnv* env, jclass classy, jlong poolPtr, jobject self, jstring javaName,
		jint id)
{
	sjme_alloc_pool* pool;
	sjme_rom_library result;

	/* Get the pool we are allocating within. */
	pool = SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr);

	/* Allocate resultant library. */

	sjme_todo("Implement this?");
	return 0;
}
