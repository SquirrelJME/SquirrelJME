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

@end

sjme_errorCode sjme_scritchui_cocoa_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar,
	sjme_attrInNullable sjme_pointer ignored)
{
	if (inState == NULL || inMenuBar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Menus bars are not a thing in Cocoa, there is the main application */
	/* menu but that is a menu itself. We just let ScritchUI track the */
	/* menus that should be here. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_cocoa_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	if (inState == NULL || intoMenu == NULL || childItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Menu bars do not exist, so inserting into them does nothing. */
	if (intoMenu->common.type == SJME_SCRITCHUI_TYPE_MENU_BAR)
		return SJME_ERROR_NONE;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_cocoa_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu,
	sjme_attrInNullable sjme_pointer ignored)
{
	SJMEMenu* cocoaMenu;

	if (inState == NULL || inMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup new menu. */
	cocoaMenu = [SJMEMenu new];

	/* Store it. */
	inMenu->menuKind.common.handle[SJME_SUI_COCOA_H_NSVIEW] = cocoaMenu;

	/* Success! */
	return SJME_ERROR_NONE;
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
		return SJME_ERROR_NONE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
