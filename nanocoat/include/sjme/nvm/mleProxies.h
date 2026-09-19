/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Proxies for MLE shelves.
 *
 * @file
 * @since 2026/09/19
 */

#ifndef SJME_C_SQUIRRELJME_MLEPROXIES_H
#define SJME_C_SQUIRRELJME_MLEPROXIES_H

#include "sjme/nvm/instanceProxy.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_MLEPROXIES_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_nvm_mle_scritchUiProxyHandler(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject proxyInstance,
	sjme_attrInNotNull sjme_jmethodID proxyMethod);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_MLEPROXIES_H
}
#undef SJME_CXX_SQUIRRELJME_MLEPROXIES_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_MLEPROXIES_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_MLEPROXIES_H */