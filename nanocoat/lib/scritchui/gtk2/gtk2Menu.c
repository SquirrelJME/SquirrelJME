/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gdk/gdkkeysyms.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
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
	inMenuBar->menuKind.common
		.handle[SJME_SUI_GTK2_H_WIDGET] = widget;
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	GtkWidget* intoWidget;
	GtkWidget* childWidget;
	
	if (inState == NULL || intoMenu == NULL || childItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get widgets for injection. */
	intoWidget = intoMenu->common.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* If we are adding a menu, we cannot add the menu itself as we need */
	/* to add the menu item that the menu associates with. */
	if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU)
		childWidget = childItem->common.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
	else
		childWidget = childItem->common
			.handle[SJME_SUI_GTK2_H_WIDGET];
	
	/* Attach accelerator, but only if a normal item. */
	if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU_ITEM)
		inState->implIntern->accelUpdate(inState, childItem,
			childWidget, SJME_JNI_TRUE);
	
	/* Anything that can be inserted into, is just a menu shell. */
	gtk_menu_shell_insert(GTK_MENU_SHELL(intoWidget),
		childWidget, atIndex);
	
	/* Menu items need to be shown, otherwise they will be invisible. */
	gtk_widget_show(intoWidget);
	gtk_widget_show(childWidget);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem)
{
	GtkWidget* widget;
	GtkAccelGroup* gtkAccel;
	
	if (inState == NULL || inMenuItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Create new item. */
	widget = gtk_menu_item_new();
	
	/* Store handle for later. */
	inMenuItem->menuKind.common
		.handle[SJME_SUI_GTK2_H_WIDGET] = widget;
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu)
{
	GtkWidget* menuWidget;
	GtkWidget* itemLike;
	
	if (inState == NULL || inMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Create new menu. */
	menuWidget = gtk_menu_new();
	
	/* All menu pieces are essentially items that get added into. */
	itemLike = gtk_menu_item_new();
	gtk_menu_item_set_submenu(GTK_MENU_ITEM(itemLike),
		menuWidget);

	/* This needs to be visible. */
	gtk_widget_show(itemLike);
	
	/* Store handle for later. */
	inMenu->menuKind.common
		.handle[SJME_SUI_GTK2_H_WIDGET] = menuWidget;
	inMenu->menuKind.common.handle[SJME_SUI_GTK2_H_TOP_WIDGET] = itemLike;
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_gtk2_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex)
{
	sjme_errorCode error;
	sjme_scritchui_uiMenuHasChildren parentMenu;
	sjme_scritchui_uiMenuKind childItem;
	GtkWidget* menuWidget;
	GtkWidget* removeWidget;
	
	if (inState == NULL || fromMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get parent information. */
	parentMenu = NULL;
	if (sjme_error_is(error = inState->intern->getMenuHasChildren(
		inState, fromMenu, &parentMenu)) ||
		parentMenu == NULL)
		return sjme_error_default(error);
	
	/* Recover widgets. */
	menuWidget = fromMenu->common.handle[SJME_SUI_GTK2_H_WIDGET];
	childItem = parentMenu->children->elements[atIndex];
	
	/* If we are removing a menu, we need to remove the item and not the */
	/* menu itself. */
	if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU)
		removeWidget = childItem->common.handle[SJME_SUI_GTK2_H_TOP_WIDGET];
	else
		removeWidget =
			childItem->common.handle[SJME_SUI_GTK2_H_WIDGET];
			
	/* Remove accelerator from item. */
	if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU_ITEM)
		inState->implIntern->accelUpdate(inState, childItem,
			removeWidget, SJME_JNI_FALSE);
	
	/* Remove item, note that this does not use any index based removal */
	/* but instead container based removal, so we just remove the handle. */
	gtk_container_remove(GTK_CONTAINER(menuWidget),
		removeWidget);
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
