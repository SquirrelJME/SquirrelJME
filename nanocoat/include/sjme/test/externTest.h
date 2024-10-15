/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * External test support.
 * 
 * @since 2023/12/21
 */

#ifndef SQUIRRELJME_EXTERNTEST_H
#define SQUIRRELJME_EXTERNTEST_H

#include "sjme/nvm/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_EXTERNTEST_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Basic main test entry point.
 *
 * @param argc Argument count.
 * @param argv Program arguments.
 * @param nextTest The name of the next test.
 * @return The exit code.
 * @since 2023/12/21
 */
int sjme_test_main(int argc, sjme_lpstr* argv, sjme_lpcstr* nextTest);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_EXTERNTEST_H
}
		#undef SJME_CXX_SQUIRRELJME_EXTERNTEST_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_EXTERNTEST_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_EXTERNTEST_H */
