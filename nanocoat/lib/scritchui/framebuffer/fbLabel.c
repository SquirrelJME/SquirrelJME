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

sjme_errorCode sjme_scritchui_fb_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiCommon wrappedCommon;
	
	if (inState == NULL || inCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedCommon = inCommon->handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Just forward to the wrapper. */
	return wrappedState->apiInThread->labelSetString(wrappedState,
		wrappedCommon, inString);
}
