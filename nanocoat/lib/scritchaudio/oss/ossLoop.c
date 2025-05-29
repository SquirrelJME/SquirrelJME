/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <unistd.h>
#include <sys/ioctl.h>

#include "lib/scritchaudio/scritchaudioIntern.h"
#include "lib/scritchaudio/oss/ossIntern.h"

sjme_errorCode sjme_scritchaudio_oss_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInValue sjme_jlong clock,
	sjme_attrInValue sjme_jint expected48KHzSamples,
	sjme_attrInValue sjme_jint expected44KHzSamples)
{
	int fd, trigger;
	sjme_scritchaudio_stream stream;
	sjme_jint freqAt, samples, bytesPerSample, bufSize;
	sjme_pointer buf;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover stream, ignore if not ready yet. */
	stream = inState->stream;
	if (stream == NULL)
		return SJME_ERROR_NONE;
	
#if 1 || defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("OSS Tick: %lld", inState->clock.clock.full / 1000000);
#endif

	/* Recover the file descriptor. */
	fd = stream->data.fd;

	/* Which base samples do we start at? */
	if ((stream->rate % 8000) == 0)
	{
		freqAt = 48000;
		samples = expected48KHzSamples;
	}
	else
	{
		freqAt = 44100;
		samples = expected44KHzSamples;
	}
	
	/* Trim down sample count until we match the given set. */
	while (freqAt > stream->rate)
	{
		samples >>= 2;
		freqAt >>= 2;
	}

	/* Bytes per sample? */
	bytesPerSample = sjme_scritchaudio_bytesPerSample[stream->format];

	/* Allocate sample buffer */
	bufSize = bytesPerSample * stream->channels * samples;
	buf = sjme_alloca(bufSize);
	if (buf == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Render the buffer. */
	
	/* Disable playback. */
	trigger = 0;
	ioctl(fd, SNDCTL_DSP_SETTRIGGER, &trigger);

	/* Write the buffer data. */
	write(fd, buf, bufSize);

	/* Resume playback. */
	trigger = PCM_ENABLE_OUTPUT;
	ioctl(fd, SNDCTL_DSP_SETTRIGGER, &trigger);
	ioctl(fd, SNDCTL_DSP_POST, NULL);

	/* Nothing. */
	return SJME_ERROR_NONE;
}
