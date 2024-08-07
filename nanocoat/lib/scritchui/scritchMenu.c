/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

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
		return sjme_error_notImplemented(0);
	
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
	
	/* Recount index position. */
	for (i = 0; i <= n; i++)
		childList->elements[i]->index = i;
	
	/* Forward to native implementation. */
	return inState->impl->menuInsert(inState, intoMenu, atIndex, childItem);
}

sjme_errorCode sjme_scritchui_core_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem)
{
	sjme_scritchui_impl_initParamMenuItem init;
	
	if (inState == NULL || outMenuItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set whatever next menu ID there is. */
	memset(&init, 0, sizeof(init));
	init.opaqueId = ++inState->nextMenuItemId;
		
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_commonNew(inState,
		(sjme_scritchui_uiCommon*)outMenuItem,
		sizeof(**outMenuItem),
		SJME_SCRITCHUI_TYPE_MENU_ITEM,
		(sjme_scritchui_coreGeneric_commonNewImplFunc)
			inState->impl->menuItemNew,
		&init);
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
		return sjme_error_notImplemented(0);
	
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

sjme_errorCode sjme_scritchui_intern_menuItemActivate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind atRover,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind itemActivated)
{
	sjme_errorCode error;
	sjme_scritchui_uiMenuHasParent parent;
	sjme_scritchui_uiMenuBar menuBar;
	sjme_scritchui_uiWindow window;
	sjme_scritchui_listener_menuItemActivate* infoUser;
	
	if (inState == NULL || atRover == NULL || itemActivated == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Check to see if this has a parent. */
	parent = NULL;
	if (sjme_error_is(error = inState->intern->getMenuHasParent(inState,
		atRover, &parent)) || parent == NULL)
	{
		/* There is no parent possible, so we might be at the top level. */ 
		if (error == SJME_ERROR_INVALID_ARGUMENT &&
			atRover->common.type == SJME_SCRITCHUI_TYPE_MENU_BAR)
		{
			menuBar = (sjme_scritchui_uiMenuBar)atRover;
			
			/* Activate menu item in the window. */
			window = menuBar->window;
			if (window != NULL)
			{
				/* Get callback. */
				infoUser = &SJME_SCRITCHUI_LISTENER_USER(window,
					menuItemActivate);
					
				/* Execute if available. */
				if (infoUser->callback != NULL)
					return infoUser->callback(inState, window,
						itemActivated);
			}
			
			/* Ignore otherwise. */
			return SJME_ERROR_NONE;
		}
		
		/* Failed! */
		return sjme_error_default(error);
	}
	
	/* It does, so if it has a parent, recurse up the chain! */
	if (parent->parent != NULL)
		return inState->intern->menuItemActivate(inState,
			parent->parent, itemActivated);
	
	/* It does not, so stop. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_intern_menuItemActivateById(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind atRover,
	sjme_attrInNotNull sjme_jint itemActivated,
	sjme_attrInValue sjme_jint itemMask)
{
	sjme_errorCode error;
	sjme_scritchui_listener_menuItemActivate* infoCore;
	sjme_scritchui_uiMenuHasChildren child;
	sjme_scritchui_uiMenuItem menuItem;
	sjme_jint i, n, opaqueId;
	sjme_scritchui_uiMenuKind* children;
	sjme_scritchui_uiMenuKind tryChild;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (itemMask == 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
#if 0
	/* Debug. */
	sjme_message("menuItemActivateById(%p, %p, %p, %d)",
		inState, inWindow, atRover, itemActivated);
#endif
	
	/* If the window has no listener or no bar, ignore. */
	infoCore = &SJME_SCRITCHUI_LISTENER_USER(inWindow, menuItemActivate);
	if (infoCore->callback == NULL || inWindow->menuBar == NULL)
		return SJME_ERROR_NONE;
	
	/* Check to see if this has a child. */
	child = NULL;
	if (sjme_error_is(error = inState->intern->getMenuHasChildren(inState,
		atRover, &child)) || child == NULL)
	{
		/* There is no child, so this is a menu item. */ 
		if (error == SJME_ERROR_INVALID_ARGUMENT &&
			atRover->common.type == SJME_SCRITCHUI_TYPE_MENU_ITEM)
		{
			/* Recover item. */
			menuItem = (sjme_scritchui_uiMenuItem)atRover;
			
			/* Forward to callback if this is the item. */
			opaqueId = menuItem->opaqueId;
			if (opaqueId != 0 &&
				(opaqueId & itemMask) == (itemActivated & itemMask))
				return infoCore->callback(inState, inWindow,
					(sjme_scritchui_uiMenuKind)menuItem);
			
			/* Continue otherwise. */
			return SJME_ERROR_CONTINUE;
		}
		
		/* Some other kind of menu item? */
		if (error == SJME_ERROR_INVALID_ARGUMENT)
			return SJME_ERROR_CONTINUE;
		
		/* Failed. */
		return sjme_error_default(error);
	}
	
	/* Make copy of children, so it does not change in the middle. */
	n = child->numChildren;
	children = sjme_alloca(sizeof(*children) * n);
	if (children == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memmove(children, child->children->elements,
		sizeof(*children) * n);
	
	/* Go through each child. */
	for (i = 0; i < n; i++)
	{
		/* Get child here. */
		tryChild = children[i];
		
		/* Recursive search for the menu command. */
		error = inState->intern->menuItemActivateById(inState, inWindow,
			tryChild, itemActivated, itemMask);
		
		/* Try next child? */
		if (error == SJME_ERROR_CONTINUE)
			continue;
		
		/* Fail? */
		else if (sjme_error_is(error))
			return sjme_error_default(error);
		
		/* We found the menu item. */
		else
			break;
	}
	
	/* Nothing left to check. */
	return SJME_ERROR_NONE;
}
