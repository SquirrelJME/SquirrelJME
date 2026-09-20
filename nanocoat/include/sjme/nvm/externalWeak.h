/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * External weak references.
 *
 * @file
 * @since 2026/09/20
 */

#ifndef SJME_C_SQUIRRELJME_EXTERNALWEAK_H
#define SJME_C_SQUIRRELJME_EXTERNALWEAK_H

#include "sjme/config.h"
#include "sjme/error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_EXTERNALWEAK_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Potentially returns the default ScritchUI interface to use.
 *
 * @param outInterface The resultant default ScritchUI interface,
 * or @code NULL @endcode if none is used.
 * @return Any resultant error, if any.
 * @since 2026/09/20
 */
sjme_errorCode sjme_extern_scritchUiInterface(
	sjme_attrOutNotNull sjme_lpcstr* outInterface);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_EXTERNALWEAK_H
}
#undef SJME_CXX_SQUIRRELJME_EXTERNALWEAK_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_EXTERNALWEAK_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_EXTERNALWEAK_H */