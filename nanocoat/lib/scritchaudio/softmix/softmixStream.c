/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"

/** Fallback for audio formats. */
static const sjme_scritchaudio_format
	sjme_scritchaudio_formatFallback[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS] =
{
	SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS,
	SJME_SCRITCHAUDIO_FORMAT_BYTE_U8,
	SJME_SCRITCHAUDIO_FORMAT_BYTE_U8,
	SJME_SCRITCHAUDIO_FORMAT_BYTE_U8,
	SJME_SCRITCHAUDIO_FORMAT_SHORT_S16,
	SJME_SCRITCHAUDIO_FORMAT_INT_S24,
	SJME_SCRITCHAUDIO_FORMAT_INT_S32,
};

sjme_errorCode sjme_scritchaudio_softmix_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInValue sjme_jboolean attach,
	sjme_attrInNotNull sjme_scritchaudio_source source)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	sjme_scritchaudio_stream result;
	sjme_scritchaudio_format origFormat;
	sjme_scritchaudio_rate origRate;
	sjme_scritchaudio_channels origChannels;
	
	if (inState == NULL || outStream == NULL || inName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inFormat != SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC &&
		(inFormat < 0 || inFormat >= SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS))
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inRate != SJME_SCRITCHAUDIO_RATE_AUTOMATIC &&
		(inRate <= 0))
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inChannels != SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC &&
		(inChannels <= 0))
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Try to use the requested format. */
	result = NULL;
	if (sjme_error_is(error = wrappedState->impl->streamCreate(
		wrappedState, &result, inName, inFormat, inRate, inChannels)) ||
		result == NULL)
	{
		/* Only check against unsupported format. */
		if (error != SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT)
			return sjme_error_default(error);
	}
	
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
	while (result == NULL)
	{
		/* Debug. */
		sjme_message("streamCreate(%d, %d, %d)",
			inFormat, inRate, inChannels);
		
		/* Try to use the requested format. */
		if (sjme_error_is(error = wrappedState->impl->streamCreate(
			wrappedState, &result, inName, inFormat, inRate, inChannels)) ||
			result == NULL)
		{
			/* Only check against unsupported format. */
			if (error != SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT)
				return sjme_error_default(error);

			/* Use a fallback audio format. */
			inFormat = sjme_scritchaudio_formatFallback[inFormat];
			if (inFormat == SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS)
			{
				/* Maybe the rate is too high? */
				inRate /= 2;
				if (inRate < SJME_SCRITCHAUDIO_RATE_HZ_8000)
				{
					/* Maybe the number of channels is not supported? */
					inChannels /= 2;
					if (inChannels <= 0)
						return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;

					/* We reduced the channel count, so revert the rate. */
					inRate = origRate;
				}

				/* We reduced the rate, so revert the format. */
				inFormat = origFormat;
			}
		}
	}

	/* Do we need to convert the stream? */
	if (origFormat != inFormat || origRate != inRate ||
		origChannels != inChannels)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;
}
