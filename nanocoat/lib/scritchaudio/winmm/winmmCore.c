/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/scritchaudioIntern.h"
#include "lib/scritchaudio/winmm/winmmIntern.h"

/**
 * WinMM implementation functions.
 *
 * @since 2025/05/10
 */
static const sjme_scritchaudio_implFunctions sjme_scritchaudio_winmmFunctions =
{
	sjme_sm(.driverName, "winmm"),
	sjme_sm(.allFormatsOwnMixing, SJME_JNI_FALSE),
	sjme_sm(.supportsMultiStream, SJME_JNI_TRUE),
	sjme_sm(.apiInit, sjme_scritchaudio_winmm_apiInit),
	sjme_sm(.disconnect, NULL),
	sjme_sm(.loopIterate, sjme_scritchaudio_winmm_loopIterate),
	sjme_sm(.queryMidiPorts, sjme_scritchaudio_winmm_queryMidiPorts),
	sjme_sm(.sourceAttach, sjme_scritchaudio_winmm_sourceAttach),
	sjme_sm(.streamCreate, sjme_scritchaudio_winmm_streamCreate),
	sjme_sm(.nativeCallback, NULL),
};

sjme_errorCode SJME_SCRITCHAUDIO_DYLIB_SYMBOL_DECLARE(winmm)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_thread_mainFunc bindAudioThread,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchaudio result;

	if (inPool == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Forward initialize. */
	result = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_core_init(inPool, &result,
		initFrontEnd, &sjme_scritchaudio_winmmFunctions,
		bindAudioThread)) || result == NULL)
		return sjme_error_default(error);

	/* Success! */
	*outState = result;
	return SJME_ERROR_NONE;
}

SJME_SCRITCHAUDIO_DYLIB_API_EXPORT_SET(winmm)

sjme_errorCode sjme_scritchaudio_winmm_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* This uses manual polling. */
	inState->bugs.manualPoll = SJME_JNI_TRUE;

#if defined(SJME_CONFIG_HAS_BROKEN_CODE)
	/* Does WinMM support triggering or not? */
	inState->bugs.noTriggering = SJME_JNI_TRUE;
#endif

	/* There needs to be at least one audio device. */
	if (waveOutGetNumDevs() <= 0)
		return SJME_ERROR_HEADLESS_AUDIO;

	/* Success! Not much else to do here. */
	return SJME_ERROR_NONE;
}
