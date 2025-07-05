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

sjme_errorCode sjme_attrExport SJME_SCRITCHUI_DYLIB_SYMBOL(pure)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable const sjme_scritchui_externalFunctions* externals,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd);

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
