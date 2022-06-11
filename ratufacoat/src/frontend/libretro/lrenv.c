/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>

#include "frontend/libretro/lrenv.h"
#include "frontend/libretro/lrjoypad.h"
#include "frontend/libretro/lrvfs.h"

/** Debug buffer size for messages. */
#define DEBUG_BUF 512

/** Configuration options for the core. */
struct retro_variable sjme_libRetro_coreOptions[] =
{
	/* CPU and Timing. */
	{SJME_LIBRETRO_CONFIG_THREAD_MODEL,
		"Threading Model; "
			SJME_LIBRETRO_CONFIG_THREAD_MODEL_COOP "|"
			SJME_LIBRETRO_CONFIG_THREAD_MODEL_SMT},
	{SJME_LIBRETRO_CONFIG_COOP_CYCLES,
		"CPU Speed in MHz (Co-Op); "
			SJME_LIBRETRO_CONFIG_DEFAULT_CYCLES "|100|300|600|1|5|10|30"},
	{"squirreljme_coop_locked_clock",
		"Lock RTC to CPU Cycles (Co-Op); "
			"disabled|enabled"},
	
	/* Graphics. */
	{SJME_LIBRETRO_CONFIG_DISPLAY_SIZE,
		"Display Size; "
			"240x320|320x240|320x320|480x640|640x480|480x480|640x640|96x65|"
			"128x128|128x160|176x208|176x220"},
	{SJME_LIBRETRO_CONFIG_PIXEL_FORMAT,
		"Pixel Format; "
#if defined(SQUIRRELJME_PS2)
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_ABGR1555 "|"
#else
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGBA8888 "|"
#endif
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGBA4444 "|"
		    SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGB565 "|"
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGB555 "|"
#if defined(SQUIRRELJME_PS2)
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_RGBA8888 "|"
#else
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_ABGR1555 "|"
#endif
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_65536I "|"
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_256I "|"
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_4I "|"
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_2I "|"
			SJME_LIBRETRO_CONFIG_PIXEL_FORMAT_1I},
	
	/* ROM Order. */
	{SJME_LIBRETRO_CONFIG_ROM_ORDER,
		"ROM Order; "
			SJME_LIBRETRO_CONFIG_ROM_ORDER_EXT_INT "|"
			SJME_LIBRETRO_CONFIG_ROM_ORDER_INT_EXT "|"
			SJME_LIBRETRO_CONFIG_ROM_ORDER_EXT "|"
			SJME_LIBRETRO_CONFIG_ROM_ORDER_INT},
	
	/* End. */
	{NULL, NULL}
};

/**
 * Fallback logging for when messages via the logging API are not supported.
 * 
 * @param level The log level. 
 * @param fmt The logging format.
 * @param ... Any parameters for the call.
 * @since 2022/01/02
 */
static void RETRO_CALLCONV sjme_libRetro_fallBackLog(
	enum retro_log_level level, const char *message, ...)
{
	char buf[DEBUG_BUF];
	va_list args;
	
	/* Load message buffer. */
	va_start(args, message);
	memset(buf, 0, sizeof(buf));
	vsnprintf(buf, DEBUG_BUF, message, args);
	va_end(args);
	
	/* Print output message. */ 
	fprintf(stderr, "LR[%d]: %s\n",
		level, buf);
}

/**
 * Sets RetroArch environment information.
 * 
 * @param callback Callback for environment set.
 * @since 2022/01/02 
 */
SJME_GCC_USED void retro_set_environment(retro_environment_t callback)
{
	bool noGameFlag, achievementFlag;
	struct retro_log_callback logging;
	
	/* Store the callback for later. */
	g_libRetroCallbacks.environmentFunc = callback;
	
	/* Try to get a logger. */
	memset(&logging, 0, sizeof(logging));
	callback(RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &logging);
	if (logging.log != NULL)
		g_libRetroCallbacks.loggingFunc = logging.log;
	else
		g_libRetroCallbacks.loggingFunc = sjme_libRetro_fallBackLog;
	
	/* Initialize the joypad control setup. */
	sjme_libRetro_joyPadSetEnvironment(callback);
	
	/* Setup core configuration. */
	callback(RETRO_ENVIRONMENT_SET_VARIABLES, sjme_libRetro_coreOptions);
	
	/* The core can launch without a game. */
	noGameFlag = true;
	callback(RETRO_ENVIRONMENT_SET_SUPPORT_NO_GAME, &noGameFlag);
	
	/* Support achievements. */
	achievementFlag = true;
	callback(RETRO_ENVIRONMENT_SET_SUPPORT_ACHIEVEMENTS, &achievementFlag);
	
	/* Initialize VFS callback. */
	sjme_libRetro_vfsSetEnvironment(callback);
}
