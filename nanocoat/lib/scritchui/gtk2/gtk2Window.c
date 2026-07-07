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
#include "sjme/util.h"

static gboolean sjme_scritchui_gtk2_eventDelete(GtkWidget* widget,
	GdkEvent* event,
	gpointer data)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiWindow inWindow;
	sjme_scritchui_listener_close* infoCore;
	
	/* Restore component. */
	inWindow = (sjme_scritchui_uiWindow)data;
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
	inWindow = (sjme_scritchui_uiWindow)data;
	inState = inWindow->component.common.state;
	
	/* We need to recurse and have ScritchUI handle this. */
	inState->intern->updateVisibleWindow(
		inState, inWindow,
		event->state != GDK_VISIBILITY_FULLY_OBSCURED);
	
	/* Always continue handling. */
	return FALSE;
}

static gboolean sjme_scritchui_gtk2_eventWindowExpose(
	GtkWindow* gtkWindow,
	GdkEventExpose* event,
	gpointer data)
{
	sjme_scritchui inState;
	sjme_scritchui_uiWindow inWindow;
	
	if (event == NULL)
		return FALSE;
	
	/* Disconnect this signal, we only want to call this once. */
	g_signal_handlers_disconnect_by_func(gtkWindow,
		(gpointer)sjme_scritchui_gtk2_eventWindowExpose, data);
	
	/* These must be valid. */
	inWindow = data;
	if (gtkWindow == NULL || data == NULL)
		return FALSE;
	
	/* Set minimum window size, if specified. */
	inState = inWindow->component.common.state;
	if (inWindow->min.width != 0 && inWindow->min.height != 0)
		return inState->apiInThread->windowContentMinimumSize(
			inState, inWindow, inWindow->min.width, inWindow->min.height);
	
	/* Always continue handling. */
	return FALSE;
}

static void sjme_scritchui_gtk2_nukeMenuBox(GtkWidget* widget,
	gpointer gtkMenuBox)
{
	/* Remove from container. */
	gtk_container_remove(GTK_CONTAINER(gtkMenuBox), widget);
}

