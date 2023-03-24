/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Memory IO test structure.
 * 
 * @since 2023/03/24
 */

#ifndef SQUIRRELJME_MEMIOTESTSTRUCT_H
#define SQUIRRELJME_MEMIOTESTSTRUCT_H

#include "memio/memtag.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MEMIOTESTSTRUCT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Structure.
 *
 * @since 2022/12/20
 */
typedef struct testStruct
{
	/** First. */
	sjme_jint a;

	/* Second. */
	sjme_jint b;

	/* Third. */
	sjme_jint c;
} testStruct;

/** Tagged version of the struct. */
SJME_MEMIO_DECL_TAGGED(testStruct);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MEMIOTESTSTRUCT_H
}
		#undef SJME_CXX_SQUIRRELJME_MEMIOTESTSTRUCT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMIOTESTSTRUCT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMIOTESTSTRUCT_H */
