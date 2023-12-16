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
#include "sjme/util.h"

jint SJME_JNI_METHOD(SJME_CLASS_CHAR_STAR, _1_1utfStrlen)
	(JNIEnv* env, jclass* classy, jlong addr)
{
	return sjme_stringLength(SJME_JLONG_TO_POINTER(const char*, addr));
}