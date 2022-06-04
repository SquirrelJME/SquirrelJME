/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "frontend/libretro/lrfreeze.h"
#include "frontend/libretro/lrlocal.h"
#include "frontend/libretro/lrscreen.h"

/** Is there no support for fast-forward overrides? */
static sjme_jboolean sjme_libRetro_noFastForwardOverride;

SJME_EXTERN_C SJME_GCC_USED bool retro_serialize(void* data, size_t size)
{
	sjme_libRetroState* current = g_libRetroState;

	/* Notice. */
	sjme_libRetro_message(-1, "Attempting save state.");

	/* A save can only happen if the engine is actually running. */
	if (current == NULL || current->state == NULL)
	{
		sjme_libRetro_message(-1,
			"Cannot save, engine not running.");

		return false;
	}

	/* Cannot save states in SMT mode. */
	if (current->state->config.threadModel != SJME_ENGINE_THREAD_MODEL_COOP)
	{
		sjme_libRetro_message(-1,
			"Saving only possible in Cooperative Threading model.");

		return false;
	}

	return false;
}

SJME_EXTERN_C SJME_GCC_USED size_t retro_serialize_size(void)
{
	sjme_message("TODO: Use actual serialization size.");
	return 1024;
}

SJME_EXTERN_C SJME_GCC_USED bool retro_unserialize(const void* data, size_t size)
{
	return false;
}

void sjme_libRetro_inhibitFastForward(sjme_jboolean inhibit)
{
	struct retro_fastforwarding_override override;
	bool inFastForward;

	/* Ignore if this is not supported, it will just have no effect. */
	if (sjme_libRetro_noFastForwardOverride)
		return;

	/* Are we in fast-forward mode? */
	inFastForward = false;
	if (!g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_GET_FASTFORWARDING, &inFastForward))
		inFastForward = false;

	/* Setup override details, if we are in FF mode we should drop out of
	 * it if we become inhibited with it. */
	memset(&override, 0, sizeof(override));
	override.ratio = (inhibit ? 1.0F : 0.0F);
	override.inhibit_toggle = inhibit;
	override.notification = !inhibit;
	override.fastforward = (inFastForward && !inhibit);

	/* Set new fast forwarding detail, if not supported remember it so that we
	 * do not hammer the API every frame when we do this. */
	if (!g_libRetroCallbacks.environmentFunc(
		RETRO_ENVIRONMENT_SET_FASTFORWARDING_OVERRIDE, &override))
		sjme_libRetro_noFastForwardOverride = sjme_true;

	/* Emit a message if we are in fast-forward mode that we are dropping
	 * out of it. */
	if (inhibit && inFastForward)
		sjme_libRetro_message(-1,
			"Not fast-forwarding, unsupported in current mode.");
}
