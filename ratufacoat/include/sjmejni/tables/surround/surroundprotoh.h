/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Surrounding prototypes declared in a header.
 * 
 * @since 2022/12/17
 */

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SURROUNDPROTOH_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/* Remove previous declaration. */
#if defined(SJME_FUNC_SURROUND)
	#undef SJME_FUNC_SURROUND
#endif

#if defined(SJME_FUNC_SURROUND_SUFFIX)
	#undef SJME_FUNC_SURROUND_SUFFIX
#endif

/** Change the name so the name does not clash in any way. */
#define SJME_FUNC_SURROUND(x) sjme_impl ## x

/** End of line for prototype. */
#define SJME_FUNC_SURROUND_SUFFIX SJME_CODE_SECTION("sjmejni") ;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SURROUNDPROTOH_H
}
		#undef SJME_CXX_SQUIRRELJME_SURROUNDPROTOH_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SURROUNDPROTOH_H */
#endif     /* #ifdef __cplusplus */
