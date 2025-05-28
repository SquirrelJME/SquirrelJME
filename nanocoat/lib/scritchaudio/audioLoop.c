/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/scritchaudio.h"
#include "lib/scritchaudio/scritchaudioIntern.h"

sjme_errorCode sjme_scritchaudio_core_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	sjme_jlong now;
	sjme_jint latency, expected48KHzSamples;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Update the clock time. */
	inState->nal->nanoTime(&now);
	inState->clock.clock.full = now.full - inState->clock.clockBase.full;

	/* Get the latency to determine the sample count. */
	latency = (sjme_atomic_sjme_jint_get(&inState->pollDelayMillis) *
		1000000) + sjme_atomic_sjme_jint_get(&inState->pollDelayNanos);

	/* Calculate the expected number of samples. */
	/* rate * latency. */
	expected48KHzSamples = (448 * (latency / 100)) / 10;
	
	/* Only forward if the handler supports this. */
	if (inState->impl->loopIterate != NULL)
		return inState->impl->loopIterate(inState, inState->clock.clock,
			expected48KHzSamples);
	return SJME_ERROR_NONE;
}
