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

static sjme_attrOptimize sjme_errorCode sjme_scritchaudio_softmix_renderSource(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_source inSource,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* sourceInfo,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* destInfo,
	sjme_attrInNotNull sjme_scritchaudio_buffer* destBuf)
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
	memset(sourceBuf, 0, bufSize);

	/* Call source render function. */
	if (sjme_error_is(error = inSource->renderFunc(inState, inSource,
		sourceInfo, sourceBuf)))
		return sjme_error_default(error);

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
			goto fail_any;

		/* Calculate the rate scale. */
		if (sourceInfo.rate != destInfo->rate)
			sourceInfo.fromIncr = sjme_fixed_div(
				sjme_fixed_hi(sourceInfo.rate / 100),
				sjme_fixed_hi(destInfo->rate / 100));
		else
			sourceInfo.fromIncr = SJME_FIXED_ONE;
		destInfo->fromIncr = sourceInfo.fromIncr;

		/* The destination is always one. */
		sourceInfo.toIncr = SJME_FIXED_ONE;
		destInfo->toIncr = sourceInfo.toIncr;

		/* Forward render. */
		if (sjme_error_is(error = sjme_scritchaudio_softmix_renderSource(
			sourceState, source, &sourceInfo,
			destInfo, destBuf)))
			goto fail_any;

		/* Render and mix in the next source. */
		continue;
fail_any:
		anyError = sjme_error_default(error);
	}

	/* Success? */
	return anyError;
}

static sjme_errorCode sjme_scritchaudio_softmix_peerNone(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_connection wrappedConn;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Is this a wrapped connection we know about? */
	wrappedConn = NULL;
	if (inConn->type == SJME_SCRITCHAUDIO_CONN_STREAM)
		wrappedConn = SJME_AS_AUDIO_CONN(
			SJME_AS_AUDIO_STREAM(inConn)->data.wrapped);
	else if (inConn->type == SJME_SCRITCHAUDIO_CONN_SOURCE)
		wrappedConn = SJME_AS_AUDIO_CONN(
			SJME_AS_AUDIO_SOURCE(inConn)->data.wrapped);

	/* Do we know about this connection type? */
	if (wrappedConn != NULL)
	{
		/* Handle disconnect if explicit, otherwise soft dispatch */
		if (explicit)
			return wrappedState->api->disconnect(wrappedState, wrappedConn);
		return wrappedState->intern->peerNoneDispatch(wrappedState,
			wrappedConn, SJME_JNI_FALSE);
	}

	/* Otherwise, do nothing. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_softmix_peerDisconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_connection wrappedConn;
	sjme_scritchaudio_connection wrappedPeer;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState != inConn->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Is this a wrapped connection we know about? */
	wrappedConn = NULL;
	if (inConn->type == SJME_SCRITCHAUDIO_CONN_STREAM)
		wrappedConn = SJME_AS_AUDIO_CONN(
			SJME_AS_AUDIO_STREAM(inConn)->data.wrapped);
	else if (inConn->type == SJME_SCRITCHAUDIO_CONN_SOURCE)
		wrappedConn = SJME_AS_AUDIO_CONN(
			SJME_AS_AUDIO_SOURCE(inConn)->data.wrapped);
	
	/* Is this a wrapped peer we know about? */
	wrappedPeer = NULL;
	if (inPeer->type == SJME_SCRITCHAUDIO_CONN_STREAM)
		wrappedPeer = SJME_AS_AUDIO_CONN(
			SJME_AS_AUDIO_STREAM(inPeer)->data.wrapped);
	else if (inPeer->type == SJME_SCRITCHAUDIO_CONN_SOURCE)
		wrappedPeer = SJME_AS_AUDIO_CONN(
			SJME_AS_AUDIO_SOURCE(inPeer)->data.wrapped);

	/* Do we know about this connection type? */
	/* Forward disconnect signal to the lower level. */
	if (wrappedConn != NULL && wrappedPeer != NULL)
		if (wrappedConn->peerDisconnect != NULL)
			return wrappedConn->peerDisconnect(wrappedState,
				wrappedConn, wrappedPeer, explicit);

	/* Otherwise, do nothing. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_softmix_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_source wrapped;
	
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
	sjme_scritchaudio wrappedState;
	sjme_errorCode error;
	sjme_scritchaudio_stream wrapped;
	sjme_scritchaudio_format origFormat;
	sjme_scritchaudio_rate origRate;
	sjme_scritchaudio_channels origChannels;
	sjme_scritchaudio_source wrappedSource;
	
	if (inState == NULL || inOutStream == NULL || inName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* If automatic, choose a format to use. */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		inFormat = SJME_SCRITCHAUDIO_FORMAT_INT_S32;
	if (inRate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		inRate = SJME_SCRITCHAUDIO_RATE_HZ_44100;
	if (inChannels == SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		inChannels = SJME_SCRITCHAUDIO_CHANNELS_STEREO;

	/* Remember the original values, used for audio conversion. */
	origFormat = inFormat;
	origRate = inRate;
	origChannels = inChannels;

	/* Fallback to less precise formats. */
	wrapped = NULL;
	while (wrapped == NULL)
	{
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
		/* Debug. */
		sjme_message("streamCreate(%d, %d, %d)",
			inFormat, inRate, inChannels);
#endif
		
		/* Try to use the requested format. */
		if (sjme_error_is(error = wrappedState->intern->streamCreate(
			wrappedState, &wrapped, inName, inFormat, inRate, inChannels)) ||
			wrapped == NULL)
		{
			/* Only check against unsupported format. */
			if (error != SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT)
				return sjme_error_default(error);
			
			/* Reduce the rate. */
			if (sjme_error_is(error = inState->intern->fallbackNext(
				inState, origFormat, origRate, origChannels,
				&inFormat, &inRate, &inChannels)))
				return sjme_error_default(error);
		}
	}

	/* Set stream details. */
	/* Note that if the stream needs to be wrapped with a format conversion */
	/* that is handled in the renderer by comparing the wrapped format */
	/* with the renderer format. If a renderer happens to have the same */
	/* format, then we do no conversion. */
	inOutStream->data.wrapped = wrapped;
	inOutStream->connection.noPeers = sjme_scritchaudio_softmix_peerNone;
	inOutStream->connection.peerDisconnect =
		sjme_scritchaudio_softmix_peerDisconnect;
	
	/* Setup underlying source stream to render mixed audio. */
	wrappedSource = NULL;
	if (sjme_error_is(error = wrappedState->api->sourceAttach(wrappedState,
		wrapped, &wrappedSource,
		sjme_scritchaudio_softmix_render, inFormat, inRate, inChannels,
		NULL)) || wrapped == NULL)
		goto fail_subSource;

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_allocResult:
fail_subSource:
	if (wrapped != NULL)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	return sjme_error_default(error);
}
