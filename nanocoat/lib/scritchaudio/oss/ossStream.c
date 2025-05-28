/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_LINUX)
	#include <sys/ioctl.h>
#else
	#include <stropts.h>
#endif

#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "lib/scritchaudio/oss/ossIntern.h"

/** OSS Audio formats. */
static const int
	sjme_scritchaudio_oss_format[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS] =
{
	AFMT_U8,
	AFMT_A_LAW,
	AFMT_MU_LAW,
	AFMT_S16_NE,
#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN) && \
	defined(AFMT_S32_LE)
	AFMT_S32_LE,
#elif defined(SJME_CONFIG_HAS_BIG_ENDIAN) && \
	defined(AFMT_S32_BE)
	AFMT_S32_BE,
#else
	-1,
#endif
	-1,
	-1,
};

static sjme_errorCode sjme_scritchaudio_oss_peerNone(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH - 7;

	/* OSS does not care about any peers. */
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("oss_peerNone(%p, %p, %d)",
		inState, inConn, explicit);
#endif
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
		return SJME_ERROR_AUDIO_STATE_MISMATCH - 7;

	/* OSS does not care about any peers. */
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("oss_peerDisconnect(%p, %p, %p, %d)",
		inState, inConn, inPeer, explicit);
#endif
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_oss_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource)
{
	if (inState == NULL || inStream == NULL || inSource == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Just set peer disconnection functions, despite not doing much. */
	inSource->connection.noPeers = sjme_scritchaudio_oss_peerNone;
	inSource->connection.peerDisconnect =
		sjme_scritchaudio_oss_peerDisconnect;

	/* OSS is completely manually polled, so nothing is ever registered. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_oss_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
	int fd, ossFormat, ossChannels, ossRate;
	sjme_scritchaudio_stream result;
	sjme_errorCode error;
	
	if (inState == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Unsupported format? */
	if (sjme_scritchaudio_oss_format[inFormat] == -1)
		return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;

	/* If automatic, choose a format to use. */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		inFormat = SJME_SCRITCHAUDIO_FORMAT_BYTE_U8;
	if (inRate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		inRate = SJME_SCRITCHAUDIO_RATE_HZ_44100;
	if (inChannels == SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		inChannels = SJME_SCRITCHAUDIO_CHANNELS_STEREO;

	/* Try to open the DSP device. */
	fd = open(SJME_SCRITCHAUDIO_OSS_DSP, O_RDONLY, 0);
	if (fd == -1)
		return SJME_ERROR_HEADLESS_AUDIO;

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

	/* Set sample rate. */
	ossRate = inRate;
	if (ioctl(fd, SNDCTL_DSP_SPEED, &ossRate) == -1)
	{
		error = SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
		goto fail_format;
	}

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*result), (sjme_pointer*)&result)) || result == NULL)
		goto fail_allocResult;

	/* Set stream details. */
	result->connection.inState = inState;
	result->connection.type = SJME_SCRITCHAUDIO_CONN_STREAM;
	result->format = inFormat;
	result->rate = inRate;
	result->channels = inChannels;
	result->data.fd = fd;
	result->connection.noPeers = sjme_scritchaudio_oss_peerNone;
	result->connection.peerDisconnect = sjme_scritchaudio_oss_peerDisconnect;

	/* Return the resultant stream. */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
fail_format:
	close(fd);
	return sjme_error_default(error);
}
