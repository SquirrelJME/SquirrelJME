/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JNI Compatibility header.
 * 
 * @since 2025/10/22
 */

#ifndef SJME_C_SQUIRRELJME_JNI_H
#define SJME_C_SQUIRRELJME_JNI_H

#include "sjme/config.h"
#include "sjme/jvm/jni_md.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JNI_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Constructs a JNI version. */
#define SJME_JNI_VERSION(major, minor) \
	((INT32_C(major) << INT32_C(4)) | INT32_C(minor))

/** JNI 1.1. */
#define JNI_VERSION_1_1 SJME_JNI_VERSION(1, 1)

/** JNI 1.2. */
#define JNI_VERSION_1_2 SJME_JNI_VERSION(1, 2)

/** JNI 1.4. */
#define JNI_VERSION_1_2 SJME_JNI_VERSION(1, 4)

/** JNI 1.6. */
#define JNI_VERSION_1_2 SJME_JNI_VERSION(1, 6)

/** JNI 1.8. */
#define JNI_VERSION_1_2 SJME_JNI_VERSION(1, 8)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JNI_H
}
#undef SJME_CXX_SQUIRRELJME_JNI_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JNI_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JNI_H */
