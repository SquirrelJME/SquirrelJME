/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/framebuffer/fbIntern.h"
#include "lib/scritchui/scritchui.h"

sjme_errorCode sjme_scritchui_fb_choiceItemInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Perform refresh. */
	return inState->implIntern->refresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Perform refresh. */
	return inState->implIntern->refresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemSetEnabled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Perform refresh. */
	return inState->implIntern->refresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemSetImage(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inRgbDataLen,
	sjme_attrInPositiveNonZero sjme_jint inRgbScanLen,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Perform refresh. */
	return inState->implIntern->refresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemSetSelected(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Perform refresh. */
	return inState->implIntern->refresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Perform refresh. */
	return inState->implIntern->refresh(inState, inComponent);
}
