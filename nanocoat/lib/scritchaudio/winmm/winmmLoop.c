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

sjme_errorCode sjme_scritchaudio_winmm_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream)
{
	sjme_errorCode error, playError, rendError;
	HWAVEOUT hWaveOut;
	MMRESULT mmResult;
	sjme_scritchaudio_streamBuffer* rend;
	sjme_scritchaudio_streamBuffer* play;
	WAVEHDR* playHeader;
	sjme_jint i, n;
	sjme_scritchaudio_source source;
	sjme_jboolean missedPrepare;
	sjme_list(sjme_scritchaudio_source)* sources;
	sjme_scritchaudio_renderInfo* renderInfo;

	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover stream. */
	inStream = (inStream != NULL ? inStream : inState->stream);
	if (inStream == NULL)
		return SJME_ERROR_AUDIO_DESTROYED;

	/* Recover the output handle. */
	hWaveOut = inStream->data.handle;
	if (hWaveOut == NULL)
		return SJME_ERROR_AUDIO_DESTROYED;

	/* Recover the single source. */
	sources = inStream->sources;
	if (sources == NULL)
		return SJME_ERROR_AUDIO_AWAITING;
	source = NULL;
	for (i = 0, n = sources->length; i < n; i++)
	{
		source = sources->elements[i];
		if (source != NULL)
			break;
	}

	/* None found? */
	if (source == NULL)
		return SJME_ERROR_AUDIO_AWAITING;

	/* Recover the render info. */
	renderInfo = &inStream->data.renderInfo;

	/* Which buffer are we playing and which are we rendering? */
	rend = &inStream->data.buffers[inStream->data.renderBuffer];
	play = &inStream->data.buffers[!inStream->data.renderBuffer];

	/* Reset error states. */
	playError = SJME_ERROR_NONE;
	rendError = SJME_ERROR_NONE;

	/* Set up the play header. */
	playHeader = play->header;
	memset(playHeader, 0, inStream->data.headerSize);
	playHeader->lpData = play->buffer;
	playHeader->dwBufferLength = renderInfo->bufSize;
	playHeader->dwFlags = 0;

	/* Prepare the header and try to send the data to the sound card. */
	/* If it is not busy, then we can actually push data to it. */
	mmResult = waveOutPrepareHeader(hWaveOut, playHeader,
		sizeof(WAVEHDR));
	missedPrepare = (mmResult == MMSYSERR_HANDLEBUSY ||
		mmResult == MMSYSERR_INVALHANDLE);
	if (!missedPrepare)
	{
		/* Send the playback buffer to the sound card. */
		mmResult = waveOutWrite(hWaveOut, playHeader, sizeof(WAVEHDR));
		if (mmResult != MMSYSERR_NOERROR && !sjme_error_is(playError))
			playError = SJME_ERROR_AUDIO_WRITE_FAILED;
	}

	/* While the sound card is playing audio, render the next batch of */
	/* audio into the buffer. */
	rendError = source->renderFunc(inState, source, renderInfo,
		(sjme_scritchaudio_buffer*)rend->buffer);

	/* If we missed preparing the audio, then try playing it now after we */
	/* rendered something. */
	if (missedPrepare)
	{
		/* Wait until ready. */
		do
		{
			mmResult = waveOutPrepareHeader(hWaveOut, playHeader,
				sizeof(WAVEHDR));
		} while (mmResult == MMSYSERR_HANDLEBUSY ||
			mmResult == MMSYSERR_INVALHANDLE);

		/* Send the playback buffer to the sound card. */
		mmResult = waveOutWrite(hWaveOut, playHeader, sizeof(WAVEHDR));
		if (mmResult != MMSYSERR_NOERROR && !sjme_error_is(playError))
			playError = SJME_ERROR_AUDIO_WRITE_FAILED;
	}

	/* Unprepare the play header, wait until it actually stops. */
#if 1
	do
	{
#endif
		mmResult = waveOutUnprepareHeader(hWaveOut, playHeader,
			sizeof(WAVEHDR));
#if 1
	} while (mmResult == WAVERR_STILLPLAYING);
#endif
	if (mmResult != MMSYSERR_NOERROR && mmResult != WAVERR_STILLPLAYING &&
		!sjme_error_is(playError))
		playError = SJME_ERROR_AUDIO_PREPARE_FAILED;

	/* Ladder any errors. */
	if (sjme_error_is(rendError))
		return sjme_error_default(rendError);
	if (sjme_error_is(playError))
		return sjme_error_default(playError);
	return SJME_ERROR_NONE;
}
