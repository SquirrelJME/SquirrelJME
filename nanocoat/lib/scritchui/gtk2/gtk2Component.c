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
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiPaintable inPaintable;
	sjme_scritchui_paintListenerFunc listener;
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
	inPaintable = NULL;
	if (sjme_error_is(inState->intern->getPaintable(inState,
		inComponent, &inPaintable)) ||
		inPaintable == NULL)
		return TRUE;
	
	/* No actual paint listener? */
	listener = inPaintable->listener;
	if (listener == NULL)
	{
		inPaintable->lastError = SJME_ERROR_NO_LISTENER;
		return TRUE;
	}
	
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
	{
		inPaintable->lastError = SJME_ERROR_OUT_OF_MEMORY;
		return TRUE;
	}
	
	/* Clear it. */
	memset(rawPixels, 0, bufLen);
	
	/* Forward to callback. */
	sjme_atomic_sjme_jint_set(&inPaintable->inPaint, 1);
	error = listener(inState, inComponent,
		inPaintable,
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
	
	/* No longer painting. */
	inPaintable->lastError = error;
	sjme_atomic_sjme_jint_set(&inPaintable->inPaint, 0);
	
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
			G_CALLBACK(sjme_scritchui_gtk2_exposeHandler), inComponent);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
