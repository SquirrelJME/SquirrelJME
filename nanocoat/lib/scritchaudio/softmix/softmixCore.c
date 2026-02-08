/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"
#include "sjme/multithread.h"

/**
 * Software Mixer implementation functions.
 *
 * @since 2025/05/10
 */
const sjme_scritchaudio_implFunctions sjme_scritchaudio_softmixFunctions =
{
	sjme_sm(.driverName, "softmix"),
	sjme_sm(.allFormatsOwnMixing, SJME_JNI_TRUE),
	sjme_sm(.supportsMultiStream, SJME_JNI_TRUE),
	sjme_sm(.apiInit, sjme_scritchaudio_softmix_apiInit),
	sjme_sm(.disconnect, NULL),
	sjme_sm(.loopIterate, NULL),
	sjme_sm(.queryMidiPorts, sjme_scritchaudio_softmix_queryMidiPorts),
	sjme_sm(.sourceAttach, sjme_scritchaudio_softmix_sourceAttach),
	sjme_sm(.streamCreate, sjme_scritchaudio_softmix_streamCreate),
	sjme_sm(.nativeCallback, NULL),
};

sjme_errorCode sjme_scritchaudio_softmix_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedState;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover the wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_HEADLESS_AUDIO;
	
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("Software mixer is ready!");
#endif

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_initThread:
fail_noLoopIterate:
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("softMix init failed: %d", error);
#endif
	
	return sjme_error_default(error);
}
