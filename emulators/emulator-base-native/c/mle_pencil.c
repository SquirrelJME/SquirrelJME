/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

// The class to forward to
#define SWINGPENCIL_CLASSNAME "cc/squirreljme/emulator/uiform/SwingPencilShelf"

// Descriptors
#define SWINGPENCIL_CAPABILITIES_DESC "(I)I"
#define SWINGPENCIL_NATIVEIMAGELOADTYPES_DESC "()I"
#define SWINGPENCIL_NATIVEIMAGELOADRGBA_DESC "(I[BIILcc/squirreljme/jvm/mle/callbacks/NativeImageLoadCallback;)Ljava/lang/Object;"

JNIEXPORT jint JNICALL Impl_mle_PencilShelf_capabilities(JNIEnv* env,
	jclass classy, jint pixelFormat)
{
	return forwardCallStaticInteger(env,  SWINGPENCIL_CLASSNAME,
		"capabilities", SWINGPENCIL_CAPABILITIES_DESC,
		pixelFormat);
}

JNIEXPORT jobject JNICALL Impl_mle_PencilShelf_nativeImageLoadRGBA(JNIEnv* env,
	jclass classy, jint type, jbyteArray buf, jint off, jint len,
	jobject callback)
{
	return forwardCallStaticObject(env, SWINGPENCIL_CLASSNAME,
		"nativeImageLoadRGBA", SWINGPENCIL_NATIVEIMAGELOADRGBA_DESC,
		type, buf, off, len, callback);
}

JNIEXPORT jint JNICALL Impl_mle_PencilShelf_nativeImageLoadTypes(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticInteger(env, SWINGPENCIL_CLASSNAME,
		"nativeImageLoadTypes", SWINGPENCIL_NATIVEIMAGELOADTYPES_DESC);
}

static const JNINativeMethod mlePencilMethods[] =
{
	{"capabilities", SWINGPENCIL_CAPABILITIES_DESC,
		(void*)Impl_mle_PencilShelf_capabilities},
	{"nativeImageLoadRGBA", SWINGPENCIL_NATIVEIMAGELOADRGBA_DESC,
		(void*)Impl_mle_PencilShelf_nativeImageLoadRGBA},
	{"nativeImageLoadTypes", SWINGPENCIL_NATIVEIMAGELOADTYPES_DESC,
		(void*)Impl_mle_PencilShelf_nativeImageLoadTypes},
};

jint JNICALL mlePencilInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/PencilShelf"),
		mlePencilMethods, sizeof(mlePencilMethods) / sizeof(JNINativeMethod));
}
