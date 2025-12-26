/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/
#include <sys/stat.h>

#include "lib/scritchaudio/scritchaudioIntern.h"
#include "lib/scritchaudio/oss/ossIntern.h"

/**
 * OSS implementation functions.
 *
 * @since 2025/05/10
 */
static const sjme_scritchaudio_implFunctions sjme_scritchaudio_ossFunctions =
{
	sjme_sm(.apiInit, sjme_scritchaudio_oss_apiInit),
	sjme_sm(.disconnect, NULL),
	sjme_sm(.loopIterate, sjme_scritchaudio_oss_loopIterate),
	sjme_sm(.queryMidiPorts, sjme_scritchaudio_oss_queryMidiPorts),
	sjme_sm(.sourceAttach, sjme_scritchaudio_oss_sourceAttach),
	sjme_sm(.streamCreate, sjme_scritchaudio_oss_streamCreate),
};

sjme_errorCode SJME_SCRITCHAUDIO_DYLIB_SYMBOL_DECLARE(oss)(
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
		initFrontEnd, &sjme_scritchaudio_ossFunctions,
		bindAudioThread)) || result == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = result;
	return SJME_ERROR_NONE;
}

SJME_SCRITCHAUDIO_DYLIB_API_EXPORT_SET(oss)

sjme_errorCode sjme_scritchaudio_oss_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	struct stat ignored;
	int dspResult, midiResult;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* OSS is always manually polled. */
	inState->bugs.manualPoll = SJME_JNI_TRUE;

	/* OSS not available in any way? These would be the default output and */
	/* the default MIDI device. */
	memset(&ignored, 0, sizeof(ignored));
	dspResult = stat(SJME_SCRITCHAUDIO_OSS_DSP, &ignored);
	midiResult = stat(SJME_SCRITCHAUDIO_OSS_MIDI, &ignored);
	if (dspResult != 0 && midiResult == 0)
		return SJME_ERROR_HEADLESS_AUDIO;

	/* Success! Not much else to do here. */
	return SJME_ERROR_NONE;
}
