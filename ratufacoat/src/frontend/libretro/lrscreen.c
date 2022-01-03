/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>

#include "frontend/libretro/lrlocal.h"

/** Debug buffer size for messages. */
#define DEBUG_BUF 512

/** Recommended screen width. */
#define SJME_RECOMMENDED_SCREEN_WIDTH 240

/** Recommended screen height. */
#define SJME_RECOMMENDED_SCREEN_HEIGHT 320

/** Maximum screen width. */
#define SJME_MAX_SCREEN_WIDTH 640

/** Maximum screen height. */
#define SJME_MAX_SCREEN_HEIGHT 640

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
	g_libRetroCallbacks.videoFunc = callback;
}

void sjme_libRetro_message(sjme_jbyte percent, const char* const format, ...)
{
	char buf[DEBUG_BUF];
	va_list args;
	struct retro_message oldMessage;
	struct retro_message_ext extMessage;
	unsigned int messageVersion;
	size_t messageLen;
	
	/* Load message buffer. */
	va_start(args, format);
	memset(buf, 0, sizeof(buf));
	vsnprintf(buf, DEBUG_BUF, format, args);
	va_end(args);
	
	/* Determine safe string length for the message to add a new line. */
	buf[DEBUG_BUF - 1] = '\0';
	buf[DEBUG_BUF - 2] = '\0';
	buf[DEBUG_BUF - 3] = '\0';
	messageLen = strlen(buf);
#if defined(_WIN32)
	buf[messageLen] = '\r';
	buf[messageLen + 1] = '\n';
#else
	buf[messageLen] = '\n';
#endif
	
	/* Print output message. */ 
	g_libRetroCallbacks.loggingFunc(RETRO_LOG_INFO, buf);
	
	/* Which version of messages are we on? */
	messageVersion = 0;
	g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_GET_MESSAGE_INTERFACE_VERSION, &messageVersion);
	
	/* Setup screen message. */
	memset(&oldMessage, 0, sizeof(oldMessage));
	memset(&extMessage, 0, sizeof(extMessage));
	oldMessage.msg = extMessage.msg = buf;
	oldMessage.frames = extMessage.duration = 3000;
	extMessage.level = RETRO_LOG_INFO;
	extMessage.progress = (int8_t)(percent < 0 || percent > 100 ?
		-1 : percent);
	extMessage.priority = 100;
	extMessage.target = RETRO_MESSAGE_TARGET_OSD;
	extMessage.type = (percent < 0 || percent > 100 ?
		RETRO_MESSAGE_TYPE_STATUS : RETRO_MESSAGE_TYPE_PROGRESS);
	
	/* We do not want to send the new line to the OSD. */
	buf[messageLen] = '\0'; 
	
	/* Send to the screen to view. */
	if (messageVersion >= 1)
		g_libRetroCallbacks.environmentFunc(RETRO_ENVIRONMENT_SET_MESSAGE_EXT,
			&oldMessage);
	else
		g_libRetroCallbacks.environmentFunc(RETRO_ENVIRONMENT_SET_MESSAGE,
			&extMessage);
}
