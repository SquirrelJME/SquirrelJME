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

static gboolean sjme_scritchui_gtk2_eventConfigure(
	sjme_attrUnused GtkWidget* widget,
	GdkEventConfigure* event, gpointer data)
{
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_listener_size* infoCore;
	
	/* Restore component. */
	inComponent = (sjme_scritchui_uiComponent)data;
	if (inComponent == NULL || event == NULL || data == NULL)
		return FALSE;
	
	/* Restore state. */
	inState = inComponent->common.state;
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, size);
	
	/* Forward accordingly. */
	if (infoCore->callback != NULL)
		infoCore->callback(inState, inComponent,
			event->width, event->height);
	
	/* Always continue handling. */
	return FALSE;
}

static gboolean sjme_scritchui_gtk2_eventExpose(
	GtkWidget* widget,
	GdkEventExpose* event,
	gpointer data)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_listener_paint* infoCore;
	sjme_scritchui_pencil pencil;
	sjme_jint w, h;
	sjme_frontEnd frontEnd;
	sjme_scritchui_pencilFont defaultFont;
	GdkRectangle clipRect;
	
	/* Check nulls before proceeding. */
	if (widget == NULL || event == NULL || data == NULL)
		return FALSE;
	
	/* Restore. */
	inComponent = (sjme_scritchui_uiComponent)data;
	inState = inComponent->common.state;
	
	/* Not something we can paint? */
	paint = NULL;
	if (sjme_error_is(inState->intern->getPaintable(inState,
		inComponent, &paint)) ||
		paint == NULL)
		return FALSE;
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(paint, paint);
	
	/* No actual paint listener? */
	if (infoCore->callback == NULL)
		return FALSE;
	
	/* Determine area to draw. */
	memset(&clipRect, 0, sizeof(clipRect));
	gdk_region_get_clipbox(event->region, &clipRect);
	w = clipRect.x + clipRect.width;
	h = clipRect.y + clipRect.height;
	
	/* Setup frontend info. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.wrapper = widget->window;
	frontEnd.data = widget->style->fg_gc[widget->state];
	
	/* A default font is required. */
	defaultFont = NULL;
	if (sjme_error_is(inState->intern->fontBuiltin(inState,
		&defaultFont)) || defaultFont == NULL)
		return FALSE;
	
	/* Setup pencil for drawing. */
	pencil = &paint->pencil;
	memset(pencil, 0, sizeof(*pencil));
	if (sjme_error_is(sjme_scritchpen_initStatic(pencil,
		inState,
		&sjme_scritchui_gtk2_pencilFunctions,
		NULL, NULL,
		SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888,
		0, 0, w, h, w,
		defaultFont, &frontEnd)))
		return FALSE;
	
	/* The clipping area is set to the region that needs redrawing. */
	pencil->api->setClip(pencil, event->area.x, event->area.y,
		event->area.width, event->area.height);
	
	/* Forward to callback. */
	error = infoCore->callback(inState, inComponent,
		pencil,
		w, h, 0);
	
	/* Reset state. */
	pencil->api->setDefaults(pencil);
	
	/* Do not perform standard drawing, unless an error occurs. */
	if (!sjme_error_is(error))
		return TRUE;
	
	/* Debug. */
	sjme_message("Native draw failed: %d", error);
	
	return FALSE;
}

