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
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"

static gboolean sjme_scritchui_gtk2_eventDelete(GtkWidget* widget,
	GdkEvent* event,
	gpointer data)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiWindow inWindow;
	sjme_scritchui_listener_close* infoCore;
	
	/* Restore component. */
	inWindow = (sjme_scritchui_uiComponent)data;
	if (inWindow == NULL)
		return TRUE;
	
	/* Restore state. */
	inState = inWindow->component.common.state;
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inWindow, close);
	
	/* Forward to callback. */
	error = SJME_ERROR_NONE;
	if (infoCore->callback != NULL)
		error = infoCore->callback(inState, inWindow);
	
	/* Cancel deletion? */
	if (error == SJME_ERROR_CANCEL_WINDOW_CLOSE)
		return TRUE;
	
	/* False will destroy the window! */
	return FALSE;
}

static gboolean sjme_scritchui_gtk2_eventVisibilityNotify(
	GtkWidget* widget,
	GdkEventVisibility* event,
	gpointer data)
{
	sjme_scritchui inState;
	sjme_scritchui_uiWindow inWindow;
	
	/* Check nulls before proceeding. */
	if (widget == NULL || event == NULL || data == NULL)
		return FALSE;
	
	/* Restore. */
	inWindow = (sjme_scritchui_uiComponent)data;
	inState = inWindow->component.common.state;
	
	/* We need to recurse and have ScritchUI handle this. */
	inState->intern->updateVisibleWindow(
		inState, inWindow,
		event->state != GDK_VISIBILITY_FULLY_OBSCURED);
	
	/* Always continue handling. */
	return FALSE;
}

sjme_errorCode sjme_scritchui_gtk2_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_errorCode error;
	GtkWindow* gtkWindow;
	GdkGeometry geometry;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (width <= 0 || height <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover window. */
	gtkWindow = inWindow->component.common.handle;
	
	/* Setup geometry. */
	memset(&geometry, 0, sizeof(geometry));
	geometry.min_width = width;
	geometry.min_height = height;
	geometry.base_width = width;
	geometry.base_height = height;
	
	/* Set minimum size. */
	gtk_window_set_geometry_hints(gtkWindow,
		gtkWindow,
		&geometry,
		GDK_HINT_MIN_SIZE | GDK_HINT_BASE_SIZE);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow)
{
	GtkWindow* gtkWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We only care for top-level windows. */
	gtkWindow = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	if (gtkWindow == NULL)
		return SJME_ERROR_CANNOT_CREATE;
	
	/* Setup window. */
	inWindow->component.common.handle = gtkWindow;
	
	/* Common widget init. */
	inState->implIntern->widgetInit(inState, GTK_WIDGET(gtkWindow));
	
	/* Set default title. */
	gtk_window_set_title(gtkWindow,
		inState->wmInfo->defaultTitle);
	
	/* Need to set window class for consistency. */
	gtk_window_set_wmclass(gtkWindow,
		inState->wmInfo->xwsClass,
		inState->wmInfo->xwsClass);
	
	/* Set visibility change listener, which requires some logic. */
	g_signal_connect(gtkWindow, "visibility-notify-event",
		G_CALLBACK(sjme_scritchui_gtk2_eventVisibilityNotify), inWindow);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_windowSetCloseListenerFunc(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close))
{
	GtkWindow* gtkWindow;
	sjme_scritchui_listener_close* infoCore;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	gtkWindow = inWindow->component.common.handle;
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inWindow, close);
	
	/* Basic signal connection. */
	return inState->implIntern->reconnectSignal(inState,
		GTK_WIDGET(gtkWindow),
		inWindow,
		infoCore,
		inListener,
		copyFrontEnd,
		G_CALLBACK(sjme_scritchui_gtk2_eventDelete),
		1, "delete-event");
}

sjme_errorCode sjme_scritchui_gtk2_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	sjme_errorCode error;
	GtkWindow* gtkWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	gtkWindow = inWindow->component.common.handle;
	
	/* Show or hide it. */
	if (isVisible)
		gtk_window_present(GTK_WINDOW(gtkWindow));
	else
		gtk_widget_hide(GTK_WIDGET(gtkWindow));
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
