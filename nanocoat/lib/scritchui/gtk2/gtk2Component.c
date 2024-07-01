/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gdk/gdkkeysyms.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/core/core.h"


static sjme_jint sjme_scritchui_gtk_mapKey(guint in)
{
	guint32 unicode;
	
	/* Map to hard keys first for specific keys such as numberpad keys */
	/* as they technically map to unicode, theoretically. */
	switch (in)
	{
		case GDK_KEY_Alt_L:
		case GDK_KEY_Alt_R:
			return SJME_SCRITCHINPUT_KEY_ALT;
		case GDK_KEY_Caps_Lock:
			return SJME_SCRITCHINPUT_KEY_CAPSLOCK;
		case GDK_KEY_Menu:
		case GDK_KEY_MenuKB:
		case GDK_KEY_MenuPB:
		case GDK_KEY_TopMenu:
			return SJME_SCRITCHINPUT_KEY_CONTEXT_MENU;
		case GDK_KEY_Control_L:
		case GDK_KEY_Control_R:
			return SJME_SCRITCHINPUT_KEY_CONTROL;
		case GDK_KEY_Down:
		case GDK_KEY_KP_Down:
			return SJME_SCRITCHINPUT_KEY_DOWN;
		case GDK_KEY_End:
		case GDK_KEY_KP_End:
			return SJME_SCRITCHINPUT_KEY_END;
		case GDK_KEY_F1:
			return SJME_SCRITCHINPUT_KEY_F1;
		case GDK_KEY_F2:
			return SJME_SCRITCHINPUT_KEY_F2;
		case GDK_KEY_F3:
			return SJME_SCRITCHINPUT_KEY_F3;
		case GDK_KEY_F4:
			return SJME_SCRITCHINPUT_KEY_F4;
		case GDK_KEY_F5:
			return SJME_SCRITCHINPUT_KEY_F5;
		case GDK_KEY_F6:
			return SJME_SCRITCHINPUT_KEY_F6;
		case GDK_KEY_F7:
			return SJME_SCRITCHINPUT_KEY_F7;
		case GDK_KEY_F8:
			return SJME_SCRITCHINPUT_KEY_F8;
		case GDK_KEY_F9:
			return SJME_SCRITCHINPUT_KEY_F9;
		case GDK_KEY_F10:
			return SJME_SCRITCHINPUT_KEY_F10;
		case GDK_KEY_F11:
			return SJME_SCRITCHINPUT_KEY_F11;
		case GDK_KEY_F12:
			return SJME_SCRITCHINPUT_KEY_F12;
		case GDK_KEY_F13:
			return SJME_SCRITCHINPUT_KEY_F13;
		case GDK_KEY_F14:
			return SJME_SCRITCHINPUT_KEY_F14;
		case GDK_KEY_F15:
			return SJME_SCRITCHINPUT_KEY_F15;
		case GDK_KEY_F16:
			return SJME_SCRITCHINPUT_KEY_F16;
		case GDK_KEY_F17:
			return SJME_SCRITCHINPUT_KEY_F17;
		case GDK_KEY_F18:
			return SJME_SCRITCHINPUT_KEY_F18;
		case GDK_KEY_F19:
			return SJME_SCRITCHINPUT_KEY_F19;
		case GDK_KEY_F20:
			return SJME_SCRITCHINPUT_KEY_F20;
		case GDK_KEY_F21:
			return SJME_SCRITCHINPUT_KEY_F21;
		case GDK_KEY_F22:
			return SJME_SCRITCHINPUT_KEY_F22;
		case GDK_KEY_F23:
			return SJME_SCRITCHINPUT_KEY_F23;
		case GDK_KEY_F24:
			return SJME_SCRITCHINPUT_KEY_F24;
		case GDK_KEY_Home:
		case GDK_KEY_KP_Home:
			return SJME_SCRITCHINPUT_KEY_HOME;
		case GDK_KEY_Insert:
		case GDK_KEY_KP_Insert:
			return SJME_SCRITCHINPUT_KEY_INSERT;
		case GDK_KEY_Left:
		case GDK_KEY_KP_Left:
			return SJME_SCRITCHINPUT_KEY_LEFT;
		case GDK_KEY_Meta_L:
		case GDK_KEY_Meta_R:
			return SJME_SCRITCHINPUT_KEY_META;
		case GDK_KEY_Num_Lock:
			return SJME_SCRITCHINPUT_KEY_NUMLOCK;
		case GDK_KEY_KP_0:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_0;
		case GDK_KEY_KP_1:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_1;
		case GDK_KEY_KP_2:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_2;
		case GDK_KEY_KP_3:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_3;
		case GDK_KEY_KP_4:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_4;
		case GDK_KEY_KP_5:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_5;
		case GDK_KEY_KP_6:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_6;
		case GDK_KEY_KP_7:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_7;
		case GDK_KEY_KP_8:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_8;
		case GDK_KEY_KP_9:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_9;
		case GDK_KEY_KP_Decimal:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_DECIMAL;
		case GDK_KEY_KP_Divide:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_DIVIDE;
		case GDK_KEY_KP_Enter:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_ENTER;
		case GDK_KEY_KP_Subtract:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_MINUS;
		case GDK_KEY_KP_Multiply:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_MULTIPLY;
		case GDK_KEY_KP_Add:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_PLUS;
		case GDK_KEY_Page_Down:
		case GDK_KEY_KP_Page_Down:
			return SJME_SCRITCHINPUT_KEY_PAGE_DOWN;
		case GDK_KEY_Page_Up:
		case GDK_KEY_KP_Page_Up:
			return SJME_SCRITCHINPUT_KEY_PAGE_UP;
		case GDK_KEY_Pause:
		case GDK_KEY_AudioPause:
			return SJME_SCRITCHINPUT_KEY_PAUSE;
		case GDK_KEY_Print:
		case GDK_KEY_3270_PrintScreen:
			return SJME_SCRITCHINPUT_KEY_PRINTSCREEN;
		case GDK_KEY_Right:
		case GDK_KEY_KP_Right:
			return SJME_SCRITCHINPUT_KEY_RIGHT;
		case GDK_KEY_Scroll_Lock:
			return SJME_SCRITCHINPUT_KEY_SCROLLLOCK;
		case GDK_KEY_Shift_L:
		case GDK_KEY_Shift_R:
			return SJME_SCRITCHINPUT_KEY_SHIFT;
		case GDK_KEY_Up:
		case GDK_KEY_KP_Up:
			return SJME_SCRITCHINPUT_KEY_UP;
		
		case GDK_KEY_Return:
		case GDK_KEY_ISO_Enter:
		case GDK_KEY_3270_Enter:
			return SJME_SCRITCHINPUT_KEY_ENTER;
		
		case GDK_KEY_BackSpace:
			return SJME_SCRITCHINPUT_KEY_BACKSPACE;
		
		case GDK_KEY_Delete:
			return SJME_SCRITCHINPUT_KEY_DELETE;
		
		case GDK_KEY_Tab:
			return SJME_SCRITCHINPUT_KEY_TAB;
		
		case GDK_KEY_Escape:
			return SJME_SCRITCHINPUT_KEY_ESCAPE;
	}
	
	/* Is this a unicode key? Map to char if so... */
	unicode = gdk_keyval_to_unicode(in);
	if (unicode >= ' ' && unicode <= 0xFFFF)
		return unicode;
	
	/* Unknown key. */
	return SJME_SCRITCHINPUT_KEY_UNKNOWN;
}

