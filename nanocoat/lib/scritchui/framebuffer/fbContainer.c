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

sjme_errorCode sjme_scritchui_fb_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedInContainer;
	sjme_scritchui_uiComponent wrappedAddComponent;
	
	if (inState == NULL || inContainer == NULL || addComponent == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedInContainer = inContainer->common.handle;
	wrappedAddComponent = addComponent->common.handle;
	
	/* Forward call. */
	return wrappedState->api->containerAdd(wrappedState,
		wrappedInContainer, wrappedAddComponent);
}

sjme_errorCode sjme_scritchui_fb_containerRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedInContainer;
	sjme_scritchui_uiComponent wrappedRemoveComponent;
	
	if (inState == NULL || inContainer == NULL || removeComponent == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedInContainer = inContainer->common.handle;
	wrappedRemoveComponent = removeComponent->common.handle;
	
	/* Forward repaint. */
	return wrappedState->api->containerRemove(wrappedState,
		wrappedInContainer,
		wrappedRemoveComponent);
}

sjme_errorCode sjme_scritchui_fb_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedInContainer;
	sjme_scritchui_uiComponent wrappedInComponent;
	
	if (inState == NULL || inContainer == NULL || inComponent == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedInContainer = inContainer->common.handle;
	wrappedInComponent = inComponent->common.handle;
	
	/* Forward call. */
	return wrappedState->api->containerSetBounds(wrappedState,
		wrappedInContainer,
		wrappedInComponent,
		x, y, width, height);
}
