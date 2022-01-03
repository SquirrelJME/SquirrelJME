/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>
#include <stdlib.h>

#include "frontend/libretro/lrlocal.h"
#include "frontend/libretro/lrenv.h"
#include "util.h"

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
		RETRO_MESSAGE_TYPE_NOTIFICATION : RETRO_MESSAGE_TYPE_PROGRESS);
	
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

sjme_jboolean sjme_libRetro_screenConfig(sjme_engineConfig* config)
{
	struct retro_variable getVar;
	const char* rawSize;
	const char* rawFormat;
	sjme_jint width, height;
	sjme_jint split;
	sjme_pixelFormat pixelFormat;
	
	/* Notice. */
	sjme_libRetro_message(10, "Determining screen size.");
	
	/* Get screen size setting. */
	memset(&getVar, 0, sizeof(getVar));
	getVar.key = SJME_LIBRETRO_CONFIG_DISPLAY_SIZE;
	rawSize = NULL;
	if (g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_GET_VARIABLE, &getVar))
		if (getVar.value != NULL)
			rawSize = getVar.value;
	
	/* Decode screen size. */
	width = height = -1;
	if (rawSize != NULL)
	{
		/* Find where the x splitter is. */
		split = sjme_strIndexOf(rawSize, 'x');
		if (split >= 0)
		{
			width = strtol(rawSize, NULL, 10);
			height = strtol(&rawSize[split + 1], NULL, 10);
		}
	}
	
	/* Default to certain sizes? */
	if (width <= 0 || width >= SJME_MAX_SCREEN_WIDTH)
		width = SJME_RECOMMENDED_SCREEN_WIDTH;
	if (height <= 0 || height >= SJME_MAX_SCREEN_HEIGHT)
		height = SJME_RECOMMENDED_SCREEN_HEIGHT;
		
	/* Get pixel format setting. */
	memset(&getVar, 0, sizeof(getVar));
	getVar.key = SJME_LIBRETRO_CONFIG_PIXEL_FORMAT;
	rawFormat = NULL;
	if (g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_GET_VARIABLE, &getVar))
		if (getVar.value != NULL)
			rawFormat = getVar.value;
	
	/* Fallback to a default if not set. */
	if (rawFormat == NULL)
		rawFormat = SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGBA8888;
	
	/* Determine which pixel format is used. */
	if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGBA4444))
		pixelFormat = SJME_PIXEL_FORMAT_SHORT_RGBA4444;
	else if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGB565))
		pixelFormat = SJME_PIXEL_FORMAT_SHORT_RGB565;
	else if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGB555))
		pixelFormat = SJME_PIXEL_FORMAT_SHORT_RGB555;
	else if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_ABGR1555))
		pixelFormat = SJME_PIXEL_FORMAT_SHORT_ABGR1555;
	else if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_65536I))
		pixelFormat = SJME_PIXEL_FORMAT_SHORT_INDEXED65536;
	else if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_256I))
		pixelFormat = SJME_PIXEL_FORMAT_BYTE_INDEXED256;
	else if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_4I))
		pixelFormat = SJME_PIXEL_FORMAT_PACKED_INDEXED4;
	else if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_2I))
		pixelFormat = SJME_PIXEL_FORMAT_PACKED_INDEXED2;
	else if (0 == strcmp(rawFormat, SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_1I))
		pixelFormat = SJME_PIXEL_FORMAT_PACKED_INDEXED1;
	else
		pixelFormat = SJME_PIXEL_FORMAT_INT_RGB888;
	
	/* Store screen size. */
	config->numScreens = 1;
	config->screens[0].width = width;
	config->screens[0].height = height;
	config->screens[0].pixelFormat = pixelFormat;
	
	/* Notice. */
	sjme_libRetro_message(10, "Using %dx%d (%s #%d).",
		width, height, rawFormat, pixelFormat);
	
	return sjme_true;
}
