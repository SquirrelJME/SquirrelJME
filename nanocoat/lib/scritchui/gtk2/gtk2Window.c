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
	sjme_errorCode error;
	GtkWindow* gtkWindow;
	GtkWidget* menuBar;
	GdkGeometry geometry;
	sjme_scritchui_dim* overhead;
	GtkAllocation alloc;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (width <= 0 || height <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover window. */
	gtkWindow = inWindow->component.common.handle[SJME_SUI_GTK2_H_WIDGET];
	menuBar = inWindow->component.common.handle[SJME_SUI_GTK2_H_WINBAR];
	
	/* Calculate window overhead, we can only consider this if there is */
	/* a menu bar that would add overhead. */
	overhead = &inWindow->minOverhead;
	memset(overhead, 0, sizeof(*overhead));
	if (menuBar != NULL)
	{
		/* Get the size allocation of the menu bar. */
		memset(&alloc, 0, sizeof(alloc));
		gtk_widget_get_allocation(menuBar, &alloc);
		
		/* Add its height. */
		overhead->height = alloc.height;
	}
	
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
		inListener,
		copyFrontEnd,
		G_CALLBACK(sjme_scritchui_gtk2_eventDelete),
		SJME_JNI_FALSE,
		1, "delete-event");
}

sjme_errorCode sjme_scritchui_gtk2_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar)
{
	GtkWindow* gtkWindow;
	GtkMenuBar* gtkMenuBar;
	GtkMenuBar* gtkExistingBar;
	GtkTable* gtkTable;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NONE;
		
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
	}
	
	/* Place into the table at the top. */
	if (gtkMenuBar != NULL)
	{
		/* Attach to top of table. */
		gtk_table_attach(GTK_TABLE(gtkTable),
			GTK_WIDGET(gtkMenuBar),
			0, 1, 0, 1,
			GTK_FILL | GTK_EXPAND,
			GTK_SHRINK,
			0, 0);
		
		/* Show the menu bar. */
		gtk_widget_show(GTK_WIDGET(gtkMenuBar));
		
		/* Remember this bar for future changes. */
		inWindow->component.common.handle[SJME_SUI_GTK2_H_WINBAR] = gtkMenuBar;
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	GtkWindow* gtkWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover window. */
	gtkWindow = inWindow->component.common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Show or hide it. */
	if (isVisible)
		gtk_window_present(GTK_WINDOW(gtkWindow));
	else
		gtk_widget_hide(GTK_WIDGET(gtkWindow));
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
