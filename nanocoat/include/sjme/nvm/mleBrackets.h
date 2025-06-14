/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * MLE Bracket instance types.
 * 
 * @since 2025/02/23
 */

#ifndef SQUIRRELJME_MLEBRACKETS_H
#define SQUIRRELJME_MLEBRACKETS_H

#include "sjme/nvm/instance.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_MLEBRACKETS_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The name of the pipe bracket. */
#define SJME_NVM_BRACKET_NAME_PIPE \
	"cc/squirreljme/jvm/mle/brackets/PipeBracket"
	
/**
 * Pipe bracket.
 *
 * @since 2025/02/23
 */
typedef struct sjme_nvm_mle_pipeBase sjme_nvm_mle_pipeBase;

/**
 * Pipe bracket.
 *
 * @since 2025/02/23
 */
typedef sjme_nvm_mle_pipeBase* sjme_nvm_mle_pipe;

struct sjme_nvm_mle_pipeBase
{
	/** Base object. */
	sjme_jobjectBase object;

	/** Is output stream? */
	sjme_jboolean isOutput;
	
	/** The wrapped stream. */
	union
	{
		/** Base closeable. */
		sjme_closeable closeable;
		
		/** Input stream. */
		sjme_stream_input in;

		/** Output stream. */
		sjme_stream_output out;
	} stream;
};
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_MLEBRACKETS_H
}
#undef SJME_CXX_MLEBRACKETS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_MLEBRACKETS_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MLEBRACKETS_H */
