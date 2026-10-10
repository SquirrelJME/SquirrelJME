/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Static SquirrelJME payload setup, can be used at compile time to set some
 * initial boot settings and otherwise embedded into the image.
 * 
 * @file
 * @since 2023/07/27
 */

#ifndef SJME_C_PAYLOAD_H
#define SJME_C_PAYLOAD_H

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_PAYLOAD_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_PAYLOAD_H
}
		#undef SJME_CXX_SQUIRRELJME_PAYLOAD_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_PAYLOAD_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PAYLOAD_H */
