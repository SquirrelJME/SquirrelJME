/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gdk/gdk.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_gtk2_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	sjme_errorCode error;
	GdkDisplay* gdkDisplay;
	GdkScreen** gdkScreens;
	gint numScreens, gi;
	sjme_jint maxScreens, i;
	sjme_scritchui_uiScreen makeScreen;

	if (inState == NULL || outScreens == NULL || inOutNumScreens == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Too small of an array? */
	maxScreens = *inOutNumScreens;
	if (maxScreens <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (inState->intern->mapScreen == NULL)
		return sjme_error_notImplemented(0);
	
	/* Get the default display, which has all the screens. */
	gdkDisplay = gdk_display_get_default();
	if (gdkDisplay == NULL)
		return SJME_ERROR_HEADLESS_DISPLAY;
	
	/* How many screens are there? */
	numScreens = gdk_display_get_n_screens(gdkDisplay);
	
	/* Allocate screen set. */
	gdkScreens = sjme_alloca(sizeof(*gdkScreens) * numScreens);
	if (gdkScreens == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(gdkScreens, 0, sizeof(*gdkScreens) * numScreens);
	
	/* Request all screens. */
	for (gi = 0; gi < numScreens; gi++)
		gdkScreens[gi] = gdk_display_get_screen(gdkDisplay,
			gi);
		
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
				gdkScreens[i])) ||
				makeScreen == NULL)
			return sjme_error_default(error);
		
		/* Update information. */
		makeScreen->displayHandle = gdkDisplay;
		
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
