/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/scritchaudioIntern.h"
#include "lib/scritchaudio/oss/ossIntern.h"

sjme_errorCode sjme_scritchaudio_oss_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing. */
	sjme_message("OSS Tick.");
	return SJME_ERROR_NONE;
}
