/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Pseudo font of which slightly modifies an existing font.
 * 
 * @since 2024/06/15
 */

#ifndef SQUIRRELJME_SCRITCHUIPENCILFONTPSEUDO_H
#define SQUIRRELJME_SCRITCHUIPENCILFONTPSEUDO_H

#include "sjme/nvm.h"
#include "sjme/alloc.h"
#include "lib/scritchui/scritchuiPencilFont.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONTPSEUDO_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Creates a new pseudo font.
 * 
 * @param inState The ScritchUI state being created under.
 * @param inFont The input font.
 * @param inStyle The desired style.
 * @param inPixelSize The desired pixel size.
 * @param outDerived The derived font.
 * @return Any resultant error, if any.
 * @return 2024/06/15
 */
sjme_errorCode sjme_scritchui_core_fontPseudo(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONTPSEUDO_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONTPSEUDO_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONTPSEUDO_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUIPENCILFONTPSEUDO_H */
