/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * LibRetro Support.
 *
 * @since 2019/06/10
 */

#include <libretro.h>

#include "sjmerc.h"

/* Callbacks. */
static retro_log_printf_t         log_cb = NULL;
static retro_video_refresh_t      video_cb = NULL;
static retro_audio_sample_batch_t audio_cb = NULL;
static retro_set_led_state_t      led_cb = NULL;
static retro_environment_t        environ_cb = NULL;
static retro_input_poll_t         input_poll_cb = NULL;
static retro_input_state_t        input_state_cb = NULL;

/** Fallback logging, does nothing. */
static void fallback_log(enum retro_log_level level, const char* fmt, ...)
{
}

/** Sets system information on RetroArch. */
void retro_get_system_info(struct retro_system_info* info)
{
	/* Wipe. */
	memset(info, 0, sizeof(*info));
	
	/* Set properties. */
	info->library_name = "squirreljme";
	info->library_version = "0.3.0";
	info->valid_extensions = "jar";
	info->need_fullpath = true;
	
	/* SquirrelJME works with JAR files, which are ZIP files. */
	info->block_extract = true;
}

/** Initializes the RetroArch environment. */
void retro_set_environment(retro_environment_t cb)
{
	bool bflag;
	struct retro_log_callback logging;
	struct retro_input_descriptor input_desc[] =
		{
			/* SOFT LEFT. */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_SELECT, "Soft 1 (Left Shoulder)"},
			
			/* SOFT LEFT. */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_START, "Soft 2 (Right Shoulder)"},
			
			/* [1  o~o] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_X, "1 Voicemail (Game A)"},
			
			/* [2  abc] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_UP, "2 ABC (Game Up)"},
			
			/* [3  def] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_A, "3 DEF (Game B)"},
			
			/* [4  ghi] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_LEFT, "4 GHI (Game Left)"},
			
			/* [5  jkl] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_L2, "5 JKL (Game Fire/Select)"},
			
			/* [6  mno] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_RIGHT, "6 MNO (Game Right)"},
			
			/* [7 pqrs] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_Y, "7 PQRS (Game C)"},
			
			/* [8  tuv] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_DOWN, "8 TUV (Game Down)"},
			
			/* [9 wxyz] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_B, "9 WXYZ (Game D)"},
			
			/* [*  shf] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_L, "Star (Shift)"},
			
			/* [0    +] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_R2, "0 Operator (Plus)"},
			
			/* [# spac] */
			{0, RETRO_DEVICE_JOYPAD, 0,
				RETRO_DEVICE_ID_JOYPAD_R, "Pound (Space)"},
			
			/* End. */
			{0}
		};
	
	/* Use this environment callback. */
	environ_cb = cb;
	
	/* Try to get a logger. */
	environ_cb(RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &logging);
	log_cb = (logging.log != NULL ? logging.log : fallback_log);
	
	/* The core can launch without a game. */
	bflag = true;
	environ_cb(RETRO_ENVIRONMENT_SET_SUPPORT_NO_GAME, &bflag);
	
	/* Describe the SquirrelJME Controller layout. */
	/* [SOFT LEFT] [SOFT RIGHT] */
	/* [BACK] */
	/* [1  o~o] [2  abc] [3  def] */
	/* [4  ghi] [5  jkl] [6  mno] */
	/* [7 pqrs] [8  tuv] [9 wxyz] */
	/* [*  shf] [0    +] [# spac] */
	/* RetroPad looks like so: */
	/*    X-1    */
	/* Y-7   A-3 */
	/*    B-9    */
	environ_cb(RETRO_ENVIRONMENT_SET_INPUT_DESCRIPTORS, input_desc);
}

/** RetroArch initialization. */
void retro_init(void)
{
	enum retro_pixel_format format;
	
	/* Use ARGB 32-bit. */
	format = RETRO_PIXEL_FORMAT_XRGB8888;
	
	/* Try setting it, ignore otherwise? */
	if (environ_cb(RETRO_ENVIRONMENT_SET_PIXEL_FORMAT, &format))
		log_cb(RETRO_LOG_INFO, "Using XRGB8888?\n");
}
