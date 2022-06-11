/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Local RetroArch Support.
 * 
 * @since 2021/02/27
 */

#ifndef SQUIRRELJME_LRLOCAL_H
#define SQUIRRELJME_LRLOCAL_H

#include <libretro/libretro.h>

#include "engine/scaffold.h"
#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_LRLOCAL_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Callbacks for RetroArch.
 * 
 * @since 2022/01/02
 */
typedef struct sjme_libRetroCallbacks
{
	/** Callback for the environment. */
	retro_environment_t environmentFunc;
	
	/** Callback for logging. */
	retro_log_printf_t loggingFunc;

	/** Input polling callback. */
	retro_input_poll_t inputPollFunc;
	
	/** Input state callback. */
	retro_input_state_t inputStateFunc;
	
	/** Audio callback. */
	retro_audio_sample_batch_t audioSampleBatchFunc;
	
	/** Audio sample callback. */
	retro_audio_sample_t audioSampleFunc;
	
	/** Function for drawing video. */
	retro_video_refresh_t videoFunc;
	
	/** VFS interface. */
	struct retro_vfs_interface* vfs;
} sjme_libRetroCallbacks;

/**
 * The held state for RetroArch.
 * 
 * @since 2021/02/27
 */
typedef struct sjme_libRetroState
{
	/** The current engine configuration. */
	sjme_engineConfig config;
	
	/** The state of the running engine. */
	sjme_engineState* state;
} sjme_libRetroState;

/** Available callbacks. */
extern sjme_libRetroCallbacks g_libRetroCallbacks;

/** The global RetroArch State. */
extern sjme_libRetroState* g_libRetroState;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_LRLOCAL_H
}
#undef SJME_CXX_SQUIRRELJME_LRLOCAL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_LRLOCAL_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LRLOCAL_H */
