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
#if !defined(SJME_BARE_METAL) && (defined(SJME_FEATURE_C99) || \
	defined(SJME_FEATURE_MSVC_HAS_STDINT) || \
	(defined(__WATCOMC__) && __WATCOMC__ >= 1270) || \
	(defined(__GNUC__) && __GNUC__ >= 4) || \
	(defined(PSP) || defined(PS2)))
	/** <stdint.h> is available. */
	#define SJME_HAS_STDINT_H
#endif

/* Is there a terminal output? */
#if !defined(SJME_SYSTEM_IEEE1275)
	#define SJME_HAS_TERMINAL_OUTPUT
#endif

/* Is PowerPC available? */
#if defined(__PPC) || defined(__PPC__) || defined(_ARCH_PPC)
	/** Power PC is available. */
	#define SJME_HAS_POWERPC
#endif

/* Is SPARC available? */
#if defined(__sparc__) || defined(__sparc)
	/** SPARC is available. */
	#define SJME_HAS_SPARC
#endif

/* Attempt detection of pointer sizes based on architecture? */
#if (defined(__SIZEOF_POINTER__) && __SIZEOF_POINTER__ == 4) || \
	defined(_ILP32) || defined(__ILP32__)
	/** Pointer size. */
	#define SJME_POINTER 32
#elif (defined(__SIZEOF_POINTER__) && __SIZEOF_POINTER__ == 8) || \
	defined(_LP64) || defined(_LP64)
	/** Pointer size. */
	#define SJME_POINTER 64
#else
	/* 64-bit seeming architecture, common 64-bit ones? */
	#if defined(__amd64__) || defined(__amd64__) || defined(__x86_64__) || \
		defined(__x86_64) || defined(_M_X64) || defined(_M_AMD64) ||          \
        defined(_M_ARM64) || defined(_M_ARM64EC) || \
        defined(__aarch64__) || defined(__ia64__) || defined(_IA64) ||  \
        defined(__IA64__) || defined(__ia64) || defined(_M_IA64) || \
        defined(__itanium__) || defined(__powerpc64__) || \
		defined(__ppc64__) || defined(__PPC64__) || defined(_ARCH_PPC64) ||   \
        defined(_WIN64)
		/** Pointer size. */
		#define SJME_POINTER 64
	#else
		/** Pointer size. */
		#define SJME_POINTER 32
	#endif
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
