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

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_JNIHELPER_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

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
 * Declares a JNI method.
 *
 * @param inClass The class this is in.
 * @param methodName The name of the method.
 * @since 2023/12/08
 */
#define JNI_METHOD(inClass, methodName) \
	sjme_attrUnused JNIEXPORT JNICALL SJME_TOKEN_PASTE_PP(Java_, \
	SJME_TOKEN_PASTE_PP(SJME_TOKEN_PASTE_PP(inClass, _), methodName))

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
