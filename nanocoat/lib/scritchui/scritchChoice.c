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
	memmove(outItemTemplate, &choice->items->elements[atIndex],
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
	sjme_jint atIndex;
	
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
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	*inOutIndex = atIndex;
	if (inState->impl->choiceItemInsert == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	return inState->impl->choiceItemInsert(inState, inComponent,
		inOutIndex);
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
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	if (inState->impl->choiceItemRemove == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
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
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	if (inState->impl->choiceItemSetEnabled == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
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
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	if (inState->impl->choiceItemSetImage == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
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
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	if (inState->impl->choiceItemSetSelected == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
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
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	if (inState->impl->choiceItemSetString == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
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
