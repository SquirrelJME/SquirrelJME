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

static gboolean sjme_scritchui_gtk2_eventConfigure(
	sjme_attrUnused GtkWidget* widget,
	GdkEventConfigure* event, gpointer data)
{
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_listener_size* infoCore;
	
	/* Restore component. */
	inComponent = (sjme_scritchui_uiComponent)data;
	if (inComponent == NULL)
		return TRUE;
	
	/* Restore state. */
	inState = inComponent->common.state;
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, size);
	
	/* Forward accordingly. */
	if (infoCore->callback != NULL)
		infoCore->callback(inState, inComponent,
			event->width, event->height);
	
	/* Always continue handling. */
	return TRUE;
}

static gboolean sjme_scritchui_gtk2_eventExpose(
	GtkWidget* widget, GdkEventExpose* event, gpointer data)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_listener_paint* infoCore;
	sjme_jint rawArea, x, y, w, h;
	sjme_jint bufLen;
	sjme_jint* rawPixels;
	
	/* Restore component. */
	inComponent = (sjme_scritchui_uiComponent)data;
	if (inComponent == NULL)
		return TRUE;
	
	/* Restore state. */
	inState = inComponent->common.state;
	
	/* Not something we can paint? */
	paint = NULL;
	if (sjme_error_is(inState->intern->getPaintable(inState,
		inComponent, &paint)) ||
		paint == NULL)
		return TRUE;
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(paint, paint);
	
	/* No actual paint listener? */
	if (infoCore->callback == NULL)
		return TRUE;
	
	/* Determine area to draw. */
	x = event->area.x;
	y = event->area.y;
	w = event->area.width;
	h = event->area.height;
	rawArea = w * h;
	
	/* Allocate raw pixel data, because we cannot directly access */
	/* the pixels used by a GtkWidget. */
	bufLen = sizeof(*rawPixels) * rawArea;
	rawPixels = sjme_alloca(bufLen);
	if (rawPixels == NULL)
		return TRUE;
	
	/* Clear it. */
	memset(rawPixels, 0, bufLen);
	
	/* Forward to callback. */
	error = infoCore->callback(inState, inComponent,
		SJME_GFX_PIXEL_FORMAT_INT_RGB888,
		w, h,
		rawPixels, 0, bufLen,
		NULL, 0,
		0, 0, w, h, 0);
	
	/* Draw the data we have. */
	gdk_draw_rgb_32_image(GDK_DRAWABLE(widget->window),
		widget->style->fg_gc[widget->state],
		x, y, w, h,
		GDK_RGB_DITHER_MAX, (guchar*)rawPixels, w * 4);
	
	/* Do not perform standard drawing, unless an error occurs. */
	if (sjme_error_is(error))
		return TRUE;
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

sjme_errorCode sjme_scritchui_gtk2_componentRevalidate(
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
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	GtkWidget* widget;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_listener_paint* infoCore;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get the component's paint. */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return sjme_error_default(error);
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(paint, paint);
	
	/* Recover widget. */
	widget = (GtkWidget*)inComponent->common.handle;
	
	/* Disconnect old signal. */
	if (paint->extra != 0)
	{
		/* No longer painted by us. */
		gtk_widget_set_app_paintable(widget, FALSE);
			
		/* Disconnect. */
		gtk_signal_disconnect(widget, (gulong)paint->extra);
		
		/* Clear data. */
		infoCore->extra = 0;
		infoCore->callback = 0;
		memset(&infoCore->frontEnd, 0, sizeof(infoCore->frontEnd));
	}
	
	/* Connect new handler. */
	if (inListener != NULL)
	{
		/* Fill in. */
		infoCore->callback = inListener;
		if (copyFrontEnd != NULL)
			memmove(&infoCore->frontEnd, copyFrontEnd,
				sizeof(*copyFrontEnd));
		
		/* Connect signal. */
		paint->extra = g_signal_connect(widget, "expose-event",
			G_CALLBACK(sjme_scritchui_gtk2_eventExpose), inComponent);
		
		/* We want to handle paints now. */
		gtk_widget_set_app_paintable(widget, TRUE);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_sizeListenerFunc inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd)
{
	GtkWidget* widget;
	sjme_scritchui_listener_size* infoCore;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, size);
	
	/* Recover widget. */	
	widget = (GtkWidget*)inComponent->common.handle;
	
	/* Disconnect old signal? */
	if (infoCore->extra != 0)
	{
		/* Disconnect. */
		gtk_signal_disconnect(widget, (gulong)infoCore->extra);
		
		/* Clear data. */
		infoCore->extra = 0;
		infoCore->callback = 0;
		memset(&infoCore->frontEnd, 0, sizeof(infoCore->frontEnd));
	}
	
	/* Connect signal. */
	if (inListener != NULL)
	{
		/* Fill in. */
		infoCore->callback = inListener;
		if (copyFrontEnd != NULL)
			memmove(&infoCore->frontEnd, copyFrontEnd,
				sizeof(*copyFrontEnd));
		
		/* Connect signal now. */
		infoCore->extra = g_signal_connect(widget, "configure-event",
			G_CALLBACK(sjme_scritchui_gtk2_eventConfigure), inComponent);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
