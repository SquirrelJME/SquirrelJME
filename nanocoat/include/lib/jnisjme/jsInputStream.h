/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Bridges between SquirrelJME's Input Stream and JNI's Input Stream.
 * 
 * @since 2026/06/01
 */

#ifndef SJME_C_SQUIRRELJME_JSINPUTSTREAM_H
#define SJME_C_SQUIRRELJME_JSINPUTSTREAM_H

#include "sjme/jvm/use/useJni.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JSINPUTSTREAM_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Wraps the given Java @code InputStream @endcode with a SquirrelJME stream.
 *
 * @param allocPool The allocation pool used.
 * @param outStream The resultant stream.
 * @param jEnv The JNI environment.
 * @param jInputStream The Java @code InputStream @endcode to wrap.
 * @return Any resultant error, if any.
 * @since 2026/06/01
 */
sjme_errorCode sjme_js_wrapInputStream(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull JNIEnv* jEnv,
	sjme_attrInNotNull jobject jInputStream);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JSINPUTSTREAM_H
}
#undef SJME_CXX_SQUIRRELJME_JSINPUTSTREAM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JSINPUTSTREAM_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JSINPUTSTREAM_H */
