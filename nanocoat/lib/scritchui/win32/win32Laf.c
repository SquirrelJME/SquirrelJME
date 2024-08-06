/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

sjme_errorCode sjme_scritchui_win32_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor)
{
	int dx;
	HBRUSH brush;
	LOGBRUSH logBrush;
	
	if (inState == NULL || inContext == NULL || outRGB == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Which color ID to get? */
	dx = 0;
	switch (elementColor)
	{
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BACKGROUND:
			dx = COLOR_WINDOW;
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BORDER:
			dx = COLOR_3DLIGHT;
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOREGROUND:
			dx = COLOR_WINDOWTEXT; 
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BACKGROUND:
			dx = COLOR_HIGHLIGHT;
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BORDER:
			dx = COLOR_3DHIGHLIGHT;
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_FOREGROUND:
			dx = COLOR_HIGHLIGHTTEXT;
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOCUS_BORDER:
			dx = COLOR_HOTLIGHT;
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_BACKGROUND:
			dx = COLOR_BTNFACE;
			break;
			
		case SJME_SCRITCHUI_LAF_ELEMENT_COLOR_PANEL_FOREGROUND:
			dx = COLOR_BTNTEXT;
			break;
			
			/* Unknown. */
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Get brush color. */
	brush = GetSysColorBrush(dx);
	if (brush == NULL)
	{
		/* Fallback to system color. */
		*outRGB = GetSysColor(dx);
		return SJME_ERROR_NONE;
	}
	
	/* Get brush information. */
	memset(&logBrush, 0, sizeof(logBrush));
	if (GetObject(brush, sizeof(logBrush), &logBrush) <
		sizeof(logBrush))
	{
		/* Fallback to system color. */
		*outRGB = GetSysColor(dx);
		return SJME_ERROR_NONE;
	}

	/* Extract color component. */
	*outRGB = ((GetRValue(logBrush.lbColor) & 0xFF) << 16) |
		((GetGValue(logBrush.lbColor) & 0xFF) << 8) |
		(GetBValue(logBrush.lbColor) & 0xFF);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