static sjme_jint sjme_scritchui_gtk_mapModifier(guint state)
{
	sjme_jint result;
	
	/* Map all modifiers. */
	result = 0;
	if (state & GDK_SHIFT_MASK)
		result |= SJME_SCRITCHINPUT_MODIFIER_SHIFT;
	if (state & GDK_MOD1_MASK)
		result |= SJME_SCRITCHINPUT_MODIFIER_ALT;
	if (state & GDK_MOD2_MASK)
		result |= SJME_SCRITCHINPUT_MODIFIER_CHR;
	if (state & GDK_CONTROL_MASK)
		result |= SJME_SCRITCHINPUT_MODIFIER_CTRL;
	if (state & (GDK_SUPER_MASK | GDK_HYPER_MASK | GDK_META_MASK))
		result |= SJME_SCRITCHINPUT_MODIFIER_COMMAND;
	
	return result;
}

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
	sjme_scritchui_pencilBase pencil;
	sjme_jint w, h;
	sjme_frontEnd frontEnd;
	sjme_scritchui_pencilFont defaultFont;
	
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
	w = event->area.width;
	h = event->area.height;
	
	/* Setup frontend info. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.wrapper = widget->window;
	frontEnd.data = widget->style->fg_gc[widget->state];
	
	/* A default font is required. */
	defaultFont = NULL;
	if (sjme_error_is(inState->api->fontBuiltin(inState,
		&defaultFont)) || defaultFont == NULL)
		return FALSE;
	
	/* Setup pencil for drawing. */
	memset(&pencil, 0, sizeof(pencil));
	if (sjme_error_is(sjme_scritchui_pencilInitStatic(&pencil,
		&sjme_scritchui_gtk2_pencilFunctions,
		SJME_GFX_PIXEL_FORMAT_INT_RGB888,
		w, h, w, defaultFont, &frontEnd)))
		return FALSE;
	
	/* The clipping area is set to the region that needs redrawing. */
	pencil.api->setClip(&pencil, event->area.x, event->area.y,
		event->area.width, event->area.height);
	
	/* Forward to callback. */
	error = infoCore->callback(inState, inComponent,
		&pencil,
		w, h, 0);
	
	/* Do not perform standard drawing, unless an error occurs. */
	if (!sjme_error_is(error))
		return TRUE;
	return FALSE;
}

