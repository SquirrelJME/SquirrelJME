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

static gboolean sjme_scritchui_gtk2_eventScrollChildByBar(
	GtkAdjustment* xAdjust,
	gpointer data)
{
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	GtkWidget* topWidget;
	
	if (data == NULL)
		return FALSE;
	
	/* Recover component and state. */
	inComponent = (sjme_scritchui_uiComponent)data;
	inState = inComponent->common.state;
	
	/* Request that the widget redraw itself. */
	topWidget = inComponent->common.handle[SJME_SUI_GTK2_H_WIDGET];
	gtk_widget_queue_draw(topWidget);
	
	/* Let GTK handle more of this. */
	return FALSE;
}

static gboolean sjme_scritchui_gtk2_eventScrollChildByKey(
	GtkScrolledWindow* gtkScroll,
	GtkScrollType scrollType,
	gboolean unknown,
	gpointer data)
{
	GtkWidget* child;
	
	/* Tell the child, if there is one, to redraw. */
	child = gtk_bin_get_child(GTK_BIN(gtkScroll));
	if (child != NULL)
		gtk_widget_queue_draw(child);
	
	/* Let GTK continue handling this. */
	return FALSE;
}

sjme_errorCode sjme_scritchui_gtk2_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScrollPanel inScrollPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	GtkScrolledWindow* gtkScroll;
	GtkAdjustment* hAdjust;
	GtkAdjustment* vAdjust;
	
	if (inState == NULL || inScrollPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup new scroll. */
	gtkScroll = GTK_SCROLLED_WINDOW(gtk_scrolled_window_new(
		NULL, NULL));
	if (gtkScroll == NULL)
		return SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED;
	
	/* Common widget init. */
	inState->implIntern->widgetInit(inState, GTK_WIDGET(gtkScroll));
	
	/* Store information. */
	inScrollPanel->component.common.handle[SJME_SUI_GTK2_H_WIDGET] =
		gtkScroll;
		
	/* Remove the shadow, it is not needed and also remove glitchies. */
	gtk_scrolled_window_set_shadow_type(gtkScroll,
		GTK_SHADOW_NONE);
	
	/* Repaint when keys scroll the window. */
	g_signal_connect(gtkScroll, "scroll-child",
		G_CALLBACK(sjme_scritchui_gtk2_eventScrollChildByKey), inScrollPanel);
	
	/* Get adjustments. */
	hAdjust = gtk_scrolled_window_get_hadjustment(gtkScroll);
	vAdjust = gtk_scrolled_window_get_vadjustment(gtkScroll);
	
	g_signal_connect(hAdjust, "changed",
		G_CALLBACK(sjme_scritchui_gtk2_eventScrollChildByBar), inScrollPanel);
	g_signal_connect(hAdjust, "value-changed",
		G_CALLBACK(sjme_scritchui_gtk2_eventScrollChildByBar), inScrollPanel);
		
	g_signal_connect(vAdjust, "changed",
		G_CALLBACK(sjme_scritchui_gtk2_eventScrollChildByBar), inScrollPanel);
	g_signal_connect(vAdjust, "value-changed",
		G_CALLBACK(sjme_scritchui_gtk2_eventScrollChildByBar), inScrollPanel);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
