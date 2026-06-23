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

static void sjme_scritchaudio_winmm_guid(GUID* guid, sjme_jint data1,
	sjme_jshort data2, sjme_jshort data3, sjme_jbyte data4a,
	sjme_jbyte data4b, sjme_jbyte data4c, sjme_jbyte data4d,
	sjme_jbyte data4e, sjme_jbyte data4f, sjme_jbyte data4g,
	sjme_jbyte data4h)
{
	guid->Data1 = data1;
	guid->Data2 = data2;
	guid->Data3 = data3;
	guid->Data4[0] = data4a;
	guid->Data4[1] = data4b;
	guid->Data4[2] = data4c;
	guid->Data4[3] = data4d;
	guid->Data4[4] = data4e;
	guid->Data4[5] = data4f;
	guid->Data4[6] = data4g;
	guid->Data4[7] = data4h;
}

static sjme_errorCode sjme_scritchaudio_winmm_peerNone(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	HWAVEOUT handle;
	sjme_scritchaudio_stream stream;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;
	
	/* Closing our only stream? */
	if (inConn->type == SJME_SCRITCHAUDIO_CONN_STREAM && explicit)
	{
		stream = (sjme_scritchaudio_stream)inConn;
		
		/* Is the handle valid? */
		handle = stream->data.handle;
		if (handle != NULL)
		{
			/* Destroy. */
			stream->data.handle = NULL;
			
			/* Close the handle. */
			waveOutClose(handle);
		}
	}

	/* WinMM does not care about any other peers. */
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
	sjme_list(sjme_scritchaudio_source)* sources;
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
	sjme_errorCode error;
	MMRESULT mmResult;
	HWAVEOUT hWaveOut;
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_XP)
	WAVEFORMATEXTENSIBLE format;
#else
	struct
	{
		WAVEFORMATEX Format;
	} format;
#endif

	if (inState == NULL || inOutStream == NULL || inName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If automatic, choose a format to use. */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		inFormat = SJME_SCRITCHAUDIO_FORMAT_INT_S32;
	if (inRate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		inRate = SJME_SCRITCHAUDIO_RATE_HZ_44100;
	if (inChannels == SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		inChannels = SJME_SCRITCHAUDIO_CHANNELS_STEREO;

	/* Fill in wave format. */
	memset(&format, 0, sizeof(format));
	format.Format.cbSize = sizeof(format);

	/* "00000003-0000-0010-8000-00aa00389b71" */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_FLOAT_F32)
	{
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_XP)
		format.Format.wFormatTag = WAVE_FORMAT_IEEE_FLOAT;
		sjme_scritchaudio_winmm_guid(&format.SubFormat,
			0x00000003, 0x0000, 0x0010,
			0x80, 0x00, 0x00, 0xaa,
			0x00, 0x38, 0x9b, 0x71);
#else
		return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
#endif
	}

	/* "00000001-0000-0010-8000-00aa00389b71" */
	else
	{
		format.Format.wFormatTag = WAVE_FORMAT_PCM;
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_XP)
		sjme_scritchaudio_winmm_guid(&format.SubFormat,
			0x00000001, 0x0000, 0x0010,
			0x80, 0x00, 0x00, 0xaa,
			0x00, 0x38, 0x9b, 0x71);
#endif
	}

	/* Samples. */
	format.Format.wBitsPerSample =
		sjme_scritchaudio_bytesPerSample[inFormat] * 8;
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_XP)
	format.Samples.wValidBitsPerSample = format.Format.wBitsPerSample;
#endif
	format.Format.nSamplesPerSec = inRate;

	/* Channels. */
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_XP)
	format.dwChannelMask = (1 << inChannels) - 1;
#endif
	format.Format.nChannels = inChannels;

	/* Buffer size and alignment. */
	format.Format.nBlockAlign = format.Format.nChannels *
		(format.Format.wBitsPerSample / 8);
	format.Format.nAvgBytesPerSec = format.Format.nSamplesPerSec *
		format.Format.nBlockAlign;

	/* Set stream info. */
	inOutStream->format = inFormat;
	inOutStream->rate = inRate;
	inOutStream->channels = inChannels;
	inOutStream->connection.noPeers = sjme_scritchaudio_winmm_peerNone;
	inOutStream->connection.peerDisconnect =
		sjme_scritchaudio_winmm_peerDisconnect;

	/* Calculate the render info. */
	hWaveOut = NULL;
	if (sjme_error_is(error = inState->intern->calcRenderInfo(
		inState, inOutStream, NULL, &inOutStream->data.renderInfo)))
		goto fail_any;

	/* Headers are this big. */
	inOutStream->data.headerSize = sizeof(WAVEHDR);

	/* Open the default device. */
	mmResult = waveOutOpen(&hWaveOut, WAVE_MAPPER, (WAVEFORMATEX*)&format,
		(DWORD_PTR)inState->impl->nativeCallback,
		(DWORD_PTR)inOutStream,
		WAVE_FORMAT_DIRECT | CALLBACK_FUNCTION | WAVE_ALLOWSYNC);
	if (mmResult != MMSYSERR_NOERROR || hWaveOut == NULL)
		return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("waveOutOpen: Success!");
#endif

	/* Set stream details. */
	inOutStream->data.handle = hWaveOut;

	/* Playback needs to be "resumed" */
	waveOutRestart(hWaveOut);

	/* Return the resultant stream. */
	return SJME_ERROR_NONE;

fail_allocBuf:
fail_any:
	if (hWaveOut != NULL)
		waveOutClose(hWaveOut);

	return sjme_error_default(error);
}
