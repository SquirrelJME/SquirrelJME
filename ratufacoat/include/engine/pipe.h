/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Pipe access and otherwise.
 * 
 * @since 2022/01/08
 */

#ifndef SQUIRRELJME_PIPE_H
#define SQUIRRELJME_PIPE_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_PIPE_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Standard pipe descriptor identifiers.
 *
 * @since 2020/06/14
 */
typedef enum sjme_standardPipeType
{
	/** Standard input. */
	SJME_STANDARD_PIPE_STDIN = 0,
	
	/** Standard output. */
	SJME_STANDARD_PIPE_STDOUT = 1,
	
	/** Standard error. */
	SJME_STANDARD_PIPE_STDERR = 2,
	
	/** The number of standard pipes. */
	NUM_SJME_STANDARD_PIPES = 3,
} sjme_standardPipeType;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_PIPE_H
}
#undef SJME_CXX_SQUIRRELJME_PIPE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_PIPE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PIPE_H */
