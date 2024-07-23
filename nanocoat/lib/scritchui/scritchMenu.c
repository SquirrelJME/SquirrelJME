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
	sjme_list_sjme_scritchui_uiMenuHasParent* newList;
	sjme_scritchui_uiMenuHasParent childMenu;
	sjme_jint i, o, n;
	
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
	if (parentMenu->numChildren < n)
		n = parentMenu->numChildren;
	if (atIndex > n)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Make sure child is not already in here. */
	if (childList != NULL)
		for (i = 0; i < n; i++)
			if (childList->elements[i] == childItem)
				return SJME_ERROR_MEMBER_EXISTS;
	
	/* Not implemented? Fail here so we go through all validity checks */
	/* first. */
	if (inState->impl->menuInsert == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Setup new list to store the child in. */
	if (sjme_error_is(error = sjme_list_alloc(inState->pool, n + 1,
		&newList, sjme_scritchui_uiMenuHasParent, 0)) || newList == NULL)
		return sjme_error_default(error);
	
	/* Copy entire set over. */
	for (i = 0, o = 0; i < n; i++)
	{
		/* Inject here? */
		if (i == atIndex)
			newList->elements[o++] = childItem;
		
		/* Copy normal. */
		if (childList != NULL)
			newList->elements[o++] = childList->elements[i];
	}
	
	/* Use new list. */
	parentMenu->children = newList;
	parentMenu->numChildren = n + 1;
	
	/* Clear up old list. */
	if (childList != NULL)
		if (sjme_error_is(error = sjme_alloc_free(childList)))
			return sjme_error_default(error);
	
	/* Forward to native implementation. */
	return inState->impl->menuInsert(inState, intoMenu, atIndex, childItem);
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
	sjme_scritchui_uiMenuHasChildren result;
	
	if (inState == NULL || inMenuKind == NULL || outHasChildren == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Depends on the type. */
	result = NULL;
	switch (inMenuKind->common.type)
	{
		case SJME_SCRITCHUI_TYPE_MENU:
			result = &((sjme_scritchui_uiMenu)inMenuKind)->children;
			break;
			
		case SJME_SCRITCHUI_TYPE_MENU_BAR:
			result = &((sjme_scritchui_uiMenuBar)inMenuKind)->children;
			break;
		
			/* Invalid. */
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	*outHasChildren = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_getMenuHasParent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind inMenuKind,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuHasParent* outHasParent)
{
	sjme_scritchui_uiMenuHasParent result;
	
	if (inState == NULL || inMenuKind == NULL || outHasParent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Depends on the type. */
	result = NULL;
	switch (inMenuKind->common.type)
	{
		case SJME_SCRITCHUI_TYPE_MENU:
			result = &((sjme_scritchui_uiMenu)inMenuKind)->parent;
			break;
			
		case SJME_SCRITCHUI_TYPE_MENU_ITEM:
			result = &((sjme_scritchui_uiMenuItem)inMenuKind)->parent;
			break;
		
			/* Invalid. */
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	*outHasParent = result;
	return SJME_ERROR_NONE;
}
