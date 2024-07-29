/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"
#include "sjme/alloc.h"

sjme_errorCode sjme_scritchui_core_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent)
{
	sjme_scritchui_uiContainer container;
	sjme_list_sjme_scritchui_uiComponent* list;
	sjme_list_sjme_scritchui_uiComponent* newList;
	sjme_errorCode error;
	sjme_jint freeSlot, i, n;
	
	if (inState == NULL || inContainer == NULL || addComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cannot add a window to a container despite windows being components. */
	if (addComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Not implemented? */
	if (inState->impl->containerAdd == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Only certain types are containers. */
	if (sjme_error_is(error = inState->intern->getContainer(inState,
		inContainer, &container)) ||
		container == NULL)
		return sjme_error_default(error);
	
	/* Cannot add a component to multiple containers, they must be removed */
	/* manually for consistency purposes. */
	if (addComponent->parent != NULL)
		return SJME_ERROR_ALREADY_IN_CONTAINER;
	
	/* Find free slot in child set. */
	freeSlot = -1;
	list = container->components;
	n = (list == NULL ? 0 : list->length);
	if (list != NULL)
		for (i = 0; i < n; i++)
			if (list->elements[i] == NULL)
			{
				freeSlot = i;
				break;
			}
	
	/* No free slot? Need to alloc list... */
	if (freeSlot < 0)
	{
		/* Setup new list. */
		newList = NULL;
		if (sjme_error_is(error = sjme_list_alloc(inState->pool, n + 4,
			&newList, sjme_scritchui_uiComponent, 0)) || newList == NULL)
			return sjme_error_default(error);
		
		/* Need to copy everything over? */
		if (list != NULL)
			for (i = 0; i < n; i++)
				newList->elements[i] = list->elements[i];
		
		/* Free slot is at the end. */
		freeSlot = n;
		
		/* Set new list. */
		container->components = newList;
		
		/* Free old list. */
		sjme_alloc_free(list);
		
		/* Need to use this one! */
		list = newList;
	}
	
	/* Set free slot in component. */
	list->elements[freeSlot] = addComponent;
	
	/* Forward call. */	
	if (sjme_error_is(error = inState->impl->containerAdd(inState,
		inContainer, container,
		addComponent)))
	{
		/* Undo. */
		list->elements[freeSlot] = NULL;
		
		return sjme_error_default(error);
	}
	
	/* Update parent. */
	addComponent->parent = inContainer;
	
	/* If a component is added to a container which is actually visible */
	/* but the native UI system does not support visibility at this level */
	/* then we need to call the visibility update ourselves. */
	/* That is at the subcomponent granularity and not windows themselves. */
	if (inState->impl->componentSetVisibleListener == NULL)
		if (inContainer->state.isVisible || inContainer->state.isUserVisible)
			inState->intern->updateVisibleComponent(inState,
				addComponent, SJME_JNI_TRUE);
		
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_containerRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent)
{
	sjme_errorCode error;
	sjme_scritchui_uiContainer container;
	sjme_list_sjme_scritchui_uiComponent* list;
	sjme_jint i, n;
	
	if (inState == NULL || inContainer == NULL || removeComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->containerRemove == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
		
	/* Only certain types are containers. */
	if (sjme_error_is(error = inState->intern->getContainer(inState,
		inContainer, &container)) ||
		container == NULL)
		return sjme_error_default(error);
	
	/* Get component list, if nothing then it is not here ever. */
	list = container->components;
	if (list == NULL)
		return SJME_ERROR_NOT_IN_CONTAINER;
	
	/* Go through and find the index of the item. */
	for (i = 0, n = list->length; i < n; i++)
	{
		/* Ignore ones that are not this one. */
		if (list->elements[i] != removeComponent)
			continue;
		
		/* Forward call. */
		if (sjme_error_is(error = inState->impl->containerRemove(
			inState, inContainer, container, removeComponent)))
			return sjme_error_default(error);
		
		/* Clear component from item set. */
		list->elements[i] = NULL;
		
		/* Remove parent from the component. */
		removeComponent->parent = NULL;
		
		/* Since we removed the component, it is no longer visible. */
		if (inState->impl->componentSetVisibleListener == NULL)
			inState->intern->updateVisibleComponent(inState,
				removeComponent, SJME_JNI_FALSE);
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* If this is reached then the component is not here. */
	return SJME_ERROR_NOT_IN_CONTAINER;
}

sjme_errorCode sjme_scritchui_core_containerRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer)
{
	sjme_errorCode error;
	sjme_scritchui_uiContainer container;
	sjme_list_sjme_scritchui_uiComponent* list;
	sjme_scritchui_uiComponent remove;
	sjme_jint i, n;
	
	if (inState == NULL || inContainer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->containerRemove == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Only certain types are containers. */
	if (sjme_error_is(error = inState->intern->getContainer(inState,
		inContainer, &container)) ||
		container == NULL)
		return sjme_error_default(error);
	
	/* Get component list, if nothing is inside then ignore. */
	list = container->components;
	if (list == NULL)
		return SJME_ERROR_NONE;
	
	/* Go through the list and remove individual components. */
	for (i = 0, n = list->length; i < n; i++)
	{
		/* Skip blank slots. */
		remove = list->elements[i];
		if (remove == NULL)
			continue;
		
		/* Forward removal. */
		if (sjme_error_is(error = inState->apiInThread->containerRemove(
			inState, inContainer, remove)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	if (inState == NULL || inContainer == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (x < 0 || y < 0 || width <= 0 || height <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* This component is not in this specific container. */
	if (inContainer != inComponent->parent)
		return SJME_ERROR_NOT_SUB_COMPONENT;
	
	/* Not implemented? */
	if (inState->impl->containerSetBounds == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward call. */
	return inState->impl->containerSetBounds(inState,
		inContainer, inComponent, x, y, width, height);
}

sjme_errorCode sjme_scritchui_core_intern_getContainer(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiContainer* outContainer)
{
	sjme_scritchui_uiContainer container;
	
	if (inState == NULL || inComponent == NULL || outContainer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only certain types can be a container. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_PANEL:
			container = &((sjme_scritchui_uiPanel)inComponent)->container;
			break;
			
		case SJME_SCRITCHUI_TYPE_SCROLL_PANEL:
			container = &((sjme_scritchui_uiScrollPanel)
				inComponent)->container;
			break;
		
		case SJME_SCRITCHUI_TYPE_WINDOW:
			container = &((sjme_scritchui_uiWindow)inComponent)->container;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	*outContainer = container;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_updateVisibleContainer(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInValue sjme_jboolean isVisible)
{
	sjme_errorCode error;
	sjme_list_sjme_scritchui_uiComponent* list;
	sjme_scritchui_uiComponent sub;
	sjme_scritchui_uiContainer container;
	sjme_jint i, n;
	
	if (inState == NULL || inContainer == NULL)
		return SJME_ERROR_NONE;
	
	/* We need the container itself. */
	container = NULL;
	if (sjme_error_is(error = inState->intern->getContainer(inState,
		inContainer, &container)) ||
		container == NULL)
		return sjme_error_default(error);
	
	/* Do not fail on the first call! */
	error = SJME_ERROR_NONE;
	
	/* Update our own component. */
	error |= inState->intern->updateVisibleComponent(inState,
		inContainer, isVisible);
	
	/* Need to go through the list. */
	list = container->components;
	if (list != NULL)
	{
		/* Go through everything. */
		for (i = 0, n = list->length; i < n; i++)
		{
			/* If there is no component in this slot, just ignore. */
			sub = list->elements[i];
			if (sub == NULL)
				continue;
			
			/* Send update for callback. */
			error |= inState->intern->updateVisibleComponent(inState,
				sub, isVisible);
		}
		
		/* If the list has changed, due to callback?, free it. */
		if (container->components != list)
		{
			sjme_alloc_free(list);
			list = NULL;
		}
	}
	
	/* Return whatever error state there was. */
	return error;
}
