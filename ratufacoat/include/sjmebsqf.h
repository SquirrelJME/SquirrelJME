/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Built-In SQF Font.
 *
 * @since 2019/10/06
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATSJMFHSJMEBSQFH
#define SJME_hGRATUFACOATSJMFHSJMEBSQFH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATSJMFHSJMEBSQFH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/** Upper shift value mask, since shifting off the type is undefined. */
static sjme_jint sjme_sh_umask[33] =
{
	SJME_JINT_C(0xFFFFFFFF),
	SJME_JINT_C(0xFFFFFFFE),
	SJME_JINT_C(0xFFFFFFFC),
	SJME_JINT_C(0xFFFFFFF8),
	SJME_JINT_C(0xFFFFFFF0),
	SJME_JINT_C(0xFFFFFFE0),
	SJME_JINT_C(0xFFFFFFC0),
	SJME_JINT_C(0xFFFFFF80),
	SJME_JINT_C(0xFFFFFF00),
	SJME_JINT_C(0xFFFFFE00),
	SJME_JINT_C(0xFFFFFC00),
	SJME_JINT_C(0xFFFFF800),
	SJME_JINT_C(0xFFFFF000),
	SJME_JINT_C(0xFFFFE000),
	SJME_JINT_C(0xFFFFC000),
	SJME_JINT_C(0xFFFF8000),
	SJME_JINT_C(0xFFFF0000),
	SJME_JINT_C(0xFFFE0000),
	SJME_JINT_C(0xFFFC0000),
	SJME_JINT_C(0xFFF80000),
	SJME_JINT_C(0xFFF00000),
	SJME_JINT_C(0xFFE00000),
	SJME_JINT_C(0xFFC00000),
	SJME_JINT_C(0xFF800000),
	SJME_JINT_C(0xFF000000),
	SJME_JINT_C(0xFE000000),
	SJME_JINT_C(0xFC000000),
	SJME_JINT_C(0xF8000000),
	SJME_JINT_C(0xF0000000),
	SJME_JINT_C(0xE0000000),
	SJME_JINT_C(0xC0000000),
	SJME_JINT_C(0x80000000),
	SJME_JINT_C(0x00000000)
};

/** Lower shift value mask, since shifting off the type is undefined. */
static sjme_jint sjme_sh_lmask[33] =
{
	SJME_JINT_C(0xFFFFFFFF),
	SJME_JINT_C(0x7FFFFFFF),
	SJME_JINT_C(0x3FFFFFFF),
	SJME_JINT_C(0x1FFFFFFF),
	SJME_JINT_C(0x0FFFFFFF),
	SJME_JINT_C(0x07FFFFFF),
	SJME_JINT_C(0x03FFFFFF),
	SJME_JINT_C(0x01FFFFFF),
	SJME_JINT_C(0x00FFFFFF),
	SJME_JINT_C(0x007FFFFF),
	SJME_JINT_C(0x003FFFFF),
	SJME_JINT_C(0x001FFFFF),
	SJME_JINT_C(0x000FFFFF),
	SJME_JINT_C(0x0007FFFF),
	SJME_JINT_C(0x0003FFFF),
	SJME_JINT_C(0x0001FFFF),
	SJME_JINT_C(0x0000FFFF),
	SJME_JINT_C(0x00007FFF),
	SJME_JINT_C(0x00003FFF),
	SJME_JINT_C(0x00001FFF),
	SJME_JINT_C(0x00000FFF),
	SJME_JINT_C(0x000007FF),
	SJME_JINT_C(0x000003FF),
	SJME_JINT_C(0x000001FF),
	SJME_JINT_C(0x000000FF),
	SJME_JINT_C(0x0000007F),
	SJME_JINT_C(0x0000003F),
	SJME_JINT_C(0x0000001F),
	SJME_JINT_C(0x0000000F),
	SJME_JINT_C(0x00000007),
	SJME_JINT_C(0x00000003),
	SJME_JINT_C(0x00000001),
	SJME_JINT_C(0x00000000)
};