static sjme_errorCode sjme_scritchui_gtk2_eventInputButton(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	GdkEventButton* event,
	sjme_attrInNotNull sjme_scritchinput_event* fill)
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
		sjme_scritchui_gtk_mapModifier(event->state);
	fill->data.mouseButton.x = event->x;
	fill->data.mouseButton.y = event->y;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_eventInputKey(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	GdkEventKey* event,
	sjme_attrInNotNull sjme_scritchinput_event* fill)
{
	sjme_jint code;
	
	if (inState == NULL || inComponent == NULL || event == NULL ||
		fill == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Map to standard key */
	code = sjme_scritchui_gtk_mapKey(event->keyval);
	if (code == SJME_SCRITCHINPUT_KEY_UNKNOWN)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Determine type. */
	if (event->type == GDK_KEY_PRESS)
		fill->type = SJME_SCRITCHINPUT_TYPE_KEY_PRESSED;
	else if (event->type == GDK_KEY_RELEASE)
		fill->type = SJME_SCRITCHINPUT_TYPE_KEY_RELEASED;
	else
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Setup data. */
	fill->data.key.code = code;
	fill->data.key.modifiers = sjme_scritchui_gtk_mapModifier(event->state);
	
	/* Success!. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_eventInputMotion(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	GdkEventMotion* event,
	sjme_attrInNotNull sjme_scritchinput_event* fill)
{
	if (inState == NULL || inComponent == NULL || event == NULL ||
		fill == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* There is only a single type. */
	fill->type = SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION;
	
	/* Map. */
	fill->data.mouseMotion.buttonMask = 0;
	fill->data.mouseMotion.modifiers =
		sjme_scritchui_gtk_mapModifier(event->state);
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
	sjme_scritchinput_event fill;
	
	if (widget == NULL || event == NULL || data == NULL)
		return FALSE;
	
	/* Restore. */
	inComponent = (sjme_scritchui_uiComponent)data;
	inState = inComponent->common.state;
	
	/* Setup base event. */
	memset(&fill, 0, sizeof(fill));
	inState->nanoTime(&fill.time);
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, input);
	
	/* No actual input listener? */
	if (infoCore->callback == NULL)
		return FALSE;
	
	/* Key event. */
	if (event->type == GDK_BUTTON_PRESS ||
		event->type == GDK_BUTTON_RELEASE)
		error = sjme_scritchui_gtk2_eventInputButton(
			inState, inComponent,
			(GdkEventButton*)event, &fill);
	
	/* Mouse button. */
	else if (event->type == GDK_KEY_PRESS ||
		event->type == GDK_KEY_RELEASE)
		error = sjme_scritchui_gtk2_eventInputKey(
			inState, inComponent,
			(GdkEventKey*)event, &fill);
		
	/* Mouse motion. */
	else if (event->type == GDK_MOTION_NOTIFY)
		error = sjme_scritchui_gtk2_eventInputMotion(
			inState, inComponent,
			(GdkEventMotion*)event, &fill);
	
	/* Unknown, so ignore. */
	else
		return FALSE;
	
	/* Failed? */
	if (sjme_error_is(error))
		return FALSE;
	
	/* Forward accordingly. */
	if (sjme_error_is(infoCore->callback(inState, inComponent,
		&fill)))
		return FALSE;
	
	/* We handled this, so stop. */
	return TRUE;
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
	if (inComponent->common.handleB != NULL)
		widget = (GtkWidget*)inComponent->common.handleB;
	else
		widget = (GtkWidget*)inComponent->common.handle;
	
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
		5,
		"button-press-event", "button-release-event",
		"motion-notify-event", "key-press-event", "key-release-event");
	
	/* Did this fail? */
	return error;
}

sjme_errorCode sjme_scritchui_gtk2_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint))
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
	}
	
	/* Basic signal connection. */
	if (sjme_error_is(error = inState->implIntern->reconnectSignal(
		inState,
		widget,
		inComponent,
		infoCore,
		inListener,
		copyFrontEnd,
		G_CALLBACK(sjme_scritchui_gtk2_eventExpose),
		1, "expose-event")))
		return sjme_error_default(error);
	
	/* Connect new handler. */
	if (inListener != NULL)
	{
		/* We want to handle paints now. */
		gtk_widget_set_app_paintable(widget, TRUE);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	widget = (GtkWidget*)inComponent->common.handle;
	
	/* Basic signal connection. */
	return inState->implIntern->reconnectSignal(inState, widget,
		inComponent,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_CORE(
			inComponent, size),
		inListener,
		copyFrontEnd,
		G_CALLBACK(sjme_scritchui_gtk2_eventConfigure),
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
	widget = (GtkWidget*)inComponent->common.handle;
	
	/* Get widget allocation. */
	memset(&alloc, 0, sizeof(alloc));
	gtk_widget_get_allocation(widget, &alloc);
	
	/* Copy sizes. */
	if (outWidth != NULL)
		*outWidth = alloc.width;
	if (outHeight != NULL)
		*outHeight = alloc.height;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
