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

/** Fallback logging, does nothing. */
static void fallback_log(enum retro_log_level level, const char* fmt, ...)
{
}

/* Callbacks. */
static retro_audio_sample_batch_t audio_cb = NULL;
static retro_audio_sample_t audio_samble_cb = NULL;
static retro_environment_t environ_cb = NULL;
static retro_input_poll_t input_poll_cb = NULL;
static retro_input_state_t input_state_cb = NULL;
static retro_log_printf_t log_cb = fallback_log;
static retro_video_refresh_t video_cb = NULL;

/** Returns the supported RetroArch version. */
unsigned retro_api_version(void)
{
	return RETRO_API_VERSION;
}

/** Region. */
unsigned retro_get_region(void)
{
	return RETRO_REGION_NTSC;
}

/** Sets system information on RetroArch. */
void retro_get_system_info(struct retro_system_info* info)
{
	/* Wipe. */
	memset(info, 0, sizeof(*info));
	
	/* Set properties. */
	info->library_name = "squirreljme";
	info->library_version = SQUIRRELJME_VERSION_STRING;
	info->valid_extensions = "jar";
	
	/* Full path is not needed, but game data may be specified!. */
	info->need_fullpath = false;
	
	/* SquirrelJME works with JAR files, which are ZIP files. */
	info->block_extract = true;
}

/** Get audio/video information. */
void retro_get_system_av_info(struct retro_system_av_info* info)
{
	info->timing.fps = 60;
	info->timing.sample_rate = 48000;

	info->geometry.base_width   = 240;
	info->geometry.base_height  = 320;
	info->geometry.max_width    = 240;
	info->geometry.max_height   = 320;
	info->geometry.aspect_ratio = 0.75;
}

/** Set audio sample callback. */
void retro_set_audio_sample(retro_audio_sample_t cb)
{
	audio_samble_cb = cb;
}

/** Set audio sample batching. */
void retro_set_audio_sample_batch(retro_audio_sample_batch_t cb)
{
	audio_cb = cb;
}

/** Sets controller port device. */
void retro_set_controller_port_device(unsigned port, unsigned device)
{
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
			
			/* SOFT RIGHT. */
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
	struct retro_variable vars[] =
		{
			{"squirreljme_debug_notes",
				"Enable Debug Notes; disabled|enabled"},
			
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
	
	/* Set variables. */
	environ_cb(RETRO_ENVIRONMENT_SET_VARIABLES, vars);
}

/** Set input polling. */
void retro_set_input_poll(retro_input_poll_t cb)
{	
	input_poll_cb = cb;
}

/** Set input state. */
void retro_set_input_state(retro_input_state_t cb)
{
	input_state_cb = cb;
}

/** Set video refresh callback. */
void retro_set_video_refresh(retro_video_refresh_t cb)
{
	video_cb = cb;
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

/** Destroy. */
void retro_deinit(void)
{
}

/** Resets the system. */
void retro_reset(void)
{
}

/** Runs single frame. */
void retro_run(void)
{
#define VIDEO_RAM_SIZE (320 * 240)
	static uint32_t videoram[VIDEO_RAM_SIZE];
	static uint32_t seed = 1234567;
	uint32_t i;
	
	/* Poll for input because otherwise it prevents RetroArch from accessing */
	/* the menu. */
	input_poll_cb();
	
	for (i = 0; i < VIDEO_RAM_SIZE; i++)
	{
		videoram[i] = i + (seed * 65536) + seed;
		seed = (seed * 7) + i;
	}
	
	video_cb(videoram, 240, 320, 240 * sizeof(uint32_t));
}

/** Serialize size? */
size_t retro_serialize_size(void)
{
	return 0;
}

/** Serialize? */
bool retro_serialize(void* data, size_t size)
{
	return false;
}

/** Unserialize? */
bool retro_unserialize(const void* data, size_t size)
{
	return false;
}

/** Get memory data? */
void* retro_get_memory_data(unsigned id)
{
	return NULL;
}

/** Set memory data? */
size_t retro_get_memory_size(unsigned id)
{
	return 0;
}

/** Reset cheat. */
void retro_cheat_reset(void)
{
}

/** Set cheat. */
void retro_cheat_set(unsigned index, bool enabled, const char* code)
{
}

/** Load a game? */
bool retro_load_game(const struct retro_game_info* info)
{
	return true;
}

/** Load game special? */
bool retro_load_game_special(unsigned type, const struct retro_game_info* info,
	size_t num)
{
	return false;
}

/** Unload a game? */
void retro_unload_game(void)
{
}
