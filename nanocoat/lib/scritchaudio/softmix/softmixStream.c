/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"

static sjme_errorCode sjme_scritchaudio_softmix_wrappedRender(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_source inSource)
{
	if (inState == NULL || inSource == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	
	/* Forward to wrapped. */
	wrapped = NULL;
	if (sjme_error_is(error = wrappedState->api->sourceAttach(wrappedState,
		inStream->data.wrapped, &wrapped,
		sjme_scritchaudio_softmix_wrappedRender, NULL)) || wrapped == NULL)
		return sjme_error_default(error);

	/* Initialize data. */
	inSource->connection.noPeers = sjme_scritchaudio_softmix_peerNone;
	inSource->connection.peerDisconnect =
		sjme_scritchaudio_softmix_peerDisconnect;
	inSource->data.wrapped = wrapped;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_softmix_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
	sjme_scritchaudio wrappedState;
	sjme_errorCode error;
	sjme_scritchaudio_stream wrapped, result;
	sjme_scritchaudio_format origFormat;
	sjme_scritchaudio_rate origRate;
	sjme_scritchaudio_channels origChannels;
	
	if (inState == NULL || outStream == NULL || inName == NULL)
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
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*result), (sjme_pointer*)&result)) || wrapped == NULL)
		goto fail_allocResult;

	/* Set stream details. */
	/* Note that if the stream needs to be wrapped with a format conversion */
	/* that is handled in the renderer by comparing the wrapped format */
	/* with the renderer format. If a renderer happens to have the same */
	/* format, then we do no conversion. */
	result->connection.inState = inState;
	result->connection.type = SJME_SCRITCHAUDIO_CONN_STREAM;
	result->format = origFormat;
	result->rate = origRate;
	result->channels = origChannels;
	result->data.wrapped = wrapped;
	result->connection.noPeers = sjme_scritchaudio_softmix_peerNone;
	result->connection.peerDisconnect =
		sjme_scritchaudio_softmix_peerDisconnect;

	/* We created a stream, so make sure the audio playback is faster. */
	sjme_atomic_sjme_jint_set(&inState->pollDelayMillis, 25);
	sjme_atomic_sjme_jint_set(&inState->pollDelayNanos, 0);

	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;
	
fail_allocResult:
	if (wrapped != NULL)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	return sjme_error_default(error);
}
