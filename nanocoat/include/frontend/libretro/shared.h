/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Shared RetroArch Details.
 * 
 * @since 2023/11/22
 */

#ifndef SQUIRRELJME_SHARED_H
#define SQUIRRELJME_SHARED_H

#include "3rdparty/libretro/libretro.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SHARED_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Environment callback.
 * 
 * @param cmd The command to call.
 * @param data The data to pass.
 * @return A boolean depending on success or failure.
 * @since 2023/11/22
 */
extern retro_environment_t sjme_libretro_envCallback;

/**
 * Callback for video refresh.
 * 
 * @param data The buffer data.
 * @param width The buffer width.
 * @param height The buffer height.
 * @param pitch The buffer pitch.
 * @since 2023/11/22
 */
extern retro_video_refresh_t sjme_libretro_videoRefreshCallback;

/**
 * Returns the interface to the SquirrelJME Extension interface.
 * 
 * @return The pointer to the interface address.
 * @since 2023/11/22 
 */
const struct retro_get_proc_address_interface* sjme_libretro_extInterface(
	void);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SHARED_H
}
		#undef SJME_CXX_SQUIRRELJME_SHARED_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SHARED_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SHARED_H */
