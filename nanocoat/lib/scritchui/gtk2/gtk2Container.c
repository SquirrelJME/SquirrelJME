/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/core/core.h"

sjme_errorCode sjme_scritchui_gtk2_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent)
{
	GtkWindow* windowTarget;
	GtkFixed* fixed;
	GtkWidget* addWidget;
	
	if (inState == NULL || inContainer == NULL || inContainerData == NULL ||
		addComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Widget should always be the same. */
	/* Use secondary handle for adding, if used. */
	if (addComponent->common.handle[SJME_SUI_GTK2_H_TOP_WIDGET] != NULL)
		addWidget = (GtkWidget*)addComponent->common
			.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
	else
		addWidget = (GtkWidget*)addComponent->common
			.handle[SJME_SUI_GTK2_H_WIDGET];

	/* Debug. */
	sjme_message("containerAdd(%p (%d), %p (%d))",
		inContainer, inContainer->common.type,
		addComponent,
		addComponent->common.type);
	
	/* Which type is this? */
	switch (inContainer->common.type)
	{
			/* Attach to the window table, takes as much space as possible. */
		case SJME_SCRITCHUI_TYPE_WINDOW:
			windowTarget = inContainer->common
				.handle[SJME_SUI_GTK2_H_WINTABLE];
			gtk_table_attach(GTK_TABLE(windowTarget),
				GTK_WIDGET(addWidget),
				0, 1, 1, 2,
				GTK_FILL | GTK_EXPAND,
				GTK_FILL | GTK_EXPAND,
				0, 0);
			break;
		
			/* Place into fixed at basic coordinates. */
		case SJME_SCRITCHUI_TYPE_PANEL:
		case SJME_SCRITCHUI_TYPE_SCROLL_PANEL:
			fixed = (GtkFixed*)inContainer->common
				.handle[SJME_SUI_GTK2_H_WIDGET];
			gtk_fixed_put(GTK_FIXED(fixed),
				addWidget, 0, 0);
			break;
		
		default:
			return SJME_ERROR_NOT_IMPLEMENTED;
	}
	
	/* The widget needs to be shown for it to appear. */
	gtk_widget_show(addWidget);
	
	/* If this widget has the default focus, then force grab it. */
	/* But only if it can actually get focus. */
	if (gtk_widget_get_can_focus(addWidget))
		if (gtk_widget_has_default(addWidget) ||
			gtk_widget_get_can_default(addWidget))
			gtk_widget_grab_focus(addWidget);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_containerRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent)
{
	GtkWindow* windowTarget;
	GtkFixed* fixed;
	GtkWidget* removeWidget;
	
	if (inState == NULL || inContainer == NULL || inContainerData == NULL ||
		removeComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Widget should always be the same. */
	/* Since the secondary handle gets added, we need to remove it. */
	if (removeComponent->common.handle[SJME_SUI_GTK2_H_TOP_WIDGET] != NULL)
		removeWidget = (GtkWidget*)removeComponent->common
			.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
	else
		removeWidget = (GtkWidget*)removeComponent->common
			.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Which type is this? */
	switch (inContainer->common.type)
	{
		case SJME_SCRITCHUI_TYPE_WINDOW:
			windowTarget = inContainer->common
				.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
			gtk_container_remove(GTK_CONTAINER(windowTarget),
				removeWidget);
			break;
		
			/* Place into fixed at basic coordinates. */
		case SJME_SCRITCHUI_TYPE_PANEL:
		case SJME_SCRITCHUI_TYPE_SCROLL_PANEL:
			fixed = (GtkFixed*)inContainer->common
				.handle[SJME_SUI_GTK2_H_WIDGET];
			gtk_container_remove(GTK_CONTAINER(fixed),
				removeWidget);
			break;
		
		default:
			return SJME_ERROR_NOT_IMPLEMENTED;
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
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
	GtkFixed* gtkFixed;
	GtkWidget* moveWidget;
	GtkWidget* subWidget;
	
	if (inState == NULL || inContainer == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (width <= 0 || height <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover widget, we might have an alternate in use. */
	if (inComponent->common.handle[SJME_SUI_GTK2_H_TOP_WIDGET] != NULL)
	{
		moveWidget = (GtkWidget*)inComponent->common
			.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
		subWidget = (GtkWidget*)inComponent->common
			.handle[SJME_SUI_GTK2_H_WIDGET];
	}
	else
	{
		moveWidget = (GtkWidget*)inComponent->common
			.handle[SJME_SUI_GTK2_H_WIDGET];
		subWidget = NULL;
	}
	
	/* Depends on the container type. */
	switch (inContainer->common.type)
	{
			/* Need to move within the panel but also set widget size. */
		case SJME_SCRITCHUI_TYPE_PANEL:
		case SJME_SCRITCHUI_TYPE_SCROLL_PANEL:
			gtkFixed = (GtkFixed*)inContainer->common
				.handle[SJME_SUI_GTK2_H_WIDGET];
			gtk_fixed_move(gtkFixed, moveWidget, x, y);
			break;
		
			/* Nothing needs to be done for windows. */
		case SJME_SCRITCHUI_TYPE_WINDOW:
			break;
		
		default:
			return SJME_ERROR_NOT_IMPLEMENTED;
	}
	
	/* Regardless of the container this is in, set its size. */
	gtk_widget_set_size_request(moveWidget, width, height);
	if (subWidget != NULL)
		gtk_widget_set_size_request(subWidget, width, height);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
