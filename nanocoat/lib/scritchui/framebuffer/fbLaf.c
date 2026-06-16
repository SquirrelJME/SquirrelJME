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

sjme_errorCode sjme_scritchui_fb_lafDpiProject(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrInValue sjme_jboolean toBase,
	sjme_attrInNullable sjme_jint* inOutX,
	sjme_attrInNullable sjme_jint* inOutY,
	sjme_attrInNullable sjme_jint* inOutW,
	sjme_attrInNullable sjme_jint* inOutH)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	
	if (inState == NULL || (inOutX == NULL && inOutY == NULL &&
		inOutW == NULL && inOutH == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Is there a wrapped component? */
	wrappedComponent = NULL;
	if (inContext != NULL)
		wrappedComponent = inContext->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward to the native implementation, since it knows its own scale. */
	return wrappedState->apiInThread->lafDpiProject(wrappedState,
		wrappedComponent, toBase, inOutX, inOutY, inOutW, inOutH);
}

sjme_errorCode sjme_scritchui_fb_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	
	if (inState == NULL || outRGB == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Is there a wrapped component? */
	wrappedComponent = NULL;
	if (inContext != NULL)
		wrappedComponent = inContext->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Just forward it. */
	return wrappedState->apiInThread->lafElementColor(wrappedState,
		wrappedComponent, outRGB, elementColor);
}

sjme_errorCode sjme_scritchui_fb_lafMetric(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInValue sjme_scritchui_lafMetricType metricType)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;

	if (inState == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Is there a wrapped component? */
	wrappedComponent = NULL;
	if (inContext != NULL)
		wrappedComponent = inContext->common.handle[SJME_SUI_FB_H_WRAPPED];

	/* Just forward it. */
	return wrappedState->apiInThread->lafMetric(wrappedState,
		wrappedComponent, outValue, metricType);
}
