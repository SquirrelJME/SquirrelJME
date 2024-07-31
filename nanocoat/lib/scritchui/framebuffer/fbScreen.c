/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

/** The base count for supported screens in the framebuffer. */
#define SJME_FB_SCREEN_COUNT 16

sjme_errorCode sjme_scritchui_fb_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_jint maxScreens;
	sjme_scritchui_uiScreen wrappedScreens[SJME_FB_SCREEN_COUNT];
	sjme_jint numWrappedScreens, i;
	sjme_scritchui_uiScreen makeScreen;
	
	if (inState == NULL || outScreens == NULL || inOutNumScreens == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
		
	/* Too small of an array? */
	maxScreens = *inOutNumScreens;
	if (maxScreens <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Not implemented? */
	if (inState->intern->mapScreen == NULL)
		return sjme_error_notImplemented(0);
	
	/* Get wrapped screens. */
	memset(wrappedScreens, 0, sizeof(wrappedScreens));
	numWrappedScreens = SJME_FB_SCREEN_COUNT;
	if (sjme_error_is(error = wrappedState->apiInThread->screens(
		wrappedState, wrappedScreens,
		&numWrappedScreens)))
		return sjme_error_default(error);
	
	/* Map screens to ScritchUI. */
	for (i = 0; i < numWrappedScreens; i++)
	{
		/* Ignore if nothing here. */
		if (wrappedScreens[i] == NULL)
			continue;
		
		/* Setup new screen. */
		makeScreen = NULL;
		if (sjme_error_is(error = inState->intern->mapScreen(inState,
				i, &makeScreen,
				wrappedScreens[i])) ||
				makeScreen == NULL)
			return sjme_error_default(error);
		
		/* Update information. */
		makeScreen->displayHandle = wrappedScreens[i];
		
		/* Store result. */
		if (i < maxScreens)
			outScreens[i] = makeScreen;
	}
	
	/* Clear the rest, if any. */
	for (; i < maxScreens; i++)
		outScreens[i] = NULL;
	
	/* Return resultant count. */
	if (maxScreens < numWrappedScreens)
		*inOutNumScreens = maxScreens;
	else
		*inOutNumScreens = numWrappedScreens;
	return SJME_ERROR_NONE;
}
