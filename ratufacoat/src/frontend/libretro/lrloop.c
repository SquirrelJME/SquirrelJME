/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "frontend/frontfunc.h"
#include "frontend/libretro/lrfreeze.h"
#include "frontend/libretro/lrjar.h"
#include "frontend/libretro/lrlocal.h"
#include "frontend/libretro/lrloop.h"
#include "frontend/libretro/lrscreen.h"
#include "memory.h"
#include "frontend/libretro/lrenv.h"

sjme_libRetroCallbacks g_libRetroCallbacks =
{
	NULL, NULL, NULL, NULL
};

sjme_libRetroState* g_libRetroState = NULL;

/** Front-end bridge for RetroArch. */
static const sjme_frontBridge sjme_libRetro_frontBridge =
{
	.exit = sjme_libRetro_loopExit,
};

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
	
	/* Forward destruction. */
	g_libRetroState = NULL;
	sjme_libRetro_deinit(oldState);
	
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
	
	/* Initialize a blank state. */
	newState = sjme_malloc(sizeof(*newState), NULL);
	memset(newState, 0, sizeof(*newState));
	
	/* Set the front-bridge interface for engine-to-RetroArch calls. */
	newState->config.frontBridge = &sjme_libRetro_frontBridge;
	
	/* Setup engine configuration from the settings. */
	okayInit = sjme_true;
	okayInit &= sjme_libRetro_selectRom(&newState->config);
	okayInit &= sjme_libRetro_screenConfig(&newState->config);
	okayInit &= sjme_libRetro_loopConfig(&newState->config);
	
	/* Did initialization fail? */
	if (!okayInit)
	{
		/* Nope, de-init. */
		sjme_libRetro_deinit(newState);
		
		/* Emit a failure message. */
		sjme_libRetro_message(-1,
			"Could not configure engine.");
		
		/* Fail. */
		return;
	}
	
	/* Notice. */
	sjme_libRetro_message(50, "Configuration complete.");
	
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
	
	/* Poll for input because otherwise it prevents the user from accessing */
	/* the RetroArch menu. */
	g_libRetroCallbacks.inputPollFunc();
	
	/* Do nothing if there is no state. */
	currentState = g_libRetroState;
	if (currentState == NULL)
	{
		/* Nothing is running, so we cannot fast-forward. */
		sjme_libRetro_inhibitFastForward(sjme_true);
		
		/* Draw a completely blank display since we need to show something,
		 * otherwise RetroArch will freeze and not respond at all. */
		memset(badScreen, 0, sizeof(badScreen));
		g_libRetroCallbacks.videoFunc(badScreen,
			1, 1, sizeof(badScreen));
		
		return;
	}
	
	sjme_todo("Run single frame?");
}

void sjme_libRetro_deinit(sjme_libRetroState* state)
{
	/* Destroy Screen. */
	if (state->video.pixels != NULL)
	{
		sjme_free(state->video.pixels, NULL);
		state->video.pixels = NULL;
	}
	
	/* De-allocate the VM state. */
	sjme_free(state, NULL);
}

sjme_jboolean sjme_libRetro_loopConfig(sjme_engineConfig* config)
{
	struct retro_variable getVar;
	const char* modelValue;
	const char* cycleValue;
	sjme_jint cycles;
	
	/* Notice. */
	sjme_libRetro_message(10, "Determining loop config.");
	
	/* Get thread model setting. */
	memset(&getVar, 0, sizeof(getVar));
	getVar.key = SJME_LIBRETRO_CONFIG_THREAD_MODEL;
	modelValue = NULL;
	if (g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_GET_VARIABLE, &getVar))
		if (getVar.value != NULL)
			modelValue = getVar.value;
			
	/* Use a default value. */
	if (modelValue == NULL)
		modelValue = SJME_LIBRETRO_CONFIG_THREAD_MODEL_COOP;
	
	/* Configuration setting depends. */
	if (0 == strcmp(modelValue, SJME_LIBRETRO_CONFIG_THREAD_MODEL_SMT))
		config->threadModel = SJME_ENGINE_THREAD_MODEL_COOP;
	else
		config->threadModel = SJME_ENGINE_THREAD_MODEL_SMT;
		
	/* Get cycle model setting. */
	memset(&getVar, 0, sizeof(getVar));
	getVar.key = SJME_LIBRETRO_CONFIG_COOP_CYCLES;
	cycleValue = NULL;
	if (g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_GET_VARIABLE, &getVar))
		if (getVar.value != NULL)
			cycleValue = getVar.value;
	
	/* Default value? */
	if (cycleValue == NULL)
		cycleValue = SJME_LIBRETRO_CONFIG_DEFAULT_CYCLES;
	
	/* Parse cycle value. */
	cycles = strtol(cycleValue, NULL, 10);
	config->coopCycleLimit = (cycles * 1000000) / SJME_LIBRETRO_FRAME_RATE;
	
	/* Notice. */
	sjme_libRetro_message(100, "Determined loop config.");
	
	return sjme_true;
}

sjme_jboolean sjme_libRetro_loopExit(sjme_jint exitCode, sjme_error* error)
{
	/* Notice. */
	sjme_libRetro_message(-1, "Exiting with %d.", exitCode);
	
	/* Destroy the engine first. */
	retro_deinit();
	
	/* Tell the front-end to shut the core down. */
	if (!g_libRetroCallbacks.environmentFunc(RETRO_ENVIRONMENT_SHUTDOWN, NULL))
	{
		/* Emit a message saying we cannot just shut the core down. */
		sjme_libRetro_message(-1, "Core initiated "
			"shut-down not supported, thus it is now safe to turn off your "
			"device.");
		
		/* Fail here. */
		sjme_setError(error, SJME_ERROR_NOT_SUPPORTED, 0);
		
		return sjme_false;
	}
	
	/* Shut-down I suppose was a success. */
	return sjme_true;
}

