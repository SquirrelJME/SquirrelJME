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

/** Screen size. */
#define SJME_RETROARCH_WIDTH 240
#define SJME_RETROARCH_HEIGHT 320
#define SJME_RETROARCH_VIDEORAMSIZE 76800

/** RatufaCoat's Video RAM memory. */
static sjme_jint sjme_ratufacoat_videoram[SJME_RETROARCH_VIDEORAMSIZE];

/** Fallback logging, does nothing. */
static void fallback_log(enum retro_log_level level, const char* fmt, ...)
{
}

/** Callbacks. */
static retro_audio_sample_batch_t audio_cb = NULL;
static retro_audio_sample_t audio_samble_cb = NULL;
static retro_environment_t environ_cb = NULL;
static retro_input_poll_t input_poll_cb = NULL;
static retro_input_state_t input_state_cb = NULL;
static retro_log_printf_t log_cb = fallback_log;
static struct retro_vfs_interface* vfs_cb = NULL;
static retro_video_refresh_t video_cb = NULL;

/** Basically loaded ROM file. */
static void* sjme_retroarch_basicrom = NULL;

/** Loaded JVM instance. */
static sjme_jvm* sjme_retroarch_jvm = NULL;

/** Native functions. */
static sjme_nativefuncs sjme_retroarch_nativefuncs;

/** Options for RetroArch. */
static sjme_jvmoptions sjme_retroarch_options;

/** OptionJAR. */
static struct
{
	/** Pointer to the ROM data. */
	void* ptr;
	
	/** Size of the ROM. */
	sjme_jint size;
} sjme_retroarch_optionjar;

/** Error state. */
static sjme_error sjme_retroarch_error =
	{SJME_ERROR_NONE, 0};

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

	info->geometry.base_width   = SJME_RETROARCH_WIDTH;
	info->geometry.base_height  = SJME_RETROARCH_HEIGHT;
	info->geometry.max_width    = SJME_RETROARCH_WIDTH;
	info->geometry.max_height   = SJME_RETROARCH_HEIGHT;
	info->geometry.aspect_ratio =
		(double)SJME_RETROARCH_WIDTH / (double)SJME_RETROARCH_HEIGHT;
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
			{0, 0, 0, 0, NULL}
		};
	struct retro_variable vars[] =
		{
			{"squirreljme_debug_notes",
				"Enable Debug Notes; disabled|enabled"},
			{"squirreljme_cycles_per_frame",
				"Cycles Per Frame; "
				"1048576|2097152|4194304|32768|65536|131072|262144|524288"},
			
			/* End. */
			{NULL, NULL}
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
#define SJME_RETROARCH_STDERR_MAX_BUFFER 256
	static char buffer[SJME_RETROARCH_STDERR_MAX_BUFFER + 1];
	static int at;
	struct retro_log_callback logging;
	int i;
	
	/* Dump buffer contents? */
	if (b == '\n')
	{
		/* Try to get the logger again because for some reason RetroArch */
		/* nukes our callback and then it never works again? */
		environ_cb(RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &logging);
		log_cb = (logging.log != NULL ? logging.log : fallback_log);
		
		/* Dump it. */
		log_cb(RETRO_LOG_INFO, "%s\n", buffer);
		
		/* Reset for next run. */
		at = 0;
		memset(buffer, 0, sizeof(buffer));
	}
	
	/* Add to buffer. */
	else
	{
		/* Place into buffer. */
		i = at;
		if (i < SJME_RETROARCH_STDERR_MAX_BUFFER)
			buffer[i] = (char)b;
		
		/* Next position. */
		at = i + 1;
	}
	
	/* Just say it always works. */
	return 1;
#undef SJME_RETROARCH_STDERR_MAX_BUFFER
}

/** Returns a framebuffer structure. */
sjme_framebuffer* sjme_retroarch_framebuffer(void)
{
	sjme_framebuffer* rv;
	
	/* Allocate one. */
	rv = calloc(1, sizeof(*rv));
	if (rv == NULL)
		return NULL;
	
	/* Fill information out. */
	rv->pixels = sjme_ratufacoat_videoram;
	rv->width = SJME_RETROARCH_WIDTH;
	rv->height = SJME_RETROARCH_HEIGHT;
	rv->scanlen = SJME_RETROARCH_WIDTH;
	rv->format = SJME_PIXELFORMAT_INTEGER_RGB888;
	rv->numpixels = SJME_RETROARCH_VIDEORAMSIZE;
	
	return rv;
}

/** OptionJAR access. */
sjme_jint sjme_retroarch_optional_jar(void** ptr, sjme_jint* size)
{
	if (ptr != NULL)
		*ptr = sjme_retroarch_optionjar.ptr;
	
	if (size != NULL)
		*size = sjme_retroarch_optionjar.size;
	
	if (sjme_retroarch_optionjar.ptr == NULL)
		return 0;
	return 1;
}

