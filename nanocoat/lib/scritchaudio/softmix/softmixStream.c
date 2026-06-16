/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"
#include "sjme/fixed.h"
#include "sjme/util.h"

static sjme_errorCode sjme_attrOptimize sjme_scritchaudio_softmix_renderSource(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_source inSource,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* sourceInfo,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* destInfo,
	sjme_attrInNotNull sjme_scritchaudio_buffer* destBuf,
	sjme_attrInValue sjme_jboolean isFirst)
{
	sjme_errorCode error;
	sjme_jint bufSize;
	sjme_pointer sourceBuf;
	sjme_scritchaudio_softmix_mixer mixer;
	
	if (inState == NULL || inSource == NULL || sourceInfo == NULL)
		return SJME_ERROR_NONE;

	/* Allocate buffer to render to. */
	bufSize = sourceInfo->bufSize;
	sourceBuf = sjme_alloca(bufSize);
	if (sourceBuf == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* If the source format is unsigned, we need to actually set the proper */
	/* zero level, otherwise there will be clicks/pops. */
	if (sourceInfo->format == SJME_SCRITCHAUDIO_FORMAT_BYTE_U8)
		memset(sourceBuf, 0x80, bufSize);
	else
		memset(sourceBuf, 0, bufSize);

	/* Call source render function. */
	if (sjme_error_is(error = inSource->renderFunc(inState, inSource,
		sourceInfo, sourceBuf)))
		return sjme_error_default(error);
	
	/* If this is the first render, and it matches the native format, then */
	/* we do not actually need to perform mixing of any kind. */
	if (isFirst && sourceInfo->channels == destInfo->channels &&
		sourceInfo->format == destInfo->format &&
		sourceInfo->rate == destInfo->rate &&
		destInfo->bufSize == bufSize)
	{
		memmove(destBuf, sourceBuf, bufSize);
		return SJME_ERROR_NONE;
	}

	/* Get the mixer to use. */
	mixer = sjme_scritchaudio_softmix_mixers[sourceInfo->format]
		[destInfo->format];
	if (mixer == NULL)
	{
		sjme_todo("Mixing %d -> %d", sourceInfo->format, destInfo->format);
		return sjme_error_notImplemented((sourceInfo->format *
			SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS) + destInfo->format);
	}
	
	/* Mix audio into the target buffer. */
	return mixer(sourceInfo, sourceBuf, destInfo, destBuf);
}

static sjme_attrOptimize sjme_errorCode sjme_scritchaudio_softmix_render(
	sjme_attrInNotNull sjme_scritchaudio wrappedState,
	sjme_attrInNotNull sjme_scritchaudio_source wrappedSource,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* destInfo,
	sjme_attrInNotNull sjme_scritchaudio_buffer* destBuf)
{
	sjme_errorCode error, anyError;
	sjme_scritchaudio inState, sourceState;
	sjme_scritchaudio_stream inStream, wrappedStream, sourceStream;
	sjme_scritchaudio_source source;
	sjme_list(sjme_scritchaudio_source)* sources;
	sjme_scritchaudio_renderInfo sourceInfo;
	sjme_jboolean first;
	sjme_jint i, n;
	
	if (wrappedState == NULL || wrappedSource == NULL || destInfo == NULL ||
		destBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover the top level state. */
	inState = sjme_atomic_g(sjme_pointer, &wrappedState->topState);
	if (inState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Recover stream. */
	inStream = inState->stream;
	if (inStream == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Recover the wrapped stream. */
	wrappedStream = wrappedSource->inStream;
	if (wrappedStream == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Is there actually anything to render? */
	sources = inState->stream->sources;
	if (sources == NULL)
		return SJME_ERROR_NONE;

	/* Run through all the sources to render. */
	anyError = SJME_ERROR_NONE;
	first = SJME_JNI_TRUE;
	for (i = 0, n = sources->length; i < n; i++)
	{
		/* Skip blank slots. */
		source = sources->elements[i];
		if (source == NULL)
			continue;

		/* Extract source elements. */
		sourceState = source->inStream->connection.inState;
		sourceStream = source->inStream;

		/* Calculate the sub-render info. */
		memset(&sourceInfo, 0, sizeof(sourceInfo));
		if (sjme_error_is(error = inState->intern->calcRenderInfo(
			sourceState, sourceStream, source, &sourceInfo)))
		{
			/* Let audio process still. */
			anyError = sjme_error_default(error);
			first = SJME_JNI_FALSE;
			continue;
		}

		/* Calculate the rate scale. */
		/* Note if dest = 2 and src = 1, if dest / src = 2 then only one */
		/* channel will be mixed, if src / dest = 0.5 then both channels */
		/* will be mixed. */
		if (sourceInfo.rate != destInfo->rate ||
			sourceInfo.channels != destInfo->channels)
			sourceInfo.fromIncr = sjme_fixed_mul(sjme_fixed_div(
				sjme_fixed_hi(sourceInfo.rate / 100),
				sjme_fixed_hi(destInfo->rate / 100)),
				sjme_fixed_fraction(sourceInfo.channels,
					destInfo->channels));
		else
			sourceInfo.fromIncr = SJME_FIXED_ONE;
		destInfo->fromIncr = sourceInfo.fromIncr;

		/* The destination is always one, unless there are more channels. */
		/* Note if dest = 2 and src = 1, if dest / src = 2 then only one */
		/* speaker will have audio, if src / dest = 0.5 then both speakers */
		/* will have audio. */
		if (sourceInfo.channels != destInfo->channels)
			sourceInfo.toIncr = sjme_fixed_fraction(sourceInfo.channels,
				destInfo->channels);
		else
			sourceInfo.toIncr = SJME_FIXED_ONE;
		destInfo->toIncr = sourceInfo.toIncr;

		/* Forward render. */
		if (sjme_error_is(error = sjme_scritchaudio_softmix_renderSource(
			sourceState, source, &sourceInfo,
			destInfo, destBuf, first)))
		{
			/* Let audio process still. */
			anyError = sjme_error_default(error);
			first = SJME_JNI_FALSE;
			continue;
		}
		
		/* No longer first. */
		first = SJME_JNI_FALSE;

		/* Render and mix in the next source. */
		continue;
	}

	/* Success? */
	return anyError;
}

static sjme_errorCode sjme_scritchaudio_softmix_peerNone(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_stream underStream;
	sjme_scritchaudio_source underSource;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* If the stream is being closed, disconnect the underlying stream. */
	if (inConn->type == SJME_SCRITCHAUDIO_CONN_STREAM && explicit)
	{
		/* Close the underlying source. */
		underSource = inState->under.source;
		if (underSource != NULL)
		{
			/* Disconnect the source. */
			inState->under.source = NULL;
			if (sjme_error_is(error = wrappedState->api->disconnect(
				wrappedState, SJME_SAU_CAST_CONNECTION(underSource))))
				return sjme_error_default(error);
		}
		
		/* Remove the underlying stream. */
		underStream = inState->under.stream;
		if (underStream != NULL)
		{
			/* Disconnect the source. */
			inState->under.stream = NULL;
			if (sjme_error_is(error = wrappedState->api->disconnect(
				wrappedState, SJME_SAU_CAST_CONNECTION(underStream))))
				return sjme_error_default(error);
		}
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_softmix_peerDisconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_stream underStream;
	sjme_scritchaudio_source underSource;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;
	
	/* Nothing currently needs to be done here as when a peer disconnects */
	/* we should not try to change the underlying audio rate if any sound */
	/* is playing. */
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_softmix_underlay(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outUnderStream)
{
	sjme_errorCode error;
	sjme_jint i, n;
	sjme_list(sjme_scritchaudio_source)* sources;
	sjme_scritchaudio_source source;
	sjme_scritchaudio_format bestFormat, origFormat;
	sjme_scritchaudio_rate bestRate, origRate;
	sjme_scritchaudio_channels bestChannels, origChannels;
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_stream underStream;
	sjme_scritchaudio_source underSource;
	
	if (inState == NULL || outUnderStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover the wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Start with the worst format, it only gets better. */
	bestFormat = SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC;
	bestRate = SJME_SCRITCHAUDIO_RATE_AUTOMATIC;
	bestChannels = SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC;
	
	/* Do the input formats improve on the worst format? */
	if (inFormat != SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		bestFormat = sjme_max(bestFormat, inFormat);
	if (inRate != SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		bestRate = sjme_max(bestRate, inRate);
	if (inChannels != SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		bestChannels = sjme_max(bestChannels, inChannels);
	
	/* Does the stream specify a better format? */
	if (inState->stream != NULL)
	{
		/* Does this source use a better format? */
		if (inState->stream->format != SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
			bestFormat = sjme_max(bestFormat, inState->stream->format);
		if (inState->stream->rate != SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
			bestRate = sjme_max(bestRate, inState->stream->rate);
		if (inState->stream->channels != SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
			bestChannels = sjme_max(bestChannels, inState->stream->channels);
	}
	
	/* Determine the best format based on all the currently connected */
	/* streams, so we can choose a better format. */
	if (inState->stream != NULL && inState->stream->sources != NULL)
	{
		/* Go through each attached source. */
		sources = inState->stream->sources;
		for (i = 0, n = sources->length; i < n; i++)
		{
			source = sources->elements[i];
			if (source == NULL)
				continue;
			
			/* Does this source use a better format? */
			if (source->format != SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
				bestFormat = sjme_max(bestFormat, source->format);
			if (source->rate != SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
				bestRate = sjme_max(bestRate, source->rate);
			if (source->channels != SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
				bestChannels = sjme_max(bestChannels, source->channels);
		}
	}
	
	/* Does the underlying stream exist? */
	underStream = inState->under.stream;
	underSource = inState->under.source;
	if (underStream != NULL)
	{
		/* If it does, is the format not the best one desired? */
		/* Note if the best format is automatic, we do not want to just */
		/* change the underlying stream for no reason. */
		if (underSource == NULL ||
			(bestFormat != SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC &&
				underSource->format != bestFormat) ||
			(bestRate != SJME_SCRITCHAUDIO_RATE_AUTOMATIC &&
				underSource->rate != bestRate) ||
			(bestChannels != SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC &&
				underSource->channels != bestChannels))
		{
			/* Disconnect the source if there is one. */
			if (underSource != NULL)
			{
				/* Disconnect the source. */
				inState->under.source = NULL;
				if (sjme_error_is(error = wrappedState->api->disconnect(
					wrappedState, SJME_SAU_CAST_CONNECTION(underSource))))
					goto fail_disconnectSource;
				
				/* No longer connected. */
				underSource = NULL;
			}
			
			/* Disconnect the stream. */
			inState->under.stream = NULL;
			if (sjme_error_is(error = wrappedState->api->disconnect(
				wrappedState, SJME_SAU_CAST_CONNECTION(underStream))))
				goto fail_disconnectStream;
			
			/* Destroyed! */
			underStream = NULL;
		}
		
		/* If the underlying stream is still here, then it is the same */
		/* format and we need not reopen it. */
		if (underStream != NULL)
		{
			*outUnderStream = underStream;
			return SJME_ERROR_NONE;
		}
	}
	
	/* Does the underlying system support this exact best case? */
	underStream = NULL;
	if (sjme_error_is(error = wrappedState->intern->streamCreate(wrappedState,
		&underStream, "SquirrelJMEScritchAudio",
		bestFormat, bestRate, bestChannels)))
	{
		/* Unsupported format is okay, we will just try again. */
		if (error != SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT)
			goto fail_underlayCreate;
		
		/* Since we really have no idea what the sound card supports */
		/* natively, we really only have the choice of opening a stream */
		/* with the almost best options possible. Of course, this */
		/* ultimately could fail in the end. */
		bestFormat = sjme_max(bestFormat, SJME_SCRITCHAUDIO_FORMAT_INT_S32);
		bestRate = sjme_max(bestRate, SJME_SCRITCHAUDIO_RATE_HZ_44100);
		bestChannels = sjme_max(bestChannels,
			SJME_SCRITCHAUDIO_CHANNELS_STEREO);
	}

	/* Store these for downgrading. */
	origFormat = bestFormat;
	origRate = bestRate;
	origChannels = bestChannels;
	
	/* Try worse and worse formats. */
	while (underStream == NULL)
		if (sjme_error_is(error = wrappedState->intern->streamCreate(
			wrappedState, &underStream, "SquirrelJMEScritchAudio",
			bestFormat, bestRate, bestChannels)))
		{
			/* Some other error?. */
			if (error != SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT)
				goto fail_underlayCreate;

			/* Notice. */
			sjme_messageB("Failed underlay(%d, %d, %d)",
				bestFormat, bestRate, bestChannels);
			
			/* Reduce the rate. */
			if (sjme_error_is(error = inState->intern->fallbackNext(
				inState, origFormat, origRate, origChannels,
				&bestFormat, &bestRate, &bestChannels)))
				goto fail_rateReduce;
		}
	
	/* Never got a stream? */
	if (underStream == NULL)
	{
		error = SJME_ERROR_AUDIO_NO_RESOURCES;
		goto fail_noStream;
	}
	
	/* Directly attach to the source for rendering. */
	if (sjme_error_is(error = wrappedState->api->sourceAttach(
		wrappedState, underStream, &underSource,
		sjme_scritchaudio_softmix_render,
		bestFormat, bestRate, bestChannels, NULL)))
		goto fail_attach;
	
	/* This stream and source are now valid! */
	inState->under.stream = underStream;
	inState->under.source = underSource;
	
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("softmixUnderlay(%p): Attached with %d %d %d!",
		inState, bestFormat, bestRate, bestChannels);
#endif
	
	/* Return the created stream. */
	*outUnderStream = underStream;
	return SJME_ERROR_NONE;
	
fail_attach:
	/* Since we could not attach, disconnect the stream so we do not leave */
	/* the sound card open. */
	if (underStream != NULL)
		wrappedState->api->disconnect(wrappedState,
			SJME_SAU_CAST_CONNECTION(underStream));
fail_noStream:
fail_disconnectSource:
fail_disconnectStream:
fail_underlayCreate:
fail_rateReduce:
	
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("softmixUnderlay(%p): Failed with %d!",
		(void*)inState, error);
#endif
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchaudio_softmix_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_stream underStream;
	
	if (inState == NULL || inStream == NULL || inSource == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Initialize data. */
	/* Note that the source is not forwarded as we do mixing ourselves when */
	/* it is requested. */
	inSource->connection.noPeers = sjme_scritchaudio_softmix_peerNone;
	inSource->connection.peerDisconnect =
		sjme_scritchaudio_softmix_peerDisconnect;
	
	/* Make sure the underlying stream is properly primed, note that */
	/* the source might want a better format than what is currently used. */
	underStream = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_softmix_underlay(
		inState, inSource->format, inSource->rate, inSource->channels,
		&underStream)) || underStream == NULL)
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_softmix_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_scritchaudio_stream inOutStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_stream underStream;
	
	if (inState == NULL || inOutStream == NULL || inName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if !defined(SJME_CONFIG_HAS_FLOAT_HARD)
	/* No floating point support means no floating point audio. */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_FLOAT_F32)
		return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;
#endif
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Set peer callbacks. */
	inOutStream->connection.noPeers = sjme_scritchaudio_softmix_peerNone;
	inOutStream->connection.peerDisconnect =
		sjme_scritchaudio_softmix_peerDisconnect;
	
	/* Make sure the underlying stream is properly primed. */
	underStream = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_softmix_underlay(
		inState, inFormat, inRate, inChannels,
		&underStream)) || underStream == NULL)
		return sjme_error_default(error);
	
	/* Nothing else needs to be done as this is all virtualized. */
	return SJME_ERROR_NONE;
}
