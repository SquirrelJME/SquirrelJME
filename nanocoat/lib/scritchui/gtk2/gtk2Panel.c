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

sjme_errorCode sjme_scritchui_gtk2_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus)
{
	GtkWidget* widget;
	
	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set focusable. */
	widget = inPanel->component.common.handle;
	gtk_widget_set_can_focus(widget,
		SJME_JBOOLEAN_TO_GBOOLEAN(enableFocus));
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel)
{
	sjme_errorCode error;
	GtkWidget* fixed;

	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup GTK widget, used fixed as we want exact placements. */
	fixed = NULL;
	if ((fixed = gtk_fixed_new()) == NULL)
		goto fail_gtkWidget;
	
	/* Does not have a window component. */
	gtk_fixed_set_has_window(GTK_FIXED(fixed), FALSE);
	
	/* Common widget init. */
	inState->implIntern->widgetInit(GTK_WIDGET(fixed));
	
	/* Store information. */
	inPanel->component.common.handle = fixed;
	
	/* For this panel, we want to handle these events. */
	gtk_widget_add_events(GTK_WIDGET(fixed),
		GDK_POINTER_MOTION_MASK |
		GDK_BUTTON_PRESS_MASK |
		GDK_BUTTON_RELEASE_MASK |
		GDK_KEY_PRESS_MASK |
		GDK_KEY_RELEASE_MASK |
		GDK_BUTTON_MOTION_MASK);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_gtkWidget:
	return sjme_error_default(error);
}
