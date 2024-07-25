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
#include "lib/scritchui/scritchui.h"

static sjme_errorCode sjme_scritchui_fb_choiceRefresh(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent =
		inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Request repaint for choice update. */
	return wrappedState->api->componentRepaint(wrappedState,
		wrappedComponent,
		0, 0, INT32_MAX, INT32_MAX);
}

sjme_errorCode sjme_scritchui_fb_choiceItemInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex)
{
	return sjme_scritchui_fb_choiceRefresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex)
{
	return sjme_scritchui_fb_choiceRefresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemSetEnabled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled)
{
	return sjme_scritchui_fb_choiceRefresh(inState, inComponent);
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
	return sjme_scritchui_fb_choiceRefresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemSetSelected(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected)
{
	return sjme_scritchui_fb_choiceRefresh(inState, inComponent);
}

sjme_errorCode sjme_scritchui_fb_choiceItemSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString)
{
	return sjme_scritchui_fb_choiceRefresh(inState, inComponent);
}
