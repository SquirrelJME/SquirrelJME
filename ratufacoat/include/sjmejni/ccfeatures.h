/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * C Compiler Feature Detection.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_CCFEATURES_H
#define SQUIRRELJME_CCFEATURES_H

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CCFEATURES_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/* Check version of Standard C. */
#if defined(__STDC_VERSION__)
	#if __STDC_VERSION__ >= 201112L
		/** C11 Available. */
		#define SJME_FEATURE_C11
	#endif

	#if __STDC_VERSION__ >= 199901L
		/** C99 Available. */
		#define SJME_FEATURE_C99
	#endif
#endif

/* Visual Studio. */
#if defined(_MSC_VER)
	/** Visual Studio is available. */
	#define SJME_FEATURE_MSVC

	#if defined(_MSC_VER) && _MSC_VER >= 1600
		#define SJME_FEATURE_MSVC_HAS_STDINT
	#endif
#endif

/* GCC. */
#if defined(__GNUC__)
	#define SJME_FEATURE_GCC
#endif

/* Is there <stdint.h>? */
#if !defined(SQUIRRELJME_BARE_METAL) && (defined(SJME_FEATURE_C99) || \
	defined(SJME_FEATURE_MSVC_HAS_STDINT) || \
	(defined(__WATCOMC__) && __WATCOMC__ >= 1270) || \
	(defined(__GNUC__) && __GNUC__ >= 4) || \
	(defined(PSP) || defined(PS2)))
	/** <stdint.h> is available. */
	#define SJME_HAS_STDINT_H
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CCFEATURES_H
}
		#undef SJME_CXX_SQUIRRELJME_CCFEATURES_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CCFEATURES_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CCFEATURES_H */
