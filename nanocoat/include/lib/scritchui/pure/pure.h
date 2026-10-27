/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Pure ScritchUI definitions.
 * 
 * @since 2025/03/03
 */

#ifndef SQUIRRELJME_PURE_H
#define SQUIRRELJME_PURE_H

#include "lib/scritchui/scritchui.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_PURE_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Functions used to provide the pure framebuffer interface.
 * 
 * @since 2026/10/27
 */
typedef struct sjme_scritchui_pure_functions
{
	/** The driver name. */
	sjme_lpcstr driverName;
} sjme_scritchui_pure_functions;
	
/**
 * Initializes the pure framebuffer interface.
 * 
 * @param inPool The allocation pool to use.
 * @param outState The resultant state.
 * @param pureFunctions The pure function interface, which provides access to
 * the framebuffer.
 * @return Any resultant error, if any.
 * @since 2026/10/27
 */
sjme_errorCode sjme_attrExport sjme_scritchui_pure_init(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull const sjme_scritchui_pure_functions* pureFunctions);
	
sjme_errorCode sjme_scritchui_pure_apiInit(
	sjme_attrInNotNull sjme_scritchui inState);
	
sjme_errorCode sjme_scritchui_pure_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);
	
sjme_errorCode sjme_scritchui_pure_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_PURE_H
}
#undef SJME_CXX_PURE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_PURE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PURE_H */
