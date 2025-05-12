/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"

/**
 * Software Mixer implementation functions.
 *
 * @since 2025/05/10
 */
static const sjme_scritchaudio_implFunctions
	sjme_scritchaudio_softmixFunctions =
{
	.apiInit = sjme_scritchaudio_softmix_apiInit,
	.queryMidiPorts = sjme_scritchaudio_softmix_queryMidiPorts,
	.sourceAttach = sjme_scritchaudio_softmix_sourceAttach,
	.streamCreate = sjme_scritchaudio_softmix_streamCreate,
};

sjme_errorCode sjme_scritchaudio_softmix_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
