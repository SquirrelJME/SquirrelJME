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
#include "sjme/fixed.h"

sjme_errorCode sjme_scritchui_core_intern_mapScreen(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jint screenId,
	sjme_attrInOutNotNull sjme_scritchui_uiScreen* outScreen,
	sjme_attrInNullable sjme_scritchui_handle updateHandle)
{
	sjme_errorCode error;
	sjme_jint numOldScreens, i;
	sjme_list(sjme_scritchui_uiScreen)* oldScreens;
	sjme_list(sjme_scritchui_uiScreen)* newScreens;
	sjme_scritchui_uiScreen maybe;
	
	if (inState == NULL || outScreen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
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
				maybe->screenHandle = updateHandle;
			
			/* Success! */
			return SJME_ERROR_NONE;
		}
	}
	
	/* Allocate new screen. */
	maybe = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inState->pool,
		sizeof(*maybe), NULL, (void**)&maybe, NULL)) || maybe == NULL)
		goto fail_alloc;
		
	/* Common initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		SJME_SUI_CAST_COMMON(maybe), SJME_JNI_FALSE,
		SJME_SCRITCHUI_TYPE_SCREEN)))
		goto fail_commonInit;
	
	/* Fill in information. */
	maybe->common.magic = SJME_SCRITCHUI_OBJECT_MAGIC;
	maybe->common.state = inState;
	maybe->common.type = SJME_SCRITCHUI_TYPE_SCREEN;
	maybe->screenHandle = updateHandle;
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
	
fail_commonInit:
fail_alloc:
	if (maybe != NULL)
	{
		sjme_alloc_free(maybe);
		maybe = NULL;
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_screenGetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScreen inScreen,
	sjme_attrInNullable sjme_scritchui_uiComponent forComponent,
	sjme_attrOutNullable sjme_scritchui_rect* pixelBound,
	sjme_attrOutNullable sjme_scritchui_rect* mmBound)
{
	sjme_errorCode error;
	sjme_scritchui_rect px, mm;
	
	if (inState == NULL || inScreen == NULL ||
		(pixelBound == NULL && mmBound == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->screenGetBounds == NULL)
		return sjme_error_notImplemented(0);
	
	/* Get the bounds, which will be normalized. */
	memset(&px, 0, sizeof(px));
	memset(&mm, 0, sizeof(mm));
	if (sjme_error_is(error = inState->impl->screenGetBounds(
		inState, inScreen, forComponent, &px, &mm)))
		return sjme_error_default(error);
	
	/* Pixels do not start at zero and millimeters are zero? This means */
	/* the native system cannot determine where the actual screen is */
	/* located. */
	if ((px.s.x != 0 || px.s.y != 0) && (mm.s.x == 0 && mm.s.y == 0))
	{
		/* Determine new base coordinates. */
		mm.s.x = sjme_fixed_int(sjme_fixed_mul(sjme_fixed_hi(px.s.x),
			sjme_fixed_fraction(px.d.width, mm.d.width)));
		mm.s.y = sjme_fixed_int(sjme_fixed_mul(sjme_fixed_hi(px.s.x),
			sjme_fixed_fraction(px.d.height, mm.d.height)));
	}
	
	/* If no component was requested, cache it for later usage. */
	if (forComponent == NULL)
	{
		memmove(&inScreen->pixelBound, &px, sizeof(px));
		memmove(&inScreen->mmBound, &mm, sizeof(mm));
	}
	
	/* Return what was requested. */
	if (pixelBound != NULL)
		memmove(pixelBound, &px, sizeof(px));
	if (mmBound != NULL)
		memmove(mmBound, &mm, sizeof(mm));
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(screen))
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_core_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	sjme_jint maxScreens;
	
	if (inState == NULL || outScreens == NULL || inOutNumScreens == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cannot fit anything? */
	maxScreens = *inOutNumScreens;
	if (maxScreens <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Missing API? */
	if (inState->impl->screens == NULL)
		return sjme_error_notImplemented(0);
	
	/* Forward call. */
	return inState->impl->screens(inState, outScreens, inOutNumScreens);
}
