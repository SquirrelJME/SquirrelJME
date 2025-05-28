/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchaudio/softmix/softmixIntern.h"

static sjme_errorCode sjme_scritchaudio_softmix_streamMix(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInValue sjme_jlong clock,
	sjme_attrInValue sjme_jint expected48KHzSamples)
{
	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_softmix_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInValue sjme_jlong clock,
	sjme_attrInValue sjme_jint expected48KHzSamples)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedState;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Copy the clock directly. */
	memmove(&wrappedState->clock.clock, &clock,
		sizeof(inState->clock.clock));
	
	/* Forward directly to the native iterator, skipping any API code as */
	/* most everything was already calculated. */
	if (wrappedState->impl->loopIterate != NULL)
		return wrappedState->impl->loopIterate(wrappedState, clock,
			expected48KHzSamples);
	
	return SJME_ERROR_NONE;
}
