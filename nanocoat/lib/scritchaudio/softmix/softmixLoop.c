/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchaudio/softmix/softmixIntern.h"

static sjme_errorCode sjme_scritchaudio_softmix_streamMix(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInValue sjme_jlong clock,
	sjme_attrInValue sjme_jint expected48KHzSamples)
{
	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_softmix_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInValue sjme_jlong clock,
	sjme_attrInValue sjme_jint expected48KHzSamples)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_stream stream;
	sjme_list_sjme_scritchaudio_stream* streams;
	sjme_scritchaudio_rate bestRate;
	sjme_scritchaudio_format bestFormat;
	sjme_scritchaudio_channels bestChannels;
	sjme_jint i, n, rawBufferLen;
	sjme_pointer rawBuffer;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Copy the clock directly. */
	memmove(&wrappedState->clock.clock, &clock,
		sizeof(inState->clock.clock));

	/* Grab state lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&inState->lock)))
		return sjme_error_default(error);

	/* Reset format. */
	bestRate = SJME_SCRITCHAUDIO_RATE_HZ_8000;
	bestFormat = SJME_SCRITCHAUDIO_FORMAT_BYTE_U8;
	bestChannels = SJME_SCRITCHAUDIO_CHANNELS_MONO;
	
	/* Get streams to render. */
	streams = inState->streams;
	if (streams == NULL)
		goto skip_noStreams;

	/* Determine the best rate, format, and channels to use. */
	for (i = 0, n = streams->length; i < n; i++)
	{
		/* Skip empty spots. */
		stream = streams->elements[i];
		if (stream == NULL)
			continue;

		/* Better? */
		if (stream->rate > bestRate)
			bestRate = stream->rate;
		if (stream->format > bestFormat)
			bestFormat = stream->format;
		if (stream->channels > bestChannels)
			bestChannels = stream->channels;
	}

	/* Allocate audio buffer to render into. */
	rawBufferLen = 1234;
	rawBuffer = sjme_alloca(rawBufferLen);
	if (rawBuffer == NULL)
		goto skip_noStreams;
	memset(rawBuffer, 0, rawBufferLen);
	
	/* Go through and render all streams. */
	for (i = 0, n = streams->length; i < n; i++)
	{
		/* Skip empty spots. */
		stream = streams->elements[i];
		if (stream == NULL)
			continue;
		
		/* Grab stream lock. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&stream->connection.lock)))
			return sjme_error_default(error);

		/* Mix audio for the stream. */
		if (sjme_error_is(error = sjme_scritchaudio_softmix_streamMix(
			inState, stream, clock,	expected48KHzSamples)))
		{
			/* Release lock before failing. */
			sjme_thread_spinLockRelease(&stream->connection.lock, NULL);
			return sjme_error_default(error);
		}
		
		/* Release stream lock. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			&stream->connection.lock, NULL)))
			return sjme_error_default(error);
	}

	/* Release state lock. */
skip_noStreams:
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&inState->lock,
		NULL)))
		return sjme_error_default(error);

	/* Forward directly to the native iterator, skipping any API code as */
	/* most everything was already calculated. */
	if (wrappedState->impl->loopIterate != NULL)
		return wrappedState->impl->loopIterate(wrappedState, clock,
			expected48KHzSamples);
	
	return SJME_ERROR_NONE;
}
