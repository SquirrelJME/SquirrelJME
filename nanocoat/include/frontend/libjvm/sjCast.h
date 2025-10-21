/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Cast handling and helpers between JNI and SquirrelJME.
 * 
 * @since 2025/10/20
 */

#ifndef SJME_C_SQUIRRELJME_SJCAST_H
#define SJME_C_SQUIRRELJME_SJCAST_H

#include "frontend/libjvm/internals.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SJCAST_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Maps a Java Object to a SquirrelJME Object.
 *
 * @param env The context environment.
 * @param outSo Output SquirrelJME Object.
 * @param inJo Input Java Object.
 * @return Any resultant error, if any.
 * @since 2025/10/20
 */
sjme_errorCode sjme_jni_joToSo(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrOutNotNull sjme_jobject* outSo,
	sjme_attrInNotNull jobject inJo);

/**
 * Maps a JavSquirrelJME Object to a Java Object.
 * 
 * @param env The context environment.
 * @param outJo Output Java Object.
 * @param inSo Input SquirrelJME Object.
 * @return Any resultant error, if any.
 * @since 2025/10/20
 */
sjme_errorCode sjme_jni_soToJo(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jobject* outJo,
	sjme_attrOutNotNull sjme_jobject inSo);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SJCAST_H
}
#undef SJME_CXX_SQUIRRELJME_SJCAST_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SJCAST_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SJCAST_H */
