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
	sjme_errorCode error;
	WAVEHDR* header;
	HWAVEOUT hWaveOut;
	MMRESULT mmResult;
	sjme_pointer buf;
	sjme_jint bufSize, i, n;
	sjme_scritchaudio_source source;
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

	/* Obtain sample buffer */
	buf = inStream->data.buffer;
	if (buf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* If the source format is unsigned, we need to actually set the proper */
	/* zero level, otherwise there will be clicks/pops. */
	bufSize = renderInfo->bufSize;
	if (renderInfo->format == SJME_SCRITCHAUDIO_FORMAT_BYTE_U8)
		memset(buf, 0x80, bufSize);
	else
		memset(buf, 0, bufSize);

	/* Setup output header. */
	header = inStream->data.header;
	memset(header, 0, sizeof(*header));
	header->lpData = buf;
	header->dwBufferLength = bufSize;
	header->dwFlags = 0;

	/* Wait until the output header is fully ready. */
	do
	{
		mmResult = waveOutPrepareHeader(hWaveOut, header,
			sizeof(WAVEHDR));
	} while (mmResult == MMSYSERR_HANDLEBUSY ||
		mmResult == MMSYSERR_INVALHANDLE);

	/* Render source. */
	if (sjme_error_is(error = source->renderFunc(inState,
		source, renderInfo, (sjme_scritchaudio_buffer*)buf)))
		return sjme_error_default(error);

	/* Failed to prepare? */
	if (mmResult != MMSYSERR_NOERROR)
		return SJME_ERROR_AUDIO_PREPARE_FAILED;

	/* Write to the audio device. */
	mmResult = waveOutWrite(hWaveOut, header, sizeof(WAVEHDR));
	if (mmResult != MMSYSERR_NOERROR)
		return SJME_ERROR_AUDIO_WRITE_FAILED;

	/* Unprepare the header. */
	do
	{
		mmResult = waveOutUnprepareHeader(hWaveOut, header,
			sizeof(WAVEHDR));
	} while (mmResult == WAVERR_STILLPLAYING);
	if (mmResult != MMSYSERR_NOERROR)
		return SJME_ERROR_AUDIO_PREPARE_FAILED;

	/* Success! */
	return SJME_ERROR_NONE;

#if 0
	sjme_errorCode error, errorRender;
	sjme_scritchaudio inState;
	sjme_scritchaudio_stream inStream;
	sjme_pointer buf;
	sjme_jint bufSize, i, n;
	sjme_scritchaudio_source source;
	WAVEHDR* header;
	MMRESULT mmResult;
	sjme_scritchaudio_renderInfo renderInfo;
	sjme_list(sjme_scritchaudio_source)* sources;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("(%p, %d, %p, %d, %d)",
		hWaveOut, uMsg, dwInstance, dwParam1, dwParam2);
#endif

	/* Reset other errors. */
	error = SJME_ERROR_NONE;
	errorRender = SJME_ERROR_NONE;
	mmResult = MMSYSERR_NOERROR;

	/* We only care about open and write. */
	if (uMsg != WOM_OPEN && uMsg != WOM_DONE)
		return;

	/* Recover stream. */
	inStream = (sjme_scritchaudio_stream)dwInstance;
	if (inStream == NULL)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_any;
	}

	/* Recover state. */
	inState = inStream->connection.inState;
	if (inState == NULL)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_any;
	}

	/* We are in the audio thread and need to bind natively */
	/* before we can call into any attached JVM. */
	if (sjme_atomic_cs(sjme_jint, &inStream->data.bound, 0, 1))
	{
		/* Set loop thread. */
		inStream->loopThread = sjme_thread_currentR();
		sjme_atomic_s(sjme_jint, &inStream->loopThreadReady, 1);

		/* Call native binder. */
		if (inState->bindAudioThread != NULL)
			inState->bindAudioThread(inStream);
	}

	/* Allocate the buffer. */
	buf = inStream->data.buffer;
	if (buf == NULL)
	{
		error = SJME_ERROR_ILLEGAL_STATE;
		goto fail_any;
	}

	/* Grab the header. */
	header = inStream->data.header;
	if (header == NULL)
	{
		error = SJME_ERROR_ILLEGAL_STATE;
		goto fail_any;
	}

	/* What is the buffer size? */
	bufSize = renderInfo.bufSize;

	/* If the source format is unsigned, we need to actually set the proper */
	/* zero level, otherwise there will be clicks/pops. */
	if (renderInfo.format == SJME_SCRITCHAUDIO_FORMAT_BYTE_U8)
		memset(buf, 0x80, bufSize);
	else
		memset(buf, 0, bufSize);

	/* Recover the single source, if done playing the last buffer. */
	source = NULL;
	if (uMsg == WOM_DONE)
	{
		/* Lock the shared lock. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			inStream->connection.lock)))
			goto fail_any;

		/* Recover the single source. */
		sources = inStream->sources;
		if (sources != NULL)
			for (i = 0, n = sources->length; i < n; i++)
			{
				source = sources->elements[i];
				if (source != NULL)
					break;
			}

		/* Free the shared lock. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			inStream->connection.lock, NULL)))
			goto fail_any;
	}

#if defined(SJME_CONFIG_EXPERIMENT_WINMM_PAUSE)
	/* Disable playback, if playback is synchronous then pausing */
	/* does not occur. */
	if (uMsg == WOM_OPEN || uMsg == WOM_DONE)
	{
		mmResult = waveOutPause(hWaveOut);
		if (mmResult != MMSYSERR_NOERROR && mmResult != MMSYSERR_NOTSUPPORTED)
		{
			error = SJME_ERROR_AUDIO_TRIGGER_FAILED;
			goto fail_any;
		}
	}
#endif

	/* Render source. */
	if (source == NULL || source->renderFunc == NULL)
		errorRender = SJME_ERROR_AUDIO_AWAITING;
	else
		errorRender = source->renderFunc(inState,
			source, &renderInfo, (sjme_scritchaudio_buffer*)buf);

	/* Only send data when there is nothing left. */
	if (uMsg == WOM_OPEN || uMsg == WOM_DONE)
	{
		/* Prepare the wave header. */
		header->lpData = buf;
		header->dwBufferLength = bufSize;
		header->dwFlags = 0;
		do
		{
			sjme_message("Prepare... %d", mmResult);
			mmResult = waveOutPrepareHeader(hWaveOut, header,
				sizeof(*header));
		} while (mmResult == MMSYSERR_HANDLEBUSY ||
			mmResult == MMSYSERR_INVALHANDLE);

		/* Failed to prepare? */
		if (mmResult != MMSYSERR_NOERROR)
		{
			error = SJME_ERROR_AUDIO_PREPARE_FAILED;
			goto fail_any;
		}

		/* Write to the audio device. */
		mmResult = waveOutWrite(hWaveOut, header, sizeof(*header));
		if (mmResult != MMSYSERR_NOERROR)
		{
			error = SJME_ERROR_AUDIO_WRITE_FAILED;
			goto fail_any;
		}
	}

#if defined(SJME_CONFIG_EXPERIMENT_WINMM_PAUSE)
	/* Resume playback, if it was previously paused. */
	if ((uMsg == WOM_OPEN || uMsg == WOM_DONE) && mmResult == MMSYSERR_NOERROR)
	{
		mmResult = waveOutRestart(hWaveOut);
		if (mmResult != MMSYSERR_NOERROR && mmResult != MMSYSERR_NOTSUPPORTED)
		{
			error = SJME_ERROR_AUDIO_TRIGGER_FAILED;
			goto fail_any;
		}
	}
#endif

	/* Did the render fail? */
	if (sjme_error_is(errorRender))
	{
		error = errorRender;
		goto fail_any;
	}

	/* Success! */
	return;

fail_any:
	/* Set last callback error. */
	if (inStream != NULL)
		sjme_atomic_s(sjme_jint, &inStream->lastError,
			error);

#if defined(SJME_CONFIG_DEBUG)
	if (error != SJME_ERROR_AUDIO_AWAITING)
	{
		sjme_message("winMM Error: %d %d (mm: %d; win: %d)",
			error, errorRender, mmResult, GetLastError());
		sjme_message("(%p, %d, %p, %d, %d)",
			hWaveOut, uMsg, dwInstance, dwParam1, dwParam2);
	}
#endif

	/* Close self to stop this thread. */
	waveOutClose(hWaveOut);
#endif
}
