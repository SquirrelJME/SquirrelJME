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
			inState->impl->menuBarNew,
		NULL);
}

sjme_errorCode sjme_scritchui_core_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	sjme_errorCode error;
	sjme_list_sjme_scritchui_uiMenuKind* childList;
	sjme_list_sjme_scritchui_uiMenuKind* newList;
	sjme_scritchui_uiMenuHasChildren parentMenu;
	sjme_scritchui_uiMenuHasParent childMenu;
	sjme_jint i, o, n;
	
	if (inState == NULL || intoMenu == NULL || childItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (atIndex < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Get parent information. */
	parentMenu = NULL;
	if (sjme_error_is(error = inState->intern->getMenuHasChildren(
		inState, SJME_SUI_CAST_MENU_KIND(intoMenu),
		&parentMenu)) ||
		parentMenu == NULL)
		return sjme_error_default(error);
	
	/* Get child information. */
	childMenu = NULL;
	if (sjme_error_is(error = inState->intern->getMenuHasParent(
		inState, SJME_SUI_CAST_MENU_KIND(childItem),
		&childMenu)) ||
		childMenu == NULL)
		return sjme_error_default(error);
	
	/* Already has a parent? */
	if (childMenu->parent != NULL)
		return SJME_ERROR_HAS_PARENT;
	
	/* Out of bounds? */
	childList = parentMenu->children;
	n = parentMenu->numChildren;
	if (atIndex > n)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Make sure child is not already in here. */
	if (childList != NULL)
		for (i = 0; i < n; i++)
			if (SJME_SUI_CAST_MENU_KIND(childList->elements[i]) ==
				SJME_SUI_CAST_MENU_KIND(childItem))
				return SJME_ERROR_MEMBER_EXISTS;
	
	/* Not implemented? Fail here so we go through all validity checks */
	/* first. */
	if (inState->impl->menuInsert == NULL)
		return sjme_error_notImplemented();
	
	/* Do we need to make a new list? */
	if (childList == NULL || (n + 1) > childList->length)
	{
		/* Setup new list. */
		newList = NULL;
		if (sjme_error_is(error = sjme_list_alloc(inState->pool, n + 1,
			&newList, sjme_scritchui_uiMenuKind, 0)) || newList == NULL)
			return sjme_error_default(error);
		
		/* Copy all items over? */
		if (childList != NULL)
			for (i = 0; i < n; i++)
				newList->elements[i] = childList->elements[i];
		
		/* Use this list. */
		parentMenu->children = newList;
		
		/* Free old list. */
		if (childList != NULL)
		{
			if (sjme_error_is(error = sjme_alloc_free(childList)))
				return sjme_error_default(error);
			childList = NULL;
		}
		
		/* We are using this list now. */
		childList = newList;
	}
	
	/* Move entries up. */
	for (i = n - 1, o = n; i > atIndex; i--, o--)
		childList->elements[o] = childList->elements[i];
	childList->elements[atIndex] = childItem;
	
	/* List is now this big. */
	parentMenu->numChildren = n + 1;
	
	/* Associate parent. */
	childMenu->parent = SJME_SUI_CAST_MENU_KIND(intoMenu);
	
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
			inState->impl->menuItemNew,
		NULL);
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
			inState->impl->menuNew,
		NULL);
}

sjme_errorCode sjme_scritchui_core_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex)
{
	sjme_errorCode error;
	sjme_scritchui_uiMenuHasChildren parentMenu;
	sjme_scritchui_uiMenuHasParent childMenu;
	sjme_list_sjme_scritchui_uiMenuKind* childList;
	sjme_scritchui_uiMenuKind childAt;
	sjme_jint i, o, n;
	
	if (inState == NULL || fromMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (atIndex < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Get parent information. */
	parentMenu = NULL;
	if (sjme_error_is(error = inState->intern->getMenuHasChildren(
		inState, SJME_SUI_CAST_MENU_KIND(fromMenu),
		&parentMenu)) ||
		parentMenu == NULL)
		return sjme_error_default(error);
	
	/* Not implemented? */
	if (inState->impl->menuRemove == NULL)
		return sjme_error_notImplemented();
	
	/* Empty? */
	childList = parentMenu->children;
	if (childList == NULL)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Limit to the actual number of children. */
	n = parentMenu->numChildren;
	
	/* Out of bounds? */
	if (atIndex >= n)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Obtain the child menu. */
	childAt = childList->elements[atIndex];
	childMenu = NULL;
	if (sjme_error_is(error = inState->intern->getMenuHasParent(
		inState, SJME_SUI_CAST_MENU_KIND(childAt),
		&childMenu)) || childMenu == NULL)
		return sjme_error_default(error);
	
	/* Nothing here? */
	if (SJME_SUI_CAST_MENU_KIND(childAt) == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Call native removal of item. */
	if (sjme_error_is(error = inState->impl->menuRemove(inState,
		fromMenu, atIndex)))
		return sjme_error_default(error);
	
	/* Disassociate. */
	childMenu->parent = NULL;

	/* Remove from list and move down everything. */
	for (o = atIndex, i = atIndex + 1; i < n;)
		childList->elements[o++] = childList->elements[i++];
	childList->elements[n - 1] = NULL;
	
	/* Count down. */
	parentMenu->numChildren = n - 1;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_menuRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu)
{
	sjme_errorCode error;
	sjme_scritchui_uiMenuHasChildren parentMenu;
	
	if (inState == NULL || fromMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get parent information. */
	if (sjme_error_is(error = inState->intern->getMenuHasChildren(
		inState, SJME_SUI_CAST_MENU_KIND(fromMenu),
		&parentMenu)) || parentMenu == NULL)
		return sjme_error_default(error);
	
	/* Keep clearing out until nothing is left. */
	while (parentMenu->numChildren > 0)
	{
		/* Double check, there should be an item here. */
		if (SJME_SUI_CAST_MENU_KIND(parentMenu->children->elements[0]) == NULL)
			return SJME_ERROR_ILLEGAL_STATE;
		
		/* Remove it. */
		if (sjme_error_is(error = inState->api->menuRemove(inState,
			fromMenu, 0)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_getMenuHasChildren(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind inMenuKind,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuHasChildren* outHasChildren)
{
	sjme_scritchui_uiMenuHasChildren result;
	
	if (inState == NULL || inMenuKind == NULL || outHasChildren == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Check. */
	if (SJME_SUI_CAST_MENU_KIND(inMenuKind) == NULL)
		return SJME_ERROR_INVALID_ARGUMENT;

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
	
	/* Check. */
	if (SJME_SUI_CAST_MENU_KIND(inMenuKind) == NULL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
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
