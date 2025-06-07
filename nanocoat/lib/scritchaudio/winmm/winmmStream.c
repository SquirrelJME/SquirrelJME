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

sjme_errorCode sjme_scritchaudio_winmm_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource)
{
	if (inState == NULL || inStream == NULL || inSource == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_winmm_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
	MMRESULT mmResult;
	HWAVEOUT handle;
	WAVEFORMATEX format;

	if (inState == NULL || outStream == NULL || inName == NULL)
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

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}