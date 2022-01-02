/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libretro/lrlocal.h"

/** Recommended screen width. */
#define SJME_RECOMMENDED_SCREEN_WIDTH 240

/** Recommended screen height. */
#define SJME_RECOMMENDED_SCREEN_HEIGHT 320

/** Maximum screen width. */
#define SJME_MAX_SCREEN_WIDTH 640

/** Maximum screen height. */
#define SJME_MAX_SCREEN_HEIGHT 640

/** Video refresh callback. */
static retro_video_refresh_t sjme_libRetro_videoRefresh = NULL;

/**
 * Initializes the audio video information.
 * 
 * @param info Audio/Video information.
 * @since 2022/01/22 
 */
SJME_GCC_USED void retro_get_system_av_info(struct retro_system_av_info* info)
{
	/* Always 60FPS at 48KHz. */
	info->timing.fps = 60;
	info->timing.sample_rate = 48000;
	
	/* If there is a global state, use the same video properties as that. */
	if (g_libRetroState != NULL)
	{
		info->geometry.base_width = g_libRetroState->video.width;
		info->geometry.base_width = g_libRetroState->video.height;
	}
	
	/* Otherwise, assume some defaults. */
	else
	{
		info->geometry.base_width = SJME_RECOMMENDED_SCREEN_WIDTH;
		info->geometry.base_width = SJME_RECOMMENDED_SCREEN_HEIGHT;
	}
	
	/* Absolute possible maximums? */
	info->geometry.max_width = SJME_MAX_SCREEN_WIDTH;
	info->geometry.max_height = SJME_MAX_SCREEN_HEIGHT;
	
	/* Keep 1:1 pixels. */
	info->geometry.aspect_ratio =
		(float)info->geometry.base_width / (float)info->geometry.base_height;
}

/**
 * Sets the video refresh callback.
 * 
 * @param callback The callback function.
 * @since 2021/01/22
 */
SJME_GCC_USED void retro_set_video_refresh(retro_video_refresh_t callback)
{
	sjme_libRetro_videoRefresh = callback;
}
