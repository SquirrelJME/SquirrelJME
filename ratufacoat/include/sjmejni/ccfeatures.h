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
		/** MSVC has the <stdint.h> header. */
		#define SJME_FEATURE_MSVC_HAS_STDINT
	#endif
#endif

/* GCC. */
#if defined(__GNUC__)
	/** Using GCC. */
	#define SJME_FEATURE_GCC

	#if __GNUC__ <= 2
		/** Old GCC is used. */
		#define SJME_FEATURE_OLD_GCC
	#endif
#endif

/* Is there <stdint.h>? */
#if !defined(SJME_BARE_METAL) && (defined(SJME_FEATURE_C99) || \
	defined(SJME_FEATURE_MSVC_HAS_STDINT) || \
	(defined(__WATCOMC__) && __WATCOMC__ >= 1270) || \
	(defined(__GNUC__) && __GNUC__ >= 4) || \
	(defined(PSP) || defined(PS2)) || \
	defined(SQUIRRELJME_BUNDLED_STDC))
	/** <stdint.h> is available. */
	#define SJME_HAS_STDINT_H
#endif

/** Linux. */
#if defined(__linux__) || defined(__gnu_linux__)
	#define SJME_HAS_LINUX 1
#endif

/* Is there a terminal output? */
#if !defined(SJME_SYSTEM_IEEE1275) && !defined(SJME_SYSTEM_PALMOS)
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

/* Synthetic long? */
#if 0
#if defined(SJME_SYSTEM_PALMOS) && defined(__m68k__)
	/** Synthetic long should be used. */
	#define SJME_HAS_SYNTHETIC_LONG
#endif
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

/* Is this a big endian system? */
#if defined(__BIG_ENDIAN__) || defined(__ARMEB__) || defined(__THUMBEB__) || \
	defined(__AARCH64EB__) || defined(_MIPSEB) || defined(__MIPSEB) || \
	defined(__MIPSEB__) || \
	(defined(__BYTE_ORDER__) && defined(__ORDER_BIG_ENDIAN__) && \
		__BYTE_ORDER__ == __ORDER_BIG_ENDIAN__) || \
	(defined(SJME_SYSTEM_PALMOS) && defined(__m68k__))
	/** Big Endian. */
	#define SJME_ENDIAN_BIG
#else
	/** Little Endian. */
	#define SJME_ENDIAN_LITTLE
#endif

/* Reduce waste? */
#if defined(SJME_SYSTEM_PALMOS)
	#define SJME_REDUCE_WASTE
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
