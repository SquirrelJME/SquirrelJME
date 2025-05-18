/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/scritchaudio.h"
#include "lib/scritchaudio/scritchaudioIntern.h"

sjme_errorCode sjme_scritchaudio_core_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInValue sjme_jboolean attach,
	sjme_attrInNotNull sjme_scritchaudio_source source)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_core_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
	if (inState == NULL || outStream == NULL || inName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_message("create(%d %d %d)", inFormat, inRate, inChannels);
	if (inFormat != SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC &&
		(inFormat < 0 || inFormat >= SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS))
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inRate != SJME_SCRITCHAUDIO_RATE_AUTOMATIC &&
		(inRate <= 0))
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inChannels != SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC &&
		(inChannels <= 0))
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
