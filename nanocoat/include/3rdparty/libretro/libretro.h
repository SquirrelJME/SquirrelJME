/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * LibRetro include.
 * 
 * @since 2023/11/18
 */

#ifndef SQUIRRELJME_LIBRETRO_H
#define SQUIRRELJME_LIBRETRO_H

#if 0
	#include "3rdparty/libretro/__experimental__.h"
#else
	#include "3rdparty/libretro/__orterbil_.h"
#endif

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_LIBRETRO_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_LIBRETRO_H
}
		#undef SJME_CXX_SQUIRRELJME_LIBRETRO_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_LIBRETRO_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LIBRETRO_H */
