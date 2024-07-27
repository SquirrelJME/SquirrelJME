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

sjme_errorCode sjme_scritchui_core_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor)
{
	sjme_errorCode error;
	sjme_jint rgb;
	
	if (inState == NULL || outRGB == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (elementColor < 0 ||
		elementColor >= SJME_SCRITCHUI_NUM_LAF_ELEMENT_COLOR)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (inState->impl->lafElementColor == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Panels are always black on white. */
	rgb = 0;
	if (elementColor == SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_BACKGROUND)
		rgb = 0xFFFFFFFF;
	else if (elementColor == SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_FOREGROUND)
		rgb = 0xFF000000;
	
	/* Obtain theme color. */
	else if (sjme_error_is(error = inState->impl->lafElementColor(
		inState, inContext, &rgb, elementColor)))
		return sjme_error_default(error);
	
	/* Normalize color. */
	*outRGB = rgb | 0xFF000000;
	return SJME_ERROR_NONE;
}
