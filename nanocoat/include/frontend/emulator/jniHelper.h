/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Helper for JNI functions accordingly, to simplify how they are declared.
 * 
 * @since 2023/12/08
 */

#ifndef SQUIRRELJME_JNIHELPER_H
#define SQUIRRELJME_JNIHELPER_H

#include "sjme/stdTypes.h"
#include "sjme/error.h"
#include "sjme/tokenUtils.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_JNIHELPER_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The NanoCoat virtual machine package. */
#define SJME_PACKAGE_NANOCOAT cc_squirreljme_vm_nanocoat

/**
 * Maps a pointer to the @c jlong type.
 *
 * @param pointer The pointer to map.
 * @return The resultant pointer.
 * @since 2023/12/08
 */
#define SJME_POINTER_TO_JLONG(pointer) \
	((jlong)((intptr_t)(pointer)))

/**
 * Maps a pointer to the given type.
 *
 * @param pointer The pointer to unmap.
 * @return The resultant pointer.
 * @since 2023/12/08
 */
#define SJME_JLONG_TO_POINTER(type, pointer) \
	((type)((intptr_t)((jlong)(pointer))))

/**
 * Declares a JNI class.
 *
 * @param inPackage The package of the class.
 * @param className The name of the class.
 * @since 2023/12/12
 */
#define SJME_JNI_CLASS(inPackage, className) \
	SJME_TOKEN_PASTE_PP(SJME_TOKEN_PASTE_PP(inPackage, _), className)

/**
 * Declares a JNI method.
 *
 * @param inClass The class this is in.
 * @param methodName The name of the method.
 * @since 2023/12/08
 */
#define SJME_JNI_METHOD(inClass, methodName) \
	sjme_attrUnused JNIEXPORT JNICALL SJME_TOKEN_PASTE_PP(Java_, \
	SJME_TOKEN_PASTE_PP(SJME_TOKEN_PASTE_PP(inClass, _), methodName))

/** Allocation link. */
#define SJME_CLASS_ALLOC_LINK \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, AllocLink)

/** The Java allocation pool class. */
#define SJME_CLASS_ALLOC_POOL \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, AllocPool)

/** Allocation sizeof utility. */
#define SJME_CLASS_ALLOC_SIZEOF \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, AllocSizeOf)

/** Character array string. */
#define SJME_CLASS_CHAR_STAR \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, CharStar)

/** Flat list. */
#define SJME_CLASS_FLAT_LIST \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, FlatList)

/** The frame class. */
#define SJME_CLASS_NVM_FRAME \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, NvmFrame)

/** The state class. */
#define SJME_CLASS_NVM_STATE \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, NvmState)

/** The thread class. */
#define SJME_CLASS_NVM_THREAD \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, NvmThread)

/** The library class. */
#define SJME_CLASS_VIRTUAL_LIBRARY \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, VirtualLibrary)

/** The suite class. */
#define SJME_CLASS_VIRTUAL_SUITE \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, VirtualSuite)

/** The boot parameter class. */
#define SJME_CLASS_NVM_BOOT_PARAM \
	SJME_JNI_CLASS(SJME_PACKAGE_NANOCOAT, NvmBootParam)

/**
 * Checks to see if a virtual machine call failed.
 *
 * @param env The Java environment.
 * @return If there is an exception.
 * @since 2023/12/29
 */
sjme_jboolean sjme_jni_checkVMException(JNIEnv* env);

/**
 * Throws a @c VMException .
 *
 * @param env The current Java environment.
 * @param code The error code.
 * @since 2023/12/08
 */
void sjme_jni_throwVMException(JNIEnv* env, sjme_errorCode code);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_JNIHELPER_H
}
		#undef SJME_CXX_SQUIRRELJME_JNIHELPER_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_JNIHELPER_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_JNIHELPER_H */
