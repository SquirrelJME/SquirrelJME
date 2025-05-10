/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/oss/ossIntern.h"

sjme_errorCode sjme_scritchaudio_oss_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInValue sjme_jboolean attach,
	sjme_attrInNotNull sjme_scritchaudio_source source)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_oss_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
	if (inState == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
