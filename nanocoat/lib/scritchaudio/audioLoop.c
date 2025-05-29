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
	sjme_errorCode error;
	sjme_jlong now;
	sjme_jint latency, expected44KHzSamples, expected48KHzSamples;
	sjme_scritchaudio_stream stream;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Update the clock time. */
	inState->nal->nanoTime(&now);
	inState->clock.clock.full = now.full - inState->clock.clockBase.full;

	/* If there is no stream, do not bother. */
	stream = inState->stream;
	if (stream == NULL)
		return SJME_ERROR_NONE;

	/* Get the latency to determine the sample count. */
	/* Add extra latency of 25ms. */
	latency = (sjme_atomic_sjme_jint_get(&inState->pollDelayMillis) *
		1000000) + 25000000;
	if (sjme_atomic_sjme_jint_get(&inState->pollDelayNanos) > 0)
		latency += 1000000;

	/* Calculate the expected number of samples. */
	/* rate * latency. */
	expected44KHzSamples = (441 * (latency / 10000)) / 1000;
	expected48KHzSamples = (448 * (latency / 10000)) / 1000;
	
	/* Only forward if the handler supports this. */
	if (inState->impl->loopIterate != NULL)
	{
		/* Lock the shared lock. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&stream->sharedLock)))
			return sjme_error_default(error);
		
		/* Run the loop. */
		error = inState->impl->loopIterate(inState, inState->clock.clock,
			expected48KHzSamples, expected44KHzSamples);

		/* Release the lock. */
		if (sjme_error_is(sjme_thread_spinLockRelease(&stream->sharedLock,
			NULL)))
			return sjme_error_defaultOr(error, SJME_ERROR_ILLEGAL_STATE);

		/* Return whatever error was given. */
		return error;
	}
	
	return SJME_ERROR_NONE;
}