static sjme_errorCode sjme_scritchui_gtk2_eventInputButton(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	GdkEventButton* event,
	sjme_attrInNotNull sjme_scritchinput_event* fill,
	sjme_attrInNotNull sjme_scritchinput_event* fillTwo)
{
	if (inState == NULL || inComponent == NULL || event == NULL ||
		fill == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine type. */
	if (event->type == GDK_BUTTON_PRESS)
		fill->type = SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED;
	else if (event->type == GDK_BUTTON_RELEASE)
		fill->type = SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED;
	else
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Map button and modifiers. */
	fill->data.mouseButton.button = event->button;
	fill->data.mouseButton.modifiers =
		inState->implIntern->mapGtkToScritchMod(event->state);
	fill->data.mouseButton.x = event->x;
	fill->data.mouseButton.y = event->y;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_eventInputKey(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	GdkEventKey* event,
	sjme_attrInNotNull sjme_scritchinput_event* fill,
	sjme_attrInNotNull sjme_scritchinput_event* fillTwo)
{
	sjme_jint code;
	guint unicode;
	
	if (inState == NULL || inComponent == NULL || event == NULL ||
		fill == NULL || fillTwo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Map to standard key */
	unicode = 0;
	code = inState->implIntern->mapGtkToScritchKey(event->keyval,
		&unicode);
	if (code == SJME_SCRITCHINPUT_KEY_UNKNOWN)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Unicode key? */
	if (event->type == GDK_KEY_PRESS)
	{
		fillTwo->type = SJME_SCRITCHINPUT_TYPE_KEY_CHAR_PRESSED;
		fill->data.key.code = (sjme_jint)unicode;
	}
	
	/* Determine type. */
	if (event->type == GDK_KEY_PRESS)
		fillTwo->type = SJME_SCRITCHINPUT_TYPE_KEY_PRESSED;
	else if (event->type == GDK_KEY_RELEASE)
		fillTwo->type = SJME_SCRITCHINPUT_TYPE_KEY_RELEASED;
	else
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Setup data. */
	fillTwo->data.key.code = code;
	fillTwo->data.key.modifiers =
		inState->implIntern->mapGtkToScritchMod(event->state);
	
	/* Success!. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_eventInputMotion(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	GdkEventMotion* event,
	sjme_attrInNotNull sjme_scritchinput_event* fill,
	sjme_attrInNotNull sjme_scritchinput_event* fillTwo)
{
	if (inState == NULL || inComponent == NULL || event == NULL ||
		fill == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* There is only a single type. */
	fill->type = SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION;
	
	/* Map. */
	fill->data.mouseMotion.buttonMask = 0;
	fill->data.mouseMotion.modifiers =
		inState->implIntern->mapGtkToScritchMod(event->state);
	fill->data.mouseMotion.x = event->x;
	fill->data.mouseMotion.y = event->y;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static gboolean sjme_scritchui_gtk2_eventInput(
	GtkWidget* widget,
	GdkEvent* event,
	gpointer data)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_listener_input* infoCore;
	sjme_scritchinput_event fill, fillTwo;
	
	if (widget == NULL || event == NULL || data == NULL)
		return FALSE;
	
	/* Restore. */
	inComponent = (sjme_scritchui_uiComponent)data;
	inState = inComponent->common.state;
	
	/* Setup base event. */
	memset(&fill, 0, sizeof(fill));
	memset(&fillTwo, 0, sizeof(fillTwo));
	inState->nanoTime(&fill.time);
	inState->nanoTime(&fillTwo.time);
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, input);
	
	/* No actual input listener? */
	if (infoCore->callback == NULL)
		return FALSE;
	
	/* Mouse button. */
	if (event->type == GDK_BUTTON_PRESS ||
		event->type == GDK_BUTTON_RELEASE)
		error = sjme_scritchui_gtk2_eventInputButton(
			inState, inComponent,
			(GdkEventButton*)event, &fill, &fillTwo);
	
		
	/* Keyboard key. */
	else if (event->type == GDK_KEY_PRESS ||
		event->type == GDK_KEY_RELEASE)
		error = sjme_scritchui_gtk2_eventInputKey(
			inState, inComponent,
			(GdkEventKey*)event, &fill, &fillTwo);
		
	/* Mouse motion. */
	else if (event->type == GDK_MOTION_NOTIFY)
		error = sjme_scritchui_gtk2_eventInputMotion(
			inState, inComponent,
			(GdkEventMotion*)event, &fill, &fillTwo);
	
	/* Unknown, so ignore. */
	else
		return FALSE;
	
	/* Failed? */
	if (sjme_error_is(error))
		return FALSE;
	
	/* Forward accordingly. */
	error = SJME_ERROR_NONE;
	if (fill.type != SJME_SCRITCHINPUT_TYPE_UNKNOWN)
		error |= sjme_error_is(infoCore->callback(inState, inComponent,
			&fill));
	if (fillTwo.type != SJME_SCRITCHINPUT_TYPE_UNKNOWN)
		error |= sjme_error_is(infoCore->callback(inState, inComponent,
			&fillTwo));
	
	/* Failed? */
	if (sjme_error_is(error))
		return FALSE;
	
	/* We handled this, so stop. */
	return TRUE;
}

sjme_errorCode sjme_scritchui_gtk2_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	GtkWidget* widget;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover widget. */
	widget = (GtkWidget*)inComponent->common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Grab focus. */
	gtk_widget_grab_focus(widget);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus)
{
	GtkWidget* widget;
	
	if (inState == NULL || inComponent == NULL || outHasFocus == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover widget. */
	widget = (GtkWidget*)inComponent->common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Does this have focus? */
	if (gtk_widget_has_focus(widget) || gtk_widget_is_focus(widget))
		*outHasFocus = SJME_JNI_TRUE;
	else
		*outHasFocus = SJME_JNI_FALSE;
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
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
	widget = (GtkWidget*)inComponent->common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Max value just means to draw everywhere. */
	if (width <= 0 || height <= 0 ||
		width == INT32_MAX || height == INT32_MAX)
		gtk_widget_queue_draw(widget);
	else
		gtk_widget_queue_draw_area(widget,
			x, y, width, height);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
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
	widget = (GtkWidget*)inComponent->common
		.handle[SJME_SUI_GTK2_H_WIDGET];
	gtk_widget_show_all(widget);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_componentSetInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(input))
{
	sjme_errorCode error;
	GtkWidget* widget;
	GdkEventMask mask;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover widget, use secondary if there is one. */
	if (inComponent->common.handle[SJME_SUI_GTK2_H_TOP_WIDGET] != NULL)
		widget =
			(GtkWidget*)inComponent->common.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
	else
		widget = (GtkWidget*)inComponent->common
			.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Mask for wanting events. */
	mask = GDK_POINTER_MOTION_MASK |
		GDK_BUTTON_PRESS_MASK |
		GDK_BUTTON_RELEASE_MASK |
		GDK_KEY_PRESS_MASK |
		GDK_KEY_RELEASE_MASK;
	
	/* Either remove the event mask or add them in. */
	if (inListener != NULL)
		gtk_widget_add_events(widget, mask);
	else
		gtk_widget_set_events(widget,
			gtk_widget_get_events(widget) & (~mask));
	
	/* Set event for key press. */
	return inState->implIntern->reconnectSignal(
		inState,
		widget,
		inComponent,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_CORE(
			inComponent, input),
		inListener,
		copyFrontEnd,
		G_CALLBACK(sjme_scritchui_gtk2_eventInput),
		SJME_JNI_FALSE,
		5,
		"button-press-event", "button-release-event",
		"motion-notify-event", "key-press-event", "key-release-event");
}

sjme_errorCode sjme_scritchui_gtk2_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint))
{
	sjme_errorCode error;
	GtkWidget* widget;
	GtkWidget* topWidget;
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
	widget = (GtkWidget*)inComponent->common
		.handle[SJME_SUI_GTK2_H_WIDGET];
	topWidget = (GtkWidget*)inComponent->common
		.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
	
	/* Disconnect old signal. */
	if (paint->extra != 0)
	{
		/* No longer painted by us. */
		gtk_widget_set_app_paintable(widget, FALSE);
		if (topWidget != NULL)
			gtk_widget_set_app_paintable(topWidget,
				FALSE);
		
		/* Make not double buffered. */
		gtk_widget_set_double_buffered(widget, FALSE);
		if (topWidget != NULL)
			gtk_widget_set_double_buffered(topWidget,
				FALSE);
	}
	
	/* Basic signal connection. */
	if (sjme_error_is(error = inState->implIntern->reconnectSignal(
		inState,
		widget,
		inComponent,
		(sjme_scritchui_listener_void*)infoCore,
		inListener,
		copyFrontEnd,
		G_CALLBACK(sjme_scritchui_gtk2_eventExpose),
		SJME_JNI_FALSE,
		1, "expose-event")))
		return sjme_error_default(error);
	
	/* Connect new handler. */
	if (inListener != NULL)
	{
		/* We want to handle paints now. */
		gtk_widget_set_app_paintable(widget, TRUE);
		if (topWidget != NULL)
			gtk_widget_set_app_paintable(topWidget,
				TRUE);
				
		/* Make double buffered. */
		gtk_widget_set_double_buffered(widget, TRUE);
		if (topWidget != NULL)
			gtk_widget_set_double_buffered(topWidget,
				TRUE);
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size))
{
	GtkWidget* widget;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover widget. */	
	widget = (GtkWidget*)inComponent->common
		.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Basic signal connection. */
	return inState->implIntern->reconnectSignal(inState, widget,
		inComponent,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_CORE(
			inComponent, size),
		inListener,
		copyFrontEnd,
		G_CALLBACK(sjme_scritchui_gtk2_eventConfigure),
		SJME_JNI_FALSE,
		1, "configure-event");
}

sjme_errorCode sjme_scritchui_gtk2_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight)
{
	GtkWidget* widget;
	GtkAllocation alloc;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover widget. */	
	widget = (GtkWidget*)inComponent->common
		.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Get widget allocation. */
	memset(&alloc, 0, sizeof(alloc));
	gtk_widget_get_allocation(widget, &alloc);
	
	/* Copy sizes. */
	if (outWidth != NULL)
		*outWidth = alloc.width;
	if (outHeight != NULL)
		*outHeight = alloc.height;
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
