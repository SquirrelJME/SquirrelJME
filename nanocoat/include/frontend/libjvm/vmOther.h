/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Other functions.
 * 
 * @since 2026/06/28
 */

#ifndef SJME_C_SQUIRRELJME_VMOTHER_H
#define SJME_C_SQUIRRELJME_VMOTHER_H

#include "sjme/config.h"
#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/internals.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_VMOTHER_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Not implemented fallback.
 *
 * @param ignored Ignored.
 * @param ... Ignored.
 * @return Fails always.
 * @since 2026/06/28
 */
int sjme_jni_EnvTodoImpl(int ignored, ...);

/**
 * Not implemented fallback.
 *
 * @param ignored Ignored.
 * @param ... Ignored.
 * @return Fails always.
 * @since 2026/06/28
 */
int sjme_jni_JvmTodoImpl(int ignored, ...);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_VMOTHER_H
}
#undef SJME_CXX_SQUIRRELJME_VMOTHER_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_VMOTHER_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_VMOTHER_H */