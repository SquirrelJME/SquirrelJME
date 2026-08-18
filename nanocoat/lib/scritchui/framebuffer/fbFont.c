/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_fb_fontScanSystem(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_jint* outCount)
{
	sjme_scritchui wrappedState;
	
	if (inState == NULL || outCount == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If there is a system implementation, use it. */
	wrappedState = inState->wrappedState;
	if (wrappedState->impl->fontScanSystem != NULL)
		return wrappedState->impl->fontScanSystem(wrappedState, outCount);
	
	/* There was no internal implementation, so return no fonts. */
	*outCount = 0;
	return SJME_ERROR_NONE;
}
