/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Test data.
 * 
 * @file
 * @since 2026/10/31
 */

#ifndef SJME_C_SQUIRRELJME_DATA_H
#define SJME_C_SQUIRRELJME_DATA_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_DATA_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
/**
 * Software long test data parts.
 * 
 * @since 2026/10/31
 */
typedef struct sjme_test_jlongPart
{
	/** The high bits. */
	sjme_jint hi;
	
	/** The low bits. */
	sjme_juint lo;
} sjme_test_jlongPart;
	
/**
 * Software double test data parts.
 * 
 * @since 2026/10/31
 */
typedef struct sjme_test_jdoublePart
{
	/** The high bits. */
	sjme_juint hi;
	
	/** The low bits. */
	sjme_juint lo;
} sjme_test_jdoublePart;
	
/** The number of test longs. */
#define SJME_TEST_NUM_LONGS 1024

extern const sjme_test_jlongPart sjme_test_longs_in[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_add[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_sub[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_mul[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_div[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_rem[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_neg[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_and[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_or[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_xor[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_shl[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_shr[SJME_TEST_NUM_LONGS];
extern const sjme_test_jlongPart sjme_test_longs_ushr[SJME_TEST_NUM_LONGS];
extern const sjme_jint sjme_test_longs_cmp[SJME_TEST_NUM_LONGS];
extern const sjme_jint sjme_test_longs_int[SJME_TEST_NUM_LONGS];
extern const sjme_juint sjme_test_longs_float[SJME_TEST_NUM_LONGS];
extern const sjme_test_jdoublePart sjme_test_longs_double[SJME_TEST_NUM_LONGS];
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_DATA_H
}
#undef SJME_CXX_SQUIRRELJME_DATA_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_DATA_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_DATA_H */