/** Bit mask for font drawing. */
static sjme_jint sjme_drawcharbitmask[] =
{
	SJME_JINT_C(0x01),
	SJME_JINT_C(0x02),
	SJME_JINT_C(0x04),
	SJME_JINT_C(0x08),
	SJME_JINT_C(0x10),
	SJME_JINT_C(0x20),
	SJME_JINT_C(0x40),
	SJME_JINT_C(0x80),
};

/** SQF Character Widths. */
static sjme_jbyte sjme_fontcharwidths[] =
{
	6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6
};

/** SQF Character validity. */
static sjme_jbyte sjme_fontisvalidchar[] =
{
	1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
};

/** SQF Character Bitmaps. */
static sjme_jbyte sjme_fontcharbmp[] =
{
	12, 30, 51, 45, 55, 63, 55, 30, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 0, 4, 0, 0, 0, 10, 10, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 10, 10, 31, 10, 10, 10, 31, 10, 10, 0, 0, 0, 4,
	14, 21, 5, 14, 20, 20, 21, 14, 4, 0, 0, 18, 21, 10, 8, 4, 2, 10,
	21, 9, 0, 0, 0, 12, 18, 1, 1, 2, 5, 21, 9, 30, 0, 0, 0, 4, 4, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 2, 2, 2, 2, 2, 2, 2, 12, 0, 0, 0,
	6, 8, 8, 8, 8, 8, 8, 8, 6, 0, 0, 0, 21, 14, 31, 14, 21, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 4, 4, 31, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 12, 8, 0, 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 12, 0, 0, 0, 0, 16, 16, 8, 8, 4, 4, 2, 2, 0, 0, 0,
	14, 17, 17, 21, 21, 21, 17, 17, 14, 0, 0, 0, 7, 4, 4, 4, 4, 4, 4,
	4, 31, 0, 0, 0, 14, 17, 16, 8, 4, 2, 1, 1, 31, 0, 0, 0, 14, 17, 16,
	16, 14, 16, 16, 17, 14, 0, 0, 0, 17, 17, 17, 17, 31, 16, 16, 16,
	16, 0, 0, 0, 31, 1, 1, 15, 16, 16, 16, 17, 14, 0, 0, 0, 30, 1, 1,
	1, 15, 17, 17, 17, 14, 0, 0, 0, 31, 16, 16, 8, 8, 4, 4, 2, 2, 0,
	0, 0, 14, 17, 17, 17, 14, 17, 17, 17, 14, 0, 0, 0, 14, 17, 17, 17,
	30, 16, 16, 16, 16, 0, 0, 0, 0, 0, 0, 12, 12, 0, 0, 0, 12, 12, 0,
	0, 0, 0, 0, 12, 12, 0, 0, 0, 12, 8, 0, 0, 0, 16, 8, 4, 2, 4, 8, 16,
	0, 0, 0, 0, 0, 0, 0, 0, 14, 0, 14, 0, 0, 0, 0, 0, 0, 2, 4, 8, 16,
	8, 4, 2, 0, 0, 0, 0, 14, 17, 16, 16, 8, 4, 4, 0, 4, 0, 0, 0, 14,
	17, 21, 21, 21, 21, 29, 1, 30, 0, 0, 0, 14, 17, 17, 17, 31, 17, 17,
	17, 17, 0, 0, 0, 15, 17, 17, 17, 15, 17, 17, 17, 15, 0, 0, 0, 30,
	1, 1, 1, 1, 1, 1, 1, 30, 0, 0, 0, 15, 17, 17, 17, 17, 17, 17, 17,
	15, 0, 0, 0, 31, 1, 1, 1, 7, 1, 1, 1, 31, 0, 0, 0, 31, 1, 1, 1, 7,
	1, 1, 1, 1, 0, 0, 0, 30, 1, 1, 1, 29, 17, 17, 17, 14, 0, 0, 0, 17,
	17, 17, 17, 31, 17, 17, 17, 17, 0, 0, 0, 28, 8, 8, 8, 8, 8, 8, 8,
	28, 0, 0, 0, 16, 16, 16, 16, 16, 16, 17, 17, 14, 0, 0, 0, 17, 17,
	9, 5, 3, 5, 9, 17, 17, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 31, 0, 0,
	0, 27, 21, 21, 17, 17, 17, 17, 17, 17, 0, 0, 0, 17, 19, 19, 21, 21,
	21, 25, 25, 17, 0, 0, 0, 14, 17, 17, 17, 17, 17, 17, 17, 14, 0, 0,
	0, 15, 17, 17, 17, 15, 1, 1, 1, 1, 0, 0, 0, 14, 17, 17, 17, 17, 17,
	21, 9, 22, 0, 0, 0, 15, 17, 17, 17, 15, 9, 17, 17, 17, 0, 0, 0, 14,
	17, 1, 1, 14, 16, 16, 17, 14, 0, 0, 0, 31, 4, 4, 4, 4, 4, 4, 4, 4,
	0, 0, 0, 17, 17, 17, 17, 17, 17, 17, 17, 14, 0, 0, 0, 17, 17, 17,
	17, 17, 17, 18, 20, 24, 0, 0, 0, 17, 17, 17, 17, 17, 21, 21, 21,
	10, 0, 0, 0, 17, 17, 10, 4, 4, 10, 17, 17, 17, 0, 0, 0, 17, 17, 17,
	10, 4, 4, 4, 4, 4, 0, 0, 0, 31, 16, 16, 8, 4, 2, 1, 1, 31, 0, 0,
	0, 14, 2, 2, 2, 2, 2, 2, 2, 14, 0, 0, 0, 0, 1, 1, 2, 2, 4, 4, 8,
	8, 0, 0, 0, 14, 8, 8, 8, 8, 8, 8, 8, 14, 0, 0, 0, 4, 10, 17, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0, 4, 8, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 17, 17, 17, 46, 0, 0,
	0, 1, 1, 1, 1, 15, 17, 17, 17, 15, 0, 0, 0, 0, 0, 0, 0, 28, 2, 2,
	2, 28, 0, 0, 0, 16, 16, 16, 16, 30, 17, 17, 17, 30, 0, 0, 0, 0, 0,
	0, 0, 14, 17, 15, 1, 30, 0, 0, 0, 0, 24, 4, 4, 14, 4, 4, 4, 4, 0,
	0, 0, 0, 0, 0, 0, 30, 17, 17, 17, 30, 16, 15, 0, 0, 1, 1, 1, 15,
	17, 17, 17, 17, 0, 0, 0, 0, 0, 4, 0, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0,
	8, 0, 8, 8, 8, 8, 8, 4, 0, 0, 0, 1, 1, 17, 17, 15, 17, 17, 17, 0,
	0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 15, 21, 21,
	21, 21, 0, 0, 0, 0, 0, 0, 0, 15, 17, 17, 17, 17, 0, 0, 0, 0, 0, 0,
	0, 14, 17, 17, 17, 14, 0, 0, 0, 0, 0, 0, 0, 15, 17, 17, 17, 15, 1,
	1, 0, 0, 0, 0, 0, 30, 17, 17, 17, 30, 16, 16, 0, 0, 0, 0, 0, 28,
	2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 30, 1, 14, 16, 15, 0, 0, 0, 0, 0,
	4, 4, 14, 4, 4, 4, 24, 0, 0, 0, 0, 0, 0, 0, 17, 17, 17, 17, 14, 0,
	0, 0, 0, 0, 0, 0, 17, 17, 18, 20, 24, 0, 0, 0, 0, 0, 0, 0, 17, 17,
	21, 21, 10, 0, 0, 0, 0, 0, 0, 0, 17, 10, 4, 10, 17, 0, 0, 0, 0, 0,
	0, 0, 17, 17, 17, 30, 16, 16, 15, 0, 0, 0, 0, 0, 31, 8, 4, 2, 31,
	0, 0, 0, 8, 4, 4, 4, 2, 4, 4, 4, 8, 0, 0, 0, 8, 8, 8, 8, 8, 8, 8,
	8, 8, 0, 0, 0, 2, 4, 4, 4, 8, 4, 4, 4, 2, 0, 0, 0, 0, 22, 9, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 12, 30, 51, 45, 55, 63, 55, 30, 12, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 8, 0, 8, 8, 8, 8, 8, 8, 8, 0, 0, 0, 0, 8, 28, 10, 10, 10, 28,
	8, 0, 0, 24, 4, 4, 4, 14, 4, 4, 4, 30, 0, 0, 0, 12, 18, 2, 15, 2,
	15, 2, 18, 12, 0, 0, 0, 17, 17, 10, 4, 4, 14, 4, 14, 4, 0, 0, 0,
	10, 4, 30, 1, 1, 14, 16, 16, 15, 0, 0, 0, 30, 1, 3, 13, 17, 22, 24,
	16, 15, 0, 0, 0, 10, 4, 0, 0, 28, 2, 12, 16, 14, 0, 0, 0, 14, 17,
	29, 19, 19, 19, 29, 17, 14, 0, 0, 0, 4, 10, 10, 20, 0, 30, 0, 0,
	0, 0, 0, 0, 0, 40, 20, 10, 5, 10, 20, 40, 0, 0, 0, 0, 0, 0, 0, 0,
	30, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 30,
	33, 39, 43, 39, 43, 43, 33, 30, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 8, 20, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 31,
	4, 4, 31, 0, 0, 0, 0, 28, 16, 28, 4, 28, 0, 0, 0, 0, 0, 0, 0, 28,
	16, 24, 16, 28, 0, 0, 0, 0, 0, 0, 0, 10, 4, 31, 16, 8, 4, 2, 1, 31,
	0, 0, 0, 0, 0, 0, 0, 0, 17, 17, 17, 15, 1, 1, 0, 22, 23, 23, 22,
	20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 12, 12, 0, 0, 0, 0, 0, 0,
	10, 4, 0, 0, 30, 16, 12, 2, 30, 0, 0, 0, 12, 8, 8, 28, 0, 0, 0, 0,
	0, 0, 0, 0, 8, 20, 8, 0, 28, 0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 20, 40,
	20, 10, 5, 0, 0, 0, 0, 30, 5, 5, 5, 13, 5, 5, 5, 30, 0, 0, 0, 0,
	0, 0, 0, 26, 21, 13, 5, 26, 0, 0, 0, 10, 17, 17, 17, 10, 4, 4, 4,
	4, 0, 0, 0, 0, 0, 4, 0, 4, 4, 2, 1, 17, 17, 14, 0, 2, 4, 0, 14, 17,
	17, 31, 17, 17, 0, 0, 0, 8, 4, 0, 14, 17, 17, 31, 17, 17, 0, 0, 0,
	4, 10, 0, 14, 17, 17, 31, 17, 17, 0, 0, 0, 22, 9, 0, 14, 17, 17,
	31, 17, 17, 0, 0, 0, 10, 0, 0, 14, 17, 17, 31, 17, 17, 0, 0, 0, 4,
	10, 4, 14, 17, 17, 31, 17, 17, 0, 0, 0, 30, 5, 5, 5, 15, 5, 5, 5,
	29, 0, 0, 0, 0, 30, 1, 1, 1, 1, 1, 1, 30, 4, 6, 0, 2, 4, 0, 30, 2,
	14, 2, 2, 30, 0, 0, 0, 4, 2, 0, 30, 2, 14, 2, 2, 30, 0, 0, 0, 4,
	10, 0, 30, 2, 14, 2, 2, 30, 0, 0, 0, 10, 0, 0, 30, 2, 14, 2, 2, 30,
	0, 0, 0, 2, 4, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 8, 4, 0, 4, 4, 4, 4,
	4, 4, 0, 0, 0, 4, 10, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 10, 0, 0, 4,
	4, 4, 4, 4, 4, 0, 0, 0, 14, 18, 18, 18, 23, 18, 18, 18, 14, 0, 0,
	0, 22, 9, 0, 19, 19, 21, 21, 25, 25, 0, 0, 0, 2, 4, 0, 14, 17, 17,
	17, 17, 14, 0, 0, 0, 8, 4, 0, 14, 17, 17, 17, 17, 14, 0, 0, 0, 4,
	10, 0, 14, 17, 17, 17, 17, 14, 0, 0, 0, 22, 9, 0, 14, 17, 17, 17,
	17, 14, 0, 0, 0, 10, 0, 0, 14, 17, 17, 17, 17, 14, 0, 0, 0, 0, 0,
	0, 17, 10, 4, 10, 17, 0, 0, 0, 0, 14, 25, 25, 21, 21, 21, 19, 19,
	14, 0, 0, 0, 2, 4, 0, 17, 17, 17, 17, 17, 14, 0, 0, 0, 8, 4, 0, 17,
	17, 17, 17, 17, 14, 0, 0, 0, 4, 10, 0, 17, 17, 17, 17, 17, 14, 0,
	0, 0, 10, 0, 0, 17, 17, 17, 17, 17, 14, 0, 0, 0, 8, 4, 0, 17, 17,
	10, 4, 4, 4, 0, 0, 0, 1, 1, 15, 17, 17, 17, 15, 1, 1, 0, 0, 0, 14,
	17, 17, 9, 5, 9, 17, 17, 13, 0, 0, 0, 0, 2, 4, 0, 14, 17, 17, 17,
	46, 0, 0, 0, 0, 8, 4, 0, 14, 17, 17, 17, 46, 0, 0, 0, 0, 4, 10, 0,
	14, 17, 17, 17, 46, 0, 0, 0, 0, 22, 9, 0, 14, 17, 17, 17, 46, 0,
	0, 0, 0, 0, 10, 0, 14, 17, 17, 17, 46, 0, 0, 0, 4, 10, 4, 0, 14,
	17, 17, 17, 46, 0, 0, 0, 0, 0, 0, 0, 11, 20, 14, 5, 26, 0, 0, 0,
	0, 0, 0, 0, 30, 1, 1, 1, 30, 4, 6, 0, 0, 4, 8, 0, 28, 18, 14, 2,
	28, 0, 0, 0, 0, 16, 8, 0, 28, 18, 14, 2, 28, 0, 0, 0, 0, 8, 20, 0,
	28, 18, 14, 2, 28, 0, 0, 0, 0, 0, 20, 0, 28, 18, 14, 2, 28, 0, 0,
	0, 0, 0, 2, 4, 0, 4, 4, 4, 4, 0, 0, 0, 0, 0, 8, 4, 0, 4, 4, 4, 4,
	0, 0, 0, 0, 0, 4, 10, 0, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 10, 0, 4,
	4, 4, 4, 0, 0, 0, 0, 0, 24, 7, 10, 17, 17, 17, 14, 0, 0, 0, 0, 0,
	22, 9, 0, 15, 17, 17, 17, 0, 0, 0, 0, 0, 4, 8, 0, 12, 18, 18, 12,
	0, 0, 0, 0, 0, 8, 4, 0, 12, 18, 18, 12, 0, 0, 0, 0, 0, 4, 10, 0,
	12, 18, 18, 12, 0, 0, 0, 0, 0, 20, 10, 0, 12, 18, 18, 12, 0, 0, 0,
	0, 0, 0, 20, 0, 12, 18, 18, 12, 0, 0, 0, 0, 0, 0, 4, 0, 14, 0, 4,
	0, 0, 0, 0, 0, 0, 0, 0, 14, 25, 21, 19, 14, 0, 0, 0, 0, 0, 2, 4,
	0, 17, 17, 17, 14, 0, 0, 0, 0, 0, 8, 4, 0, 17, 17, 17, 14, 0, 0,
	0, 0, 0, 4, 10, 0, 17, 17, 17, 14, 0, 0, 0, 0, 0, 0, 10, 0, 17, 17,
	17, 14, 0, 0, 0, 0, 0, 8, 4, 0, 17, 17, 17, 30, 16, 15, 0, 0, 0,
	1, 1, 15, 17, 17, 17, 15, 1, 1, 0, 0, 0, 0, 10, 0, 17, 17, 17, 30,
	16, 15, 0
};

