/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Traversal test definitions.
 * 
 * @since 2024/09/02
 */

#ifndef SQUIRRELJME_TESTTRAVERSE_H
#define SQUIRRELJME_TESTTRAVERSE_H

#include "sjme/stdTypes.h"
#include "sjme/traverse.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_TESTTRAVERSE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#define MAX_ELEMENTS 32

typedef struct test_data
{
	sjme_jint a;
	
	sjme_jint b;
} test_data;

SJME_TRAVERSE_DECLARE(test_data, 0);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_TESTTRAVERSE_H
}
		#undef SJME_CXX_SQUIRRELJME_TESTTRAVERSE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_TESTTRAVERSE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TESTTRAVERSE_H */
