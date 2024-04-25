/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"

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
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow)
{
	sjme_errorCode error;
	sjme_scritchui_uiWindow window;
	GtkWindow* gtkWindow;
	
	if (inState == NULL || outWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate resultant window. */
	window = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool, sizeof(*window),
		&window)) ||
		window == NULL)
		goto fail_alloc;
	
	/* We only care for top-level windows. */
	gtkWindow = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	if (gtkWindow == NULL)
	{
		error = SJME_ERROR_CANNOT_CREATE;
		goto fail_newWindow;
	}
	
	/* Setup window. */
	window->component.common.type = SJME_SCRITCHUI_TYPE_WINDOW;
	window->component.common.handle = gtkWindow;
	
	/* Set default title. */
	gtk_window_set_title(gtkWindow,
		inState->wmInfo->defaultTitle);
	
	/* Need to set window class for consistency. */
	gtk_window_set_wmclass(gtkWindow,
		inState->wmInfo->xwsClass,
		inState->wmInfo->xwsClass);
	
	/* Success! */
	*outWindow = window;
	return SJME_ERROR_NONE;

fail_newWindow:
fail_alloc:
	if (window != NULL)
	{
		sjme_alloc_free(window);
		window = NULL;
	}
	
	return sjme_error_default(error);
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
	
	/* Success! */
	return SJME_ERROR_NONE;
}
