/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Engine stubs.
 * 
 * @since 2022/05/22
 */

#ifndef SQUIRRELJME_ENGINESTUB_H
#define SQUIRRELJME_ENGINESTUB_H

#include "frontend/frontfunc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ENGINESTUB_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Front function stubs. */
extern const sjme_frontBridge sjme_testStubFrontBridge; 

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ENGINESTUB_H
}
		#undef SJME_CXX_SQUIRRELJME_ENGINESTUB_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ENGINESTUB_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ENGINESTUB_H */
