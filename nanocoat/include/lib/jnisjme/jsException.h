/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JNI Exception Helpers.
 * 
 * @since 2026/10/20
 */

#ifndef SJME_C_SQUIRRELJME_JSEXCEPTION_H
#define SJME_C_SQUIRRELJME_JSEXCEPTION_H

#include "sjme/jvm/use/useJni.h"
#include "sjme/error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JSEXCEPTION_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Checks to see if a virtual machine call failed.
 *
 * @param env The Java environment.
 * @return If there is an exception.
 * @since 2023/12/29
 */
sjme_jboolean sjme_js_checkVMException(JNIEnv* env);

/**
 * Throws a @c VMException .
 *
 * @param env The current Java environment.
 * @param code The error code.
 * @since 2023/12/08
 */
void sjme_js_throwVMException(JNIEnv* env, sjme_errorCode code);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JSEXCEPTION_H
}
#undef SJME_CXX_SQUIRRELJME_JSEXCEPTION_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JSEXCEPTION_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JSEXCEPTION_H */
