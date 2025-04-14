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
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus)
{
	GtkWidget* widget;
	
	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set focusable. */
	widget =
		inPanel->component.common.handle[SJME_SUI_GTK2_H_WIDGET];
	gtk_widget_set_can_focus(widget,
		SJME_JBOOLEAN_TO_GBOOLEAN(enableFocus));
	
	/* Set the default focus for this panel? */
	gtk_widget_set_can_default(widget,
		enableFocus && defaultFocus);
	if (enableFocus && defaultFocus)
		gtk_widget_grab_default(widget);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	sjme_errorCode error;
	GtkWidget* fixed;
	GtkWidget* eventBox;

	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup GTK widget, used fixed as we want exact placements. */
	fixed = NULL;
	if ((fixed = gtk_fixed_new()) == NULL)
		return SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED;
	
	/* And event box as well. */
	eventBox = NULL;
	if ((eventBox = gtk_event_box_new()) == NULL)
		return SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED;
	
	/* Does not have a window component. */
	gtk_fixed_set_has_window(GTK_FIXED(fixed), FALSE);
	
	/* Put fixed in the box. */
	gtk_container_add(GTK_CONTAINER(eventBox), fixed);
	
	/* Because the fixed is in the event box, it needs to be shown. */
	gtk_widget_show(fixed);
	
	/* Common widget init. */
	inState->implIntern->widgetInit(inState, GTK_WIDGET(fixed));
	
	/* Store information. */
	inPanel->component.common.handle[SJME_SUI_GTK2_H_WIDGET] = fixed;
	inPanel->component.common.handle[SJME_SUI_GTK2_H_TOP_WIDGET] = eventBox;
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
