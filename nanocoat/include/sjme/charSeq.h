/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Character sequences.
 * 
 * @since 2024/06/26
 */

#ifndef SQUIRRELJME_CHARSEQ_H
#define SQUIRRELJME_CHARSEQ_H

#include "sjme/nvm.h" 

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CHARSEQ_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * A character sequence which contains a set of characters within a string,
 * may be modifiable or not.
 * 
 * @since 2024/06/26
 */
typedef struct sjme_charSeq sjme_charSeq;

/**
 * Functions which are used to process character sequences.
 * 
 * @since 2024/06/26
 */
typedef struct sjme_charSeq_functions
{
} sjme_charSeq_functions;

struct sjme_charSeq
{
	/** Front end data, if any. */
	sjme_frontEnd frontEnd;
	
	/** Context pointer, if any. */
	sjme_pointer context;
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CHARSEQ_H
}
		#undef SJME_CXX_SQUIRRELJME_CHARSEQ_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CHARSEQ_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CHARSEQ_H */
