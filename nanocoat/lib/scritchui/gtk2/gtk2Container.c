/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/core/core.h"

sjme_errorCode sjme_scritchui_gtk2_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	GtkWindow* window;
	GtkFixed* fixed;
	GtkWidget* widget;
	
	if (inState == NULL || inContainer == NULL || inContainerData == NULL ||
		inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Widget should always be the same. */
	widget = (GtkWidget*)inComponent->common.handle;

	/* Debug. */
	sjme_message("containerAdd(%p (%d), %p (%d))",
		inContainer, inContainer->common.type,
		inComponent, inComponent->common.type);
	
	/* Which type is this? */
	switch (inContainer->common.type)
	{
		case SJME_SCRITCHUI_TYPE_WINDOW:
			window = (GtkWindow*)inContainer->common.handle;
			gtk_container_add(GTK_CONTAINER(window), widget);
			break;
		
			/* Place into fixed at basic coordinates. */
		case SJME_SCRITCHUI_TYPE_PANEL:
			fixed = (GtkFixed*)inContainer->common.handle;
			gtk_fixed_put(GTK_FIXED(fixed), widget, 0, 0);
			break;
		
		default:
			return SJME_ERROR_NOT_IMPLEMENTED;
	}
	
	/* The widget needs to be shown for it to appear. */
	gtk_widget_show(widget);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_errorCode error;
	GtkFixed* gtkFixed;
	GtkWidget* componentWidget;
	
	if (inState == NULL || inContainer == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (width <= 0 || height <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover widget. */
	componentWidget = (GtkWidget*)inComponent->common.handle;
	
	/* Depends on the container type. */
	switch (inContainer->common.type)
	{
			/* Need to move within the panel but also set widget size. */
		case SJME_SCRITCHUI_TYPE_PANEL:
			gtkFixed = (GtkFixed*)inContainer->common.handle;
			gtk_fixed_move(gtkFixed, componentWidget, x, y);
			gtk_widget_set_size_request(componentWidget, width, height);
			break;
		
			/* Nothing needs to be done for windows. */
		case SJME_SCRITCHUI_TYPE_WINDOW:
			break;
		
		default:
			return SJME_ERROR_NOT_IMPLEMENTED;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
