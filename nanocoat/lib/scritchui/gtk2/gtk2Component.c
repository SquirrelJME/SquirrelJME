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

static gboolean sjme_scritchui_gtk2_exposeHandler(GtkWidget* widget,
	GdkEventExpose* event, gpointer data)
{
	sjme_todo("Expose?");
	
	/* Do not perform standard drawing. */
	return FALSE;
}

sjme_errorCode sjme_scritchui_gtk2_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	GtkWidget* widget;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover widget. */
	widget = (GtkWidget*)inComponent->common.handle;
	
	/* Max value just means to draw everywhere. */
	if (width <= 0 || height <= 0 ||
		width == INT32_MAX || height == INT32_MAX)
		gtk_widget_queue_draw(widget);
	else
		gtk_widget_queue_draw_area(widget,
			x, y, width, height);
		
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	GtkWidget* widget;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We do not want to show windows themselves until they are explicitly */
	/* shown. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		return SJME_ERROR_NONE;
	
	/* There is not really a revalidate in GTK, but we can make sure */
	/* that everything is properly shown. */
	widget = (GtkWidget*)inComponent->common.handle;
	gtk_widget_show_all(widget);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_attrInNotNull sjme_scritchui_uiPaintable inPaint,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd)
{
	GtkWidget* widget;
	
	if (inState == NULL || inComponent == NULL || inPaint == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* For GTK we do not give it a listener, we just say we paint on it. */
	widget = (GtkWidget*)inComponent->common.handle;
	gtk_widget_set_app_paintable(widget,
		inListener != NULL);
	
	/* Disconnect old handler if one was used. */
	if (inPaint->extra != 0)
		gtk_signal_disconnect(widget, (gulong)inPaint->extra);
	
	/* Connect new handler. */
	if (inListener != NULL)
		inPaint->extra = g_signal_connect(widget, "expose-event",
			G_CALLBACK(sjme_scritchui_gtk2_exposeHandler), NULL);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
