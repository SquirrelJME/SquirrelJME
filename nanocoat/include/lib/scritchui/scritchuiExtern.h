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

#ifndef SQUIRRELJME_SCRITCHUIEXTERN_H
#define SQUIRRELJME_SCRITCHUIEXTERN_H

#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/dylib.h"

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
extern SJME_DYLIB_EXPORT const
	struct sjme_scritchui_sqfCodepage sqf_font_sanserif_12;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIEXTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIEXTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIEXTERN_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUIEXTERN_H */
