/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

sjme_errorCode sjme_scritchui_core_intern_mapScreen(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jint screenId,
	sjme_attrInOutNotNull sjme_scritchui_uiScreen* outScreen,
	sjme_attrInNullable sjme_scritchui_handle updateHandle)
{
	sjme_errorCode error;
	sjme_jint numOldScreens, i;
	sjme_list_sjme_scritchui_uiScreen* oldScreens;
	sjme_list_sjme_scritchui_uiScreen* newScreens;
	sjme_scritchui_uiScreen maybe;
	
	if (inState == NULL || outScreen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Soft poll. */
	if (inState->impl->loopSoftPoll != NULL)
		inState->impl->loopSoftPoll(inState);
	
	/* Check if the screen is already known. */
	oldScreens = inState->screens;
	if (oldScreens != NULL)
	{
		numOldScreens = oldScreens->length;
		for (i = 0; i < numOldScreens; i++)
		{
			maybe = oldScreens->elements[i];
			if (maybe->id != screenId)
				continue;
			
			/* Use this one. */
			*outScreen = maybe;
			
			/* Update handle? */
			if (updateHandle != NULL)
				maybe->common.handle = updateHandle;
			
			/* Success! */
			return SJME_ERROR_NONE;
		}
	}
	
	/* Allocate new screen. */
	maybe = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*maybe), &maybe)) || maybe == NULL)
		goto fail_alloc;
	
	/* Fill in information. */
	maybe->common.type = SJME_SCRITCHUI_TYPE_SCREEN;
	maybe->common.handle = updateHandle;
	maybe->id = screenId;
	
	/* Allocate a new list copy? */
	newScreens = NULL;
	if (oldScreens != NULL)
	{
		/* Use old length. */
		numOldScreens = oldScreens->length;
		
		/* Grow list. */
		if (sjme_error_is(error = sjme_list_copy(inState->pool,
			numOldScreens + 1, oldScreens, &newScreens,
			sjme_scritchui_uiScreen, 0)) || newScreens == NULL)
			goto fail_newList;
	}
	else
	{
		/* There are no old screens. */
		numOldScreens = 0;
		
		/* Allocate set of new screens. */
		if (sjme_error_is(error = sjme_list_alloc(inState->pool, 1,
			&newScreens, sjme_scritchui_uiScreen, 0)) || newScreens == NULL)
			goto fail_newList;
	}
	
	/* Set new screen at the very end. */
	newScreens->elements[numOldScreens] = maybe;
	
	/* Use this list instead. */
	inState->screens = newScreens;
	
	/* Clear the old one, if it was allocated anyway. */
	if (oldScreens != NULL)
		if (sjme_error_is(error = sjme_alloc_free(oldScreens)))
			goto fail_oldFree;
	
	/* Success! */
	*outScreen = maybe;
	return SJME_ERROR_NONE; 
	
fail_oldFree:
fail_newList:
	if (newScreens != NULL)
	{
		sjme_alloc_free(newScreens);
		newScreens = NULL;
	}
	
fail_alloc:
	if (maybe != NULL)
	{
		sjme_alloc_free(maybe);
		maybe = NULL;
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_screenListenerFunc callback)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	sjme_jint maxScreens;
	
	/* Debug. */
	sjme_message("sjme_scritchui_core_screens(%p, %p, %p)",
		inState, outScreens, inOutNumScreens);
	
	if (inState == NULL || outScreens == NULL || inOutNumScreens == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cannot fit anything? */
	maxScreens = *inOutNumScreens;
	if (maxScreens <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Soft poll. */
	if (inState->impl->loopSoftPoll != NULL)
		inState->impl->loopSoftPoll(inState);
	
	/* Missing API? */
	if (inState->impl->screens == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward call. */
	return inState->impl->screens(inState, outScreens, inOutNumScreens);
}
