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

sjme_errorCode sjme_scritchui_gtk2_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel)
{
	sjme_scritchui_uiPanel result;
	sjme_errorCode error;
	GtkWidget* widget;

	if (inState == NULL || outPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool, sizeof(*result),
		&result)) || result == NULL)
		return sjme_error_default(error);
	
	/* Setup GTK widget, used fixed as we want exact placements. */
	widget = NULL;
	if ((widget = gtk_fixed_new()) == NULL)
		goto fail_gtkWidget;
	
	/* Store information. */
	result->component.common.handle = widget;
	
	/* Success! */
	*outPanel = result;
	return SJME_ERROR_NONE;
	
fail_gtkWidget:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}
