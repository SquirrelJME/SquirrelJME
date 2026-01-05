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
#include "sjme/fixed.h"

static void sjme_scritchui_core_lafFallbackColor(
	sjme_scritchui_lafElementColorType type,
	sjme_jint* outColor)
{
	if (outColor == NULL)
		return;

	switch (type)
	{
			/* Trans rights! */
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_ACCENT_TOP:
			*outColor = INT32_C(0xFF5BCEFA);
			break;
		
			/* Trans rights!! */
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_ACCENT_BOTTOM:
			*outColor = INT32_C(0xFFF5A9B8);
			break;
			
			/* White. */
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BACKGROUND:
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_BACKGROUND:
			*outColor = INT32_C(0xFFFFFFFF);
			break;

			/* Gray. */
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BORDER:
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOCUS_BORDER:
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BACKGROUND:
			*outColor = INT32_C(0xFF7F7F7F);
			break;

			/* Black. */
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BORDER:
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOREGROUND:
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_FOREGROUND:
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_FOREGROUND:
		default:
			*outColor = INT32_C(0xFF000000);
			break;
	}
}

sjme_errorCode sjme_scritchui_core_lafDpiProject(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrInValue sjme_jboolean toBase,
	sjme_attrInNullable sjme_jint* inOutX,
	sjme_attrInNullable sjme_jint* inOutY,
	sjme_attrInNullable sjme_jint* inOutW,
	sjme_attrInNullable sjme_jint* inOutH)
{
	sjme_errorCode error;
	sjme_scritchui_uiScreen firstScreen;
	sjme_jint numScreens;
	sjme_scritchui_rect px, mm;
	sjme_fixed pxMm, def, mul;
	
	if (inState == NULL || (inOutX == NULL && inOutY == NULL &&
		inOutW == NULL && inOutH == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If DPI projection is supported natively, we can try that. We do */
	/* allow for the implementation to soft fail and fallback to default */
	/* projection handling. */
	if (inState->impl->lafDpiProject != NULL)
	{
		/* Perform the projection. */
		if (!sjme_error_is(error = inState->impl->lafDpiProject(inState,
			inContext, toBase, inOutX, inOutY, inOutW, inOutH)))
			return SJME_ERROR_NONE;
		
		/* The implementation is requesting that we fall back to software */
		/* projection handling. */
		if (error != SJME_ERROR_CONTINUE)
			return sjme_error_default(error);
	}
	
	/* Grab the first (default) screen, since that is all we care about. */
	firstScreen = NULL;
	numScreens = 1;
	if (sjme_error_is(error = inState->apiInThread->screens(inState,
		&firstScreen, &numScreens)))
	{
		/* There is no display device, so we cannot do any kind of */
		/* projecting as DPI is not a concept. */
		if (error == SJME_ERROR_HEADLESS_DISPLAY)
			return SJME_ERROR_NONE;
		
		return sjme_error_default(error);
	}
	
	/* No screen? Should have returned SJME_ERROR_HEADLESS_DISPLAY! */
	if (firstScreen == NULL)
		return SJME_ERROR_NONE;
	
	/* Get the bounds of the screen. */
	memset(&px, 0, sizeof(px));
	memset(&mm, 0, sizeof(mm));
	if (sjme_error_is(error = inState->apiInThread->screenGetBounds(
		inState, firstScreen, inContext, &px, &mm)))
		return sjme_error_default(error);
	
	/* Calculate average pixels per millimeter. */
	pxMm = sjme_fixed_div(
		sjme_fixed_fraction(px.d.width, mm.d.width) +
		sjme_fixed_fraction(px.d.height, mm.d.height),
		sjme_fixed_hi(2));
	
	/* 96dpi == 3.779529px/mm */
	/* 3.779529*(2^13) = 30961.901568 / 8192 */
	def = sjme_fixed_fraction(30961, 8192);
	
	/* Determine the multiplier to use. */
	mul = sjme_fixed_div(pxMm, def);
	
	/* Converting from HiDPI to SquirrelJME non-scaled DPI?. */
	if (toBase)
	{
		if (inOutX != NULL)
			*inOutX = sjme_fixed_int(sjme_fixed_round(
				sjme_fixed_div(sjme_fixed_hi(*inOutX), mul)));
		if (inOutY != NULL)
			*inOutY = sjme_fixed_int(sjme_fixed_round(
				sjme_fixed_div(sjme_fixed_hi(*inOutY), mul)));
		if (inOutW != NULL)
			*inOutW = sjme_fixed_int(sjme_fixed_round(
				sjme_fixed_div(sjme_fixed_hi(*inOutW), mul)));
		if (inOutH != NULL)
			*inOutH = sjme_fixed_int(sjme_fixed_round(
				sjme_fixed_div(sjme_fixed_hi(*inOutH), mul)));
	}
	
	/* Otherwise converting from non-scaled DPI to HiDPI. */
	else
	{
		if (inOutX != NULL)
			*inOutX = sjme_fixed_int(sjme_fixed_round(
				sjme_fixed_mul(sjme_fixed_hi(*inOutX), mul)));
		if (inOutY != NULL)
			*inOutY = sjme_fixed_int(sjme_fixed_round(
				sjme_fixed_mul(sjme_fixed_hi(*inOutY), mul)));
		if (inOutW != NULL)
			*inOutW = sjme_fixed_int(sjme_fixed_round(
				sjme_fixed_mul(sjme_fixed_hi(*inOutW), mul)));
		if (inOutH != NULL)
			*inOutH = sjme_fixed_int(sjme_fixed_round(
				sjme_fixed_mul(sjme_fixed_hi(*inOutH), mul)));
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

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
	
	/* Panels are always black on white. */
	rgb = 0;
	if (elementColor == SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_BACKGROUND)
		rgb = 0xFFFFFFFF;
	else if (elementColor == SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_FOREGROUND)
		rgb = 0xFF000000;
	
	/* Native theming not supported, use fallback. */
	else if (inState->impl->lafElementColor == NULL)
		sjme_scritchui_core_lafFallbackColor(elementColor,
			&rgb);
	
	/* Obtain theme color. */
	else if (sjme_error_is(error = inState->impl->lafElementColor(
		inState, inContext, &rgb, elementColor)))
	{
		/* Use fallback color. */
		if (error == SJME_ERROR_INVALID_ARGUMENT)
			sjme_scritchui_core_lafFallbackColor(elementColor,
				&rgb);
		
		/* Fail otherwise. */
		else
			return sjme_error_default(error);
	}
	
	/* Normalize color. */
	*outRGB = rgb | 0xFF000000;
	return SJME_ERROR_NONE;
}
