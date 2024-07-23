/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/core.h"
#include "sjme/alloc.h"
#include "lib/scritchui/core/coreGeneric.h"

sjme_errorCode sjme_scritchui_core_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar)
{
	if (inState == NULL || outMenuBar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_commonNew(inState,
		(sjme_scritchui_uiCommon*)outMenuBar,
		sizeof(**outMenuBar),
		SJME_SCRITCHUI_TYPE_MENU_BAR,
		(sjme_scritchui_coreGeneric_commonNewImplFunc)
			inState->impl->menuBarNew);
}

sjme_errorCode sjme_scritchui_core_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	sjme_errorCode error;
	sjme_scritchui_uiMenuHasChildren parentMenu;
	sjme_list_sjme_scritchui_uiMenuHasParent* childList;
	sjme_scritchui_uiMenuHasParent childMenu;
	sjme_jint i, n;
	
	if (inState == NULL || intoMenu == NULL || childItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (atIndex < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Get parent information. */
	if (sjme_error_is(error = inState->intern->getMenuHasChildren(
		inState, intoMenu, &parentMenu) ||
		parentMenu == NULL))
		return sjme_error_default(error);
	
	/* Get child information. */
	if (sjme_error_is(error = inState->intern->getMenuHasParent(
		inState, intoMenu, &childMenu) ||
		parentMenu == NULL))
		return sjme_error_default(error);
	
	/* Already has a parent? */
	if (childMenu->parent != NULL)
		return SJME_ERROR_HAS_PARENT;
	
	/* Out of bounds? */
	childList = parentMenu->children;
	n = (childList == NULL ? 0 : childList->length);
	if (atIndex > n)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Make sure child is not already in here. */
	if (childList != NULL)
		for (i = 0; i < n; i++)
			if (childList->elements[i] == childItem)
				return SJME_ERROR_MEMBER_EXISTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem)
{
	if (inState == NULL || outMenuItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_commonNew(inState,
		(sjme_scritchui_uiCommon*)outMenuItem,
		sizeof(**outMenuItem),
		SJME_SCRITCHUI_TYPE_MENU_ITEM,
		(sjme_scritchui_coreGeneric_commonNewImplFunc)
			inState->impl->menuItemNew);
}

sjme_errorCode sjme_scritchui_core_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu)
{
	if (inState == NULL || outMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_commonNew(inState,
		(sjme_scritchui_uiCommon*)outMenu,
		sizeof(**outMenu),
		SJME_SCRITCHUI_TYPE_MENU,
		(sjme_scritchui_coreGeneric_commonNewImplFunc)
			inState->impl->menuNew);
}

sjme_errorCode sjme_scritchui_core_intern_getMenuHasChildren(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind inMenuKind,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuHasChildren* outHasChildren)
{
	if (inState == NULL || inMenuKind == NULL || outHasChildren == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_intern_getMenuHasParent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind inMenuKind,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuHasParent* outHasParent)
{
	if (inState == NULL || inMenuKind == NULL || outHasParent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
