/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Pencil fonts.
 * 
 * @file
 * @since 2024/05/17
 */

#ifndef SJME_C_SCRITCHUIPENCILFONT_H
#define SJME_C_SCRITCHUIPENCILFONT_H

#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiPencil.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Determines the scanline length of a bitmap.
 * 
 * @param w The bitmap width.
 * @return The result scanline length.
 * @since 2024/06/27
 */
sjme_jint sjme_scritchui_pencilFontScanLen(
	sjme_attrInPositive sjme_jint w);

/**
 * Initializes a static pencil font.
 *
 * @param inState The input state.
 * @param inOutFont The resultant font.
 * @return Any resultant error, if any.
 * @since 2024/06/12
 */
sjme_errorCode sjme_scritchui_newPencilFontStatic(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_scritchui_pencilFont inOutFont);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONT_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SJME_C_SCRITCHUIPENCILFONT_H */
