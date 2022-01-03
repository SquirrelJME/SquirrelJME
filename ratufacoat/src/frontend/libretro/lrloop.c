/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "frontend/libretro/lrjar.h"
#include "frontend/libretro/lrlocal.h"
#include "frontend/libretro/lrscreen.h"

sjme_libRetroCallbacks g_libRetroCallbacks =
{
	NULL, NULL, NULL, NULL
};

sjme_libRetroState* g_libRetroState = NULL;

/**
 * Destroys the library instance.
 * 
 * @since 2022/01/02
 */
SJME_GCC_USED void retro_deinit(void)
{
	sjme_libRetroState* oldState;
	
	/* Nothing to destroy? */
	oldState = g_libRetroState;
	if (oldState == NULL)
		return;
	
	/* Notice */
	sjme_libRetro_message(0, "Destroying engine.");
	
	/* De-allocate the VM state. */
	g_libRetroState = NULL;
	memset(oldState, 0, sizeof(*oldState));
	free(oldState);
	
	/* Notice. */
	sjme_libRetro_message(100, "Engine destroyed.");
}

/**
 * Initializes the base library.
 * 
 * @since 2022/01/02
 */
SJME_GCC_USED void retro_init(void)
{
	/* Does the same action as resetting the system. */
	retro_reset();
#if 0
	
	
	enum retro_pixel_format format;
	const char* sysdir;
	char* rompath;
	struct retro_vfs_file_handle* romfile;
	int strlens;
	sjme_jint romsize, readat, readcount;
	struct retro_log_callback logging;
#if defined(SQUIRRELJME_HAS_BUILTIN)
	struct retro_variable var;
	sjme_jbyte useBuiltInRom;
#endif
	
	/* If we have a pre-existing state, destroy it so we have just one
	 * instance ever total. */
	if (g_libRetroState != NULL)
		retro_deinit();
	
	/* Reset global state */
	g_libRetroState = malloc(sizeof(*g_libRetroState));
	memset(g_libRetroState, 0, sizeof(*g_libRetroState));
	
	/* Use ARGB 32-bit. */
	format = RETRO_PIXEL_FORMAT_XRGB8888;
	
	/* Try setting it, ignore otherwise? */
	if (environ_cb(RETRO_ENVIRONMENT_SET_PIXEL_FORMAT, &format))
		log_cb(RETRO_LOG_INFO, "Using XRGB8888?\n");
	
	/* Size of the read ROM. */
	sjme_retroarch_basicrom = NULL;
	romsize = 0;

	/* Is a built-in ROM available? */
#if defined(SQUIRRELJME_HAS_BUILTIN)
	/* Which variable being read? */
	memset(&var, 0, sizeof(var));
	var.key = "squirreljme_use_external_rom";
	
	/* If it is not disabled then it is enabled */
	useBuiltInRom = 0;
	if (environ_cb(RETRO_ENVIRONMENT_GET_VARIABLE, &var))
		if (var.value != NULL)
			useBuiltInRom = !!strcmp("disabled", var.value);
	
	/* Use the built-in ROM for these. */
	if (useBuiltInRom)
	{
		sjme_retroarch_basicrom = (void*)sjme_builtInRomData;
		romsize = sjme_builtInRomSize;
	}
#endif
	
	/* Load the SummerCoat ROM using RetroArch VFS rather than letting */
	/* RatufaCoat itself load the ROM since there might be very specific */
	/* file stuff we can initially skip. */
	if (romsize == 0 && vfs_cb != NULL &&
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
	sjme_retroarch_options.romData = sjme_retroarch_basicrom;
	sjme_retroarch_options.romSize = romsize;
	
	/* Set native functions. */
	memset(&sjme_retroarch_nativefuncs, 0, sizeof(sjme_retroarch_nativefuncs));
	sjme_retroarch_nativefuncs.stderr_write = sjme_retroarch_stderr_write;
	sjme_retroarch_nativefuncs.framebuffer = sjme_retroarch_framebuffer;
	sjme_retroarch_nativefuncs.optional_jar = sjme_retroarch_optional_jar;
	
	/* Perform machine reset. */
	retro_reset();
#endif
}

SJME_GCC_USED void retro_reset(void)
{
	sjme_libRetroState* newState;
	sjme_jboolean okayInit;
	
	/* If we have a pre-existing state, destroy it so we have just one
	 * instance ever total. */
	if (g_libRetroState != NULL)
		retro_deinit();
	
	/* Notice. */
	sjme_libRetro_message(0, "Initializing engine.");
	
	/* Reset global state */
	newState = malloc(sizeof(*newState));
	memset(newState, 0, sizeof(*newState));
	
	/* Setup engine configuration from the settings. */
	okayInit = sjme_true;
	okayInit &= sjme_libRetro_selectRom(&newState->engineConfig);
	
	/* Did initialization fail? */
	if (!okayInit)
	{
		/* Nope, de-init. */
		free(newState);
		
		/* Emit a failure message. */
		sjme_libRetro_message(-1,
			"Could not initialize engine.");
		
		/* Fail. */
		return;
	}
	
	/* Notice. */
	sjme_libRetro_message(100, "Initialization complete.");
	
	/* Use this global state now that it is fully up. */
	g_libRetroState = newState;
}

/** Runs single frame. */
SJME_GCC_USED void retro_run(void)
{
	sjme_libRetroState* currentState;
	sjme_jubyte badScreen[4];
	
	/* Poll for input because otherwise it prevents RetroArch from accessing */
	/* the menu. */
	g_libRetroCallbacks.inputPollFunc();
	
	/* Do nothing if there is no state. */
	currentState = g_libRetroState;
	if (currentState == NULL)
	{
		/* Draw a completely blank display since we need to show something,
		 * otherwise RetroArch will freeze and not respond at all. */
		memset(badScreen, 0, sizeof(badScreen));
		g_libRetroCallbacks.videoFunc(badScreen,
			1, 1, sizeof(badScreen));
		
		return;
	}
	
	sjme_todo("Run single frame?");
#if 0
	static int died;
	struct retro_log_callback logging;
	struct retro_variable var;
	sjme_jint cycles, i, x, y;
	
	/* Poll for input because otherwise it prevents RetroArch from accessing */
	/* the menu. */
	g_libRetroState->inputPollFunc();
	
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
#endif
}