/** ROM load failure message. */
static sjme_jbyte sjme_romfailmessage[] =
{
	0x53, 0x71, 0x75, 0x69, 0x72, 0x72, 0x65,
	0x6c, 0x4a, 0x4d, 0x45, 0x27, 0x73, 0x20,
	0x52, 0x65, 0x74, 0x72, 0x6f, 0x41, 0x72,
	0x63, 0x68, 0x20, 0x63, 0x6f, 0x72, 0x65,
	0x20, 0x69, 0x73, 0x20, 0x61, 0x20, 0x77,
	0x6f, 0x72, 0x6b, 0x20, 0x69, 0x6e, 0x20,
	0x70, 0x72, 0x6f, 0x67, 0x72, 0x65, 0x73,
	0x73, 0x20, 0x61, 0x6e, 0x64, 0x20, 0x69,
	0x73, 0x20, 0x65, 0x78, 0x70, 0x65, 0x63,
	0x74, 0x65, 0x64, 0x20, 0x69, 0x6e, 0x20,
	0x32, 0x30, 0x32, 0x33, 0x2e, 32
};

/** Execution failure message. */
static sjme_jbyte sjme_execfailmessage[] =
{
	74, 86, 77, 32, 101, 120, 101, 99, 117, 116, 105, 111, 110, 32, 102, 97,
	105, 108, 117, 114, 101, 58, 32
};

