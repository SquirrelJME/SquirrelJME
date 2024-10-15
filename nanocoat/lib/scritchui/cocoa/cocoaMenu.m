/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/cocoa/cocoa.h"
#include "lib/scritchui/cocoa/cocoaIntern.h"

@implementation SJMEMenu : NSMenu
- (id)init
{
	return [super init];
}

@end

@implementation SJMEMenuItem : NSMenuItem
- (id)init
{
	return [super init];
}

@end

sjme_errorCode sjme_scritchui_cocoa_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar,
	sjme_attrInNullable sjme_pointer ignored)
{
	SJMEMenu* cocoaMenu;
	SJMEMenuItem* cocoaMenuItem;

	if (inState == NULL || inMenuBar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Menu bars in Cocoa are just associated with the NSApp but themselves */
	/* are just a menu and menu item. */
	cocoaMenu = [SJMEMenu new];
	cocoaMenuItem = [SJMEMenuItem new];

	/* The first is always the menu. */
	[cocoaMenu addItem:cocoaMenuItem];

	/* Store it. */
	inMenuBar->menuKind.common.handle[SJME_SUI_COCOA_H_NSVIEW] = cocoaMenu;
	inMenuBar->menuKind.common.handle[SJME_SUI_COCOA_H_NSVIEWB] =
		cocoaMenuItem;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	SJMEMenu* cocoaMenu;
	SJMEMenuItem* cocoaMenuItem;

	if (inState == NULL || intoMenu == NULL || childItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Only menus can be added into. */
	if (intoMenu->common.type != SJME_SCRITCHUI_TYPE_MENU_BAR &&
		intoMenu->common.type != SJME_SCRITCHUI_TYPE_MENU)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Recover menu to add into. */
	cocoaMenu = intoMenu->common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Recover item to add. */
	if (childItem->common.type == SJME_SCRITCHUI_TYPE_MENU_ITEM)
		cocoaMenuItem = intoMenu->common.handle[SJME_SUI_COCOA_H_NSVIEW];
	else
		cocoaMenuItem = intoMenu->common.handle[SJME_SUI_COCOA_H_NSVIEWB];

	/* Add it in. */
	[cocoaMenu addItem:cocoaMenuItem];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem,
	sjme_attrInNotNull const sjme_scritchui_impl_initParamMenuItem* init)
{
	SJMEMenuItem* cocoaMenuItem;

	if (inState == NULL || inMenuItem == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup new menu. */
	cocoaMenuItem = [SJMEMenuItem new];

	/* Store it. */
	inMenuItem->menuKind.common.handle[SJME_SUI_COCOA_H_NSVIEW] =
		cocoaMenuItem;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu,
	sjme_attrInNullable sjme_pointer ignored)
{
	SJMEMenu* cocoaMenu;
	SJMEMenuItem* cocoaMenuItem;

	if (inState == NULL || inMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup new menu, it needs an item as well. */
	cocoaMenu = [SJMEMenu new];
	cocoaMenuItem = [SJMEMenuItem new];

	/* The item's sub menu is the menu. */
	[cocoaMenuItem setSubmenu:cocoaMenu];

	/* Store it. */
	inMenu->menuKind.common.handle[SJME_SUI_COCOA_H_NSVIEW] = cocoaMenu;
	inMenu->menuKind.common.handle[SJME_SUI_COCOA_H_NSVIEWB] = cocoaMenuItem;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex)
{
	if (inState == NULL || fromMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Menu bars do not exist, so remove from them does nothing. */
	if (fromMenu->common.type == SJME_SCRITCHUI_TYPE_MENU_BAR)
		return inState->implIntern->checkError(inState,
			SJME_ERROR_NONE);

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
