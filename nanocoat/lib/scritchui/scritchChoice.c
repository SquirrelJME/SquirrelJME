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

static sjme_errorCode sjme_scritchui_core_choiceCalculate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_list_sjme_scritchui_uiChoiceItem* items)
{
	if (inState == NULL || items == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_choiceItem(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoice* outChoice,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem* outChoiceItem)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	
	if (inState == NULL || inComponent == NULL || outChoice == NULL ||
		outChoiceItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	if (sjme_error_is(error = inState->intern->getChoice(inState,
		inComponent, &choice)) || choice == NULL)
		return sjme_error_default(error);
	
	/* Check bounds. */
	if (atIndex < 0 || atIndex >= choice->numItems)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Success! */
	*outChoice = choice;
	*outChoiceItem = choice->items->elements[atIndex];
	return SJME_ERROR_NONE; 
}

sjme_errorCode sjme_scritchui_core_intern_bindFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent atRover,
	sjme_attrInNotNull sjme_scritchui_uiComponent bindComponent,
	sjme_attrInValue sjme_jboolean isGrabbing)
{
	sjme_scritchui_uiComponent parent;
	sjme_scritchui_uiWindow window;
	
	if (inState == NULL || atRover == NULL || bindComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Ignore any attempts to bind focus to a window. */
	if (bindComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		return SJME_ERROR_NONE;
	
	/* We are at the top? */
	parent = atRover->parent;
	if (parent == NULL)
	{
		/* Only valid if a window. */
		if (atRover->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		{
			window = (sjme_scritchui_uiWindow)atRover;
			
			/* If we are grabbing, we just take it. */
			if (isGrabbing)
				window->focusedComponent = bindComponent;
			
			/* Otherwise, we only clear if we have the grab. */
			else if (window->focusedComponent == bindComponent)
				window->focusedComponent = NULL;
		}
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Continue going up. */
	return inState->intern->bindFocus(inState, parent, bindComponent,
		isGrabbing);
}

sjme_errorCode sjme_scritchui_core_intern_getChoice(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiChoice* outChoice)
{
	sjme_scritchui_uiChoice choice;
	
	if (inState == NULL || inComponent == NULL || outChoice == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Depends on the type. */
	choice = NULL;
	switch (inComponent->common.type)
	{
			/* Standard list. */
		case SJME_SCRITCHUI_TYPE_LIST:
			choice = &((sjme_scritchui_uiList)inComponent)->choice;
			break;
		
			/* Unknown. */
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	*outChoice = choice;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_choiceGetSelectedIndex(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outIndex)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	sjme_scritchui_uiChoiceItem choiceItem;
	sjme_jint result;
	sjme_jint i, n;
	
	if (inState == NULL || inComponent == NULL || outIndex == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover choice. */
	choice = NULL;
	if (sjme_error_is(error = inState->intern->getChoice(inState,
		inComponent, &choice)) || choice == NULL)
		return sjme_error_default(error);
	
	/* Find the first selected item. */
	result = -1;
	for (i = 0, n = choice->numItems; i < n; i++)
	{
		/* Get the item here. */
		choiceItem = choice->items->elements[i];
		
		/* Is this selected? */
		if (choiceItem->isSelected)
		{
			result = i;
			break;
		}
	}
		
	/* Success! */
	*outIndex = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_choiceItemGet(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem outItemTemplate)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	
	if (inState == NULL || inComponent == NULL || outItemTemplate == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	if (sjme_error_is(error = inState->intern->getChoice(inState,
		inComponent, &choice)) || choice == NULL)
		return sjme_error_default(error);
	
	/* Check bounds. */
	if (atIndex < 0 || atIndex >= choice->numItems)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Copy entire template over. */
	memmove(outItemTemplate, choice->items->elements[atIndex],
		sizeof(*outItemTemplate));
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_choiceItemInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	sjme_scritchui_uiChoiceItem inject;
	sjme_list_sjme_scritchui_uiChoiceItem* choiceItems;
	sjme_list_sjme_scritchui_uiChoiceItem* newItems;
	sjme_jint atIndex, i, o, n;
	
	if (inState == NULL || inComponent == NULL || inOutIndex == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	if (sjme_error_is(error = inState->intern->getChoice(inState,
		inComponent, &choice)) || choice == NULL)
		return sjme_error_default(error);
	
	/* Determine if the index is a special identifier. */
	atIndex = *inOutIndex;
	if (atIndex == INT32_MAX)
		atIndex = choice->numItems;
	
	/* Check bounds. */
	if (atIndex < 0 || atIndex > choice->numItems)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Allocate item to be injected. */
	inject = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*inject), &inject)) || inject == NULL)
		goto fail_injectAlloc;
	
	/* Default values. */
	inject->isEnabled = SJME_JNI_TRUE;
	
	/* The number of items. */
	n = choice->numItems;
	
	/* Need room in the list, to inject at? */
	choiceItems = choice->items;
	newItems = NULL;
	if (choiceItems == NULL || (n + 1) >= choiceItems->length)
	{
		/* Allocate new list. */
		if (sjme_error_is(error = sjme_list_alloc(inState->pool,
			n + 1, &newItems, sjme_scritchui_uiChoiceItem, 0)) ||
			newItems == NULL)
			goto fail_newItemsAlloc;
		
		/* Copy everything over and cleanup. */
		if (choiceItems != NULL)
		{
			/* Copy */
			for (i = 0; i < n; i++)
				newItems->elements[i] = choiceItems->elements[i];
		
			/* Use this list instead. */
			choice->items = newItems;
			
			/* Delete the old list. */
			if (sjme_error_is(error = sjme_alloc_free(
				choiceItems)))
				goto fail_freeOld;
		}
		
		/* Use this list instead. */
		else
			choice->items = newItems;
		
		/* Re-reference list used. */
		choiceItems = newItems;
		newItems = NULL;
	}
	
	/* Move items down until the insertion point is hit. */
	for (i = n - 1, o = n; i >= atIndex; i--, o--)
	{
		choiceItems->elements[o] = choiceItems->elements[i];
		choiceItems->elements[i] = NULL;
	}
	
	/* Insert item here. */
	choiceItems->elements[atIndex] = inject;
	
	/* Item count goes up. */
	choice->numItems = n + 1;
	
	/* For exclusive and implicit lists, if the list was empty then select */
	/* always the first added element. */
	if ((choice->type == SJME_SCRITCHUI_CHOICE_TYPE_EXCLUSIVE ||
		choice->type == SJME_SCRITCHUI_CHOICE_TYPE_IMPLICIT) && n == 0)
		inject->isSelected = SJME_JNI_TRUE;
	
	/* Recalculate choice set. */
	if (sjme_error_is(error = sjme_scritchui_core_choiceCalculate(
		inState, choiceItems)))
		goto fail_recalculate;
	
	/* Forward. */
	*inOutIndex = atIndex;
	if (inState->impl->choiceItemInsert == NULL)
		return sjme_error_notImplemented(0);
	return inState->impl->choiceItemInsert(inState, inComponent,
		inOutIndex);

fail_recalculate:
fail_freeOld:
fail_newItemsAlloc:
	if (newItems != NULL)
		sjme_alloc_free(newItems);
fail_injectAlloc:
	if (inject != NULL)
		sjme_alloc_free(inject);
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_choiceItemRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	if (sjme_error_is(error = inState->intern->getChoice(inState,
		inComponent, &choice)) || choice == NULL)
		return sjme_error_default(error);
	
	/* Check bounds. */
	if (atIndex < 0 || atIndex >= choice->numItems)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Forward. */
	if (inState->impl->choiceItemRemove == NULL)
		return sjme_error_notImplemented(0);
	return inState->impl->choiceItemRemove(inState, inComponent,
		atIndex);
}

sjme_errorCode sjme_scritchui_core_choiceItemRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	if (sjme_error_is(error = inState->intern->getChoice(inState,
		inComponent, &choice)) || choice == NULL)
		return sjme_error_default(error);
	
	/* Delete all items from the choice one by one. */
	while (choice->numItems > 0)
		if (sjme_error_is(error = inState->apiInThread->choiceItemRemove(
			inState, inComponent, 0)))
			return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_choiceItemSetEnabled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	sjme_scritchui_uiChoiceItem choiceItem;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	choiceItem = NULL;
	if (sjme_error_default(error = sjme_scritchui_core_choiceItem(
		inState, inComponent, atIndex, &choice,
		&choiceItem)) || choice == NULL || choiceItem == NULL)
		return sjme_error_default(error);
	
	/* Just set the flag. */
	choiceItem->isEnabled = isEnabled;
	
	/* Recalculate choice set. */
	if (sjme_error_is(error = sjme_scritchui_core_choiceCalculate(
		inState, choice->items)))
		return sjme_error_default(error);
	
	/* Forward. */
	if (inState->impl->choiceItemSetEnabled == NULL)
		return sjme_error_notImplemented(0);
	return inState->impl->choiceItemSetEnabled(inState, inComponent,
		atIndex, isEnabled);
}

sjme_errorCode sjme_scritchui_core_choiceItemSetImage(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inRgbDataLen,
	sjme_attrInPositiveNonZero sjme_jint inRgbScanLen,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	sjme_scritchui_uiChoiceItem choiceItem;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	choiceItem = NULL;
	if (sjme_error_default(error = sjme_scritchui_core_choiceItem(
		inState, inComponent, atIndex, &choice,
		&choiceItem)) || choice == NULL || choiceItem == NULL)
		return sjme_error_default(error);
	
#if 0
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#else
	sjme_message("TODO: list images.");
#endif
	
	/* Recalculate choice set. */
	if (sjme_error_is(error = sjme_scritchui_core_choiceCalculate(
		inState, choice->items)))
		return sjme_error_default(error);
	
	/* If not natively supported, ignore. */
	if (inState->impl->choiceItemSetImage == NULL)
		return SJME_ERROR_NONE;
	
	/* Forward. */
	return inState->impl->choiceItemSetImage(inState, inComponent,
		atIndex, inRgb, inRgbOff, inRgbDataLen, inRgbScanLen, width, height);
}

sjme_errorCode sjme_scritchui_core_choiceItemSetSelected(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	sjme_scritchui_uiChoiceItem choiceItem;
	sjme_jint i, n;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	choiceItem = NULL;
	if (sjme_error_default(error = sjme_scritchui_core_choiceItem(
		inState, inComponent, atIndex, &choice,
		&choiceItem)) || choice == NULL || choiceItem == NULL)
		return sjme_error_default(error);
	
	/* Clearing a selection for an explicit/implicit list just does nothing. */
	if ((choice->type == SJME_SCRITCHUI_CHOICE_TYPE_EXCLUSIVE ||
		choice->type == SJME_SCRITCHUI_CHOICE_TYPE_IMPLICIT) && !isSelected)
		return SJME_ERROR_NONE;
	
	/* For exclusive and implicit list types, make sure this is the only */
	/* one selected item. */
	if ((choice->type == SJME_SCRITCHUI_CHOICE_TYPE_EXCLUSIVE ||
		choice->type == SJME_SCRITCHUI_CHOICE_TYPE_IMPLICIT) && isSelected)
	{
		/* Clear other selections. */
		for (i = 0, n = choice->numItems; i < n; i++)
			choice->items->elements[i]->isSelected = SJME_JNI_FALSE;
	}
	
	/* Just set the selected state. */
	choiceItem->isSelected = isSelected;
	
	/* Recalculate choice set. */
	if (sjme_error_is(error = sjme_scritchui_core_choiceCalculate(
		inState, choice->items)))
		return sjme_error_default(error);
	
	/* Forward. */
	if (inState->impl->choiceItemSetSelected == NULL)
		return sjme_error_notImplemented(0);
	return inState->impl->choiceItemSetSelected(inState, inComponent,
		atIndex, isSelected);
}

sjme_errorCode sjme_scritchui_core_choiceItemSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	sjme_scritchui_uiChoiceItem choiceItem;
	sjme_lpcstr dup;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	choiceItem = NULL;
	if (sjme_error_default(error = sjme_scritchui_core_choiceItem(
		inState, inComponent, atIndex, &choice,
		&choiceItem)) || choice == NULL || choiceItem == NULL)
		return sjme_error_default(error);
	
	/* Make defensive copy of the new string. */
	dup = NULL;
	if (inString != NULL)
		if (sjme_error_is(error = sjme_alloc_strdup(inState->pool,
			&dup, inString)) || dup == NULL)
			return sjme_error_default(error); 
	
	/* Delete old string. */
	if (choiceItem->string != NULL)
	{
		/* Free. */
		if (sjme_error_is(error = sjme_alloc_free(
			choiceItem->string)))
		{
			if (dup != NULL)
				sjme_alloc_free(dup);
			
			return sjme_error_default(error);
		}
		
		/* Clear. */
		choiceItem->string = NULL;
	}
	
	/* Set new string. */
	choiceItem->string = dup;
	
	/* Recalculate choice set. */
	if (sjme_error_is(error = sjme_scritchui_core_choiceCalculate(
		inState, choice->items)))
		return sjme_error_default(error);
	
	/* Forward. */
	if (inState->impl->choiceItemSetString == NULL)
		return sjme_error_notImplemented(0);
	return inState->impl->choiceItemSetString(inState, inComponent,
		atIndex, inString);
}

sjme_errorCode sjme_scritchui_core_choiceLength(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outLength)
{
	sjme_errorCode error;
	sjme_scritchui_uiChoice choice;
	
	if (inState == NULL || inComponent == NULL || outLength == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover choice. */
	choice = NULL;
	if (sjme_error_is(error = inState->intern->getChoice(inState,
		inComponent, &choice)) || choice == NULL)
		return sjme_error_default(error);
	
	/* Return item count. */
	*outLength = choice->numItems;
	return SJME_ERROR_NONE;
}
