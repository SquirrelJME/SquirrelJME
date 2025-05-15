/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"

/**
 * Software Mixer implementation functions.
 *
 * @since 2025/05/10
 */
const sjme_scritchaudio_implFunctions sjme_scritchaudio_softmixFunctions =
{
	.apiInit = sjme_scritchaudio_softmix_apiInit,
	.loopIterate = sjme_scritchaudio_softmix_loopIterate,
	.queryMidiPorts = sjme_scritchaudio_softmix_queryMidiPorts,
	.sourceAttach = sjme_scritchaudio_softmix_sourceAttach,
	.streamCreate = sjme_scritchaudio_softmix_streamCreate,
};

sjme_errorCode sjme_scritchaudio_softmix_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	sjme_scritchaudio wrappedStated;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover the wrapped state. */
	wrappedStated = inState->wrappedState;
	if (wrappedStated == NULL)
		return SJME_ERROR_HEADLESS_AUDIO;

	/* If the wrapped state must be manually polled, we like having threaded */
	/* audio. */
	if (wrappedStated->bugs.manualPoll)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
