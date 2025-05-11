/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/oss/ossIntern.h"

/**
 * OSS implementation functions.
 *
 * @since 2025/05/10
 */
static const sjme_scritchaudio_implFunctions sjme_scritchaudio_ossFunctions =
{
	.queryMidiPorts = sjme_scritchaudio_oss_queryMidiPorts,
	.sourceAttach = sjme_scritchaudio_oss_sourceAttach,
	.streamCreate = sjme_scritchaudio_oss_streamCreate,
};

sjme_errorCode SJME_SCRITCHAUDIO_DYLIB_SYMBOL_DECLARE(oss)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd)
{
	if (inPool == NULL || outState == NULL || initFrontEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
