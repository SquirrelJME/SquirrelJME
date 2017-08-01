/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * This contains the Java invoke interface calls.
 *
 * @since 2016/10/19
 */

/** Header guard. */
#ifndef SJME_hGJIIH
#define SJME_hGJIIH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXJIIH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#include "wintercoat.h"

/**
 * Attaches the current thread to the virtual machine.
 *
 * @param pvm The owning virtual machine.
 * @param penv The output environment.
 * @param pargs Thread start arguments.
 * @return JNI_OK on success, otherwise an error.
 * @since 2016/10/19
 */
jint JNICALL WC_JII_AttachCurrentThread(JavaVM* pvm, void** penv, void* pargs);

jint JNICALL WC_JII_AttachCurrentThreadAsDaemon(JavaVM* pvm, void** penv,
	void* pargs);

jint JNICALL WC_JII_DestroyJavaVM(JavaVM* pvm);

jint JNICALL WC_JII_DetachCurrentThread(JavaVM* pvm);

jint JNICALL WC_JII_GetEnv(JavaVM* pvm, void** penv, jint pversion);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXJIIH
}
#undef SJME_cXJIIH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXJIIH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGJIIH */

