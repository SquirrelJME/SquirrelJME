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

sjme_errorCode sjme_scritchui_gtk2_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_lpcstr inString)
{
	GtkWidget* gtkWidget;
	GtkWidget* gtkWidgetB;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover handle. */
	gtkWidget = inComponent->common.handle;
	gtkWidgetB = inComponent->common.handleB;
	
	/* Depends on the type. */
	switch (inComponent->common.type)
	{
			/* Menus consist of an item and an actual menu. */
		case SJME_SCRITCHUI_TYPE_MENU:
			gtk_menu_set_title(GTK_MENU(gtkWidget), inString);
			gtk_menu_item_set_label(GTK_MENU_ITEM(gtkWidgetB),
				inString);
			break;
		
		case SJME_SCRITCHUI_TYPE_MENU_ITEM:
			gtk_menu_item_set_label(GTK_MENU_ITEM(gtkWidget),
				inString);
			break;
		
			/* This is simple enough, nothing fancy. */
		case SJME_SCRITCHUI_TYPE_WINDOW:
			gtk_window_set_title(GTK_WINDOW(gtkWidget),
				(inString != NULL ? inString : "SquirrelJME"));
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
