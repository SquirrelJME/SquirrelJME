/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_SYS_IOCTL_H)
	#include <sys/ioctl.h>
#endif

#if defined(SJME_CONFIG_HAS_STROPTS_H)
	#include <stropts.h>
#endif

#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "lib/scritchaudio/oss/ossIntern.h"

#if !defined(AFMT_FLOAT)
	/** Floating point format. */
	#define AFMT_FLOAT 0x4000
#endif

#if !defined(AFMT_S32_LE)
	/** 32-bit Little Endian. */
	#define AFMT_S32_LE 0x1000
#endif

#if !defined(AFMT_S32_BE)
	/** 32-bit Bit Endian. */
	#define AFMT_S32_BE 0x2000
#endif

/** OSS Audio formats. */
static const int
	sjme_scritchaudio_oss_format[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS] =
{
	AFMT_U8,
	AFMT_S16_NE,
	
#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
	AFMT_S32_LE,
#else
	AFMT_S32_BE,
#endif
	
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	AFMT_FLOAT,
#else
	-1,
#endif
};

static sjme_errorCode sjme_scritchaudio_oss_peerNone(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_scritchaudio_stream stream;
	int fd;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;
	
	/* Closing our only stream? */
	if (inConn->type == SJME_SCRITCHAUDIO_CONN_STREAM && explicit)
	{
		stream = (sjme_scritchaudio_stream)inConn;
		
		/* Is the file descriptor valid? */
		fd = stream->data.fd;
		if (fd != -1)
		{
			/* Destroy. */
			stream->data.fd = -1;
			
			/* Close the file. */
			close(fd);
		}
	}

	/* OSS does not care about any other peers. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_oss_peerDisconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer,
	sjme_attrInValue sjme_jboolean explicit)
{
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

	/* OSS does not care about any peers. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_oss_sourceAttach(
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

	/* There can only be a single OSS source at a time. */
	sources = inStream->sources;
	if (sources != NULL)
		for (i = 0, n = sources->length; i < n; i++)
			if (sources->elements[i] != NULL)
				return SJME_ERROR_AUDIO_NO_RESOURCES;

	/* Just set peer disconnection functions, despite not doing much. */
	inSource->connection.noPeers = sjme_scritchaudio_oss_peerNone;
	inSource->connection.peerDisconnect =
		sjme_scritchaudio_oss_peerDisconnect;

	/* OSS is completely manually polled, so nothing is ever registered. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_oss_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_scritchaudio_stream inOutStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
	int fd;
	volatile int ossFormat, ossChannels, ossRate, actual;
	sjme_errorCode error;
	
	if (inState == NULL || inOutStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Unsupported format? */
	if (sjme_scritchaudio_oss_format[inFormat] == -1)
		return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;

	/* If automatic, choose a format to use. */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		inFormat = SJME_SCRITCHAUDIO_FORMAT_INT_S32;
	if (inRate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		inRate = SJME_SCRITCHAUDIO_RATE_HZ_44100;
	if (inChannels == SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		inChannels = SJME_SCRITCHAUDIO_CHANNELS_STEREO;

	/* Try to open the DSP device. */
	fd = open(SJME_SCRITCHAUDIO_OSS_DSP, O_WRONLY | O_NONBLOCK, 0);
	if (fd == -1)
	{
		if (errno == EBUSY)
			return SJME_ERROR_AUDIO_NO_RESOURCES;
		return SJME_ERROR_HEADLESS_AUDIO;
	}

	/* Get hardware supported formats. */
	actual = 0;
	if (ioctl(fd, SNDCTL_DSP_GETFMTS, &actual) == -1)
	{
		error = SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
		goto fail_format;
	}

	/* Is the format not supported by the actual hardware? */
	if ((actual & sjme_scritchaudio_oss_format[inFormat]) == 0)
	{
		error = SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
		goto fail_format;
	}
	
	/* Set new OSS format. */
	ossFormat = sjme_scritchaudio_oss_format[inFormat];
	if (ioctl(fd, SNDCTL_DSP_SETFMT, &ossFormat) == -1)
	{
		error = SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
		goto fail_format;
	}

	/* Set number of channels. */
	ossChannels = inChannels;
	if (ioctl(fd, SNDCTL_DSP_CHANNELS, &ossChannels) == -1)
	{
		error = SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
		goto fail_format;
	}

	/* Set sample rate, must be set last! */
	ossRate = inRate;
	if (ioctl(fd, SNDCTL_DSP_SPEED, &ossRate) == -1)
	{
		error = SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
		goto fail_format;
	}

	/* Check final format. */
	if (ossChannels != inChannels || ossRate != inRate ||
		ossFormat != sjme_scritchaudio_oss_format[inFormat])
	{
		error = SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
		goto fail_format;
	}

	/* Set stream details. */
	inOutStream->format = inFormat;
	inOutStream->rate = inRate;
	inOutStream->channels = inChannels;
	inOutStream->data.fd = fd;
	inOutStream->connection.noPeers = sjme_scritchaudio_oss_peerNone;
	inOutStream->connection.peerDisconnect =
		sjme_scritchaudio_oss_peerDisconnect;

	/* Return the resultant stream. */
	return SJME_ERROR_NONE;

fail_format:
	close(fd);
	return sjme_error_default(error);
}
