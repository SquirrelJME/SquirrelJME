/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "3rdparty/libretro/libretro.h"
#include "frontend/libretro/shared.h"
#include "sjme/modelessStars.h"

sjme_attrUnused RETRO_API void retro_get_system_av_info(
	struct retro_system_av_info* info)
{
	/* Base size. */
	info->geometry.base_width = 240;
	info->geometry.base_height = 320;
	
	/* Maximum permitted size. */
	info->geometry.max_width = 240;
	info->geometry.max_height = 320;
	
	/* Always square pixels. */
	info->geometry.aspect_ratio = 1.0F;
	
	/* Always target 60 FPS. */
	info->timing.fps = 60;
	
	/* Use a standard audio format here. */
	info->timing.sample_rate = 22050;
}

sjme_attrUnused RETRO_API void retro_set_video_refresh(
	retro_video_refresh_t refresh)
{
	sjme_libretro_videoRefreshCallback = refresh;
}
