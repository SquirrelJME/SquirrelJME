/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"

sjme_errorCode sjme_scritchaudio_softmix_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	sjme_scritchaudio wrappedState;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Finish off with the native iterator. */
	if (wrappedState->api->loopIterate != NULL)
		return wrappedState->api->loopIterate(wrappedState);
	return SJME_ERROR_NONE;
}
