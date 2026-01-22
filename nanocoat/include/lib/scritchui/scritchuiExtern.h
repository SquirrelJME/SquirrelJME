/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI externals, for internal usage only.
 * 
 * @since 2024/06/10
 */

#ifndef SJME_C_SCRITCHUIEXTERN_H
#define SJME_C_SCRITCHUIEXTERN_H

#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIEXTERN_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The internal builtin fallback font. */
extern sjme_attrExport const
	struct sjme_scritchui_sqfCodepage sqf_font_sanserif_12;
	
/** The bit-line functions which are available. */
extern const sjme_scritchui_pencilBitLineFunc
	sjme_scritchui_pencilBitLines[256];

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIEXTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIEXTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIEXTERN_H */
#endif     /* #ifdef __cplusplus */

#endif /* SJME_C_SCRITCHUIEXTERN_H */
