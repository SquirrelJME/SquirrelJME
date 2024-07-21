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

sjme_errorCode sjme_scritchui_gtk2_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar)
{
	GtkWidget* widget;
	
	if (inState == NULL || inMenuBar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Create new bar. */
	widget = gtk_menu_bar_new();
	
	/* Store handle for later. */
	inMenuBar->common.handle = widget;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem)
{
	GtkWidget* widget;
	
	if (inState == NULL || inMenuItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Create new item. */
	widget = gtk_menu_item_new();
	
	/* Store handle for later. */
	inMenuItem->common.handle = widget;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu)
{
	GtkWidget* widget;
	
	if (inState == NULL || inMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Create new menu. */
	widget = gtk_menu_new();
	
	/* Store handle for later. */
	inMenu->common.handle = widget;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
