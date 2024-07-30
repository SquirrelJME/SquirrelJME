/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gdk/gdk.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_gtk2_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScrollPanel inScrollPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	GtkScrolledWindow* gtkScroll;
	
	if (inState == NULL || inScrollPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup new scroll. */
	gtkScroll = GTK_SCROLLED_WINDOW(gtk_scrolled_window_new(
		NULL, NULL));
	if (gtkScroll == NULL)
		return SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED;
	
	/* Common widget init. */
	inState->implIntern->widgetInit(inState, GTK_WIDGET(gtkScroll));
	
	/* Store information. */
	inScrollPanel->component.common.handle[SJME_SUI_GTK2_H_WIDGET] =
		gtkScroll;
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