/** RetroArch initialization. */
void retro_init(void)
{
	enum retro_pixel_format format;
	const char* sysdir;
	char* rompath;
	struct retro_vfs_file_handle* romfile;
	int strlens;
	sjme_jint romsize, readat, readcount;
	
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
	memset(&sjme_retroarch_options, 0, sizeof(sjme_retroarch_options));
	sjme_retroarch_options.ramsize = 0;
	sjme_retroarch_options.presetrom = sjme_retroarch_basicrom;
	sjme_retroarch_options.romsize = romsize;
	
	/* Set native functions. */
	memset(&sjme_retroarch_nativefuncs, 0, sizeof(sjme_retroarch_nativefuncs));
	sjme_retroarch_nativefuncs.stderr_write = sjme_retroarch_stderr_write;
	sjme_retroarch_nativefuncs.framebuffer = sjme_retroarch_framebuffer;
	sjme_retroarch_nativefuncs.optional_jar = sjme_retroarch_optional_jar;
	
	/* Perform machine reset. */
	retro_reset();
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
	struct retro_log_callback logging;
	
	/* Destroy the JVM, if it already exists. */
	if (sjme_retroarch_jvm != NULL)
		sjme_jvmdestroy(sjme_retroarch_jvm, NULL);
	sjme_retroarch_jvm = NULL;
	
	/* Reset error code! */
	memset(&sjme_retroarch_error, 0, sizeof(sjme_retroarch_error));
	
	/* Wipe the screen to the initial state, because it gets confusing. */
	memset(&sjme_ratufacoat_videoram, 0, sizeof(sjme_ratufacoat_videoram));
	
	/* Initialize the JVM. */
	sjme_retroarch_jvm = sjme_jvmnew(&sjme_retroarch_options,
		&sjme_retroarch_nativefuncs, &sjme_retroarch_error);
	
	/* Try to get the logger again because for some reason RetroArch */
	/* nukes our callback and then it never works again? */
	environ_cb(RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &logging);
	log_cb = (logging.log != NULL ? logging.log : fallback_log);
	
	/* Note it. */
	log_cb((sjme_retroarch_error.code == SJME_ERROR_NONE ?
		RETRO_LOG_INFO : RETRO_LOG_ERROR),
		"SquirrelJME Init: %d/%08x %d/%08x\n",
		(int)sjme_retroarch_error.code,
		(unsigned)sjme_retroarch_error.code,
		(int)sjme_retroarch_error.value,
		(unsigned)sjme_retroarch_error.value);
}

/** Runs single frame. */
void retro_run(void)
{
	static int died;
	struct retro_log_callback logging;
	struct retro_variable var;
	sjme_jint cycles, i, x, y;
	
	/* Poll for input because otherwise it prevents RetroArch from accessing */
	/* the menu. */
	input_poll_cb();
	
	/* If the VM died, just display a screen. */
	if (sjme_retroarch_error.code != SJME_ERROR_NONE)
	{
		/* Print failure message only once! */
		if (died == 0)
		{
			/* Only do this once! */
			died = 1;
			
			/* Try to get the logger again because for some reason RetroArch */
			/* nukes our callback and then it never works again? */
			environ_cb(RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &logging);
			log_cb = (logging.log != NULL ? logging.log : fallback_log);
			
			/* Print error. */
			log_cb(RETRO_LOG_ERROR,
				"SquirrelJME JVM Exec Error: %d/%08x %d/%08x\n",
				(int)sjme_retroarch_error.code,
				(unsigned)sjme_retroarch_error.code,
				(int)sjme_retroarch_error.value,
				(unsigned)sjme_retroarch_error.value);
			log_cb(RETRO_LOG_ERROR, "Execution now unpredictable!\n");
		}
		
		/* Border with red. */
		for (y = 0, i = 0; y < SJME_RETROARCH_HEIGHT; y++)
			for (x = 0; x < SJME_RETROARCH_WIDTH; x++, i++)
				if (x < 2 ||
					y < 2 ||
					x >= (SJME_RETROARCH_WIDTH - 2) ||
					y >= (SJME_RETROARCH_HEIGHT - 2))
					sjme_ratufacoat_videoram[i] = 0x00FF0000;
	}
	
	/* Execute the JVM otherwise. */
	else
	{
		/* Read the cycle count. */
		memset(&var, 0, sizeof(var));
		cycles = 1048576;
		var.key = "squirreljme_cycles_per_frame";
		if (environ_cb(RETRO_ENVIRONMENT_GET_VARIABLE, &var))
			if (var.value != NULL)
				cycles = (sjme_jint)strtol(var.value, NULL, 10);
		
		/* Execute the JVM. */
		cycles = sjme_jvmexec(sjme_retroarch_jvm, &sjme_retroarch_error,
			cycles);
	}
	
	/* Draw video onto the display. */
	video_cb(sjme_ratufacoat_videoram,
		SJME_RETROARCH_WIDTH, SJME_RETROARCH_HEIGHT,
		SJME_RETROARCH_WIDTH * sizeof(uint32_t));
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
	struct retro_log_callback logging;
	
	/* Ignore if null passed, because this means no content specified! */
	/* True must always be returned, otherwise false will force RetroArch */
	/* back into the menu for JAR selection. */
	if (info == NULL)
		return true;
	
	/* Try to get the logger again because for some reason RetroArch */
	/* nukes our callback and then it never works again? */
	environ_cb(RETRO_ENVIRONMENT_GET_LOG_INTERFACE, &logging);
	log_cb = (logging.log != NULL ? logging.log : fallback_log);
	
	/* With need_fullpath=false, info->data and info->size will be valid */
	/* and can be used to boot the JAR up. */
	log_cb(RETRO_LOG_INFO, "Implant JAR: path=%s data=%p size=%d\n",
		info->path, info->data, (int)info->size);
	
	/* Set in. */
	sjme_retroarch_optionjar.ptr = info->data;
	sjme_retroarch_optionjar.size = info->size;
	
	/* Always accept this. */
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
	/* Remove. */
	sjme_retroarch_optionjar.ptr = NULL;
	sjme_retroarch_optionjar.size = 0;
}