/** BootRAM failed to load. */
static sjme_jbyte sjme_bootfailmessage[] =
{
	70, 97, 105, 108, 101, 100, 32, 116, 111, 32, 105, 110, 105, 116, 105, 97,
	108, 105, 122, 101, 32, 116, 104, 101, 32, 66, 111, 111, 116, 82, 65, 77,
	33, 32, 73, 102, 32, 116, 104, 105, 115, 32, 105, 115, 32, 100, 117, 101,
	32, 116, 111, 32, 97, 32, 99, 111, 114, 114, 117, 112, 116, 32, 82, 79, 77,
	44, 32, 118, 105, 115, 105, 116, 32, 104, 116, 116, 112, 115, 58, 47, 47,
	115, 113, 117, 105, 114, 114, 101, 108, 106, 109, 101, 46, 99, 99, 47, 117,
	118, 47, 100, 111, 119, 110, 108, 111, 97, 100, 46, 109, 107, 100, 32, 97,
	110, 100, 32, 100, 111, 119, 110, 108, 111, 97, 100, 32, 116, 104, 101, 32,
	90, 73, 80, 32, 116, 105, 116, 108, 101, 100, 32, 39, 83, 117, 109, 109,
	101, 114, 67, 111, 97, 116, 32, 82, 79, 77, 39, 46, 13, 10
};

/** SQF Defined Font. */
static sjme_sqf sjme_font =
{
	12,
	9,
	3,
	1,
	sjme_fontcharwidths,
	sjme_fontisvalidchar,
	sjme_fontcharbmp
};

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATSJMFHSJMEBSQFH
}
#undef SJME_cXRATUFACOATSJMFHSJMEBSQFH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATSJMFHSJMEBSQFH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATSJMFHSJMEBSQFH */

