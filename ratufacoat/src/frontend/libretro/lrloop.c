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
}
