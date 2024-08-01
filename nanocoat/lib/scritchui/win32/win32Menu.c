/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

sjme_errorCode sjme_scritchui_win32_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar,
	sjme_attrInNullable sjme_pointer ignored)
{
	HMENU menu;
	
	if (inState == NULL || inMenuBar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Create new menu. */
	menu = CreateMenu();
	if (menu == NULL)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED);
	
	/* Store handle. */
	inMenuBar->menuKind.common.handle[SJME_SUI_WIN32_H_HMENU] = menu;
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	MENUITEMINFO itemInfo;
	sjme_lpcstr string;
	sjme_jint opaqueId;
	
	if (inState == NULL || intoMenu == NULL || childItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Is there an opaque ID? */
	opaqueId = 0;
	if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU_ITEM)
		opaqueId = ((sjme_scritchui_uiMenuItem)childItem)->opaqueId;
	
	/* Because menu items do not exist, we need to create one at runtime. */
	memset(&itemInfo, 0, sizeof(itemInfo));
	itemInfo.cbSize = sizeof(itemInfo);
	itemInfo.fMask = MIIM_DATA;
	itemInfo.dwItemData = (LONG_PTR)childItem;
	
	/* Is an opaque ID set? */
	if (opaqueId != 0)
	{
		itemInfo.fMask |= MIIM_ID;
		itemInfo.wID = opaqueId;
	}
	
	/* If adding a submenu we need to specify that. */
	if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU)
	{
		itemInfo.fMask |= MIIM_SUBMENU;
		itemInfo.hSubMenu = childItem->common.handle[SJME_SUI_WIN32_H_HMENU];
	}
	
	/* Try to get the string to add, if there is one */
	string = NULL;
	if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU)
		string = ((sjme_scritchui_uiMenu)childItem)->labeled.label;
	else if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU_ITEM)
		string = ((sjme_scritchui_uiMenuItem)childItem)->labeled.label;
	
	/* Is a string being added? */
	if (string != NULL)
	{
		itemInfo.fMask |= MIIM_STRING;
		itemInfo.dwTypeData = string;
	}
	
	/* Create the item. */
	if (0 == InsertMenuItem(
		intoMenu->common.handle[SJME_SUI_WIN32_H_HMENU],
		atIndex, TRUE, &itemInfo))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_CREATE_FAILED);
		
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem,
	sjme_attrInNotNull const sjme_scritchui_impl_initParamMenuItem* init)
{
	if (inState == NULL || inMenuItem == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just copy the ID over. */
	inMenuItem->opaqueId = init->opaqueId;
	
	/* Menu items in Win32 only exist when they are actually within a menu, */
	/* so essentially this does nothing on purpose. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu,
	sjme_attrInNullable sjme_pointer ignored)
{
	/* Exactly the same as menu bars. */
	return sjme_scritchui_win32_menuBarNew(inState, inMenu, ignored);
}

sjme_errorCode sjme_scritchui_win32_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex)
{
	if (inState == NULL || fromMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The item just gets removed. */
	if (0 == RemoveMenu(fromMenu->common.handle[SJME_SUI_WIN32_H_HMENU],
		atIndex, MF_BYPOSITION))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
