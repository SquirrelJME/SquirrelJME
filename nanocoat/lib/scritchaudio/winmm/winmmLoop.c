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
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
	sjme_errorCode error;
	WAVEHDR header;
	HWAVEOUT handle;
	sjme_pointer buf;
	sjme_jint bufSize, i, n;
	sjme_scritchaudio_source source;
	sjme_list_sjme_scritchaudio_source* sources;

	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover stream, ignore if not ready yet. */
	inStream = inState->stream;
	if (inStream == NULL)
		return SJME_ERROR_NONE;

	/* Recover the single source. */
	sources = inStream->sources;
	if (sources == NULL)
		return SJME_ERROR_NONE;
	source = NULL;
	for (i = 0, n = sources->length; i < n; i++)
	{
		source = sources->elements[i];
		if (source != NULL)
			break;
	}

	/* None found? */
	if (source == NULL)
		return SJME_ERROR_NONE;

	/* Recover the output handle. */
	handle = inStream->data.handle;

	/* Allocate sample buffer */
	bufSize = renderInfo->bufSize;
	buf = sjme_alloca(bufSize);
	if (buf == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(buf, 0, bufSize);

	/* Render source. */
	if (sjme_error_is(error = source->renderFunc(inState,
		source, renderInfo, (sjme_scritchaudio_buffer*)buf)))
		return sjme_error_default(error);

	/* Setup output header. */
	memset(&header, 0, sizeof(header));
	header.lpData = buf;
	header.dwBufferLength = bufSize;
	header.dwLoops = 0;

	/* Disable playback. */
	if (waveOutPause(handle) != MMSYSERR_NOERROR)
		return SJME_ERROR_NATIVE_ERROR;

	/* Write to the buffer until the buffer is done. */
	while ((header.dwFlags & WHDR_DONE) == 0)
	{
		if (waveOutWrite(handle, &header, sizeof(header)) !=
			MMSYSERR_NOERROR)
			return SJME_ERROR_NATIVE_ERROR;
	}

	/* Resume playback. */
	if (waveOutRestart(handle) != MMSYSERR_NOERROR)
		return SJME_ERROR_NATIVE_ERROR;

	/* Nothing. */
	return SJME_ERROR_NONE;
}