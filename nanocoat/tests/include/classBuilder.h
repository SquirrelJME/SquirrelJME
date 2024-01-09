/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Builder for class files.
 * 
 * @since 2024/01/09
 */

#ifndef SQUIRRELJME_CLASSBUILDER_H
#define SQUIRRELJME_CLASSBUILDER_H

#include "test.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLASSBUILDER_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents the state of the class builder.
 *
 * @since 2024/01/09
 */
typedef struct sjme_classBuilder
{
	/** The stream where the raw class data is written to. */
	sjme_stream_output stream;

	/** Whatever data that is needed. */
	void* whatever;
} sjme_classBuilder;

/**
 * Initializes the class builder.
 *
 * @param inPool The pool to allocate within.
 * @param outState The output state of the class builder.
 * @param whatever Whatever data is needed, this is optional.
 * @return Any resultant error, if any.
 * @since 2024/01/09
 */
sjme_errorCode sjme_classBuilder_init(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_classBuilder* outState,
	sjme_attrInNullable void* whatever);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLASSBUILDER_H
}
		#undef SJME_CXX_SQUIRRELJME_CLASSBUILDER_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSBUILDER_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLASSBUILDER_H */
