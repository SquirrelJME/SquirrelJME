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
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_renderInfo newInfo;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Copy the clock directly. */
	memmove(&wrappedState->clock.clock, &inState->clock.clock,
		sizeof(inState->clock.clock));

	/* Go directly to the intern handler. */
	memset(&newInfo, 0, sizeof(newInfo));
	newInfo.parent = renderInfo;
	return wrappedState->intern->loopIterate(wrappedState,
		inStream->data.wrapped, &newInfo);
}
