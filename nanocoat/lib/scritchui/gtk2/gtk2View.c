/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gdk/gdk.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_gtk2_viewGetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_gtk2_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea)
{
	GtkWidget* topWidget;
	GtkWidget* subWidget;
	GtkAdjustment* hAdjust;
	GtkAdjustment* vAdjust;
	
	if (inState == NULL || inComponent == NULL || inViewArea == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get wrapped widget. */
	topWidget = inComponent->common.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
	subWidget = inComponent->common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Depends on the type. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_SCROLL_PANEL:
			/* Set size of the sub-fixed panel. */
			gtk_widget_set_size_request(subWidget,
				inViewArea->width, inViewArea->height);
			
			/* Change adjustment. */
			hAdjust = gtk_scrolled_window_get_hadjustment(
				GTK_SCROLLED_WINDOW(topWidget));
			vAdjust = gtk_scrolled_window_get_vadjustment(
				GTK_SCROLLED_WINDOW(topWidget));
			
			gtk_adjustment_set_upper(hAdjust,
				inViewArea->width);
			gtk_adjustment_set_upper(vAdjust,
				inViewArea->height);
			break;
		
		default:
			return SJME_ERROR_NOT_IMPLEMENTED;
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_viewSetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_rect* inViewRect)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_gtk2_viewSetViewListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(view))
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
