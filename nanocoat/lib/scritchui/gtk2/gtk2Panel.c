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
	sjme_attrInOutNotNull sjme_scritchui_uiPanel inOutPanel)
{
	sjme_errorCode error;
	GtkWidget* widget;

	if (inState == NULL || inOutPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup GTK widget, used fixed as we want exact placements. */
	widget = NULL;
	if ((widget = gtk_fixed_new()) == NULL)
		goto fail_gtkWidget;
	
	/* Store information. */
	inOutPanel->component.common.handle = widget;
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_gtkWidget:
	return sjme_error_default(error);
}
