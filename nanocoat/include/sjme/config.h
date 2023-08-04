/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Basic configuration header.
 * 
 * @since 2023/07/27
 */

#ifndef SQUIRRELJME_CONFIG_H
#define SQUIRRELJME_CONFIG_H

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CONFIG_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_ROM0)
	/** ROM 0 Address. */
	#define SJME_CONFIG_ROM0_ADDR &SJME_CONFIG_ROM0
#elif !defined(SJME_CONFIG_ROM0_ADDR)
	/** ROM 0 Address. */
	#define SJME_CONFIG_ROM0_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM1)
	/** ROM 1 Address. */
	#define SJME_CONFIG_ROM1_ADDR &SJME_CONFIG_ROM1
#elif !defined(SJME_CONFIG_ROM1_ADDR)
	/** ROM 1 Address. */
	#define SJME_CONFIG_ROM1_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM2)
	/** ROM 2 Address. */
	#define SJME_CONFIG_ROM2_ADDR &SJME_CONFIG_ROM2
#elif !defined(SJME_CONFIG_ROM2_ADDR)
	/** ROM 2 Address. */
	#define SJME_CONFIG_ROM2_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM3)
	/** ROM 3 Address. */
	#define SJME_CONFIG_ROM3_ADDR &SJME_CONFIG_ROM3
#elif !defined(SJME_CONFIG_ROM3_ADDR)
	/** ROM 3 Address. */
	#define SJME_CONFIG_ROM3_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM4)
	/** ROM 4 Address. */
	#define SJME_CONFIG_ROM4_ADDR &SJME_CONFIG_ROM4
#elif !defined(SJME_CONFIG_ROM4_ADDR)
	/** ROM 4 Address. */
	#define SJME_CONFIG_ROM4_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM5)
	/** ROM 5 Address. */
	#define SJME_CONFIG_ROM5_ADDR &SJME_CONFIG_ROM5
#elif !defined(SJME_CONFIG_ROM5_ADDR)
	/** ROM 5 Address. */
	#define SJME_CONFIG_ROM5_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM6)
	/** ROM 6 Address. */
	#define SJME_CONFIG_ROM6_ADDR &SJME_CONFIG_ROM6
#elif !defined(SJME_CONFIG_ROM6_ADDR)
	/** ROM 6 Address. */
	#define SJME_CONFIG_ROM6_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM7)
	/** ROM 7 Address. */
	#define SJME_CONFIG_ROM7_ADDR &SJME_CONFIG_ROM7
#elif !defined(SJME_CONFIG_ROM7_ADDR)
	/** ROM 7 Address. */
	#define SJME_CONFIG_ROM7_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM8)
	/** ROM 8 Address. */
	#define SJME_CONFIG_ROM8_ADDR &SJME_CONFIG_ROM8
#elif !defined(SJME_CONFIG_ROM8_ADDR)
	/** ROM 8 Address. */
	#define SJME_CONFIG_ROM8_ADDR NULL
#endif

#if defined(SJME_CONFIG_ROM9)
	/** ROM 9 Address. */
	#define SJME_CONFIG_ROM9_ADDR &SJME_CONFIG_ROM9
#elif !defined(SJME_CONFIG_ROM9_ADDR)
	/** ROM 9 Address. */
	#define SJME_CONFIG_ROM9_ADDR NULL
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CONFIG_H
}
		#undef SJME_CXX_SQUIRRELJME_CONFIG_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CONFIG_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CONFIG_H */