sjme_errorCode sjme_scritchui_gtk2_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	GtkWindow* gtkWindow;
	GdkGeometry geometry;
	sjme_scritchui_dim* overhead;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (width <= 0 || height <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover window. */
	gtkWindow = inWindow->component.common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* The overhead has been calculated via windowContentGetFrame(). */
	overhead = &inWindow->minOverhead;
	
	/* Setup geometry. */
	memset(&geometry, 0, sizeof(geometry));
	geometry.min_width = width + overhead->width;
	geometry.min_height = height + overhead->height;
	geometry.base_width = width + overhead->width;
	geometry.base_height = height + overhead->height;
	
	/* Set minimum size. */
	gtk_window_set_geometry_hints(gtkWindow,
		GTK_WIDGET(gtkWindow),
		&geometry,
		GDK_HINT_MIN_SIZE | GDK_HINT_BASE_SIZE);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_windowGetFrame(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrOutNullable sjme_scritchui_dim* contentSize,
	sjme_attrOutNullable sjme_scritchui_rect* frameBound,
	sjme_attrOutNullable sjme_scritchui_rect* contentBound)
{
	sjme_scritchui_uiWindow inWindow;
	sjme_scritchui_rect resultFrame, resultContent;
	GtkWindow* gtkWindow;
	GtkWidget* menuBar;
	gint menuW, menuH;
	gint ox, oy;
	GtkAllocation alloc;
	GdkRectangle extent;
	
	if (inState == NULL || inContainer == NULL ||
		(contentSize == NULL && frameBound == NULL && contentBound == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover the window. */
	inWindow = SJME_SUI_CAST_WINDOW(inContainer);
	if (inWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Clear results. */
	memset(&resultFrame, 0, sizeof(resultFrame));
	memset(&resultContent, 0, sizeof(resultContent));
	
	/* Recover window. */
	gtkWindow = inWindow->component.common.handle[SJME_SUI_GTK2_H_WIDGET];
	menuBar = inWindow->component.common.handle[SJME_SUI_GTK2_H_WINBAR];
	
	/* Calculate window overhead, we can only consider this if there is */
	/* a menu bar that would add overhead. */
	/* Note that the menu bar could be hidden with a global menu. */
	menuW = 0;
	menuH = 0;
	if (menuBar != NULL)
	{
		/* Get the size allocation of the menu bar. */
		memset(&alloc, 0, sizeof(alloc));
		gtk_widget_get_size_request(menuBar, &menuW, &menuH);
		gtk_widget_get_allocation(menuBar, &alloc);
		
		/* Use the greater of the two bounds for the menu. */
		menuH = sjme_max(menuH, alloc.height);
	}
	
	/* Get the frame extents of the window. */
	memset(&extent, 0, sizeof(extent));
	ox = oy = 0;
	if (GTK_WIDGET(gtkWindow)->window != NULL)
	{
		/* Get the frame extents, from the window manager. */
		/* EXTENT 836 379 248 378 -- ORIGIN 840 432 */
		gdk_window_get_frame_extents(GTK_WIDGET(gtkWindow)->window,
			&extent);
		
		/* Then get the origin of where our actual window is in terms */
		/* that GTK uses. */
		/* EXTENT 836 379 248 378 -- ORIGIN 840 432 */
		memset(&alloc, 0, sizeof(alloc));
		gdk_window_get_origin(GTK_WIDGET(gtkWindow)->window,
			&ox, &oy);
		gtk_widget_get_allocation(GTK_WIDGET(gtkWindow), &alloc);

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
		sjme_message("EXTENT %d %d %d %d -- ORIGIN %d %d",
			extent.x, extent.y, extent.width, extent.height, ox, oy);
#endif
		
		/* Calculate frame. */
		resultFrame.s.x = extent.x;
		resultFrame.s.y = extent.y;
		resultFrame.d.width = extent.width;
		resultFrame.d.height = extent.height;
		
		/* Calculate base content. */
		resultContent.s.x = ox;
		resultContent.s.y = oy;
		resultContent.d.width = alloc.width;
		resultContent.d.height = alloc.height;
		
		/* Is there a menu to consider? */
		if (menuH > 0)
		{
			resultContent.s.y += menuH;
			resultContent.d.height -= menuH;
		}
	}
	
	/* Give the results. */
	if (frameBound != NULL)
		memmove(frameBound, &resultFrame, sizeof(resultFrame));
	if (contentBound != NULL)
		memmove(contentBound, &resultContent, sizeof(resultContent));
	if (contentSize != NULL)
		memmove(contentSize, &resultContent.d, sizeof(resultContent.d));
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_pointer ignored)
{
	GtkWindow* gtkWindow;
	GtkTable* gtkTable;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We only care for top-level windows. */
	gtkWindow = (GtkWindow*)gtk_window_new(GTK_WINDOW_TOPLEVEL);
	if (gtkWindow == NULL)
		goto fail_newWindow;
	
	/* Table for menu bar and main content. */
	gtkTable = (GtkTable*)gtk_table_new(2, 1, FALSE);
	if (gtkTable == NULL)
		goto fail_newTable;
	
	/* Setup window. */
	inWindow->component.common.handle[SJME_SUI_GTK2_H_WIDGET] = gtkWindow;
	inWindow->component.common.handle[SJME_SUI_GTK2_H_WINTABLE] = gtkTable;

	/* Since the table will soon be referenced, we do not want to lose it. */
	g_object_ref(gtkTable);
	
	/* The table needs to be in the window. */
	gtk_container_add(GTK_CONTAINER(gtkWindow),
		GTK_WIDGET(gtkTable));
		
	/* The widgets need to be shown, otherwise they stay invisible. */
	gtk_widget_show(GTK_WIDGET(gtkTable));
	
	/* Common widget init. */
	inState->implIntern->widgetInit(inState, GTK_WIDGET(gtkWindow));
	inState->implIntern->widgetInit(inState, GTK_WIDGET(gtkTable));
	
	/* Set default title. */
	gtk_window_set_title(gtkWindow,
		inState->wmInfo->defaultTitle);
	
	/* Need to set window class for consistency. */
	gtk_window_set_wmclass(gtkWindow,
		inState->wmInfo->xwsClass,
		inState->wmInfo->xwsClass);
	
	/* Use the global accelerator group for menu items. */
	gtk_window_add_accel_group(gtkWindow, GTK_ACCEL_GROUP(
		inState->common.handle[SJME_SUI_GTK2_H_ACCELG]));
	
	/* Set visibility change listener, which requires some logic. */
	g_signal_connect(gtkWindow, "visibility-notify-event",
		G_CALLBACK(sjme_scritchui_gtk2_eventVisibilityNotify), inWindow);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
	
fail_newTable:
	if (gtkTable != NULL)
		gtk_widget_destroy(GTK_WIDGET(gtkTable));
fail_newWindow:
	if (gtkWindow != NULL)
		gtk_widget_destroy(GTK_WIDGET(gtkWindow));
	return SJME_ERROR_CANNOT_CREATE;
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
	gtkWindow = inWindow->component.common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inWindow, close);
	
	/* Basic signal connection. */
	return inState->implIntern->reconnectSignal(inState,
		GTK_WIDGET(gtkWindow),
		inWindow,
		(sjme_scritchui_listener_void*)infoCore,
		(sjme_undefinedFunction)inListener,
		copyFrontEnd,
		G_CALLBACK(sjme_scritchui_gtk2_eventDelete),
		SJME_JNI_FALSE,
		1, "delete-event");
}

sjme_errorCode sjme_scritchui_gtk2_windowSetFlags(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_jint setFlags,
	sjme_attrOutNullable sjme_jint* actualFlags)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_gtk2_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar)
{
	sjme_errorCode error;
	GtkWindow* gtkWindow;
	GdkWindow* windowGdkWindow; 
	GtkMenuBar* gtkMenuBar;
	GtkMenuBar* gtkExistingBar;
	GtkTable* gtkTable;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover window and possibly the menu bar. */
	gtkWindow = inWindow->component.common.handle[SJME_SUI_GTK2_H_WIDGET];
	gtkTable = inWindow->component.common.handle[SJME_SUI_GTK2_H_WINTABLE];
	gtkExistingBar = inWindow->component.common.handle[SJME_SUI_GTK2_H_WINBAR];
	if (inMenuBar == NULL)
		gtkMenuBar = NULL;
	else
		gtkMenuBar = inMenuBar->menuKind.common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* If a menu bar is already here, remove it */
	if (gtkExistingBar != NULL)
	{
		/* Remove. */
		gtk_container_remove(GTK_CONTAINER(gtkTable),
			GTK_WIDGET(gtkExistingBar));
			
		/* Clear state. */
		inWindow->component.common.handle[SJME_SUI_GTK2_H_WINBAR] = NULL;

		/* Do not care for this menu bar anymore. */
		g_object_unref(gtkExistingBar);
	}
	
	/* Place into the table at the top. */
	if (gtkMenuBar != NULL)
	{
		/* Attach to top of table, do not allow the menu to shrink on the */
		/* Y axis. */
		gtk_table_attach(GTK_TABLE(gtkTable),
			GTK_WIDGET(gtkMenuBar),
			0, 1, 0, 1,
			GTK_FILL | GTK_EXPAND,
			0,
			0, 0);
		
		/* Show the menu bar. */
		gtk_widget_show(GTK_WIDGET(gtkMenuBar));
		
		/* Remember this bar for future changes. */
		inWindow->component.common.handle[SJME_SUI_GTK2_H_WINBAR] = gtkMenuBar;
		
		/* Reference the bar as it is being used. */
		g_object_ref(gtkMenuBar);
		
		/* If a minimum size is set and the window is visible, this */
		/* needs to be recalculated. */
		windowGdkWindow = gtk_widget_get_window(GTK_WIDGET(gtkWindow));
		if (windowGdkWindow != NULL &&
			inWindow->min.width != 0 && inWindow->min.height != 0)
			if (gdk_window_is_viewable(windowGdkWindow) ||
				gdk_window_is_visible(windowGdkWindow))
				if (sjme_error_is(error = inState->apiInThread
					->windowContentMinimumSize(inState, inWindow,
						inWindow->min.width, inWindow->min.height)))
					return sjme_error_default(error);
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_windowSetState(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_windowState setState,
	sjme_attrOutNullable sjme_scritchui_windowState* actualState)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_gtk2_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
#define SJME_SUI_GTK2_MAX_ATTEMPTS 256
	GtkWindow* gtkWindow;
	GdkWindow* gdkWindow;
	int attempt;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	gtkWindow = inWindow->component.common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Hide or show it. */
	if (!isVisible)
		gtk_widget_hide(GTK_WIDGET(gtkWindow));
	else
	{
		/* When the window is exposed, set the minimum size here. */
		g_signal_connect(gtkWindow, "expose-event",
			G_CALLBACK(sjme_scritchui_gtk2_eventWindowExpose), inWindow);
		
		/* Present the window */
		gtk_window_present(GTK_WINDOW(gtkWindow));
		
		/* Wait for the window to be visible and viewable, we need to do */
		/* this as there can be a race condition where the window is not */
		/* yet fully on screen, which can break things on remote/indirect */
		/* connections. */
		gdkWindow = gtk_widget_get_window(GTK_WIDGET(gtkWindow));
		for (attempt = 0; attempt < SJME_SUI_GTK2_MAX_ATTEMPTS &&
			(!gdk_window_is_viewable(gdkWindow) || 
				!gdk_window_is_visible(gdkWindow)); attempt++)
			sjme_thread_yield();
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
#undef SJME_SUI_GTK2_MAX_ATTEMPTS
}
