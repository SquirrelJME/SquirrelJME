/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Any ScritchAny API.
 * 
 * @since 2025/05/11
 */

#ifndef SJME_C_SCRITCHANY_H
#define SJME_C_SCRITCHANY_H

#include "sjme/config.h"
#include "sjme/dylib.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SCRITCHANY_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The base name for the ScritchAny dynamic library. */
#define SJME_SCRITCHANY_DYLIB_NAME_BASE(area) \
	"squirreljme-scritch" #area "-"

/** The name of the dynamic library for ScritchAny. */
#define SJME_SCRITCHANY_DYLIB_NAME(area, x) \
	SJME_SCRITCHANY_DYLIB_NAME_BASE(area) SJME_TOKEN_STRING_PP(x)

/** The path name for the dynamic library for ScritchAny. */
#define SJME_SCRITCHANY_DYLIB_PATHNAME(area, x) \
	SJME_CONFIG_DYLIB_PATHNAME(SJME_SCRITCHANY_DYLIB_NAME(area, x))

/** The prefix for the dynamic library. */
#define SJME_SCRITCHANY_DYLIB_SYMBOL_PREFIX(area) \
	SJME_TOKEN_PASTE3_PP(sjme_scritch, SJME_TOKEN(area), _dylibApi)

/** The symbol to use with @c sjme_scritchany_dylibApiFunc . */
#define SJME_SCRITCHANY_DYLIB_SYMBOL(area, x) \
	SJME_TOKEN_PASTE_PP(SJME_SCRITCHANY_DYLIB_SYMBOL_PREFIX(area), x)

/** Declares the API export . */
#define SJME_SCRITCHANY_DYLIB_SYMBOL_DECLARE(area, x) \
	sjme_attrExport sjme_attrExportCall SJME_SCRITCHANY_DYLIB_SYMBOL(area, x)
		
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SCRITCHANY_H
}
#undef SJME_CXX_SCRITCHANY_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SCRITCHANY_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SCRITCHANY_H */
