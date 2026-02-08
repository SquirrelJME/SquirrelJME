/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Binary literals in preprocessor.
 * 
 * @file 
 * @since 2026/02/07
 */

#ifndef SJME_C_SQUIRRELJME_BINARY_H
#define SJME_C_SQUIRRELJME_BINARY_H

#include "sjme/tokenUtils.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_BINARY_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(binaryValues)
	
#define SJME_INTX_b0000 0
#define SJME_INTX_b0001 1
#define SJME_INTX_b0010 2
#define SJME_INTX_b0011 3
#define SJME_INTX_b0100 4
#define SJME_INTX_b0101 5
#define SJME_INTX_b0110 6
#define SJME_INTX_b0111 7
#define SJME_INTX_b1000 8
#define SJME_INTX_b1001 9
#define SJME_INTX_b1010 A
#define SJME_INTX_b1011 B
#define SJME_INTX_b1100 C
#define SJME_INTX_b1101 D
#define SJME_INTX_b1110 E
#define SJME_INTX_b1111 F
	
#pragma endregion(binaryValues)
	
/** Convert binary to hex. */
#define SJME_INTX_b(x) SJME_TOKEN_SINGLE(SJME_TOKEN_PASTE(SJME_INTX_b, x))

/** Signed 8-bit binary literal. */
#define SJME_INT8_b(a, b) \
	INT8_C(SJME_TOKEN_PASTE3_PP(0x, \
		SJME_INTX_b(a), \
		SJME_INTX_b(b)))

/** Unsigned 8-bit binary literal. */
#define SJME_UINT8_b(a, b) \
	UINT8_C(SJME_TOKEN_PASTE3_PP(0x, \
		SJME_INTX_b(a), \
		SJME_INTX_b(b)))

/** Signed 16-bit binary literal. */
#define SJME_INT16_b(a, b, c, d) \
	INT16_C(SJME_TOKEN_PASTE5_PP(0x, \
		SJME_INTX_b(a), \
		SJME_INTX_b(b), \
		SJME_INTX_b(c), \
		SJME_INTX_b(d)))

/** Unsigned 16-bit binary literal. */
#define SJME_UINT16_b(a, b, c, d) \
	UINT16_C(SJME_TOKEN_PASTE5_PP(0x, \
		SJME_INTX_b(a), \
		SJME_INTX_b(b), \
		SJME_INTX_b(c), \
		SJME_INTX_b(d)))

/** Signed 32-bit binary literal. */
#define SJME_INT32_b(a, b, c, d, e, f, g, h) \
	INT32_C(SJME_TOKEN_PASTE9_PP(0x, \
		SJME_INTX_b(a), \
		SJME_INTX_b(b), \
		SJME_INTX_b(c), \
		SJME_INTX_b(d), \
		SJME_INTX_b(e), \
		SJME_INTX_b(f), \
		SJME_INTX_b(g), \
		SJME_INTX_b(h)))

/** Unsigned 32-bit binary literal. */
#define SJME_UINT32_b(a, b, c, d, e, f, g, h) \
	UINT32_C(SJME_TOKEN_PASTE9_PP(0x, \
		SJME_INTX_b(a), \
		SJME_INTX_b(b), \
		SJME_INTX_b(c), \
		SJME_INTX_b(d), \
		SJME_INTX_b(e), \
		SJME_INTX_b(f), \
		SJME_INTX_b(g), \
		SJME_INTX_b(h)))

/** Signed 64-bit binary literal. */
#define SJME_INT64_b(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) \
	SJME_TOKEN_PASTE4_PP(0x, \
		SJME_TOKEN_PASTE8_PP(SJME_INTX_b(a), SJME_INTX_b(b), \
			SJME_INTX_b(c), SJME_INTX_b(d), \
			SJME_INTX_b(e), SJME_INTX_b(f), \
			SJME_INTX_b(g), SJME_INTX_b(h)), \
		SJME_TOKEN_PASTE8_PP(SJME_INTX_b(i), SJME_INTX_b(j), \
			SJME_INTX_b(k), SJME_INTX_b(l), \
			SJME_INTX_b(m), SJME_INTX_b(n), \
			SJME_INTX_b(o), SJME_INTX_b(p)), INT64_C())

/** Unsigned 64-bit binary literal. */
#define SJME_UINT64_b(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) \
	SJME_TOKEN_PASTE4_PP(0x, \
		SJME_TOKEN_PASTE8_PP(SJME_INTX_b(a), SJME_INTX_b(b), \
			SJME_INTX_b(c), SJME_INTX_b(d), \
			SJME_INTX_b(e), SJME_INTX_b(f), \
			SJME_INTX_b(g), SJME_INTX_b(h)), \
		SJME_TOKEN_PASTE8_PP(SJME_INTX_b(i), SJME_INTX_b(j), \
			SJME_INTX_b(k), SJME_INTX_b(l), \
			SJME_INTX_b(m), SJME_INTX_b(n), \
			SJME_INTX_b(o), SJME_INTX_b(p)), UINT64_C())

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_BINARY_H
}
#undef SJME_CXX_SQUIRRELJME_BINARY_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_BINARY_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_BINARY_H */
