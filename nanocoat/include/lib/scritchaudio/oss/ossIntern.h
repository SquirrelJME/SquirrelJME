/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal OSS definitions.
 * 
 * @since 2025/05/07
 */

#ifndef OSSINTERN_H
#define OSSINTERN_H

#if defined(SQUIRRELJME_OSS_INCLUDE_FILE)
	#include SQUIRRELJME_OSS_INCLUDE_FILE
#else
	#include <sys/soundcard.h>
#endif

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_OSSINTERN_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_OSSINTERN_H
}
#undef SJME_CXX_OSSINTERN_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_OSSINTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* OSSINTERN_H */
