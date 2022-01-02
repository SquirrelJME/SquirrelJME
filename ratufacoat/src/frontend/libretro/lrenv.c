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
const struct retro_variable sjme_libRetro_coreOptions[] =
{
	/* CPU and Timing. */
	{"squirreljme_thread_model",
		"Threading Model; "
			"cooperative|simultaneous"},
	{"squirreljme_coop_cycles",
		"CPU Cycles Per Cooperative Frame (Higher = Faster); "
			"1048576|2097152|4194304|8388608|"
			"16384|32768|65536|131072|262144|524288"},
	{"squirreljme_coop_locked_clock",
		"Lock RTC to CPU Cycles; "
			"disabled|enabled"},
	
	/* Graphics. */
	{"squirreljme_display_size",
		"Display Size; "
			"240x320|320x480|480x640|640x480|96x65|"
			"128x128|128x160|176x208|176x220"},
	{"squirreljme_display_colors",
		"Display Colors; "
			"16777216|65536|256|16|4|2"},
	
	/* ROM Order. */
	{"squirreljme_use_external_rom",
		"ROM Order; "
			"external first|internal first"},
	
	/* End. */
	{NULL, NULL}
};

/**
 * Fallback logging for when messages via the logging API are not supported.
 * 
 * @param level The log level. 
 * @param fmt The logging format.
 * @param ... Any parameters for the call.
 * @since 2021/01/02
 */
static void RETRO_CALLCONV sjme_libRetro_fallBackLog(
	enum retro_log_level level, const char *message, ...)
{
	char buf[DEBUG_BUF];
	va_list args;
	
	/* Load message buffer. */
	va_start(args, message);
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
 * @since 2021/01/02 
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
