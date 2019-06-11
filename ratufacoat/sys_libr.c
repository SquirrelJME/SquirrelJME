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
#include <streams/file_stream.h>

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
static struct retro_vfs_interface* vfs_cb = NULL;
static retro_video_refresh_t video_cb = NULL;

/* Basically loaded ROM file. */
static void* sjme_retroarch_basicrom = NULL;

/* Loaded JVM instance. */
static sjme_jvm* sjme_retroarch_jvm = NULL;

/* Native functions. */
static sjme_nativefuncs sjme_retroarch_nativefuncs;

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
   struct retro_vfs_interface_info vfs_getter = {1, NULL};
	
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
	
	/* Initialize VFS. */
	if (environ_cb(RETRO_ENVIRONMENT_GET_VFS_INTERFACE, &vfs_getter))
		if (vfs_getter.iface != NULL)
			vfs_cb = vfs_getter.iface;
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

/** Writes byte to standard error. */
sjme_jint sjme_retroarch_stderr_write(sjme_jint b)
{
}

/** RetroArch initialization. */
void retro_init(void)
{
	enum retro_pixel_format format;
	const char* sysdir;
	char* rompath;
	struct retro_vfs_file_handle* romfile;
	int strlens;
	sjme_jint romsize, readat, readcount, error;
	sjme_jvmoptions options;
	
	/* Use ARGB 32-bit. */
	format = RETRO_PIXEL_FORMAT_XRGB8888;
	
	/* Try setting it, ignore otherwise? */
	if (environ_cb(RETRO_ENVIRONMENT_SET_PIXEL_FORMAT, &format))
		log_cb(RETRO_LOG_INFO, "Using XRGB8888?\n");
	
	/* Load the SummerCoat ROM using RetroArch VFS rather than letting */
	/* RatufaCoat itself load the ROM since there might be very specific */
	/* file stuff we can initially skip. */
	romsize = 0;
	if (vfs_cb != NULL &&
		environ_cb(RETRO_ENVIRONMENT_GET_SYSTEM_DIRECTORY, &sysdir))
	{
		/* Determine length of strings. */
		strlens = sizeof(char) * (strlen(sysdir) +
			strlen("/squirreljme.sqc") + 2);
		
		/* Determine path of the ROM file. */
		rompath = calloc(1, strlens);
		if (rompath != NULL)
		{
			/* Cat it in. */
			strcat(rompath, sysdir);
			strcat(rompath, "/squirreljme.sqc");
			
			/* Open ROM for reading. */
			romfile = vfs_cb->open(rompath, RETRO_VFS_FILE_ACCESS_READ,
				RETRO_VFS_FILE_ACCESS_HINT_NONE);
			if (romfile != NULL)
			{
				/* Get size of ROM. */
				romsize = (sjme_jint)vfs_cb->size(romfile);
				
				/* Allocate ROM memory chunk. */
				sjme_retroarch_basicrom = malloc(romsize);
				if (sjme_retroarch_basicrom != NULL)
				{
					/* Copy chunks of ROM. */
					for (readat = 0; readat < romsize;)
					{
						/* Read in chunk. */
						readcount = vfs_cb->read(romfile, (void*)
							(((uintptr_t)sjme_retroarch_basicrom) + readat),
							romsize - readat);
						
						/* Error reading? */
						if (readcount < 0)
							break;
						
						/* Read this. */
						readat += readcount;
					}
				}
				
				/* Close it. */
				vfs_cb->close(romfile);
			}
			
			/* Free up. */
			free(rompath);
		}
	}
	
	/* Setup options. */
	memset(&options, 0, sizeof(options));
	options.ramsize = 0;
	options.presetrom = sjme_retroarch_basicrom;
	options.romsize = romsize;
	
	/* Copy the ROM on 64-bit, so the address is in SquirrelJME range. */
#if defined(SJME_BITS) && SJME_BITS > 32
	options.copyrom = 1;
#endif
	
	/* Set native functions. */
	sjme_retroarch_nativefuncs.stderr_write = sjme_retroarch_stderr_write;
	
	/* Initialize the JVM. */
	error = SJME_ERROR_NONE;
	sjme_retroarch_jvm = sjme_jvmnew(&options, &sjme_retroarch_nativefuncs,
		&error);
	
	/* Note it. */
	log_cb((error == SJME_ERROR_NONE ? RETRO_LOG_INFO : RETRO_LOG_ERROR),
		"SquirrelJME Init: %d/%x\n", (int)error, (unsigned)error);
}

/** Destroy. */
void retro_deinit(void)
{
	/* Be sure to wipe the ROM from existence. */
	if (sjme_retroarch_basicrom != NULL)
		free(sjme_retroarch_basicrom);
	sjme_retroarch_basicrom = NULL;
	
	/* Destroy the JVM as well. */
	if (sjme_retroarch_jvm != NULL)
		sjme_jvmdestroy(sjme_retroarch_jvm, NULL);
	sjme_retroarch_jvm = NULL;
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
	sjme_jint cycles, error;
	
	/* Poll for input because otherwise it prevents RetroArch from accessing */
	/* the menu. */
	input_poll_cb();
	
	/* Clear error state. */
	error = SJME_ERROR_NONE;
	
	/* Execute the JVM. */
	cycles = 1048576;
	cycles = sjme_jvmexec(sjme_retroarch_jvm, &error, cycles);
	
	/* Did the JVM mess up? */
	if (error != SJME_ERROR_NONE)
		log_cb(RETRO_LOG_ERROR, "SquirrelJME JVM Exec Error: %d/%x\n",
			(int)error, (unsigned)error);
	
	log_cb(RETRO_LOG_INFO, "Cycles: %d\n", (int)cycles);
	
	/* Random video noise. */
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
	/* With need_fullpath=false, info->data and info->size will be valid */
	/* and can be used to boot the JAR up. */
	
	/*fprintf(stderr, "load game? path=%s data=%p size=%d\n",
		info->path, info->data, (int)info->size);*/
	
	return true;
}

/** Load game special? */
bool retro_load_game_special(unsigned type, const struct retro_game_info* info,
	size_t num)
{
	/*
	fprintf(stderr, "load game special? typ=%d path=%s data=%p size=%d n=%d\n",
		type, info->path, info->data, (int)info->size, (int)num);
	*/
	return false;
}

/** Unload a game? */
void retro_unload_game(void)
{
}
