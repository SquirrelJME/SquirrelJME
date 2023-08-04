/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Native Shelf Abstraction (NAL).
 * 
 * @since 2023/07/29
 */

#ifndef SQUIRRELJME_NATIVE_H
#define SQUIRRELJME_NATIVE_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_NATIVE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Native Abstraction Layer functions.
 * 
 * @since 2023/07/29
 */
typedef struct sjme_nalFunctions
{
	int todo;
} sjme_nalFunctions;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_NATIVE_H
}
		#undef SJME_CXX_SQUIRRELJME_NATIVE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_NATIVE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_NATIVE_H */
