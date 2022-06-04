/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * RetroArch Audio and MIDI Support.
 *
 * @since 2021/02/27
 */

#include "frontend/libretro/lrlocal.h"

/** Set audio sample callback. */
SJME_EXTERN_C SJME_GCC_USED void retro_set_audio_sample(retro_audio_sample_t cb)
{
	g_libRetroCallbacks.audioSampleFunc = cb;
}

/** Set audio sample batching. */
SJME_EXTERN_C SJME_GCC_USED void retro_set_audio_sample_batch(retro_audio_sample_batch_t cb)
{
	g_libRetroCallbacks.audioSampleBatchFunc = cb;
}
