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

static sjme_errorCode sjme_scritchaudio_winmm_peerNone(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

	/* WinMM does not care about any peers. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_winmm_peerDisconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer,
	sjme_attrInValue sjme_jboolean explicit)
{
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

	/* WinMM does not care about any peers. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_winmm_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource)
{
	sjme_list_sjme_scritchaudio_source* sources;
	sjme_jint i, n;

	if (inState == NULL || inStream == NULL || inSource == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Can only attach in the same format. */
	if (inSource->format != inStream->format ||
		inSource->rate != inStream->rate ||
		inSource->channels != inStream->channels)
		return SJME_ERROR_AUDIO_FORMAT_MISMATCH;

	/* There can only be a single WinMM source at a time. */
	sources = inStream->sources;
	if (sources != NULL)
		for (i = 0, n = sources->length; i < n; i++)
			if (sources->elements[i] != NULL)
				return SJME_ERROR_AUDIO_NO_RESOURCES;

	/* Just set peer disconnection functions, despite not doing much. */
	inSource->connection.noPeers = sjme_scritchaudio_winmm_peerNone;
	inSource->connection.peerDisconnect =
		sjme_scritchaudio_winmm_peerDisconnect;

	/* OSS is completely manually polled, so nothing is ever registered. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_winmm_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_scritchaudio_stream inOutStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
	MMRESULT mmResult;
	HWAVEOUT handle;
	WAVEFORMATEX format;

	if (inState == NULL || inOutStream == NULL || inName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Float is not supported. */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_FLOAT_F32)
		return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;

	/* Fill in wave format. */
	memset(&format, 0, sizeof(format));
	format.cbSize = sizeof(format);
	format.wFormatTag = WAVE_FORMAT_PCM;
	format.wBitsPerSample = sjme_scritchaudio_bytesPerSample[inFormat] * 8;
	format.nSamplesPerSec = inRate;
	format.nChannels = inChannels;
	format.nBlockAlign = format.nChannels * (format.wBitsPerSample / 8);
	format.nAvgBytesPerSec = format.nSamplesPerSec * format.nBlockAlign;

	/* Open the default device. */
	handle = NULL;
	mmResult = waveOutOpen(&handle, WAVE_MAPPER, &format,
		0, 0, WAVE_FORMAT_DIRECT | CALLBACK_NULL);
	if (mmResult != MMSYSERR_NOERROR || handle == NULL)
		return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;

	/* Set stream details. */
	inOutStream->data.handle = handle;
	inOutStream->connection.noPeers = sjme_scritchaudio_winmm_peerNone;
	inOutStream->connection.peerDisconnect =
		sjme_scritchaudio_winmm_peerDisconnect;

	/* Return the resultant stream. */
	return SJME_ERROR_NONE;
}