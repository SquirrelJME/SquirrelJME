/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/cocoa/cocoa.h"
#include "lib/scritchui/cocoa/cocoaIntern.h"

sjme_errorCode sjme_scritchui_cocoa_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	sjme_errorCode error;
	NSArray* screens;
	sjme_jint i, numScreens, maxScreens;
	sjme_scritchui_uiScreen makeScreen;

	if (inState == NULL || outScreens == NULL || inOutNumScreens == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Too small of an array? */
	maxScreens = *inOutNumScreens;
	if (maxScreens <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Get all screens. */
	screens = [NSScreen screens];
	numScreens = [screens count];

	/* Limit to not write past the end. */
	if (numScreens > maxScreens)
		numScreens = maxScreens;

	/* Map screens to ScritchUI. */
	for (i = 0; i < numScreens; i++)
	{
		/* Setup new screen. */
		makeScreen = NULL;
		if (sjme_error_is(error = inState->intern->mapScreen(inState,
				i, &makeScreen,
				[screens objectAtIndex:i])) ||
				makeScreen == NULL)
			return sjme_error_default(error);

		/* Store result. */
		outScreens[i] = makeScreen;
	}

	/* Clear the rest, if any. */
	for (; i < maxScreens; i++)
		outScreens[i] = NULL;

	/* Return resultant count. */
	*inOutNumScreens = numScreens;
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
