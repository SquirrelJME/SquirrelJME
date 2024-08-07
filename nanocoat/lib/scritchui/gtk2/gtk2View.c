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
	GtkWidget* topWidget;
	GtkWidget* binChild;
	GtkAdjustment* hAdjust;
	GtkAdjustment* vAdjust;
	GtkAllocation alloc;
	
	if (inState == NULL || inComponent == NULL || outViewRect == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get wrapped widget. */
	topWidget = inComponent->common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Depends on the type. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_SCROLL_PANEL:
			/* Get the allocation of the window. */
			memset(&alloc, 0, sizeof(alloc));
			binChild = gtk_bin_get_child(GTK_BIN(topWidget));
			gtk_widget_get_allocation(binChild, &alloc);
		
			/* Get adjustment. */
			hAdjust = gtk_scrolled_window_get_hadjustment(
				GTK_SCROLLED_WINDOW(topWidget));
			vAdjust = gtk_scrolled_window_get_vadjustment(
				GTK_SCROLLED_WINDOW(topWidget));
			
			if (hAdjust != NULL)
			{
				outViewRect->s.x = gtk_adjustment_get_value(
					hAdjust);
				
				outViewRect->d.width = gtk_adjustment_get_page_size(
					hAdjust);
				if (alloc.width > outViewRect->d.width)
					outViewRect->d.width = alloc.width;
			}
			
			if (vAdjust != NULL)
			{
				outViewRect->s.y = gtk_adjustment_get_value(
					vAdjust);
				
				/* Get the higher of the two widths/heights. */
				outViewRect->d.height = gtk_adjustment_get_page_size(
					vAdjust);
				if (alloc.height > outViewRect->d.height)
					outViewRect->d.height = alloc.height;
			}
			break;
		
		default:
			return sjme_error_notImplemented(0);
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewPage)
{
	GtkWidget* topWidget;
	GtkAdjustment* hAdjust;
	GtkAdjustment* vAdjust;
	
	if (inState == NULL || inComponent == NULL || inViewArea == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get wrapped widget. */
	topWidget = inComponent->common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Depends on the type. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_SCROLL_PANEL:
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
			return sjme_error_notImplemented(0);
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_viewSetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_point* inViewPos)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_gtk2_viewSetViewListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(view))
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
