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
