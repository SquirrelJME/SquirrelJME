/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

// The class to forward to
#define SWINGPENCIL_CLASSNAME "cc/squirreljme/emulator/uiform/SwingPencilShelf"

// Descriptors
#define SWINGPENCIL_CAPABILITIES_DESC "(I)I"
#define SWINGPENCIL_NATIVEIMAGELOADTYPES_DESC "()I"

JNIEXPORT jint JNICALL Impl_mle_PencilShelf_capabilities(JNIEnv* env,
	jclass classy, jint pixelFormat)
{
	return forwardCallStaticInteger(env, SWINGPENCIL_CLASSNAME,
		"capabilities", SWINGPENCIL_CAPABILITIES_DESC,
		pixelFormat);
}

JNIEXPORT jint JNICALL Impl_mle_PencilShelf_nativeImageLoadTypes(JNIEnv* env,
	jclass classy)
{
	// This is not supported anywhere!
	return 0;
}

static const JNINativeMethod mlePencilMethods[] =
{
	{"capabilities", SWINGPENCIL_CAPABILITIES_DESC, (void*)Impl_mle_PencilShelf_capabilities},
	{"nativeImageLoadTypes", SWINGPENCIL_NATIVEIMAGELOADTYPES_DESC, (void*)Impl_mle_PencilShelf_nativeImageLoadTypes},
};

jint JNICALL mlePencilInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/PencilShelf"),
		mlePencilMethods, sizeof(mlePencilMethods) / sizeof(JNINativeMethod));
}
